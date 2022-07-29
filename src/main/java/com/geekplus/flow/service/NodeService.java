package com.geekplus.flow.service;

import com.geekplus.flow.context.UserInfoThreadLocalContext;
import com.geekplus.flow.entity.FlowNodeEntity;
import com.geekplus.flow.model.ApiResponse;
import com.geekplus.flow.model.FlowQueryModel;
import com.geekplus.flow.mapper.FlowNodeMapper;
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
    private FlowNodeMapper nodeMapper;

    //保存场景
    public ApiResponse saveNode(FlowNodeEntity node){
        String username = UserInfoThreadLocalContext.user().getUsername();
        node.setCreateTime(new Date());
        node.setCreaterName(username);
        node.setIsValid(1);
        nodeMapper.save(node);
        return ApiResponse.success(null);
    }

    //查询所有节点
    public ApiResponse selectAllNodes(){
        List<FlowNodeEntity> result = nodeMapper.selectAllNodes();
        return ApiResponse.success(result);
    }

    public ApiResponse queryPage(FlowQueryModel flowQueryModel) {
        flowQueryModel.setPagenum((flowQueryModel.getPagenum() - 1) * flowQueryModel.getPagesize());
        List<FlowNodeEntity> result = nodeMapper.queryPage(flowQueryModel);
        int count = nodeMapper.countByQuery(flowQueryModel);
        return new ApiResponse(result, count);
    }

    public ApiResponse updateNode(FlowNodeEntity nodeEntity) {
        nodeMapper.updateNode(nodeEntity);
        return ApiResponse.success(null);
    }

    public ApiResponse updateValid(FlowNodeEntity nodeEntity) {
        nodeMapper.updateValid(nodeEntity);
        return ApiResponse.success(null);
    }
}
