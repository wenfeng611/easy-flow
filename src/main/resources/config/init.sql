/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : flow

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 29/07/2022 17:29:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_flow_config
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_config`;
CREATE TABLE `t_flow_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '流程名称',
  `status` int(2) NOT NULL DEFAULT 0 COMMENT '流程状态 0未启用  1启用  2禁用',
  `create_time` datetime(0) NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '添加时间',
  `creater_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `form_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '表单地址',
  `scene_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '场景对应业务的code',
  `update_time` datetime(0) NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '更新时间',
  `graph_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'G6流程图生成数据',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `scene_code`(`scene_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '流程配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_flow_item
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_item`;
CREATE TABLE `t_flow_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '流程id',
  `current_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '节点id',
  `flow_status` int(2) NOT NULL DEFAULT 0 COMMENT '流程状态',
  `add_time` datetime(0) NOT NULL DEFAULT '2010-01-01 00:00:00' COMMENT '添加时间',
  `update_time` datetime(0) NOT NULL DEFAULT '2010-01-01 00:00:00' COMMENT '更新时间',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `scene_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '场景对应业务的code',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `flow_id`(`flow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '流程Item' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_flow_item_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_item_detail`;
CREATE TABLE `t_flow_item_detail`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flow_item_id` int(11) NOT NULL DEFAULT 0 COMMENT '流程id',
  `operate_username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '操作人',
  `operate_info` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '操作结果备注',
  `current_g6_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '当前G6node的id',
  `current_g6_node_label` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '当前G6node的id',
  `next_g6_node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '当前G6node的id',
  `next_g6_node_label` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '当前G6node的id',
  `create_time` datetime(0) NOT NULL DEFAULT '2010-01-01 00:00:00' COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `flow_item_id`(`flow_item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '流程detail' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_flow_node
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_node`;
CREATE TABLE `t_flow_node`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `node_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '节点名称',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '节点类型',
  `create_time` datetime(0) NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '添加时间',
  `creater_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `is_valid` tinyint(2) NOT NULL DEFAULT 0 COMMENT '有效状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '流程节点' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_flow_node
-- ----------------------------
INSERT INTO `t_flow_node` VALUES (1, '开始节点', 'start', '2000-01-01 00:00:00', 'System', 1);
INSERT INTO `t_flow_node` VALUES (2, '判断节点', 'judge', '2000-01-01 00:00:00', 'System', 1);
INSERT INTO `t_flow_node` VALUES (3, '结束节点', 'end', '2000-01-01 00:00:00', 'System', 1);
INSERT INTO `t_flow_node` VALUES (4, '等待节点', 'waiting', '2000-01-01 00:00:00', 'System', 1);

-- ----------------------------
-- Table structure for t_flow_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_rule`;
CREATE TABLE `t_flow_rule`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `auto_judge_rule` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '规则数据',
  `create_time` datetime(0) NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '添加时间',
  `creater_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `flow_id` int(11) NOT NULL DEFAULT 0 COMMENT '流程id',
  `update_time` datetime(0) NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '更新时间',
  `g6node_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT 'g6节点id',
  `node_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '节点类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `flow_id`(`flow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '流程节点规则' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_flow_scene
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_scene`;
CREATE TABLE `t_flow_scene`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `scene_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '场景名称',
  `scene_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '场景对应业务的code',
  `create_time` datetime(0) NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '添加时间',
  `update_time` datetime(0) NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '更新时间',
  `creater_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `is_valid` tinyint(2) NOT NULL DEFAULT 0 COMMENT '有效状态 0 前台不展示 1 前后台都展示',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '业务场景' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_flow_scene_field
-- ----------------------------
DROP TABLE IF EXISTS `t_flow_scene_field`;
CREATE TABLE `t_flow_scene_field`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `field_description` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字段描述',
  `field_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '字段名',
  `scene_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '场景对应业务的code',
  `create_time` datetime(0) NOT NULL DEFAULT '2000-01-01 00:00:00' COMMENT '添加时间',
  `creater_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '创建人名称',
  `is_valid` tinyint(2) NOT NULL DEFAULT 0 COMMENT '有效状态',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '流程场景的字段' ROW_FORMAT = Dynamic;


SET FOREIGN_KEY_CHECKS = 1;
