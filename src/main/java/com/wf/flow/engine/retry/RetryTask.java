package com.wf.flow.engine.retry;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/9/8 14:05
 */

@Getter
@Setter
public class RetryTask {
    //会对比当前节点与数据库中是否一致
    private String currentNode;
    private String sceneCode;
    private String customId;


    public RetryTask(String currentNode, String sceneCode, String customId) {
        this.currentNode = currentNode;
        this.sceneCode = sceneCode;
        this.customId = customId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RetryTask retryTask = (RetryTask) o;
        return Objects.equals(currentNode, retryTask.currentNode) &&
                Objects.equals(sceneCode, retryTask.sceneCode) &&
                Objects.equals(customId, retryTask.customId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentNode, sceneCode, customId);
    }
}
