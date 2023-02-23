package com.wf.flow.utils;


import com.wf.flow.engine.node.handler.NodeState;
import com.wf.flow.engine.node.holder.NodeHandlerHolderService;
import com.wf.flow.utils.SpringContextUtils;

import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/1 17:54
 */
public class HolderSelectorUtil {

    public static NodeHandlerHolderService nodeHandlerHolderService;

    static {
        nodeHandlerHolderService = SpringContextUtils.getBean(NodeHandlerHolderService.class);
    }

    public static NodeState getNodeState(String type){
        NodeState nodeState = nodeHandlerHolderService.getStateHandler(type);
        if(Objects.nonNull(nodeState)) return nodeState;
        //可能是脚本实现 给个默认实现 再handler方法中会找对应的执行引擎执行
        return nodeHandlerHolderService.getStateHandler("default");

    }
}
