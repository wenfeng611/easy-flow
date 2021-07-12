package com.auditflow.customize.service.auditstream;

import com.auditflow.customize.context.UserInfoThreadLocalContext;
import com.auditflow.customize.entity.AuditFlowEntity;
import com.auditflow.customize.enums.AuditStreamEnums;
import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.model.auditstream.AuditStreamQueryModel;
import com.auditflow.customize.mapper.AuditFlowItemMapper;
import com.auditflow.customize.mapper.AuditFlowMapper;
import com.auditflow.customize.utils.graph.GraphResolvor;
import io.micrometer.core.instrument.util.StringUtils;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2020/9/14 15:19
 */

@Service
public class AuditFlowService {

    @Resource
    private AuditFlowMapper auditFlowMapper;

    @Resource
    private AuditFlowItemMapper auditFlowItemMapper;

    //保存审核流
    public ApiResponse saveFlow(AuditFlowEntity flow){
        String username = UserInfoThreadLocalContext.user().getUsername();
        Date date = new Date();
        flow.setCreateTime(date);
        flow.setUpdateTime(date);
        flow.setCreaterName(username);
        flow.setStatus(AuditStreamEnums.FlowStatusEnums.NOTENABLE.getCode()); //默认启用
        flow.setGraphJson("");
        auditFlowMapper.save(flow);
        return ApiResponse.success(null);
    }

    //查询单个
    public ApiResponse findById(Integer id){
        return ApiResponse.success(auditFlowMapper.findTopById(id));
    }

    public ApiResponse queryPage(AuditStreamQueryModel auditStreamQueryModel) {
        auditStreamQueryModel.setPagenum((auditStreamQueryModel.getPagenum() - 1) * auditStreamQueryModel.getPagesize());
        List<AuditFlowEntity> result = auditFlowMapper.queryPage(auditStreamQueryModel);
        int count = auditFlowMapper.countByQuery(auditStreamQueryModel);
        return new ApiResponse(result, count);
    }

    public ApiResponse enableFlow(AuditFlowEntity auditFlowEntity) {
        AuditFlowEntity temp = auditFlowMapper.findTopById(auditFlowEntity.getId());

        if(StringUtils.isBlank(temp.getGraphJson())){
            return new ApiResponse.Fail(null,40004,"启用失败！请先设置流程！");
        }

        Pair<Boolean,String> pair =  GraphResolvor.judgeFlowGraph(temp.getGraphJson());
        if(!pair.getValue0()){
            return new ApiResponse.Fail(null,40005,pair.getValue1());
        }

        auditFlowEntity.setStatus(AuditStreamEnums.FlowStatusEnums.ENABLE.getCode());
        auditFlowMapper.updateStatus(auditFlowEntity);
        return ApiResponse.success(null);
    }

    public ApiResponse forbidFlow(AuditFlowEntity auditFlowEntity) {
        //判断流程状态 禁用：状态为启用中的记录，且不存在审批中的记录时才可以进行禁用操作
        boolean judge = judgeStatusDetail(auditFlowEntity.getId());
        if(!judge) return new ApiResponse.Fail(null,40002,"当前列表不可禁用！");

        auditFlowEntity.setStatus(AuditStreamEnums.FlowStatusEnums.FORBIDDEN.getCode());
        auditFlowMapper.updateStatus(auditFlowEntity);
        return ApiResponse.success(null);
    }

    private boolean judgeStatusDetail(Integer id) {
        AuditFlowEntity flow = auditFlowMapper.findTopById(id);
        if(AuditStreamEnums.FlowStatusEnums.ENABLE.getCode() != flow.getStatus())  return false;
        int count = auditFlowItemMapper.countRunningFlowItems(id);
        return count == 0;
    }

    public ApiResponse updateFlow(AuditFlowEntity auditFlowEntity) {
        auditFlowMapper.updateFlow(auditFlowEntity);
        return ApiResponse.success(null);
    }

    public ApiResponse updateFlowGraph(AuditFlowEntity auditFlowEntity) {
        //判断流程图必须有条件节点 且必须作为开始节点
        boolean hasCondition = GraphResolvor.hasOnlyOneCondition(auditFlowEntity.getGraphJson());
        if(!hasCondition){
            return new ApiResponse.Fail(null,400003,"保存失败!流程图必须有且仅有一个条件节点!");
        }
        auditFlowMapper.updateFlowGraph(auditFlowEntity);
        return ApiResponse.success(null);
    }
}
