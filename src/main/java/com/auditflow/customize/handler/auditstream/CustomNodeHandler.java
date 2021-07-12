package com.auditflow.customize.handler.auditstream;

import com.alibaba.fastjson.JSON;
import com.auditflow.customize.entity.AuditFlowEntity;
import com.auditflow.customize.entity.AuditFlowItemDetailEntity;
import com.auditflow.customize.entity.AuditRuleEntity;
import com.auditflow.customize.entity.NodeEntity;
import com.auditflow.customize.enums.AuditStreamEnums;
import com.auditflow.customize.handler.auditstream.context.FlowContext;
import com.auditflow.customize.model.auditstream.FlowResult;
import com.auditflow.customize.model.auditstream.GNode;
import com.auditflow.customize.service.auditstream.strage.IFlowHandleStrategy;
import com.auditflow.customize.utils.RuleJudgeTool;
import com.auditflow.customize.utils.graph.GraphResolvor;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description  自定义节点的状态处理
 */

@Slf4j
@Component
public class CustomNodeHandler extends AbstractStateHandler {

    @Override
    public FlowResult handle(FlowContext context) {
        //拿到当前节点
        GNode node = context.getCurrentNode();
        AuditFlowEntity flow = context.getFlow();
        Integer businessId = context.getBusinessId();
        //校验规则
        IFlowHandleStrategy fieldStrategy = context.getFieldStrategy();

        FlowResult flowResult = new FlowResult();
        flowResult.setSuccess(true);
        flowResult.setBusinessId(businessId);

        //查找规则 判断是否需要规则
        AuditRuleEntity rule =auditRuleMapper.findByFlowIdAndNodeId(flow.getId(),node.getId());

        //没配置规则或者配置了不开启条件 直接返回这个节点 等待人工审核
        if(Objects.isNull(rule) || !rule.isEnableCondition() || StringUtils.isBlank(rule.getAutoJudgeRule())){
            //根据id从数据库找到当前节点然后返回
            NodeEntity nodeEntity =nodeMapper.findTopById(node.getNodeId());
            flowResult.setStatus(AuditStreamEnums.FlowAuditStatusEnums.RUNNING.getCode());

            log.info("businessId: {} 自定义节点 当前节点：{} 没有规则 需要人工审核  返回值： {}",businessId,node.getLabel(),JSON.toJSONString(flowResult));
            String userids = fieldStrategy.getAuditUsersByNodeType(nodeEntity.getType(),nodeEntity.getUserIds(),businessId);
            flowResult.setAuditUserids(userids);

            flowItemMapper.updateAuditStatusAndNodeIdAndAuditUserids(null,node.getId(),businessId,userids);
            detailMapper.save(AuditFlowItemDetailEntity.buildDefault(node,null,businessId,AuditStreamEnums.FlowAuditStatusEnums.RUNNING.getCode(),"等待人工审核"));
            return  flowResult;
        }

        //根据参数判断是否满足条件
        boolean rulePass = RuleJudgeTool.judgeFlowWithRule(rule.getAutoJudgeRule(),fieldStrategy,rule.getStatisfyType(),context.getBusinessId());
       //找到下一个节点
        String nextNodeId = rulePass?rule.getSuccessToNodeId():rule.getFailToNode();
        GNode nextNode =  GraphResolvor.getNodeByG6NodeId(flow.getGraphJson(),nextNodeId);

        State nextState = chooseNextHandler(nextNode);

        //更新context里面的值
        context.setCurrentNode(nextNode);
        context.setState(nextState);
        log.info("businessId: {} 自定义节点 当前节点：{} 有规则, 是否满足： {} nextNode: {}",businessId,node.getLabel(),rulePass,nextNode.getLabel());
        flowItemMapper.updateAuditStatusAndNodeId(null,nextNode.getId(),businessId);

        detailMapper.save(AuditFlowItemDetailEntity.buildDefault(node,nextNode,businessId,AuditStreamEnums.FlowAuditStatusEnums.RUNNING.getCode(),"自动规则审核"));
        return context.getState().handle(context);
    }
}
