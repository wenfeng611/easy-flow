package com.wf.flow.engine.anno;

import org.apache.logging.log4j.util.Strings;
import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wenfeng.zhu
 * @description 定义流程节点  NodeHandlerHolderServiceImpl 中还会自动加载到数据库中
 * @date 2022/8/8 19:25
 */

@Order()
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface FlowNode {

    /**
     * 节点名称
     */
    String nodeName() default Strings.EMPTY;

    /**
     * 节点描述
     */
    String nodeDes() default Strings.EMPTY;

    /**
     * 执行完节点逻辑是否需要找下一个节点继续执行默认需要
     */
    int executeType() default 1;

    /**
     * 节点的输出参数
     */
    String outputParams() default Strings.EMPTY;

    /**
     * 节点需要的输入参数
     */
    String inputParams() default Strings.EMPTY;

    /**
     * 是否需要系统启动的时候自动插入数据库
     */
    boolean needInsert() default true;

    /**
     * 执行方式
     */
    String functionType() default "nodeHandler";

    /**
     * 分组名称
     */
    String groupName() default "base";
}
