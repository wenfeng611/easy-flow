package com.wf.flow.engine.notify;

import lombok.Data;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/12/6 14:21
 */

@Data
public class AutoNotifyTask {

    //会对比当前节点与数据库中是否一致
    private String currentNode;
    private String sceneCode;
    private String customId;

    private int fixDelay;

    public AutoNotifyTask(String currentNode, String sceneCode, String customId, int fixDelay) {
        this.currentNode = currentNode;
        this.sceneCode = sceneCode;
        this.customId = customId;
        this.fixDelay = fixDelay;
    }
}
