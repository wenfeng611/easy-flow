package com.auditflow.customize.service.auditstream;

import com.auditflow.customize.context.UserInfoThreadLocalContext;
import com.auditflow.customize.entity.NodeEntity;
import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.model.auditstream.AuditStreamQueryModel;
import com.auditflow.customize.mapper.NodeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author wenfeng.zhu
 * @description
 */
@Service
public class NodeService {

    @Resource
    private NodeMapper nodeMapper;

    //保存场景
    public ApiResponse saveNode(NodeEntity node){
        String username = UserInfoThreadLocalContext.user().getUsername();
        node.setCreateTime(new Date());
        node.setCreaterName(username);
        node.setIsValid(1);
        node.setUserIds(node.getAuditUserids());
        nodeMapper.save(node);
        return ApiResponse.success(null);
    }

    //查询所有节点
    public ApiResponse selectAllNodes(){
        List<NodeEntity> result = nodeMapper.selectAllNodes();
        return ApiResponse.success(result);
    }

    public ApiResponse queryPage(AuditStreamQueryModel auditStreamQueryModel) {
        auditStreamQueryModel.setPagenum((auditStreamQueryModel.getPagenum() - 1) * auditStreamQueryModel.getPagesize());
        List<NodeEntity> result = nodeMapper.queryPage(auditStreamQueryModel);
        int count = nodeMapper.countByQuery(auditStreamQueryModel);
        return new ApiResponse(result, count);
    }

    public ApiResponse updateNode(NodeEntity nodeEntity) {
        nodeMapper.updateNode(nodeEntity);
        return ApiResponse.success(null);
    }

    public ApiResponse updateValid(NodeEntity nodeEntity) {
        nodeMapper.updateValid(nodeEntity);
        return ApiResponse.success(null);
    }
}
