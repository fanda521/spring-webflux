/*
Navicat MySQL Data Transfer

Source Server         : mySql
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : reactive_db

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2022-10-18 23:01:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `t_name` varchar(255) DEFAULT NULL,
  `t_age` int(11) DEFAULT NULL,
  `t_num` varchar(255) DEFAULT NULL,
  `t_sex` varchar(255) DEFAULT NULL,
  `t_birthday` date DEFAULT NULL,
  `t_password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('1', 'jeffrey', '24', null, '男', '1996-05-30', '123456');
INSERT INTO `t_user` VALUES ('2', 'jeffrey', '23', null, '男', '1996-05-31', '123456');
INSERT INTO `t_user` VALUES ('3', 'jeffrey', '24', null, '男', '1996-05-31', '123456');
INSERT INTO `t_user` VALUES ('4', 'jeffre2y', '24', null, '男', '1996-05-31', '123456');
INSERT INTO `t_user` VALUES ('5', 'jeffrey5', '24', null, '男', '1996-05-31', '123456');
INSERT INTO `t_user` VALUES ('6', 'jeffrey5', '24', null, '男', '1996-05-28', '123456');
INSERT INTO `t_user` VALUES ('7', 'jeffrey5', '24', null, '男', '1996-05-27', '123456');
INSERT INTO `t_user` VALUES ('8', 'jeffrey5', '22', null, '男', '1996-04-27', '123456');
INSERT INTO `t_user` VALUES ('9', 'jeffrey6', '22', null, '男', '1996-04-27', '123456');
INSERT INTO `t_user` VALUES ('10', 'jeffrey10', '22', null, '男', '1996-04-27', '123456');
