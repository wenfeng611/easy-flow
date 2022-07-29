package com.geekplus.flow.controller;

import com.geekplus.flow.model.ApiResponse;
import com.geekplus.flow.model.FlowItemSearchModel;
import com.geekplus.flow.service.FlowItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wenfeng.zhu
 * @description
 */

@RestController
@RequestMapping("api/flowitem")
public class FlowItemController {

    @Autowired
    private FlowItemService flowItemService;


    @PostMapping("queryPage")
    public ApiResponse queryPage(@RequestBody FlowItemSearchModel searchModel){
        return flowItemService.queryPage(searchModel);
    }

    @GetMapping("restart")
    public ApiResponse queryPage(int businessId,String sceneCode){
        return flowItemService.restart(businessId,sceneCode);
    }
}
