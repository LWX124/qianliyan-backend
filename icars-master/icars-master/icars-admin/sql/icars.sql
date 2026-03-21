/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : guns

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-07-26 23:10:40
*/

DROP DATABASE IF EXISTS icars;
CREATE DATABASE IF NOT EXISTS icars DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

USE icars;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `num` int(11) DEFAULT NULL COMMENT '排序',
  `pid` int(11) DEFAULT NULL COMMENT '父部门id',
  `pids` varchar(255) DEFAULT NULL COMMENT '父级ids',
  `simplename` varchar(45) DEFAULT NULL COMMENT '简称',
  `lng` decimal(20,10) DEFAULT NULL COMMENT '经度',
  `lat` decimal(20,10) DEFAULT NULL COMMENT '纬度',
  `fullname` varchar(255) DEFAULT NULL COMMENT '全称',
  `tips` varchar(255) DEFAULT NULL COMMENT '提示',
  `version` int(11) DEFAULT NULL COMMENT '版本（乐观锁保留字段）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (30, 1, 0, '[0],', '爱车士汽车服务有限公司', NULL, NULL, '爱车士汽车服务有限公司', '', NULL);
INSERT INTO `sys_dept` VALUES (31, 2, 0, '[0],', '四川省', NULL, NULL, '四川省', '', NULL);
INSERT INTO `sys_dept` VALUES (32, 1, 31, '[0],[31],', '成都市', NULL, NULL, '成都市', '', NULL);
INSERT INTO `sys_dept` VALUES (33, NULL, 32, '[0],[31],[32],', '成都国际赛车城4S店', 104.1222900000, 30.5841770000, '成都国际赛车城4S店', '宝马', NULL);


-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `num` int(11) DEFAULT NULL COMMENT '排序',
  `pid` int(11) DEFAULT NULL COMMENT '父级字典',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `tips` varchar(255) DEFAULT NULL COMMENT '提示',
  `code` varchar(255) DEFAULT NULL COMMENT '值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=69 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES ('50', '0', '0', '性别', null, 'sys_sex');
INSERT INTO `sys_dict` VALUES ('51', '1', '50', '男', null, '1');
INSERT INTO `sys_dict` VALUES ('52', '2', '50', '女', null, '2');
INSERT INTO `sys_dict` VALUES ('53', '0', '0', '状态', null, 'sys_state');
INSERT INTO `sys_dict` VALUES ('54', '1', '53', '启用', null, '1');
INSERT INTO `sys_dict` VALUES ('55', '2', '53', '禁用', null, '2');
INSERT INTO `sys_dict` VALUES ('56', '0', '0', '账号状态', null, 'account_state');
INSERT INTO `sys_dict` VALUES ('57', '1', '56', '启用', null, '1');
INSERT INTO `sys_dict` VALUES ('58', '2', '56', '冻结', null, '2');
INSERT INTO `sys_dict` VALUES ('59', '3', '56', '已删除', null, '3');

-- ----------------------------
-- Table structure for sys_expense
-- ----------------------------
DROP TABLE IF EXISTS `sys_expense`;
CREATE TABLE `sys_expense` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `money` decimal(20,2) DEFAULT NULL COMMENT '报销金额',
  `desc` varchar(255) DEFAULT '' COMMENT '描述',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `state` int(11) DEFAULT NULL COMMENT '状态: 1.待提交  2:待审核   3.审核通过 4:驳回',
  `userid` int(11) DEFAULT NULL COMMENT '用户id',
  `processId` varchar(255) DEFAULT NULL COMMENT '流程定义id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='报销表';

-- ----------------------------
-- Records of sys_expense
-- ----------------------------

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
  `id` int(65) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `logname` varchar(255) DEFAULT NULL COMMENT '日志名称',
  `userid` int(65) DEFAULT NULL COMMENT '管理员id',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `succeed` varchar(255) DEFAULT NULL COMMENT '是否执行成功',
  `message` text COMMENT '具体消息',
  `ip` varchar(255) DEFAULT NULL COMMENT '登录ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=utf8 COMMENT='登录记录';

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` varchar(255) DEFAULT NULL COMMENT '菜单编号',
  `pcode` varchar(255) DEFAULT NULL COMMENT '菜单父编号',
  `pcodes` varchar(255) DEFAULT NULL COMMENT '当前菜单的所有父菜单编号',
  `name` varchar(255) DEFAULT NULL COMMENT '菜单名称',
  `icon` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `url` varchar(255) DEFAULT NULL COMMENT 'url地址',
  `num` int(65) DEFAULT NULL COMMENT '菜单排序号',
  `levels` int(65) DEFAULT NULL COMMENT '菜单层级',
  `ismenu` int(11) DEFAULT NULL COMMENT '是否是菜单（1：是  0：不是）',
  `tips` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` int(65) DEFAULT NULL COMMENT '菜单状态 :  1:启用   0:不启用',
  `isopen` int(11) DEFAULT NULL COMMENT '是否打开:    1:打开   0:不打开',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8 COMMENT='菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (105, 'system', '0', '[0],', '系统管理', 'fa-user', '#', 4, 1, 1, NULL, 1, 1);
