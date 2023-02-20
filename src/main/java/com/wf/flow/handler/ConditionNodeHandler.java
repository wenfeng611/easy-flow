package com.wf.flow.handler;

import com.alibaba.fastjson.JSON;
import com.wf.flow.entity.FlowConfigEntity;
import com.wf.flow.entity.FlowItemDetailEntity;
import com.wf.flow.entity.FlowRuleEntity;
import com.wf.flow.enums.FlowEnums;
import com.wf.flow.handler.context.FlowContext;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.GNode;
import com.wf.flow.service.strage.IFlowHandleStrategy;
import com.wf.flow.utils.RuleJudgeTool;
import com.wf.flow.utils.graph.GraphResolvor;
import io.micrometer.core.instrument.util.StringUtils;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 */

@Slf4j
@Component
public class ConditionNodeHandler extends AbstractNodeStateHandler {

    @Override
    public String getType() {
        return FlowEnums.FlowNodeTypeEnums.CONDITION.getCode();
    }

    @Override
    public FlowResult handle(FlowContext context) {

        //校验规则
        IFlowHandleStrategy fieldStrategy = context.getFieldStrategy();

        Integer businessId = context.getBusinessId();

        log.info("businessId: {} 判断节点",businessId);

        FlowConfigEntity flow = context.getFlow();
        GNode node = context.getCurrentNode();
        FlowRuleEntity rule =flowRuleMapper.findByFlowIdAndNodeId(flow.getId(),node.getId());
        log.info("businessId: {} 判断节点 查询rule {}", businessId,JSON.toJSONString(rule));


        FlowResult flowResult = new FlowResult();
        //没配置规则或者配置了不开启条件 直接返回这个节点
        if(Objects.isNull(rule) ||  StringUtils.isBlank(rule.getAutoJudgeRule())){
            //根据id从数据库找到当前节点然后返回
            flowResult.setStatus(FlowEnums.FlowItemStatusEnums.CONFIG_ERROR.getCode());

            log.info("businessId: {} 判断节点 当前节点：{} 没有规则异常  返回值： {}",businessId,node.getLabel(),JSON.toJSONString(flowResult));

            flowItemMapper.updateFlowItemStatusAndNodeId(FlowEnums.FlowItemStatusEnums.CONFIG_ERROR.getCode(),node.getId(),businessId);
            detailMapper.save(FlowItemDetailEntity.buildDefault(node,null,businessId, "配置异常"));
            return  flowResult;
        }

        //根据参数判断是否满足条件
        Pair<Boolean,String> pair = RuleJudgeTool.judgeFlowWithRule(rule.getAutoJudgeRule(),fieldStrategy,context.getBusinessId(),context.getBusinessData());
        //找到下一个节点
        String nextNodeId = pair.getValue();
        GNode nextNode =  GraphResolvor.getNodeByG6NodeId(flow.getGraphJson(),nextNodeId);

        NodeState nextState = chooseNextHandler(nextNode);

        //更新context里面的值
        context.setCurrentNode(nextNode);
        context.setState(nextState);
        log.info("businessId: {} 判断节点 当前节点：{} 有规则, 是否满足： {} nextNode: {}",businessId,node.getLabel(),pair.getKey(),nextNode.getLabel());
        flowItemMapper.updateFlowItemStatusAndNodeId(FlowEnums.FlowItemStatusEnums.RUNNING.getCode(),nextNode.getId(),businessId);

        detailMapper.save(FlowItemDetailEntity.buildDefault(node,nextNode,businessId, "判断节点"));
        return context.getState().handle(context);
    }

    @Override
    protected void handleBusiness(FlowContext context) { }
}
