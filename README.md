# flow




流程引擎

<img width="470" alt="a92fb81555a34605f4dc5b9a6f068b7" src="https://user-images.githubusercontent.com/58583878/220337894-73fb0c29-b82b-472e-a56d-e85c03137ce6.png">

<img width="438" alt="437d04c86f8e418efcf29ff2991e938" src="https://user-images.githubusercontent.com/58583878/220337931-f67e495d-289d-47c2-9fa9-d00b64e52ba1.png">



•1.场景定义
•2.场景流程工厂（流程实例字段通过脚本扩展）
•3.节点扩展（新增节点、节点执行器扩展）
•4.流程图连线规则解析扩展
•5.页面配置
•6.日志打印
•7.流程启动继续
•8.脚本执行接口

1.场景定义


@Scene注解 会自动插入t_flow_scene表
 sceneCode: 场景唯一code
 sceneName: 场景名称


实现SceneDefinition接口  会自动插入t_flow_scene_field表
 defineSceneField方法返回当前场景所需要的字段


如果当前场景流程需要声明成是一个子流程节点 则需要同时加上@FlowNode注解声明成子流程节点 默认处理子流程FlowHandlerRunner




2.场景流程工厂（流程实例字段通过脚本扩展）


1.实现IFlowFactory接口  可参考PickingCompleteFlowFactory
 参数： customId业务传入的唯一id 注意 不同业务如果使用的是自增id可以配置不同前缀 防止冲突
 返回值 FlowService 流程类 可以调用initFlow方法新建。需要实现一个字段获取的IFlowHandleStrategy接口，后续流程会调用这个方法获取需要的字段

2.通过脚本定义 在 t_common_script表中添加记录（可用于扩展）
 script_code 场景code
 script_group 固定值FLOW_FACTORY
 context_entry 脚本 返回值必须是 FlowService




3.节点扩展（新增节点、节点执行器扩展）


NodeRunner
 AbstractRunner
  NodeHandlerRunner
  GroovyNodeRunner
  TriggerHandlerRunner
  FlowHandlerRunner


根据节点表不同的functionType找到对应的NodeRunner执行逻辑


1.普通节点需要执行节点中代码逻辑（NodeHandlerRunner）
 1.1 继成AbstractNodeStateHandler类实现handleBusiness方法
  参数：context 流程上下文对象
     getNodeConfigEntityOrElseNull方法可以取到节点的特定配置（页面显示内容可以在t_common_metadata_entity_type、t_common_metadata_column中配置）
        params  当前节点需要的输入参数（再@FlowNode中声明的inputParams 都可以通过params#get取到）,参数与不同流程中的参数对应关系可以在页面中配置
 1.2 类上声明@FlowNode注解  会自动插入t_flow_node表
   nodeName 节点名称
   nodeDes  节点描述(默认和节点名称一致)
   executeType 执行完节点逻辑是否需要找下一个节点继续执行默认需要 例：开始节点执行完之后需要找下一个节点继续执行则配置1；等待节点需要等待人工操作pda等则配置0
   outputParams 节点的输出参数，后续节点可以使用 例: 占用暂存位节点 输出占用是否成功以及成功的lattice，后续供RS搬运
   inputParams  节点需要的输入参数 例：像rms添加箱子节点 boxCode和latticeCode就是输入参数，具体映射到流程上下文的哪个字段再页面中配置
   needInsert  需要插入到数据库  默认1
   functionType 不同的类型对应的处理不一样 每种类型都会有一个处理器实现NodeRunner接口。 对应t_flow_node表中function_type字段
       nodeHandler:     调用handleBusiness方法 默认
       flowHandler:     子流程节点的处理
       groovy:          按照groovy脚本处理
       triggerHandler:  触发节点 用于当前流程需要触发其他流程往下运行
   groupName: 分组名称 前端可以折叠显示

2.特殊节点（扩展NodeRunner 实现 也可以继成AbstractRunner）
 触发器节点 functionType为triggerHandler 对应的处理是在TriggerHandlerRunner中
  作用：当需要流程之间有交互，A流程结束之后B流程才能继续进行的时候需要实现 例如：抢占暂存位 A抢到了B没有空闲位置，A释放的时候需要触发B流程重新抢占
  页面配置参数：
   functionType: spring-bean
        groovy
   内容： spring-bean的话配置一个bean的名称 需要实现ProcessTrigger接口 用于获取需要唤醒的流程id
     groovy的话配置groovy脚本 会自动调用execute方法
   事件名称：只唤醒特定事件的节点，因为等待节点可能有多个，只需要唤醒特定状态的等待节点
   触发器参数：自行配置 可选   会直接传到ProcessTrigger的参数中 业务自己使用


 子流程节点 functionType为flowHandler 对应的处理是在FlowHandlerRunner中
  作用：是一条完成闭合流程的同时可以当做子流程引入到别的流程。 主流程看起来简洁
  定义场景的时候同事声明@FlowNode注解




备注 当functionType为groovy的时候会执行script_content中的脚本




4.流程图连线规则解析扩展


RuleParser 
 AbstractRuleParser  
  默认实现DefaultSimpleParser
通过executeType不同执行不同的解析逻辑  可以通过executeType不同来扩展解析器  
 继成AbstractRuleParser<T> 泛型会自动吧config对象映射到T类型  只需实现judgeRule方法即可


default解析器解析的json结构（前端连线规则默认保存的格式）
{
    "executeType": "default",
    "config": {
        "ruleItems": [
            {
                "type": "0",
                "target": "2",
                "field": "container_type",
                "symbol": "="
            },
            {
                "type": "0",
                "target": "true",
                "field": "currentContainerLack",
                "symbol": "="
            }
        ],
        "statisfyType": "0"
    }
}







5.页面配置


1.节点配置 双击节点
 1.1 页面节点可实现自定义配置
 1.2 定义了之后页面就会出现对应的表单，在节点的handleBusiness中可以调用context上线文的getNodeConfigEntityOrElseNull方法获取对应的特殊配置
 1.3 例如：送到指定库区节点 需要配置库区、变更状态节点需要配置需要变更的状态等等
2.连接线的配置 双击线
 2.1 双击线 可以配置对应的条件，同一个节点出来多条线会依次判断走满足条件的一条线，如果都不满足，会有异常。
 2.1 目前线的配置用的是默认实现 根据executeType找到对应的rule解析器处理  解析器需要继成AbstractRuleParser
   当前都是default走的是DefaultSimpleParser处理 后续可以使用spel则只需要继成AbstractRuleParser扩展即可




6.日志打印


AbstractNodeStateHandler中有logInfo、logWarn、logError方法提供统一日志模板
或者用ProcessLogUtil.log###打印




7.流程启动继续


FlowProcessService
 initAndStartFlow
  sceneCode：场景的code
  uniqueId：唯一id 不同的业务场景需要都保持唯一
  businessData：业务数据会透传到IFlowHandleStrategy接口中
  params：节点参数 一般pda接口操作会需要传一些参数供流程使用，params参数的优先级高于IFlowHandleStrategy接口
 continueFlow
  sceneCode：场景的code
  uniqueId：唯一id 不同的业务场景需要都保持唯一
  operateInfoRecord： 操作记录
  eventName: 触发对应事件的等待节点
  businessData：业务数据会透传到IFlowHandleStrategy接口中
  params：节点参数 一般pda接口操作会需要传一些参数供流程使用，params参数的优先级高于IFlowHandleStrategy接口

8.脚本执行接口


可以用于三方开发的时候将逻辑写在脚本中
POST /api/groovy/execute
body: {
    "scriptCode": "aaa",
    "params":{
        "a": "test"
    }
}
