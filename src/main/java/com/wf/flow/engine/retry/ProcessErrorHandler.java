package com.wf.flow.engine.retry;


import com.wf.flow.engine.service.FlowProcessService;
import com.wf.flow.entity.FlowItemEntity;
import com.wf.flow.mapper.FlowItemMapper;
import com.wf.flow.model.FlowResult;
import com.wf.flow.utils.JSONUtil;
import com.wf.flow.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wenfeng.zhu
 * @description 异常重试机制
 * @date 2022/9/8 14:00
 */
@Slf4j
public class ProcessErrorHandler implements Runnable {

    private static LinkedBlockingQueue<RetryTask> blockingQueue = new LinkedBlockingQueue<>();

    private FlowProcessService flowProcessService = SpringContextUtils.getBean(FlowProcessService.class);
    private FlowItemMapper flowItemMapper = SpringContextUtils.getBean(FlowItemMapper.class);

    static {
        ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();
        pool.scheduleWithFixedDelay(new ProcessErrorHandler(), 60, 30, TimeUnit.SECONDS);
    }

    public static boolean addRetryTask(RetryTask task) {
        //防止重复加
        if (blockingQueue.contains(task)) {
            return true;
        }
        log.info("{} 新增重试任务 {}", task.getCustomId(), JSONUtil.objToJson(task));
        return blockingQueue.offer(task);
    }

    @Override
    public void run() {
        RetryTask currentTask = null;
        try {
            Map<String, List<RetryTask>> groupMap = blockingQueue.stream().collect(Collectors.groupingBy(RetryTask::getCurrentNode));
            //同一个节点异常的依次重试 第一个成功了再进行下一个
            out:
            for (String nodeId : groupMap.keySet()) {
                List<RetryTask> retryTasks = groupMap.get(nodeId);
                for (RetryTask retryTask : retryTasks) {
                    //直接移除 如果还是失败的话会自动重新加一个重试任务
                    currentTask = retryTask;
                    blockingQueue.remove(retryTask);
                    //校验节点是否跟当前节点一致 不一致则说明已经人工处理了异常
                    FlowItemEntity flowItemEntity = flowItemMapper.findByCustomId(retryTask.getCustomId());
                    if (StringUtils.isBlank(flowItemEntity.getCurrentNodeId()) || !flowItemEntity.getCurrentNodeId().equals(retryTask.getCurrentNode())) {
                        log.info("{} 当前节点数据不匹配 不重试", retryTask.getCustomId());
                    } else {
                        FlowResult flowResult = flowProcessService.continueFlow(retryTask.getSceneCode(), retryTask.getCustomId(), "retry", null, new HashMap());
                        if (!flowResult.isSuccess()) {
                            //失败了就不便利这个节点下的其他流程
                            continue out;
                        }
                    }

                }
            }
        } catch (Exception e) {
            log.warn("CustomId:{}重试发生异常", currentTask != null ? currentTask.getCustomId() : "", e);
        }
    }

}
