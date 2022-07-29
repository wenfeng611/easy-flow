package com.geekplus.flow.controller;

import com.geekplus.flow.entity.FlowNodeEntity;
import com.geekplus.flow.model.ApiResponse;
import com.geekplus.flow.model.FlowQueryModel;
import com.geekplus.flow.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("api/node")
public class FlowNodeController {

    @Autowired
    private NodeService nodeService;

    //添加节点
    @PostMapping("saveOne")
    public ApiResponse saveOne(@RequestBody FlowNodeEntity nodeEntity) {
        return nodeService.saveNode(nodeEntity);
    }

    //查询所有节点
    @GetMapping("list")
    public ApiResponse selectAll() {
        return nodeService.selectAllNodes();
    }

    @PostMapping("queryPage")
    public ApiResponse queryPage(@RequestBody FlowQueryModel flowQueryModel){
        return nodeService.queryPage(flowQueryModel);
    }

    //更新节点
    @PostMapping("update")
    public ApiResponse update(@RequestBody FlowNodeEntity nodeEntity) {
        return nodeService.updateNode(nodeEntity);
    }

    //更新显示字段
    @PostMapping("updateValid")
    public ApiResponse updateValid(@RequestBody FlowNodeEntity nodeEntity) {
        return nodeService.updateValid(nodeEntity);
    }
}
