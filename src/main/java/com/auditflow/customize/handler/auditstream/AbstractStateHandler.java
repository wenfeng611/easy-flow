package com.auditflow.customize.handler.auditstream;

import com.auditflow.customize.enums.AuditStreamEnums;
import com.auditflow.customize.mapper.*;
import com.auditflow.customize.model.auditstream.GNode;
import com.auditflow.customize.service.auditstream.AuditRuleService;
import com.auditflow.customize.utils.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author wenfeng.zhu
 * @description
 */


public abstract class AbstractStateHandler implements State {

    public static final String FLOW_NOT_FOUNT = "未找到流程";
  
    @Resource
    protected AuditFlowMapper auditFlowMapper;

    @Resource
    protected AuditFlowItemMapper flowItemMapper;

    @Resource
    protected AuditFlowItemDetailMapper detailMapper;

    @Resource
    protected AuditRuleMapper auditRuleMapper;

    @Resource
    protected NodeMapper nodeMapper;

    @Autowired
    protected AuditRuleService ruleService;

    //根据node找到下一个处理的handler
    public  State chooseNextHandler(GNode node){
      //根据节点类型找到下一个处理的节点
      AuditStreamEnums.FlowNodeTypeEnums flowNodeType = AuditStreamEnums.FlowNodeTypeEnums.get(node.getNodeType());

      //默认结束 理论上不存在这种情况
      State nextState = new EndNodeHandler() ;
      switch (flowNodeType){
          case CUSTOMER:
              nextState= SpringContextUtils.getBean(CustomNodeHandler.class);
              break;
          case ABORD:
              nextState=SpringContextUtils.getBean(AbortNodeHandler.class);
              break;
          case END:
              nextState=SpringContextUtils.getBean(EndNodeHandler.class);
              break;
      }
      return nextState;
    }

}
