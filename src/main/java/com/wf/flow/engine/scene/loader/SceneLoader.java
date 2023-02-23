package com.wf.flow.engine.scene.loader;


import com.wf.flow.engine.anno.FlowNode;
import com.wf.flow.engine.anno.Scene;
import com.wf.flow.engine.scene.SceneDefinition;
import com.wf.flow.entity.FlowNodeEntity;
import com.wf.flow.entity.FlowSceneEntity;
import com.wf.flow.entity.FlowSceneFieldEntity;
import com.wf.flow.mapper.FlowNodeMapper;
import com.wf.flow.mapper.FlowSceneFieldMapper;
import com.wf.flow.mapper.FlowSceneMapper;
import com.wf.flow.model.FlowQueryModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wenfeng.zhu
 * @description 加载场景以及字段
 * @date 2022/8/15 16:14
 */
@Slf4j
@Component
public class SceneLoader {

    @Autowired(required = false)
    List<SceneDefinition> sceneDefinitions;
    @Autowired
    FlowSceneMapper flowSceneMapper;
    @Autowired
    FlowSceneFieldMapper flowSceneFieldMapper;
    @Autowired
    FlowNodeMapper flowNodeMapper;
    @PostConstruct
    private void init() {
        if(!CollectionUtils.isEmpty(sceneDefinitions)){
            loadScene();
            log.info("Process load scene  end size：{}",sceneDefinitions.size());
        }
    }

    private void loadScene() {
        for (SceneDefinition sceneDefinition : sceneDefinitions) {
            //判断是否有@FlowNode注解 有的话代表流程是子流程 加入节点表
            FlowNode flowNode = sceneDefinition.getClass().getDeclaredAnnotation(FlowNode.class);

            //必须要有@Scene 注解
            Scene scene = sceneDefinition.getClass().getDeclaredAnnotation(Scene.class);
            if(Objects.isNull(scene)){
                log.warn("定义场景必须添加@Scene注解定义场景唯一标识");
                continue;
            }
            if(StringUtils.isBlank(scene.sceneCode()) || StringUtils.isBlank(scene.sceneName())){
                log.warn("定义场景必须定义场景唯一标识sceneCode 和名称 sceneName");
                continue;
            }
            String sceneCode = scene.sceneCode();

            FlowSceneEntity flowSceneEntity = flowSceneMapper.findByCode(sceneCode);
            Date date = new Date();
            if(Objects.isNull(flowSceneEntity)){
                //插入场景表
                FlowSceneEntity needInsert = new FlowSceneEntity();
                needInsert.setSceneCode(sceneCode);
                needInsert.setSceneName(scene.sceneName());
                needInsert.setCreateTime(date);
                needInsert.setUpdateTime(date);
                needInsert.setCreaterName("System");
                needInsert.setIsValid(1);
                flowSceneMapper.save(needInsert);
            }

            //新增子流程节点
            if(Objects.nonNull(flowNode)){
                //防止重复插入
                FlowNodeEntity exist = flowNodeMapper.findTopByType(sceneCode);
                if(Objects.isNull(exist)){
                    FlowNodeEntity nodeEntity = new FlowNodeEntity();
                    nodeEntity.setExecuteType(flowNode.executeType());
                    nodeEntity.setType(sceneCode);  //type 就是场景code
                    nodeEntity.setFunctionType("flowHandler");
                    nodeEntity.setCreaterName("System");
                    nodeEntity.setCreateTime(new Date());
                    nodeEntity.setInputParams(flowNode.inputParams());
                    nodeEntity.setOutputParams(flowNode.outputParams());
                    nodeEntity.setIsValid(1);
                    nodeEntity.setNodeName(flowNode.nodeName());
                    nodeEntity.setGroupName(flowNode.groupName());
                    nodeEntity.setNodeDes(StringUtils.isBlank(flowNode.nodeDes())?flowNode.nodeName():flowNode.nodeDes());
                    flowNodeMapper.save(nodeEntity);
                }
            }

            Map<String, String> fieldMap = sceneDefinition.defineSceneField();
            if(Objects.isNull(fieldMap) || fieldMap.size() == 0) continue;

            FlowQueryModel queryModel = new FlowQueryModel();
            queryModel.setSceneCode(sceneCode);
            List<FlowSceneFieldEntity> flowSceneFieldEntities = flowSceneFieldMapper.queryAllByScene(queryModel);
            Set<String> existField = flowSceneFieldEntities.stream().filter(Objects::nonNull).map(FlowSceneFieldEntity::getFieldName).collect(Collectors.toSet());
            List<FlowSceneFieldEntity> needInsertSceneFields = new ArrayList<>();
            for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                if(existField.contains(entry.getKey())) continue;

                FlowSceneFieldEntity sceneFieldEntity = new FlowSceneFieldEntity();
                sceneFieldEntity.setSceneCode(sceneCode);
                sceneFieldEntity.setFieldDescription(entry.getValue());
                sceneFieldEntity.setFieldName(entry.getKey());
                sceneFieldEntity.setCreateTime(date);
                sceneFieldEntity.setCreaterName("System");
                sceneFieldEntity.setIsValid(1);
                needInsertSceneFields.add(sceneFieldEntity);
            }
            if(CollectionUtils.isEmpty(needInsertSceneFields)) continue;

            flowSceneFieldMapper.saveBatch(needInsertSceneFields);
        }
    }
}
