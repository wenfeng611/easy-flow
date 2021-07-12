package com.auditflow.customize.controller.auditstream;

import com.auditflow.customize.entity.NodeEntity;
import com.auditflow.customize.model.ApiResponse;
import com.auditflow.customize.model.auditstream.AuditStreamQueryModel;
import com.auditflow.customize.service.auditstream.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("audit/node")
public class NodeController {

    @Autowired
    private NodeService nodeService;

    //添加审核节点
    @PostMapping("saveOne")
    public ApiResponse saveOne(@RequestBody NodeEntity nodeEntity) {
        return nodeService.saveNode(nodeEntity);
    }

    //查询所有节点
    @GetMapping("list")
    public ApiResponse selectAll() {
        return nodeService.selectAllNodes();
    }

    @PostMapping("queryPage")
    public ApiResponse queryPage(@RequestBody AuditStreamQueryModel auditStreamQueryModel){
        return nodeService.queryPage(auditStreamQueryModel);
    }

    //更新审核节点
    @PostMapping("update")
    public ApiResponse update(@RequestBody NodeEntity nodeEntity) {
        return nodeService.updateNode(nodeEntity);
    }

    //更新显示字段
    @PostMapping("updateValid")
    public ApiResponse updateValid(@RequestBody NodeEntity nodeEntity) {
        return nodeService.updateValid(nodeEntity);
    }
}
