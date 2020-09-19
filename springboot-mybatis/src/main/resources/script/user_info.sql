/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : localhost:3306
 Source Schema         : springboot

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 19/09/2020 20:58:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `user_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `age` int(3) NULL DEFAULT NULL COMMENT '年龄',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  PRIMARY KEY (`user_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('8c47749d054b439b9d1490f0ac1180fb', 'Jame Blues', 'Hello World', 56, '江苏省苏州市工业园区', '231234555');
INSERT INTO `user_info` VALUES ('be22755a7c5b4f439d48f816bb418224', 'Jame Blues', 'Hello World', 56, '江苏省苏州市工业园区', '231234555');
INSERT INTO `user_info` VALUES ('c5395e3cf4df4ce490f6f0e9f86f334b', 'Jame Blues', 'Hello World', 56, '江苏省苏州市工业园区', '231234555');
INSERT INTO `user_info` VALUES ('d89a3f2f9e6947cfafeb3480e38a6473', 'Jame Blues', 'Hello World', 56, '江苏省苏州市工业园区', '231234555');
INSERT INTO `user_info` VALUES ('ee4ed39004224fe9a2cb9d217a503551', 'Jame Blues', 'Hello World', 56, '江苏省苏州市工业园区', '231234555');

SET FOREIGN_KEY_CHECKS = 1;
