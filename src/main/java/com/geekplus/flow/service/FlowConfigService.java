package com.geekplus.flow.service;

import com.geekplus.flow.context.UserInfoThreadLocalContext;
import com.geekplus.flow.entity.FlowConfigEntity;
import com.geekplus.flow.enums.FlowEnums;
import com.geekplus.flow.model.ApiResponse;
import com.geekplus.flow.model.FlowQueryModel;
import com.geekplus.flow.mapper.FlowItemMapper;
import com.geekplus.flow.mapper.FlowConfigMapper;
import com.geekplus.flow.utils.graph.GraphResolvor;
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
public class FlowConfigService {

    @Resource
    private FlowConfigMapper flowMapper;

    @Resource
    private FlowItemMapper flowItemMapper;

    //保存流程
    public ApiResponse saveFlow(FlowConfigEntity flow){
        String username = UserInfoThreadLocalContext.user().getUsername();
        Date date = new Date();
        flow.setCreateTime(date);
        flow.setUpdateTime(date);
        flow.setCreaterName(username);
        flow.setStatus(FlowEnums.FlowStatusEnums.NOTENABLE.getCode()); //默认启用
        flow.setGraphJson("");
        flowMapper.save(flow);
        return ApiResponse.success(null);
    }

    //查询单个
    public ApiResponse findById(Integer id){
        return ApiResponse.success(flowMapper.findTopById(id));
    }

    public ApiResponse queryPage(FlowQueryModel flowQueryModel) {
        flowQueryModel.setPagenum((flowQueryModel.getPagenum() - 1) * flowQueryModel.getPagesize());
        List<FlowConfigEntity> result = flowMapper.queryPage(flowQueryModel);
        int count = flowMapper.countByQuery(flowQueryModel);
        return new ApiResponse(result, count);
    }

    public ApiResponse enableFlow(FlowConfigEntity flowConfigEntity) {
        FlowConfigEntity temp = flowMapper.findTopById(flowConfigEntity.getId());

        if(StringUtils.isBlank(temp.getGraphJson())){
            return new ApiResponse.Fail(null,40004,"启用失败！请先设置流程！");
        }

        Pair<Boolean,String> pair =  GraphResolvor.judgeFlowGraph(temp.getGraphJson());
        if(!pair.getValue0()){
            return new ApiResponse.Fail(null,40005,pair.getValue1());
        }

        flowConfigEntity.setStatus(FlowEnums.FlowStatusEnums.ENABLE.getCode());
        flowMapper.updateStatus(flowConfigEntity);
        return ApiResponse.success(null);
    }

    public ApiResponse forbidFlow(FlowConfigEntity flowConfigEntity) {
        //判断流程状态 禁用：状态为启用中的记录，且不存在审批中的记录时才可以进行禁用操作
        boolean judge = judgeStatusDetail(flowConfigEntity.getId());
        if(!judge) return new ApiResponse.Fail(null,40002,"当前列表不可禁用！");

        flowConfigEntity.setStatus(FlowEnums.FlowStatusEnums.FORBIDDEN.getCode());
        flowMapper.updateStatus(flowConfigEntity);
        return ApiResponse.success(null);
    }

    private boolean judgeStatusDetail(Integer id) {
        FlowConfigEntity flow = flowMapper.findTopById(id);
        if(FlowEnums.FlowStatusEnums.ENABLE.getCode() != flow.getStatus())  return false;
        int count = flowItemMapper.countRunningFlowItems(id);
        return count == 0;
    }

    public ApiResponse updateFlow(FlowConfigEntity flowConfigEntity) {
        flowMapper.updateFlow(flowConfigEntity);
        return ApiResponse.success(null);
    }

    public ApiResponse updateFlowGraph(FlowConfigEntity flowConfigEntity) {
        //判断流程图必须有条件节点 且必须作为开始节点
        boolean hasCondition = GraphResolvor.hasOnlyOneCondition(flowConfigEntity.getGraphJson());
        if(!hasCondition){
            return new ApiResponse.Fail(null,400003,"保存失败!流程图必须有且仅有一个条件节点!");
        }
        flowMapper.updateFlowGraph(flowConfigEntity);
        return ApiResponse.success(null);
    }
}
