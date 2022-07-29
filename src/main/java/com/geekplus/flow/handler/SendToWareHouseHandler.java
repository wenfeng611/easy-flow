package com.geekplus.flow.handler;

import com.geekplus.flow.entity.FlowRuleEntity;
import com.geekplus.flow.handler.context.FlowContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/7/28 10:06
 */

@Slf4j
@Component
public class SendToWareHouseHandler  extends AbstractNodeStateHandler {

    @Override
    public String getType() {
        return "sentToRms";
    }

    @Override
    protected void handleBusiness(FlowContext context) {
        log.info("businessId {} 送回库区",context.getBusinessId());

        //查询rule g6里面的id
        String nodeId = context.getCurrentNode().getId();
        FlowRuleEntity byFlowIdAndNodeId = flowRuleMapper.findByFlowIdAndNodeId(context.getFlow().getId(), nodeId);
        log.info("businessId {} 查询节点规则",byFlowIdAndNodeId.getAutoJudgeRule());
        log.info("businessId {} 像rms发送搬运消息",context.getBusinessId());
    }
}
