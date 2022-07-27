SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for xc_audit_flow
-- ----------------------------
DROP TABLE IF EXISTS `audit_flow`;
CREATE TABLE `xc_audit_flow` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '流程名称',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '流程状态 0未启用  1启用  2禁用',
 `create_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '添加时间',
  `creater_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `form_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '表单地址',
  `scene_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '场景对应业务的code',
  `update_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '更新时间',
  `graph_json` text COLLATE utf8mb4_unicode_ci COMMENT 'G6流程图生成数据',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `scene_code` (`scene_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='审核流配置';


-- ----------------------------
-- Table structure for xc_audit_flow_item
-- ----------------------------
DROP TABLE IF EXISTS `audit_flow_item`;
CREATE TABLE `xc_audit_flow_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_id` int(11) NOT NULL DEFAULT '0' COMMENT '流程id',
  `current_node_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '节点id',
  `audit_status` int(2) NOT NULL DEFAULT '0' COMMENT '流程状态 1待审核 2未找到流程 3审核中 4已完成 5驳回',
  `add_time` datetime NOT NULL DEFAULT '2010-01-01 00:00:00' COMMENT '添加时间',
  `update_time` datetime NOT NULL DEFAULT '2010-01-01 00:00:00' COMMENT '更新时间',
  `creator` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `scene_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '场景对应业务的code',
  `audit_user_ids` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '审核人员id ,隔开',
  `job_numbers` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '待审核人的工号 , 隔开',
  `creator_job_number` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '提交人工号'
  PRIMARY KEY (`id`) USING BTREE,
  KEY `flow_id` (`flow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='审核流程Item';

-- ----------------------------
-- Table structure for xc_audit_flow_item_detail
-- ----------------------------
DROP TABLE IF EXISTS `audit_flow_item_detail`;
CREATE TABLE `xc_audit_flow_item_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_item_id` int(11) NOT NULL DEFAULT '0' COMMENT '流程id',
  `audit_username` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '审核人',
  `audit_result` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '审核结果备注',
  `current_g6_node_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '当前G6node的id',
  `current_g6_node_label` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '当前G6node的id',
  `next_g6_node_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '当前G6node的id',
  `next_g6_node_label` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '当前G6node的id',
  `audit_status` int(2) NOT NULL DEFAULT '0' COMMENT '流程状态 1待审核 2未找到流程 3审核中 4已完成 5驳回',
  `create_time` datetime NOT NULL DEFAULT '2010-01-01 00:00:00' COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `flow_item_id` (`flow_item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='审核流程detail';


-- ----------------------------
-- Table structure for xc_audit_node
-- ----------------------------
DROP TABLE IF EXISTS `audit_node`;
CREATE TABLE `xc_audit_node` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `node_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '节点名称',
  `type` int(2) NOT NULL DEFAULT '0' COMMENT '审核类型',
  `create_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '添加时间',
  `creater_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `audit_userids` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '人员id ,隔开',
  `is_valid` tinyint(2) NOT NULL DEFAULT '0' COMMENT '有效状态 0 前台不展示 1 前后台都展示',
  `user_ids` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '人员的工号id  , 隔开'
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='审核节点';


-- ----------------------------
-- Table structure for xc_audit_rule
-- ----------------------------
DROP TABLE IF EXISTS `audit_rule`;
CREATE TABLE `xc_audit_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `enable_condition` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启用条件审核',
  `statisfy_type` int(2) NOT NULL DEFAULT '0' COMMENT '0 全部满足  1任意',
  `success_to_node_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '成功流转节点',
  `fail_to_node` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '失败流转节点',
  `auto_judge_rule` text COLLATE utf8mb4_unicode_ci COMMENT 'G6流程图生成数据',
  `create_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '添加时间',
  `creater_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `flow_id` int(11) NOT NULL DEFAULT '0' COMMENT '流程id',
  `update_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '更新时间',
  `g6node_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'g6节点id',
  `node_type` int(2) NOT NULL DEFAULT '0' COMMENT '节点类型  1申请条件、2自定义、3结束、4驳回',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `flow_id` (`flow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='审核流规则';


-- ----------------------------
-- Table structure for xc_audit_scene
-- ----------------------------
DROP TABLE IF EXISTS `audit_scene`;
CREATE TABLE `xc_audit_scene` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `scene_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '场景名称',
  `scene_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '场景对应业务的code',
  `create_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '添加时间',
  `update_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '更新时间',
  `creater_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `is_valid` tinyint(2) NOT NULL DEFAULT '0' COMMENT '有效状态 0 前台不展示 1 前后台都展示',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='审核场景';

-- ----------------------------
-- Table structure for xc_audit_scene_field
-- ----------------------------
DROP TABLE IF EXISTS `audit_scene_field`;
CREATE TABLE `xc_audit_scene_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `field_description` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字段描述',
  `field_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字段名',
  `scene_code` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '场景对应业务的code',
  `create_time` datetime NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '添加时间',
  `creater_name` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `is_valid` tinyint(2) NOT NULL DEFAULT '0' COMMENT '有效状态 0 前台不展示 1 前后台都展示',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='审核场景的字段';

SET FOREIGN_KEY_CHECKS = 1;
