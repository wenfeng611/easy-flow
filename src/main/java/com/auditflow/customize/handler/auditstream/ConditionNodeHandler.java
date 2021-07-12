package com.auditflow.customize.handler.auditstream;

import com.auditflow.customize.entity.AuditFlowEntity;
import com.auditflow.customize.entity.AuditFlowItemDetailEntity;
import com.auditflow.customize.entity.AuditFlowItemEntity;
import com.auditflow.customize.entity.AuditRuleEntity;
import com.auditflow.customize.enums.AuditStreamEnums;
import com.auditflow.customize.handler.auditstream.context.FlowContext;
import com.auditflow.customize.model.auditstream.FlowResult;
import com.auditflow.customize.model.auditstream.GNode;
import com.auditflow.customize.service.auditstream.strage.IFlowHandleStrategy;
import com.auditflow.customize.utils.RuleJudgeTool;
import com.auditflow.customize.utils.graph.GraphResolvor;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wenfeng.zhu
 * @description
 */

@Slf4j
@Component
public class ConditionNodeHandler extends AbstractStateHandler {

    @Override
    public FlowResult handle(FlowContext context) {
        String username = context.getUsername();
        String sceneCode = context.getSceneCode();
        //校验规则
        IFlowHandleStrategy fieldStrategy = context.getFieldStrategy();

        //如果第一次没找到流程 重新启动流程的时候会带着businessId
        if(context.getBusinessId() == null || context.getBusinessId() == 0){
            //需要新建流程
            AuditFlowItemEntity auditFlowItemEntity = initFlowItem(username,sceneCode);
            //保存 auditFlowItemEntity  其id就是业务需要保存的id
            flowItemMapper.save(auditFlowItemEntity);
            //设置businessId
            context.setBusinessId(auditFlowItemEntity.getId());
        }

        Integer businessId = context.getBusinessId();

        log.info("businessId: {} 开始流程 查找条件节点",businessId);

        //根据场景查询流程
        List<AuditFlowEntity> sceneFlows = auditFlowMapper.findBySceneCode(sceneCode);
        //找到第一个满足条件的流程
        AuditFlowEntity finalFlow = filterFirstValidFlow(sceneFlows,fieldStrategy,businessId);
        //没有找到流程
        if(Objects.isNull(finalFlow)){
            //update 至 auditFlowItemEntity.auditStatus.NOTFOUND
            log.info("businessId: {} 没有找到流程 更新item 状态为 2未找到流程",businessId);
            flowItemMapper.updateAuditStatusAndNodeId(AuditStreamEnums.FlowAuditStatusEnums.NOTFOUND.getCode(),null,businessId);
            detailMapper.save(initDetail(businessId,AuditStreamEnums.FlowAuditStatusEnums.NOTFOUND.getCode()));

            return FlowResult.fail(FLOW_NOT_FOUNT,businessId,AuditStreamEnums.FlowAuditStatusEnums.NOTFOUND.getCode());
        }

        //更新item流程id
        flowItemMapper.updateFlowId(businessId,finalFlow.getId());

        //找到条件节点下面的  如果多条就是第一个
        Pair<GNode, GNode> gNodePair = GraphResolvor.getFirstNode(finalFlow.getGraphJson());
        GNode conditionNode = gNodePair.getValue0();
        GNode gNode = gNodePair.getValue1();

        State nextState = chooseNextHandler(gNode);

        context.setFlow(finalFlow);
        context.setCurrentNode(gNode);
        context.setState(nextState);
        //更新item的状态为 3审核中
        flowItemMapper.updateAuditStatusAndNodeId(AuditStreamEnums.FlowAuditStatusEnums.RUNNING.getCode(),gNode.getId(),businessId);
        log.info("businessId: {} 条件节点 nextNode：{} 有规则 更新item状态 3审核中",businessId,gNode.getLabel());
        detailMapper.save(AuditFlowItemDetailEntity.buildDefault(conditionNode,gNode,businessId,AuditStreamEnums.FlowAuditStatusEnums.RUNNING.getCode(),"系统自动"));
        return context.getState().handle(context);
    }

    //找第一个满足条件的flow
    private AuditFlowEntity filterFirstValidFlow(List<AuditFlowEntity> sceneFlows, IFlowHandleStrategy fieldStrategy, Integer businessId) {
        if(CollectionUtils.isEmpty(sceneFlows)) {
            return null;
        }

        //查询条件规则
        List<AuditRuleEntity> conditions = ruleService.findConditionRule(sceneFlows.stream().map(AuditFlowEntity::getId).collect(Collectors.toList()));

        //查不到说明没有配置规则 直接取第一条
        if(CollectionUtils.isEmpty(conditions)) return sceneFlows.get(0);

        Map<Integer, AuditRuleEntity> ruleFlowIdJudgeRuleMap =  conditions.stream().collect(Collectors.toMap(AuditRuleEntity::getFlowId, r->r,(a, b)->b));

        AuditFlowEntity result = null;
        AuditRuleEntity rule= null;
        for (AuditFlowEntity sceneFlow : sceneFlows) {
            //不包含这个流程的id 或者 autoJudgeRule是空说明没有配置
            if(!ruleFlowIdJudgeRuleMap.containsKey(sceneFlow.getId()) || !(rule=ruleFlowIdJudgeRuleMap.get(sceneFlow.getId())).isEnableCondition() || StringUtils.isBlank(rule.getAutoJudgeRule())){
                result = sceneFlow;
                break;
            }
            //根据参数判断是否满足条件
            if(RuleJudgeTool.judgeFlowWithRule(rule.getAutoJudgeRule(),fieldStrategy,rule.getStatisfyType(),businessId)) {
                result = sceneFlow;
                break;
            }
        }
        return result;
    }

    private AuditFlowItemEntity initFlowItem(String username, String sceneCode) {
        return AuditFlowItemEntity.builder()
                .flowId(0)
                .currentNodeId("")
                .auditStatus(AuditStreamEnums.FlowAuditStatusEnums.WAITING.getCode())
                .sceneCode(sceneCode)
                .createrName(username)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
    }

    private AuditFlowItemDetailEntity initDetail(Integer businessId, Integer auditStatus) {
        return AuditFlowItemDetailEntity.builder()
                .flowItemId(businessId)
                .auditResult(FLOW_NOT_FOUNT)
                .auditStatus(auditStatus)
                .createTime(new Date())
                .build();
    }
}
