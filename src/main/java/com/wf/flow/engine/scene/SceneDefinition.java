package com.wf.flow.engine.scene;

import java.util.Map;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/8/15 16:12
 */
public interface SceneDefinition {

    /**
     * 返回值为 key：fieldName    value：字段描述
     * @return
     */
    Map<String, String> defineSceneField();
}