package com.wf.flow.context;

import com.wf.flow.engine.node.handler.NodeState;
import com.wf.flow.engine.node.handler.impl.StartHandler;
import com.wf.flow.engine.strategy.IFlowHandleStrategy;
import com.wf.flow.utils.HolderSelectorUtil;
import com.wf.flow.utils.NodeConfigParseTool;
import com.wf.flow.utils.ParamGetTool;
import com.wf.flow.entity.FlowConfigEntity;
import com.wf.flow.entity.FlowRuleEntity;
import com.wf.flow.mapper.FlowRuleMapper;
import com.wf.flow.model.FlowResult;
import com.wf.flow.model.GNode;
import com.wf.flow.utils.SpringContextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description   流程状态记录状态对象
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlowContext {

    private NodeState state;

    private String sceneCode;
    private String mainSceneCode;  //主场景
    private IFlowHandleStrategy fieldStrategy;

    private String parentNodeId;  //上一个节点 g6生成的
    private GNode currentNode;
    private FlowConfigEntity flow;
    private Integer businessId;
    private String customId;

    private String username;

    private Object businessData;

    private Map<String, Object> params;

    //默认创建Condition处理状态
    public FlowContext(IFlowHandleStrategy fieldStrategy, String sceneCode, String username) {
        this.fieldStrategy = fieldStrategy;
        this.sceneCode = sceneCode;
        this.username = username;
        this.state=  SpringContextUtils.getBean(StartHandler.class);
    }

    public FlowContext(IFlowHandleStrategy fieldStrategy, String sceneCode, GNode node, Integer businessId, String username) {
        this.fieldStrategy = fieldStrategy;
        this.sceneCode = sceneCode;
        this.currentNode= node;
        this.businessId=businessId;
        this.username=username;
        this.state = HolderSelectorUtil.getNodeState(node.getNodeType());
    }

    public FlowContext(IFlowHandleStrategy fieldStrategy, String sceneCode, GNode node, Integer businessId, FlowConfigEntity flow, String username) {
        this(fieldStrategy,sceneCode,node,businessId,username);
        this.flow=flow;
    }

    public FlowResult run(){
        return state.handle(this);
    }

    /**
     * 获取当前节点的规则
     * @return
     */
    public <T> T getNodeConfigEntityOrElseNull(Class<T> clazz) {
        FlowRuleMapper flowRuleMapper = SpringContextUtils.getBean(FlowRuleMapper.class);
        FlowRuleEntity rule = flowRuleMapper.findByFlowIdAndNodeId(getFlow().getId(), getCurrentNode().getId());
        if(Objects.isNull(rule) || StringUtils.isEmpty(rule.getAutoJudgeRule())){
            return null;
        }
        return NodeConfigParseTool.getNodeConfigEntity(rule.getAutoJudgeRule(),clazz);
    }

    public Object getParamValue(String fieldName){
        return ParamGetTool.getFieldValueFromContextFirst(fieldName,this);
    }

    public String getCurrentNodeDisplayName(){
        if(Objects.isNull(getCurrentNode())) return Strings.EMPTY;
        return getCurrentNode().getLabel();
    }

}