package com.wf.flow.engine.anno;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wenfeng.zhu
 * @description 实现接口 SceneDefinition 自动持久化到db
 * @date 2022/8/15 16:02
 */

@Component
@Target(ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Scene {

    String sceneName() default "";

    String sceneCode() default "";
}
