package com.wf.flow.engine.notify;

import com.wf.flow.engine.service.FlowProcessService;
import com.wf.flow.entity.FlowItemEntity;
import com.wf.flow.mapper.FlowItemMapper;
import com.wf.flow.model.FlowResult;
import com.wf.flow.utils.JSONUtil;
import com.wf.flow.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author wenfeng.zhu
 * @description
 * @date 2022/12/6 14:22
 */
@Slf4j
public class AutoNotifyTaskHandler {

    private static ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(5);
    private static FlowItemMapper flowItemMapper = SpringContextUtils.getBean(FlowItemMapper.class);
    private static FlowProcessService flowProcessService = SpringContextUtils.getBean(FlowProcessService.class);

    public static void addSchedulerTask(AutoNotifyTask autoNotifyTask){
        scheduledExecutor.schedule(() -> {
            //校验节点是否跟当前节点一致 不一致则说明已经人工处理了异常
            FlowItemEntity flowItemEntity = flowItemMapper.findByCustomId(autoNotifyTask.getCustomId());
            if (StringUtils.isBlank(flowItemEntity.getCurrentNodeId()) || !flowItemEntity.getCurrentNodeId().equals(autoNotifyTask.getCurrentNode())) {
                log.info("{} 自动唤醒节点数据不匹配 不继续执行", autoNotifyTask.getCustomId());
            }else{
                FlowResult flowResult = flowProcessService.continueFlow(autoNotifyTask.getSceneCode(), autoNotifyTask.getCustomId(), "autoNotify", null, new HashMap());
                log.info("{} 自动唤醒节点数据 结果 {}",autoNotifyTask.getCustomId(), JSONUtil.objToJson(flowResult));
            }
        },autoNotifyTask.getFixDelay(), TimeUnit.SECONDS);
    }
}