INSERT INTO `sys_menu` VALUES (106, 'mgr', 'system', '[0],[system],', '用户管理', '', '/mgr', 1, 2, 1, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (107, 'mgr_add', 'mgr', '[0],[system],[mgr],', '添加用户', NULL, '/mgr/add', 1, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (108, 'mgr_edit', 'mgr', '[0],[system],[mgr],', '修改用户', NULL, '/mgr/edit', 2, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (109, 'mgr_delete', 'mgr', '[0],[system],[mgr],', '删除用户', NULL, '/mgr/delete', 3, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (110, 'mgr_reset', 'mgr', '[0],[system],[mgr],', '重置密码', NULL, '/mgr/reset', 4, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (111, 'mgr_freeze', 'mgr', '[0],[system],[mgr],', '冻结用户', NULL, '/mgr/freeze', 5, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (112, 'mgr_unfreeze', 'mgr', '[0],[system],[mgr],', '解除冻结用户', NULL, '/mgr/unfreeze', 6, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (113, 'mgr_setRole', 'mgr', '[0],[system],[mgr],', '分配角色', NULL, '/mgr/setRole', 7, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (114, 'role', 'system', '[0],[system],', '角色管理', NULL, '/role', 2, 2, 1, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (115, 'role_add', 'role', '[0],[system],[role],', '添加角色', NULL, '/role/add', 1, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (116, 'role_edit', 'role', '[0],[system],[role],', '修改角色', NULL, '/role/edit', 2, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (117, 'role_remove', 'role', '[0],[system],[role],', '删除角色', NULL, '/role/remove', 3, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (118, 'role_setAuthority', 'role', '[0],[system],[role],', '配置权限', NULL, '/role/setAuthority', 4, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (119, 'menu', 'system', '[0],[system],', '菜单管理', NULL, '/menu', 4, 2, 1, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (120, 'menu_add', 'menu', '[0],[system],[menu],', '添加菜单', NULL, '/menu/add', 1, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (121, 'menu_edit', 'menu', '[0],[system],[menu],', '修改菜单', NULL, '/menu/edit', 2, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (122, 'menu_remove', 'menu', '[0],[system],[menu],', '删除菜单', NULL, '/menu/remove', 3, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (128, 'log', 'system', '[0],[system],', '业务日志', NULL, '/log', 6, 2, 1, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (130, 'druid', 'system', '[0],[system],', '监控管理', NULL, '/druid', 7, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (131, 'dept', 'system', '[0],[system],', '部门管理', NULL, '/dept', 3, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (132, 'dict', 'system', '[0],[system],', '字典管理', NULL, '/dict', 4, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (133, 'loginLog', 'system', '[0],[system],', '登录日志', NULL, '/loginLog', 6, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (134, 'log_clean', 'log', '[0],[system],[log],', '清空日志', NULL, '/log/delLog', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (135, 'dept_add', 'dept', '[0],[system],[dept],', '添加部门', NULL, '/dept/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (136, 'dept_update', 'dept', '[0],[system],[dept],', '修改部门', NULL, '/dept/update', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (137, 'dept_delete', 'dept', '[0],[system],[dept],', '删除部门', NULL, '/dept/delete', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (138, 'dict_add', 'dict', '[0],[system],[dict],', '添加字典', NULL, '/dict/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (139, 'dict_update', 'dict', '[0],[system],[dict],', '修改字典', NULL, '/dict/update', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (140, 'dict_delete', 'dict', '[0],[system],[dict],', '删除字典', NULL, '/dict/delete', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (141, 'notice', 'system', '[0],[system],', '通知管理', NULL, '/notice', 9, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (142, 'notice_add', 'notice', '[0],[system],[notice],', '添加通知', NULL, '/notice/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (143, 'notice_update', 'notice', '[0],[system],[notice],', '修改通知', NULL, '/notice/update', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (144, 'notice_delete', 'notice', '[0],[system],[notice],', '删除通知', NULL, '/notice/delete', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (145, 'hello', '0', '[0],', '通知', 'fa-rocket', '/notice/hello', 1, 1, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (148, 'code', '0', '[0],', '代码生成', 'fa-code', '/code', 3, 1, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (149, 'api_mgr', '0', '[0],', '接口文档', 'fa-leaf', '/swagger-ui.html', 2, 1, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (150, 'to_menu_edit', 'menu', '[0],[system],[menu],', '菜单编辑跳转', '', '/menu/menu_edit', 4, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (151, 'menu_list', 'menu', '[0],[system],[menu],', '菜单列表', '', '/menu/list', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (152, 'to_dept_update', 'dept', '[0],[system],[dept],', '修改部门跳转', '', '/dept/dept_update', 4, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (153, 'dept_list', 'dept', '[0],[system],[dept],', '部门列表', '', '/dept/list', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (154, 'dept_detail', 'dept', '[0],[system],[dept],', '部门详情', '', '/dept/detail', 6, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (155, 'to_dict_edit', 'dict', '[0],[system],[dict],', '修改菜单跳转', '', '/dict/dict_edit', 4, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (156, 'dict_list', 'dict', '[0],[system],[dict],', '字典列表', '', '/dict/list', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (157, 'dict_detail', 'dict', '[0],[system],[dict],', '字典详情', '', '/dict/detail', 6, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (158, 'log_list', 'log', '[0],[system],[log],', '日志列表', '', '/log/list', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (159, 'log_detail', 'log', '[0],[system],[log],', '日志详情', '', '/log/detail', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (160, 'del_login_log', 'loginLog', '[0],[system],[loginLog],', '清空登录日志', '', '/loginLog/delLoginLog', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (161, 'login_log_list', 'loginLog', '[0],[system],[loginLog],', '登录日志列表', '', '/loginLog/list', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (162, 'to_role_edit', 'role', '[0],[system],[role],', '修改角色跳转', '', '/role/role_edit', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (163, 'to_role_assign', 'role', '[0],[system],[role],', '角色分配跳转', '', '/role/role_assign', 6, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (164, 'role_list', 'role', '[0],[system],[role],', '角色列表', '', '/role/list', 7, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (165, 'to_assign_role', 'mgr', '[0],[system],[mgr],', '分配角色跳转', '', '/mgr/role_assign', 8, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (166, 'to_user_edit', 'mgr', '[0],[system],[mgr],', '编辑用户跳转', '', '/mgr/user_edit', 9, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (167, 'mgr_list', 'mgr', '[0],[system],[mgr],', '用户列表', '', '/mgr/list', 10, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (171, 'accid', 'system', '[0],[system],', '事故上报', '', '/accid', 10, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (172, 'accid_checkSuccess', 'accid', '[0],[system],[accid],', '审核事故通过', '', '/accid/checkSuccess', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (173, 'accid_checkFail', 'accid', '[0],[system],[accid],', '审核事故不通过', '', '/accid/checkFail', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (174, 'accid_list', 'accid', '[0],[system],[accid],', '查询事故', '', '/accid/list', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (175, 'pushRecord', 'system', '[0],[system],', '推送记录', '', '/pushRecord', 11, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (176, 'pushRecord_list', 'pushRecord', '[0],[system],[pushRecord],', '查询事故推送记录', '', '/pushRecord/list', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (177, 'pushRecord_delete', 'pushRecord', '[0],[system],[pushRecord],', '删除事故推送记录', '', '/pushRecord/delete', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (178, 'alipayActivity', 'system', '[0],[system],', '支付宝红包活动', '', '/alipayActivity', 12, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (179, 'alipayActivity_add', 'alipayActivity', '[0],[system],[alipayActivity],', '创建活动', '', '/alipayActivity/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (180, 'alipayActivity_update', 'alipayActivity', '[0],[system],[alipayActivity],', '修改活动', '', '/alipayActivity/update', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (181, 'bizAlipayBill', 'system', '[0],[system],', '支付宝红包管理', '', '/bizAlipayBill', 13, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (182, 'pushFourS', 'accid', '[0],[system],[accid],', '事故推送4S', '', '/accid/pushFourS', 4, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (183, 'pushFsList', 'accid', '[0],[system],[accid],', '查询推送4s门店列表', '', '/accid/pushFsList', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (184, 'fsMgr', 'system', '[0],[system],', '4S事故推送记录', '', '/pushRecord/fsMgr', 14, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (185, 'pushFS_list', 'fsMgr', '[0],[system],[fsMgr],', '查询4s事故记录', '', '/pushRecord/pushFS/list', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (186, 'bizAlipayBill_rePay', 'bizAlipayBill', '[0],[system],[bizAlipayBill],', '重新支付', '', '/bizAlipayBill/rePay', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (187, 'pushClaims', 'accid', '[0],[system],[accid],', '事故推送理赔顾问', '', '/accid/pushClaims', 6, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (188, 'pushClaimsList', 'accid', '[0],[system],[accid],', '查询事故推送理赔顾问列表', '', '/accid/pushClaimsList', 7, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (189, 'wxpayBill', 'system', '[0],[system],', '微信红包管理', '', '/wxpayBill', 15, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (190, 'wxpayBill_rePay', 'wxpayBill', '[0],[system],[wxpayBill],', '微信重新支付', '', '/wxpayBill/rePay', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (191, 'bizWxSalary', 'system', '[0],[system],', '微信红包定义', '', '/bizWxSalary', 16, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (192, 'bizWxSalary_add', 'bizWxSalary', '[0],[system],[bizWxSalary],', '新增红包定义', '', '/bizWxSalary/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (193, 'bizWxSalary_update', 'bizWxSalary', '[0],[system],[bizWxSalary],', '修改红包定义', '', '/bizWxSalary/update', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (194, 'bizWxSalary_delete', 'bizWxSalary', '[0],[system],[bizWxSalary],', '删除红包定义', '', '/bizWxSalary/delete', 3, 3, 0, NULL, 1, NULL);
-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `content` text COMMENT '内容',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` int(11) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='通知表';

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES ('6', '世界', '10', '欢迎使用管理系统', '2017-01-11 08:53:20', '1');
INSERT INTO `sys_notice` VALUES ('8', '你好', null, '你好', '2017-05-10 19:28:57', '1');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `id` int(65) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `logtype` varchar(255) DEFAULT NULL COMMENT '日志类型',
  `logname` varchar(255) DEFAULT NULL COMMENT '日志名称',
  `userid` int(65) DEFAULT NULL COMMENT '用户id',
  `classname` varchar(255) DEFAULT NULL COMMENT '类名称',
  `method` text COMMENT '方法名称',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `succeed` varchar(255) DEFAULT NULL COMMENT '是否成功',
  `message` text COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=554 DEFAULT CHARSET=utf8 COMMENT='操作日志';

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------


DROP TABLE IF EXISTS `biz_accident`;
CREATE TABLE `biz_accident` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `openid` varchar(100) DEFAULT NULL COMMENT '上报人id',
  `video` varchar(100) DEFAULT NULL COMMENT '视频url',
  `lng` decimal(20,10) DEFAULT NULL COMMENT '经度',
  `lat` decimal(20,10) DEFAULT NULL COMMENT '纬度',
  `checkId` varchar(45) DEFAULT NULL COMMENT '审核人id',
  `status` int(11) DEFAULT NULL COMMENT '状态(1：未审核  2：审核通过  3：审核失败）',
  `reason` varchar(255) DEFAULT NULL COMMENT '审核原因',
  `address` varchar(250) DEFAULT NULL COMMENT '上报地址名称',
  `createTime` datetime DEFAULT NULL COMMENT '上报时间',
  `checkTime` datetime DEFAULT NULL COMMENT '审核时间',
  `version` int(11) DEFAULT NULL COMMENT '保留字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_id` (`id`),
  KEY `idx_date` (`createTime`,`checkTime`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='事故上报信息表';

DROP TABLE IF EXISTS `biz_wx_user`;
CREATE TABLE `biz_wx_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `openid` varchar(100) DEFAULT NULL COMMENT '微信用户id',
  `type` int(1) DEFAULT NULL COMMENT '用户来源0：公众号，1：小程序',
  `alipay_account` varchar(100) DEFAULT NULL COMMENT '支付宝账户',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机号',
  `createTime` datetime DEFAULT NULL COMMENT '上报时间',
  `version` int(11) DEFAULT NULL COMMENT '保留字段',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idx_openid`(`openid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='微信用户信息表';

CREATE TABLE `biz_push_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `account` varchar(100) DEFAULT NULL COMMENT '员工账号',
  `accid` int(11) NOT NULL COMMENT '事故主键id',
  `deptid` int(11) NOT NULL COMMENT '部门id',
  `status` int(2) NOT NULL COMMENT '状态，0：未查看，1：已查看',
  `createTime` datetime DEFAULT NULL COMMENT '推送时间',
  `modifyTime` datetime DEFAULT NULL COMMENT '修改时间',
  `version` int(11) DEFAULT NULL COMMENT '保留字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_account_accid` (`account`,`accid`),
  KEY `idx_account` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='事故推送记录';


DROP TABLE IF EXISTS `biz_alipay_activity`;
CREATE TABLE `biz_alipay_activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `crowd_no` varchar(128) NOT NULL COMMENT '活动号',
  `pay_url` varchar(256) NOT NULL COMMENT '支付链接',
  `origin_crowd_no` varchar(64) NOT NULL COMMENT '原始活动号,商户排查问题时提供的活动依据',
  `startTime` datetime NOT NULL COMMENT '活动有效开始时间',
	`endTime` datetime NOT NULL COMMENT '活动有效结束时间',
	`createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='支付宝营销红包活动表';

DROP TABLE IF EXISTS `biz_alipay_bill`;
CREATE TABLE `biz_alipay_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `accid` int(11) NOT NULL COMMENT '事故id',
    `status` int(1) NOT NULL COMMENT '支付状态 0：支付成功  1：支付失败',
	`pay_time` datetime NOT NULL COMMENT '支付时间',
	`create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
	UNIQUE INDEX `idx_accid`(`accid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='支付宝营销红包支付主表';

DROP TABLE IF EXISTS `biz_alipay_pay_record`;
CREATE TABLE `biz_alipay_pay_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `bill_id` int(11) NOT NULL COMMENT '支付主表id',
  `alipay_account` varchar(100) DEFAULT NULL COMMENT '支付宝账户',
  `response_info` json DEFAULT NULL COMMENT '接口返回数据',
  `sub_code` varchar(100) GENERATED ALWAYS AS (json_extract(`response_info`,'$.subCode')) VIRTUAL,
  `sub_msg` varchar(100) GENERATED ALWAYS AS (json_extract(`response_info`,'$.subMsg')) VIRTUAL,
	`out_biz_no` varchar(96) GENERATED ALWAYS AS (json_extract(`response_info`,'$.outBizNo')) VIRTUAL,
  `prize_amount` decimal(5,2) GENERATED ALWAYS AS (json_extract(`response_info`,'$.prizeAmount')) VIRTUAL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='支付宝营销红包支付明细表';

DROP TABLE IF EXISTS `biz_notify_mail`;
CREATE TABLE `biz_notify_mail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `email` varchar(100) NOT NULL COMMENT '邮件地址',
  `mail_type` int(1) NOT NULL COMMENT '接受通知类型 0：支付宝相关',
	`create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='系统邮件通知主表';

CREATE TABLE `biz_wx_salary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `amount` decimal(5,2) NOT NULL COMMENT '奖励金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信红包定义表';

-- ----------------------------
-- Table structure for biz_wxpay_bill
-- ----------------------------
DROP TABLE IF EXISTS `biz_wxpay_bill`;
CREATE TABLE `biz_wxpay_bill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `accid` int(11) NOT NULL COMMENT '事故id',
  `status` int(1) NOT NULL COMMENT '支付状态 0：支付成功  1：支付失败',
  `pay_time` datetime(0) NOT NULL COMMENT '支付时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_accid`(`accid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '微信企业付款到零钱红包支付主表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for biz_wx_pay_record
-- ----------------------------
DROP TABLE IF EXISTS `biz_wx_pay_record`;
CREATE TABLE `biz_wx_pay_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `bill_id` int(11) NOT NULL COMMENT '支付主表id',
  `openid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信用户对应小程序openid',
  `amount` decimal(5, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `return_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'SUCCESS/FAIL，此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断',
  `return_msg` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '返回信息，如非空，为错误原因 签名失败 参数格式校验错误',
  `result_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'SUCCESS/FAIL，业务结果',
  `mch_appid` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户appid',
  `mchid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `device_info` varchar(21) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设备号',
  `nonce_str` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '随机字符串',
  `err_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误代码',
  `err_code_des` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误代码描述',
  `partner_trade_no` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户订单号',
  `payment_no` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微信订单号',
  `payment_time` datetime(0) NOT NULL COMMENT '微信支付成功时间',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '微信企业付款到零钱红包支付明细表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `biz_claim`;
CREATE TABLE `biz_claim` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
	`openid` varchar(100) NOT NULL COMMENT '用户openid',
  `phone` varchar(255) NOT NULL COMMENT '客户手机号',
	`name` varchar(50) NOT NULL COMMENT '客户姓名',
	`cph` varchar(50) DEFAULT NULL COMMENT '车牌号',
  `type` int(1) NOT NULL COMMENT '理赔单类型',
	`status` int(1) NOT NULL COMMENT '理赔状态,0:已下单、1：理赔中、2：理赔完成',
  `desc` text COMMENT '描述',
  `claimer` varchar(30) DEFAULT NULL COMMENT '理赔负责人',
	`lng` decimal(20,10) DEFAULT NULL COMMENT '经度',
  `lat` decimal(20,10) DEFAULT NULL COMMENT '纬度',
  `address` varchar(250) DEFAULT NULL COMMENT '定位地址名称',
	`claimImg` varchar(500) DEFAULT NULL COMMENT '理赔图片',
	`createtime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='理赔单';

-- ----------------------------
-- Table structure for biz_accident_realness_view
-- ----------------------------
DROP TABLE IF EXISTS `biz_accident_realness_view`;
CREATE TABLE `biz_accident_realness_view` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
	`openid` varchar(100) NOT NULL COMMENT '用户openid',
  `total` int(11) DEFAULT NULL COMMENT '用户提交事故总数',
	`exist` int(11) DEFAULT NULL COMMENT '有效事故',
	`notexist` int(11) DEFAULT NULL COMMENT '无效事故',
	`createtime` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_openid` (`openid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户事故有效性统计表（依靠存储过程定时刷新）';


-- ----------------------------
-- 存储过程 统计用户上报事故有效性
-- ----------------------------
DROP PROCEDURE IF EXISTS refresh_biz_accident_realness_view;
CREATE PROCEDURE refresh_biz_accident_realness_view () BEGIN
	TRUNCATE TABLE biz_accident_realness_view;
	INSERT INTO biz_accident_realness_view ( openid, total, exist, notexist ) SELECT
	openid AS openid,
	count( 1 ) AS total,
	count( IF ( ( realness = 0 ), TRUE, NULL ) ) AS exist,
	count( IF ( ( realness = 1 ), TRUE, NULL ) ) AS notexist
	FROM
		biz_accident
	GROUP BY
		openid;
END

-- ----------------------------
-- 创建事件 定时触发 统计用户上报事故有效性 存储过程
-- ----------------------------
DROP EVENT e_refresh_accident_realness_view;
CREATE EVENT IF NOT EXISTS e_refresh_accident_realness_view ON SCHEDULE EVERY 1 DAY STARTS DATE_ADD(DATE_ADD(CURDATE(), INTERVAL 1 DAY), INTERVAL 1 HOUR)
ON COMPLETION PRESERVE ENABLE
DO
CALL refresh_biz_accident_realness_view ();


DROP TABLE IF EXISTS `biz_claimer_show`;
CREATE TABLE `biz_claimer_show` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
	`name` varchar(50) NOT NULL COMMENT '姓名',
	`imgUrl` varchar(255) DEFAULT NULL COMMENT '头像',
  `motto` varchar(255) NOT NULL COMMENT '座右铭',
	`story` varchar(255) NOT NULL COMMENT '事迹简介',
	`status` int(1) NOT NULL COMMENT '展示状态 0：展示   1下架',
	`createtime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='模范理赔顾问表';

-- ----------------------------
-- Table structure for sys_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_relation`;
CREATE TABLE `sys_relation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menuid` bigint(11) NULL DEFAULT NULL COMMENT '菜单id',
  `roleid` int(11) NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_relation
-- ----------------------------
INSERT INTO `sys_relation` VALUES (3377, 105, 5);
INSERT INTO `sys_relation` VALUES (3378, 106, 5);
INSERT INTO `sys_relation` VALUES (3379, 107, 5);
INSERT INTO `sys_relation` VALUES (3380, 108, 5);
INSERT INTO `sys_relation` VALUES (3381, 109, 5);
INSERT INTO `sys_relation` VALUES (3382, 110, 5);
INSERT INTO `sys_relation` VALUES (3383, 111, 5);
INSERT INTO `sys_relation` VALUES (3384, 112, 5);
INSERT INTO `sys_relation` VALUES (3385, 113, 5);
INSERT INTO `sys_relation` VALUES (3386, 114, 5);
INSERT INTO `sys_relation` VALUES (3387, 115, 5);
INSERT INTO `sys_relation` VALUES (3388, 116, 5);
INSERT INTO `sys_relation` VALUES (3389, 117, 5);
INSERT INTO `sys_relation` VALUES (3390, 118, 5);
INSERT INTO `sys_relation` VALUES (3391, 119, 5);
INSERT INTO `sys_relation` VALUES (3392, 120, 5);
INSERT INTO `sys_relation` VALUES (3393, 121, 5);
INSERT INTO `sys_relation` VALUES (3394, 122, 5);
INSERT INTO `sys_relation` VALUES (3395, 150, 5);
INSERT INTO `sys_relation` VALUES (3396, 151, 5);
INSERT INTO `sys_relation` VALUES (4424, 105, 6);
INSERT INTO `sys_relation` VALUES (4425, 184, 6);
INSERT INTO `sys_relation` VALUES (4426, 185, 6);
INSERT INTO `sys_relation` VALUES (4427, 105, 1);
INSERT INTO `sys_relation` VALUES (4428, 106, 1);
INSERT INTO `sys_relation` VALUES (4429, 107, 1);
INSERT INTO `sys_relation` VALUES (4430, 108, 1);
INSERT INTO `sys_relation` VALUES (4431, 109, 1);
INSERT INTO `sys_relation` VALUES (4432, 110, 1);
INSERT INTO `sys_relation` VALUES (4433, 111, 1);
INSERT INTO `sys_relation` VALUES (4434, 112, 1);
INSERT INTO `sys_relation` VALUES (4435, 113, 1);
INSERT INTO `sys_relation` VALUES (4436, 165, 1);
INSERT INTO `sys_relation` VALUES (4437, 166, 1);
INSERT INTO `sys_relation` VALUES (4438, 167, 1);
INSERT INTO `sys_relation` VALUES (4439, 114, 1);
INSERT INTO `sys_relation` VALUES (4440, 115, 1);
INSERT INTO `sys_relation` VALUES (4441, 116, 1);
INSERT INTO `sys_relation` VALUES (4442, 117, 1);
INSERT INTO `sys_relation` VALUES (4443, 118, 1);
INSERT INTO `sys_relation` VALUES (4444, 162, 1);
INSERT INTO `sys_relation` VALUES (4445, 163, 1);
INSERT INTO `sys_relation` VALUES (4446, 164, 1);
INSERT INTO `sys_relation` VALUES (4447, 119, 1);
INSERT INTO `sys_relation` VALUES (4448, 120, 1);
INSERT INTO `sys_relation` VALUES (4449, 121, 1);
INSERT INTO `sys_relation` VALUES (4450, 122, 1);
INSERT INTO `sys_relation` VALUES (4451, 150, 1);
INSERT INTO `sys_relation` VALUES (4452, 151, 1);
INSERT INTO `sys_relation` VALUES (4453, 128, 1);
INSERT INTO `sys_relation` VALUES (4454, 134, 1);
INSERT INTO `sys_relation` VALUES (4455, 158, 1);
INSERT INTO `sys_relation` VALUES (4456, 159, 1);
INSERT INTO `sys_relation` VALUES (4457, 130, 1);
INSERT INTO `sys_relation` VALUES (4458, 131, 1);
INSERT INTO `sys_relation` VALUES (4459, 135, 1);
INSERT INTO `sys_relation` VALUES (4460, 136, 1);
INSERT INTO `sys_relation` VALUES (4461, 137, 1);
INSERT INTO `sys_relation` VALUES (4462, 152, 1);
INSERT INTO `sys_relation` VALUES (4463, 153, 1);
INSERT INTO `sys_relation` VALUES (4464, 154, 1);
INSERT INTO `sys_relation` VALUES (4465, 132, 1);
INSERT INTO `sys_relation` VALUES (4466, 138, 1);
INSERT INTO `sys_relation` VALUES (4467, 139, 1);
INSERT INTO `sys_relation` VALUES (4468, 140, 1);
INSERT INTO `sys_relation` VALUES (4469, 155, 1);
INSERT INTO `sys_relation` VALUES (4470, 156, 1);
INSERT INTO `sys_relation` VALUES (4471, 157, 1);
INSERT INTO `sys_relation` VALUES (4472, 133, 1);
INSERT INTO `sys_relation` VALUES (4473, 160, 1);
INSERT INTO `sys_relation` VALUES (4474, 161, 1);
INSERT INTO `sys_relation` VALUES (4475, 141, 1);
INSERT INTO `sys_relation` VALUES (4476, 142, 1);
INSERT INTO `sys_relation` VALUES (4477, 143, 1);
INSERT INTO `sys_relation` VALUES (4478, 144, 1);
INSERT INTO `sys_relation` VALUES (4479, 171, 1);
INSERT INTO `sys_relation` VALUES (4480, 172, 1);
INSERT INTO `sys_relation` VALUES (4481, 173, 1);
INSERT INTO `sys_relation` VALUES (4482, 174, 1);
INSERT INTO `sys_relation` VALUES (4483, 182, 1);
INSERT INTO `sys_relation` VALUES (4484, 183, 1);
INSERT INTO `sys_relation` VALUES (4485, 175, 1);
INSERT INTO `sys_relation` VALUES (4486, 176, 1);
INSERT INTO `sys_relation` VALUES (4487, 177, 1);
INSERT INTO `sys_relation` VALUES (4488, 178, 1);
INSERT INTO `sys_relation` VALUES (4489, 179, 1);
INSERT INTO `sys_relation` VALUES (4490, 180, 1);
INSERT INTO `sys_relation` VALUES (4491, 181, 1);
INSERT INTO `sys_relation` VALUES (4492, 186, 1);
INSERT INTO `sys_relation` VALUES (4493, 145, 1);
INSERT INTO `sys_relation` VALUES (4494, 148, 1);
INSERT INTO `sys_relation` VALUES (4495, 149, 1);
-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `num` int(11) NULL DEFAULT NULL COMMENT '序号',
  `pid` int(11) NULL DEFAULT NULL COMMENT '父角色id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `deptid` int(11) NULL DEFAULT NULL COMMENT '部门名称',
  `tips` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提示',
  `version` int(11) NULL DEFAULT NULL COMMENT '保留字段(暂时没用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 1, 0, '超级管理员', 24, 'administrator', 1);
INSERT INTO `sys_role` VALUES (2, 2, 1, '临时', 26, 'temp', NULL);
INSERT INTO `sys_role` VALUES (3, 2, 0, '4S店员', 0, 'administrator_4s', NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `account` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'md5密码盐',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `sex` int(11) NULL DEFAULT NULL COMMENT '性别（1：男 2：女）',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `roleid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  `deptid` int(11) NULL DEFAULT NULL COMMENT '部门id',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态(1：启用  2：冻结  3：删除）',
  `createtime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `version` int(11) NULL DEFAULT NULL COMMENT '保留字段',
  `lng` decimal(20, 10) NULL DEFAULT NULL COMMENT '经度',
  `lat` decimal(20, 10) NULL DEFAULT NULL COMMENT '纬度',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_phone_status`(`phone`, `status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '', 'admin', '83ea1cdb2cd4974864871550a38a63d2', 'toi2b', '胡敬华', '2017-05-05 00:00:00', 1, 'sn93@qq.com', '18181998760', '1', 30, 1, '2016-01-29 08:49:53', 25, NULL, NULL);
INSERT INTO `sys_user` VALUES (2, '', 'saichecheng1', 'aadebf20a02beed9b9b2b18ca8cb6bd8', 're1bn', '赛车成4s店员1', '2018-08-06 00:00:00', 1, '', '18888888888', '6', 33, 1, '2018-08-06 09:40:14', NULL, NULL, NULL);
INSERT INTO `sys_user` VALUES (3, '', 'saichecheng2', '547a82c129eefd98a08bc974c13259c9', '91yi1', '赛车成4s店员2', '2018-08-06 00:00:00', 2, '', '18889898989', '6', 33, 1, '2018-08-06 10:34:13', NULL, NULL, NULL);
INSERT INTO `sys_user` VALUES (4, '', 'wangtao', 'd47af8e35246517abe78a71f041307aa', 'mfyyv', '王涛', '2018-08-10 00:00:00', 1, '', '18582862710', '1', 30, 1, '2018-08-10 14:07:06', NULL, NULL, NULL);



-- ----------------------------
-- Table structure for sequence
-- ----------------------------
DROP TABLE IF EXISTS `sequence`;
CREATE TABLE `sequence` (
  `name` varchar(32) NOT NULL,
  `value` int(6) DEFAULT NULL,
  `next` int(6) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sequence
-- ----------------------------
INSERT INTO `sequence` VALUES ('claim_order', '1', '1');

DROP FUNCTION IF EXISTS next_open_claim_order_num;
CREATE FUNCTION next_open_claim_order_num( seq_name varchar(30))
RETURNS VARCHAR(100)
BEGIN
    UPDATE sequence SET value=IF(last_insert_id(value+next)>= 999998,0,last_insert_id(value+next)) WHERE name=seq_name;
    RETURN last_insert_id();
END;
DROP FUNCTION IF EXISTS get_open_claim_order_num;
CREATE FUNCTION `get_open_claim_order_num`() RETURNS varchar(20) CHARSET utf8
BEGIN
    DECLARE getval VARCHAR(24);
    SET getval = (SELECT CONCAT(DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'),  LPAD((SELECT next_open_claim_order_num('claim_order')), 6, '0')));
    RETURN getval;
END
