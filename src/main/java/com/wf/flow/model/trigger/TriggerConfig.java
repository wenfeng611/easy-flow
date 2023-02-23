package com.wf.flow.model.trigger;

import lombok.Data;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/10 15:47
 */

@Data
public class TriggerConfig {

    private String functionType; //groovy、springbean   输出必须是需要触发的流程的id
    private String content; //存储的脚本或者springbean的方法
    private String event; //触发的事件 其他流程等待节点配置相对应的事件
    private String triggerParam; //触发器通过配置传入的参数
}
