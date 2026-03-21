-- 创建业务数据库
CREATE DATABASE IF NOT EXISTS `xxl_job` DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS `cheji` DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS `icars_test` DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS `icars_rest` DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS `jeesite` DEFAULT CHARACTER SET utf8mb4;

#
# XXL-JOB v2.4.1-SNAPSHOT
# Copyright (c) 2015-present, xuxueli.

CREATE database if NOT EXISTS `xxl_job` default character set utf8mb4 collate utf8mb4_unicode_ci;
use `xxl_job`;

SET NAMES utf8mb4;

CREATE TABLE `xxl_job_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_desc` varchar(255) NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
  `schedule_type` varchar(50) NOT NULL DEFAULT 'NONE' COMMENT '调度类型',
  `schedule_conf` varchar(128) DEFAULT NULL COMMENT '调度配置，值含义取决于调度类型',
  `misfire_strategy` varchar(50) NOT NULL DEFAULT 'DO_NOTHING' COMMENT '调度过期策略',
  `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_timeout` int(11) NOT NULL DEFAULT '0' COMMENT '任务执行超时时间，单位秒',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobid` varchar(255) DEFAULT NULL COMMENT '子任务ID，多个逗号分隔',
  `trigger_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '调度状态：0-停止，1-运行',
  `trigger_last_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '上次调度时间',
  `trigger_next_time` bigint(13) NOT NULL DEFAULT '0' COMMENT '下次调度时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_job_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(512) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_sharding_param` varchar(20) DEFAULT NULL COMMENT '执行器任务分片参数，格式如 1/2',
  `executor_fail_retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败重试次数',
  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` int(11) NOT NULL COMMENT '调度-结果',
  `trigger_msg` text COMMENT '调度-日志',
  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
  `handle_code` int(11) NOT NULL COMMENT '执行-状态',
  `handle_msg` text COMMENT '执行-日志',
  `alarm_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败',
  PRIMARY KEY (`id`),
  KEY `I_trigger_time` (`trigger_time`),
  KEY `I_handle_code` (`handle_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_job_log_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trigger_day` datetime DEFAULT NULL COMMENT '调度-时间',
  `running_count` int(11) NOT NULL DEFAULT '0' COMMENT '运行中-日志数量',
  `suc_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行成功-日志数量',
  `fail_count` int(11) NOT NULL DEFAULT '0' COMMENT '执行失败-日志数量',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_trigger_day` (`trigger_day`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_job_logglue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` mediumtext COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_job_registry` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `registry_group` varchar(50) NOT NULL,
  `registry_key` varchar(255) NOT NULL,
  `registry_value` varchar(255) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_g_k_v` (`registry_group`,`registry_key`,`registry_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_job_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) NOT NULL COMMENT '执行器名称',
  `address_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` text COMMENT '执行器地址列表，多地址逗号分隔',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_job_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '账号',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `role` tinyint(4) NOT NULL COMMENT '角色：0-普通用户、1-管理员',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限：执行器ID列表，多个逗号分割',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `xxl_job_lock` (
  `lock_name` varchar(50) NOT NULL COMMENT '锁名称',
  PRIMARY KEY (`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `xxl_job_group`(`id`, `app_name`, `title`, `address_type`, `address_list`, `update_time`) VALUES (1, 'xxl-job-executor-sample', '示例执行器', 0, NULL, '2018-11-03 22:21:31' );
INSERT INTO `xxl_job_info`(`id`, `job_group`, `job_desc`, `add_time`, `update_time`, `author`, `alarm_email`, `schedule_type`, `schedule_conf`, `misfire_strategy`, `executor_route_strategy`, `executor_handler`, `executor_param`, `executor_block_strategy`, `executor_timeout`, `executor_fail_retry_count`, `glue_type`, `glue_source`, `glue_remark`, `glue_updatetime`, `child_jobid`) VALUES (1, 1, '测试任务1', '2018-11-03 22:21:31', '2018-11-03 22:21:31', 'XXL', '', 'CRON', '0 0 0 * * ? *', 'DO_NOTHING', 'FIRST', 'demoJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2018-11-03 22:21:31', '');
INSERT INTO `xxl_job_user`(`id`, `username`, `password`, `role`, `permission`) VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);
INSERT INTO `xxl_job_lock` ( `lock_name`) VALUES ( 'schedule_lock');

commit;


-- JeeSite core tables (use utf8 to avoid index key length issues)
USE jeesite;
SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS js_gen_table_column;
DROP TABLE IF EXISTS js_gen_table;
DROP TABLE IF EXISTS js_sys_company_office;
DROP TABLE IF EXISTS js_sys_employee_post;
DROP TABLE IF EXISTS js_sys_user_data_scope;
DROP TABLE IF EXISTS js_sys_user_role;
DROP TABLE IF EXISTS js_sys_user;
DROP TABLE IF EXISTS js_sys_employee;
DROP TABLE IF EXISTS js_sys_company;
DROP TABLE IF EXISTS js_sys_area;
DROP TABLE IF EXISTS js_sys_config;
DROP TABLE IF EXISTS js_sys_dict_data;
DROP TABLE IF EXISTS js_sys_dict_type;
DROP TABLE IF EXISTS js_sys_file_upload;
DROP TABLE IF EXISTS js_sys_file_entity;
DROP TABLE IF EXISTS js_sys_job_log;
DROP TABLE IF EXISTS js_sys_job;
DROP TABLE IF EXISTS js_sys_lang;
DROP TABLE IF EXISTS js_sys_log;
DROP TABLE IF EXISTS js_sys_role_menu;
DROP TABLE IF EXISTS js_sys_menu;
DROP TABLE IF EXISTS js_sys_module;
DROP TABLE IF EXISTS js_sys_msg_inner_record;
DROP TABLE IF EXISTS js_sys_msg_inner;
DROP TABLE IF EXISTS js_sys_msg_push;
DROP TABLE IF EXISTS js_sys_msg_pushed;
DROP TABLE IF EXISTS js_sys_msg_template;
DROP TABLE IF EXISTS js_sys_office;
DROP TABLE IF EXISTS js_sys_post;
DROP TABLE IF EXISTS js_sys_role_data_scope;
DROP TABLE IF EXISTS js_sys_role;




/* Create Tables */

-- 代码生成表
CREATE TABLE js_gen_table
(
	table_name varchar(64) NOT NULL COMMENT '表名',
	class_name varchar(100) NOT NULL COMMENT '实体类名称',
	comments varchar(500) NOT NULL COMMENT '表说明',
	parent_table_name varchar(64) COMMENT '关联父表的表名',
	parent_table_fk_name varchar(64) COMMENT '本表关联父表的外键名',
	data_source_name varchar(64) COMMENT '数据源名称',
	tpl_category varchar(200) COMMENT '使用的模板',
	package_name varchar(500) COMMENT '生成包路径',
	module_name varchar(30) COMMENT '生成模块名',
	sub_module_name varchar(30) COMMENT '生成子模块名',
	function_name varchar(200) COMMENT '生成功能名',
	function_name_simple varchar(50) COMMENT '生成功能名（简写）',
	function_author varchar(50) COMMENT '生成功能作者',
	gen_base_dir varchar(1000) COMMENT '生成基础路径',
	options varchar(1000) COMMENT '其它生成选项',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (table_name)
) COMMENT = '代码生成表';


-- 代码生成表列
CREATE TABLE js_gen_table_column
(
	id varchar(64) NOT NULL COMMENT '编号',
	table_name varchar(64) NOT NULL COMMENT '表名',
	column_name varchar(64) NOT NULL COMMENT '列名',
	column_sort decimal(10) COMMENT '列排序（升序）',
	column_type varchar(100) NOT NULL COMMENT '类型',
	column_label varchar(50) COMMENT '列标签名',
	comments varchar(500) NOT NULL COMMENT '列备注说明',
	attr_name varchar(200) NOT NULL COMMENT '类的属性名',
	attr_type varchar(200) NOT NULL COMMENT '类的属性类型',
	is_pk char(1) COMMENT '是否主键',
	is_null char(1) COMMENT '是否可为空',
	is_insert char(1) COMMENT '是否插入字段',
	is_update char(1) COMMENT '是否更新字段',
	is_list char(1) COMMENT '是否列表字段',
	is_query char(1) COMMENT '是否查询字段',
	query_type varchar(200) COMMENT '查询方式',
	is_edit char(1) COMMENT '是否编辑字段',
	show_type varchar(200) COMMENT '表单类型',
	options varchar(1000) COMMENT '其它生成选项',
	PRIMARY KEY (id)
) COMMENT = '代码生成表列';


-- 行政区划
CREATE TABLE js_sys_area
(
	area_code varchar(100) NOT NULL COMMENT '区域编码',
	parent_code varchar(64) NOT NULL COMMENT '父级编号',
	parent_codes varchar(1000) NOT NULL COMMENT '所有父级编号',
	tree_sort decimal(10) NOT NULL COMMENT '本级排序号（升序）',
	tree_sorts varchar(1000) NOT NULL COMMENT '所有级别排序号',
	tree_leaf char(1) NOT NULL COMMENT '是否最末级',
	tree_level decimal(4) NOT NULL COMMENT '层次级别',
	tree_names varchar(1000) NOT NULL COMMENT '全节点名',
	area_name varchar(100) NOT NULL COMMENT '区域名称',
	area_type char(1) COMMENT '区域类型',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (area_code)
) COMMENT = '行政区划';


-- 公司表
CREATE TABLE js_sys_company
(
	company_code varchar(64) NOT NULL COMMENT '公司编码',
	parent_code varchar(64) NOT NULL COMMENT '父级编号',
	parent_codes varchar(1000) NOT NULL COMMENT '所有父级编号',
	tree_sort decimal(10) NOT NULL COMMENT '本级排序号（升序）',
	tree_sorts varchar(1000) NOT NULL COMMENT '所有级别排序号',
	tree_leaf char(1) NOT NULL COMMENT '是否最末级',
	tree_level decimal(4) NOT NULL COMMENT '层次级别',
	tree_names varchar(1000) NOT NULL COMMENT '全节点名',
	view_code varchar(100) NOT NULL COMMENT '公司代码',
	company_name varchar(200) NOT NULL COMMENT '公司名称',
	full_name varchar(200) NOT NULL COMMENT '公司全称',
	area_code varchar(100) COMMENT '区域编码',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	extend_s1 varchar(500) COMMENT '扩展 String 1',
	extend_s2 varchar(500) COMMENT '扩展 String 2',
	extend_s3 varchar(500) COMMENT '扩展 String 3',
	extend_s4 varchar(500) COMMENT '扩展 String 4',
	extend_s5 varchar(500) COMMENT '扩展 String 5',
	extend_s6 varchar(500) COMMENT '扩展 String 6',
	extend_s7 varchar(500) COMMENT '扩展 String 7',
	extend_s8 varchar(500) COMMENT '扩展 String 8',
	extend_i1 decimal(19) COMMENT '扩展 Integer 1',
	extend_i2 decimal(19) COMMENT '扩展 Integer 2',
	extend_i3 decimal(19) COMMENT '扩展 Integer 3',
	extend_i4 decimal(19) COMMENT '扩展 Integer 4',
	extend_f1 decimal(19,4) COMMENT '扩展 Float 1',
	extend_f2 decimal(19,4) COMMENT '扩展 Float 2',
	extend_f3 decimal(19,4) COMMENT '扩展 Float 3',
	extend_f4 decimal(19,4) COMMENT '扩展 Float 4',
	extend_d1 datetime COMMENT '扩展 Date 1',
	extend_d2 datetime COMMENT '扩展 Date 2',
	extend_d3 datetime COMMENT '扩展 Date 3',
	extend_d4 datetime COMMENT '扩展 Date 4',
	PRIMARY KEY (company_code)
) COMMENT = '公司表';


-- 公司部门关联表
CREATE TABLE js_sys_company_office
(
	company_code varchar(64) NOT NULL COMMENT '公司编码',
	office_code varchar(64) NOT NULL COMMENT '机构编码',
	PRIMARY KEY (company_code, office_code)
) COMMENT = '公司部门关联表';


-- 参数配置表
CREATE TABLE js_sys_config
(
	id varchar(64) NOT NULL COMMENT '编号',
	config_name varchar(100) NOT NULL COMMENT '名称',
	config_key varchar(100) NOT NULL COMMENT '参数键',
	config_value varchar(1000) COMMENT '参数值',
	is_sys char(1) NOT NULL COMMENT '系统内置（1是 0否）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (id)
) COMMENT = '参数配置表';


-- 字典数据表
CREATE TABLE js_sys_dict_data
(
	dict_code varchar(64) NOT NULL COMMENT '字典编码',
	parent_code varchar(64) NOT NULL COMMENT '父级编号',
	parent_codes varchar(1000) NOT NULL COMMENT '所有父级编号',
	tree_sort decimal(10) NOT NULL COMMENT '本级排序号（升序）',
	tree_sorts varchar(1000) NOT NULL COMMENT '所有级别排序号',
	tree_leaf char(1) NOT NULL COMMENT '是否最末级',
	tree_level decimal(4) NOT NULL COMMENT '层次级别',
	tree_names varchar(1000) NOT NULL COMMENT '全节点名',
	dict_label varchar(100) NOT NULL COMMENT '字典标签',
	dict_value varchar(100) NOT NULL COMMENT '字典键值',
	dict_type varchar(100) NOT NULL COMMENT '字典类型',
	is_sys char(1) NOT NULL COMMENT '系统内置（1是 0否）',
	description varchar(500) COMMENT '字典描述',
	css_style varchar(500) COMMENT 'css样式（如：color:red)',
	css_class varchar(500) COMMENT 'css类名（如：red）',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	extend_s1 varchar(500) COMMENT '扩展 String 1',
	extend_s2 varchar(500) COMMENT '扩展 String 2',
	extend_s3 varchar(500) COMMENT '扩展 String 3',
	extend_s4 varchar(500) COMMENT '扩展 String 4',
	extend_s5 varchar(500) COMMENT '扩展 String 5',
	extend_s6 varchar(500) COMMENT '扩展 String 6',
	extend_s7 varchar(500) COMMENT '扩展 String 7',
	extend_s8 varchar(500) COMMENT '扩展 String 8',
	extend_i1 decimal(19) COMMENT '扩展 Integer 1',
	extend_i2 decimal(19) COMMENT '扩展 Integer 2',
	extend_i3 decimal(19) COMMENT '扩展 Integer 3',
	extend_i4 decimal(19) COMMENT '扩展 Integer 4',
	extend_f1 decimal(19,4) COMMENT '扩展 Float 1',
	extend_f2 decimal(19,4) COMMENT '扩展 Float 2',
	extend_f3 decimal(19,4) COMMENT '扩展 Float 3',
	extend_f4 decimal(19,4) COMMENT '扩展 Float 4',
	extend_d1 datetime COMMENT '扩展 Date 1',
	extend_d2 datetime COMMENT '扩展 Date 2',
	extend_d3 datetime COMMENT '扩展 Date 3',
	extend_d4 datetime COMMENT '扩展 Date 4',
	PRIMARY KEY (dict_code)
) COMMENT = '字典数据表';


-- 字典类型表
CREATE TABLE js_sys_dict_type
(
	id varchar(64) NOT NULL COMMENT '编号',
	dict_name varchar(100) NOT NULL COMMENT '字典名称',
	dict_type varchar(100) NOT NULL COMMENT '字典类型',
	is_sys char(1) NOT NULL COMMENT '是否系统字典',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (id)
) COMMENT = '字典类型表';


-- 员工表
CREATE TABLE js_sys_employee
(
	emp_code varchar(64) NOT NULL COMMENT '员工编码',
	emp_name varchar(100) NOT NULL COMMENT '员工姓名',
	emp_name_en varchar(100) COMMENT '英文名',
	office_code varchar(64) NOT NULL COMMENT '机构编码',
	office_name varchar(100) NOT NULL COMMENT '机构名称',
	company_code varchar(64) COMMENT '公司编码',
	company_name varchar(200) COMMENT '公司名称',
	status char(1) NOT NULL COMMENT '状态（0在职 1删除 2离职）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	PRIMARY KEY (emp_code)
) COMMENT = '员工表';


-- 员工与岗位关联表
CREATE TABLE js_sys_employee_post
(
	emp_code varchar(64) NOT NULL COMMENT '员工编码',
	post_code varchar(64) NOT NULL COMMENT '岗位编码',
	PRIMARY KEY (emp_code, post_code)
) COMMENT = '员工与岗位关联表';


-- 文件实体表
CREATE TABLE js_sys_file_entity
(
	file_id varchar(64) NOT NULL COMMENT '文件编号',
	file_md5 varchar(64) NOT NULL COMMENT '文件MD5',
	file_path varchar(1000) NOT NULL COMMENT '文件相对路径',
	file_content_type varchar(200) NOT NULL COMMENT '文件内容类型',
	file_extension varchar(100) NOT NULL COMMENT '文件后缀扩展名',
	file_size decimal(31) NOT NULL COMMENT '文件大小(单位B)',
	file_meta varchar(255) COMMENT '文件信息(JSON格式)',
	PRIMARY KEY (file_id)
) COMMENT = '文件实体表';


-- 文件上传表
CREATE TABLE js_sys_file_upload
(
	id varchar(64) NOT NULL COMMENT '编号',
	file_id varchar(64) NOT NULL COMMENT '文件编号',
	file_name varchar(500) NOT NULL COMMENT '文件名称',
	file_type varchar(20) NOT NULL COMMENT '文件分类（image、media、file）',
	biz_key varchar(64) COMMENT '业务主键',
	biz_type varchar(64) COMMENT '业务类型',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (id)
) COMMENT = '文件上传表';


-- 作业调度表
CREATE TABLE js_sys_job
(
	job_name varchar(64) NOT NULL COMMENT '任务名称',
	job_group varchar(64) NOT NULL COMMENT '任务组名',
	description varchar(100) NOT NULL COMMENT '任务描述',
	invoke_target varchar(1000) NOT NULL COMMENT '调用目标字符串',
	cron_expression varchar(255) NOT NULL COMMENT 'Cron执行表达式',
	misfire_instruction decimal(1) NOT NULL COMMENT '计划执行错误策略',
	concurrent char(1) NOT NULL COMMENT '是否并发执行',
	instance_name varchar(64) DEFAULT 'JeeSiteScheduler' NOT NULL COMMENT '集群的实例名字',
	status char(1) NOT NULL COMMENT '状态（0正常 1删除 2暂停）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (job_name, job_group)
) COMMENT = '作业调度表';


-- 作业调度日志表
CREATE TABLE js_sys_job_log
(
	id varchar(64) NOT NULL COMMENT '编号',
	job_name varchar(64) NOT NULL COMMENT '任务名称',
	job_group varchar(64) NOT NULL COMMENT '任务组名',
	job_type varchar(50) COMMENT '日志类型',
	job_event varchar(200) COMMENT '日志事件',
	job_message varchar(500) COMMENT '日志信息',
	is_exception char(1) COMMENT '是否异常',
	exception_info text COMMENT '异常信息',
	create_date datetime COMMENT '创建时间',
	PRIMARY KEY (id)
) COMMENT = '作业调度日志表';


-- 国际化语言
CREATE TABLE js_sys_lang
(
	id varchar(64) NOT NULL COMMENT '编号',
	module_code varchar(64) NOT NULL COMMENT '归属模块',
	lang_code varchar(500) NOT NULL COMMENT '语言编码',
	lang_text varchar(500) NOT NULL COMMENT '语言译文',
	lang_type varchar(50) NOT NULL COMMENT '语言类型',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (id)
) COMMENT = '国际化语言';


-- 操作日志表
CREATE TABLE js_sys_log
(
	id varchar(64) NOT NULL COMMENT '编号',
	log_type varchar(50) NOT NULL COMMENT '日志类型',
	log_title varchar(500) NOT NULL COMMENT '日志标题',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_by_name varchar(100) NOT NULL COMMENT '用户名称',
	create_date datetime NOT NULL COMMENT '创建时间',
	request_uri varchar(500) COMMENT '请求URI',
	request_method varchar(10) COMMENT '操作方式',
	request_params longtext COMMENT '操作提交的数据',
	diff_modify_data text COMMENT '新旧数据比较结果',
	biz_key varchar(64) COMMENT '业务主键',
	biz_type varchar(64) COMMENT '业务类型',
	remote_addr varchar(255) NOT NULL COMMENT '操作IP地址',
	server_addr varchar(255) NOT NULL COMMENT '请求服务器地址',
	is_exception char(1) COMMENT '是否异常',
	exception_info text COMMENT '异常信息',
	user_agent varchar(500) COMMENT '用户代理',
	device_name varchar(100) COMMENT '设备名称/操作系统',
	browser_name varchar(100) COMMENT '浏览器名称',
	execute_time decimal(19) COMMENT '执行时间',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	PRIMARY KEY (id)
) COMMENT = '操作日志表';


-- 菜单表
CREATE TABLE js_sys_menu
(
	menu_code varchar(64) NOT NULL COMMENT '菜单编码',
	parent_code varchar(64) NOT NULL COMMENT '父级编号',
	parent_codes varchar(1000) NOT NULL COMMENT '所有父级编号',
	tree_sort decimal(10) NOT NULL COMMENT '本级排序号（升序）',
	tree_sorts varchar(1000) NOT NULL COMMENT '所有级别排序号',
	tree_leaf char(1) NOT NULL COMMENT '是否最末级',
	tree_level decimal(4) NOT NULL COMMENT '层次级别',
	tree_names varchar(1000) NOT NULL COMMENT '全节点名',
	menu_name varchar(100) NOT NULL COMMENT '菜单名称',
	menu_type char(1) NOT NULL COMMENT '菜单类型（1菜单 2权限 3开发）',
	menu_href varchar(1000) COMMENT '链接',
	menu_target varchar(20) COMMENT '目标',
	menu_icon varchar(100) COMMENT '图标',
	menu_color varchar(50) COMMENT '颜色',
	permission varchar(1000) COMMENT '权限标识',
	weight decimal(4) COMMENT '菜单权重',
	is_show char(1) NOT NULL COMMENT '是否显示（1显示 0隐藏）',
	sys_code varchar(64) NOT NULL COMMENT '归属系统（default:主导航菜单、mobileApp:APP菜单）',
	module_codes varchar(500) NOT NULL COMMENT '归属模块（多个用逗号隔开）',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	extend_s1 varchar(500) COMMENT '扩展 String 1',
	extend_s2 varchar(500) COMMENT '扩展 String 2',
	extend_s3 varchar(500) COMMENT '扩展 String 3',
	extend_s4 varchar(500) COMMENT '扩展 String 4',
	extend_s5 varchar(500) COMMENT '扩展 String 5',
	extend_s6 varchar(500) COMMENT '扩展 String 6',
	extend_s7 varchar(500) COMMENT '扩展 String 7',
	extend_s8 varchar(500) COMMENT '扩展 String 8',
	extend_i1 decimal(19) COMMENT '扩展 Integer 1',
	extend_i2 decimal(19) COMMENT '扩展 Integer 2',
	extend_i3 decimal(19) COMMENT '扩展 Integer 3',
	extend_i4 decimal(19) COMMENT '扩展 Integer 4',
	extend_f1 decimal(19,4) COMMENT '扩展 Float 1',
	extend_f2 decimal(19,4) COMMENT '扩展 Float 2',
	extend_f3 decimal(19,4) COMMENT '扩展 Float 3',
	extend_f4 decimal(19,4) COMMENT '扩展 Float 4',
	extend_d1 datetime COMMENT '扩展 Date 1',
	extend_d2 datetime COMMENT '扩展 Date 2',
	extend_d3 datetime COMMENT '扩展 Date 3',
	extend_d4 datetime COMMENT '扩展 Date 4',
	PRIMARY KEY (menu_code)
) COMMENT = '菜单表';


-- 模块表
CREATE TABLE js_sys_module
(
	module_code varchar(64) NOT NULL COMMENT '模块编码',
	module_name varchar(100) NOT NULL COMMENT '模块名称',
	description varchar(500) COMMENT '模块描述',
	main_class_name varchar(500) COMMENT '主类全名',
	current_version varchar(50) COMMENT '当前版本',
	upgrade_info varchar(300) COMMENT '升级信息',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (module_code)
) COMMENT = '模块表';


-- 内部消息
CREATE TABLE js_sys_msg_inner
(
	id varchar(64) NOT NULL COMMENT '编号',
	msg_title varchar(200) NOT NULL COMMENT '消息标题',
	content_level char(1) NOT NULL COMMENT '内容级别（1普通 2一般 3紧急）',
	content_type char(1) COMMENT '内容类型（1公告 2新闻 3会议 4其它）',
	msg_content text NOT NULL COMMENT '消息内容',
	receive_type char(1) NOT NULL COMMENT '接受者类型（0全部 1用户 2部门 3角色 4岗位）',
	receive_codes text COMMENT '接受者字符串',
	receive_names text COMMENT '接受者名称字符串',
	send_user_code varchar(64) COMMENT '发送者用户编码',
	send_user_name varchar(100) COMMENT '发送者用户姓名',
	send_date datetime COMMENT '发送时间',
	is_attac char(1) COMMENT '是否有附件',
	notify_types varchar(100) COMMENT '通知类型（PC APP 短信 邮件 微信）多选',
	status char(1) NOT NULL COMMENT '状态（0正常 1删除 4审核 5驳回 9草稿）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (id)
) COMMENT = '内部消息';


-- 内部消息发送记录表
CREATE TABLE js_sys_msg_inner_record
(
	id varchar(64) NOT NULL COMMENT '编号',
	msg_inner_id varchar(64) NOT NULL COMMENT '所属消息',
	receive_user_code varchar(64) NOT NULL COMMENT '接受者用户编码',
	receive_user_name varchar(100) NOT NULL COMMENT '接受者用户姓名',
	read_status char(1) NOT NULL COMMENT '读取状态（0未送达 1已读 2未读）',
	read_date datetime COMMENT '阅读时间',
	is_star char(1) COMMENT '是否标星',
	PRIMARY KEY (id)
) COMMENT = '内部消息发送记录表';


-- 消息推送表
CREATE TABLE js_sys_msg_push
(
	id varchar(64) NOT NULL COMMENT '编号',
	msg_type varchar(16) NOT NULL COMMENT '消息类型（PC APP 短信 邮件 微信）',
	msg_title varchar(200) NOT NULL COMMENT '消息标题',
	msg_content text NOT NULL COMMENT '消息内容',
	biz_key varchar(64) COMMENT '业务主键',
	biz_type varchar(64) COMMENT '业务类型',
	receive_code varchar(64) NOT NULL COMMENT '接受者账号',
	receive_user_code varchar(64) NOT NULL COMMENT '接受者用户编码',
	receive_user_name varchar(100) NOT NULL COMMENT '接受者用户姓名',
	send_user_code varchar(64) NOT NULL COMMENT '发送者用户编码',
	send_user_name varchar(100) NOT NULL COMMENT '发送者用户姓名',
	send_date datetime NOT NULL COMMENT '发送时间',
	is_merge_push char(1) COMMENT '是否合并推送',
	plan_push_date datetime COMMENT '计划推送时间',
	push_number int COMMENT '推送尝试次数',
	push_return_code varchar(200) COMMENT '推送返回结果码',
	push_return_msg_id varchar(200) COMMENT '推送返回消息编号',
	push_return_content text COMMENT '推送返回的内容信息',
	push_status char(1) COMMENT '推送状态（0未推送 1成功  2失败）',
	push_date datetime COMMENT '推送时间',
	read_status char(1) COMMENT '读取状态（0未送达 1已读 2未读）',
	read_date datetime COMMENT '读取时间',
	PRIMARY KEY (id)
) COMMENT = '消息推送表';


-- 消息已推送表
CREATE TABLE js_sys_msg_pushed
(
	id varchar(64) NOT NULL COMMENT '编号',
	msg_type varchar(16) NOT NULL COMMENT '消息类型（PC APP 短信 邮件 微信）',
	msg_title varchar(200) NOT NULL COMMENT '消息标题',
	msg_content text NOT NULL COMMENT '消息内容',
	biz_key varchar(64) COMMENT '业务主键',
	biz_type varchar(64) COMMENT '业务类型',
	receive_code varchar(64) NOT NULL COMMENT '接受者账号',
	receive_user_code varchar(64) NOT NULL COMMENT '接受者用户编码',
	receive_user_name varchar(100) NOT NULL COMMENT '接受者用户姓名',
	send_user_code varchar(64) NOT NULL COMMENT '发送者用户编码',
	send_user_name varchar(100) NOT NULL COMMENT '发送者用户姓名',
	send_date datetime NOT NULL COMMENT '发送时间',
	is_merge_push char(1) COMMENT '是否合并推送',
	plan_push_date datetime COMMENT '计划推送时间',
	push_number int COMMENT '推送尝试次数',
	push_return_content text COMMENT '推送返回的内容信息',
	push_return_code varchar(200) COMMENT '推送返回结果码',
	push_return_msg_id varchar(200) COMMENT '推送返回消息编号',
	push_status char(1) COMMENT '推送状态（0未推送 1成功  2失败）',
	push_date datetime COMMENT '推送时间',
	read_status char(1) COMMENT '读取状态（0未送达 1已读 2未读）',
	read_date datetime COMMENT '读取时间',
	PRIMARY KEY (id)
) COMMENT = '消息已推送表';


-- 消息模板
CREATE TABLE js_sys_msg_template
(
	id varchar(64) NOT NULL COMMENT '编号',
	module_code varchar(64) COMMENT '归属模块',
	tpl_key varchar(100) NOT NULL COMMENT '模板键值',
	tpl_name varchar(100) NOT NULL COMMENT '模板名称',
	tpl_type varchar(16) NOT NULL COMMENT '模板类型',
	tpl_content text NOT NULL COMMENT '模板内容',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	PRIMARY KEY (id)
) COMMENT = '消息模板';


-- 组织机构表
CREATE TABLE js_sys_office
(
	office_code varchar(64) NOT NULL COMMENT '机构编码',
	parent_code varchar(64) NOT NULL COMMENT '父级编号',
	parent_codes varchar(1000) NOT NULL COMMENT '所有父级编号',
	tree_sort decimal(10) NOT NULL COMMENT '本级排序号（升序）',
	tree_sorts varchar(1000) NOT NULL COMMENT '所有级别排序号',
	tree_leaf char(1) NOT NULL COMMENT '是否最末级',
	tree_level decimal(4) NOT NULL COMMENT '层次级别',
	tree_names varchar(1000) NOT NULL COMMENT '全节点名',
	view_code varchar(100) NOT NULL COMMENT '机构代码',
	office_name varchar(100) NOT NULL COMMENT '机构名称',
	full_name varchar(200) NOT NULL COMMENT '机构全称',
	office_type char(1) NOT NULL COMMENT '机构类型',
	leader varchar(100) COMMENT '负责人',
	phone varchar(100) COMMENT '办公电话',
	address varchar(255) COMMENT '联系地址',
	zip_code varchar(100) COMMENT '邮政编码',
	email varchar(300) COMMENT '电子邮箱',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	extend_s1 varchar(500) COMMENT '扩展 String 1',
	extend_s2 varchar(500) COMMENT '扩展 String 2',
	extend_s3 varchar(500) COMMENT '扩展 String 3',
	extend_s4 varchar(500) COMMENT '扩展 String 4',
	extend_s5 varchar(500) COMMENT '扩展 String 5',
	extend_s6 varchar(500) COMMENT '扩展 String 6',
	extend_s7 varchar(500) COMMENT '扩展 String 7',
	extend_s8 varchar(500) COMMENT '扩展 String 8',
	extend_i1 decimal(19) COMMENT '扩展 Integer 1',
	extend_i2 decimal(19) COMMENT '扩展 Integer 2',
	extend_i3 decimal(19) COMMENT '扩展 Integer 3',
	extend_i4 decimal(19) COMMENT '扩展 Integer 4',
	extend_f1 decimal(19,4) COMMENT '扩展 Float 1',
	extend_f2 decimal(19,4) COMMENT '扩展 Float 2',
	extend_f3 decimal(19,4) COMMENT '扩展 Float 3',
	extend_f4 decimal(19,4) COMMENT '扩展 Float 4',
	extend_d1 datetime COMMENT '扩展 Date 1',
	extend_d2 datetime COMMENT '扩展 Date 2',
	extend_d3 datetime COMMENT '扩展 Date 3',
	extend_d4 datetime COMMENT '扩展 Date 4',
	PRIMARY KEY (office_code)
) COMMENT = '组织机构表';


-- 员工岗位表
CREATE TABLE js_sys_post
(
	post_code varchar(64) NOT NULL COMMENT '岗位编码',
	post_name varchar(100) NOT NULL COMMENT '岗位名称',
	post_type varchar(100) COMMENT '岗位分类（高管、中层、基层）',
	post_sort decimal(10) COMMENT '岗位排序（升序）',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	PRIMARY KEY (post_code)
) COMMENT = '员工岗位表';


-- 角色表
CREATE TABLE js_sys_role
(
	role_code varchar(64) NOT NULL COMMENT '角色编码',
	role_name varchar(100) NOT NULL COMMENT '角色名称',
	role_type varchar(100) COMMENT '角色分类（高管、中层、基层、其它）',
	role_sort decimal(10) COMMENT '角色排序（升序）',
	is_sys char(1) COMMENT '系统内置（1是 0否）',
	user_type varchar(16) COMMENT '用户类型（employee员工 member会员）',
	data_scope char(1) COMMENT '数据范围设置（0未设置  1全部数据 2自定义数据）',
	status char(1) DEFAULT '0' NOT NULL COMMENT '状态（0正常 1删除 2停用）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	PRIMARY KEY (role_code)
) COMMENT = '角色表';


-- 角色数据权限表
CREATE TABLE js_sys_role_data_scope
(
	role_code varchar(64) NOT NULL COMMENT '控制角色编码',
	ctrl_type varchar(20) NOT NULL COMMENT '控制类型',
	ctrl_data varchar(64) NOT NULL COMMENT '控制数据',
	ctrl_permi varchar(64) NOT NULL COMMENT '控制权限',
	PRIMARY KEY (role_code, ctrl_type, ctrl_data, ctrl_permi)
) COMMENT = '角色数据权限表';


-- 角色与菜单关联表
CREATE TABLE js_sys_role_menu
(
	role_code varchar(64) NOT NULL COMMENT '角色编码',
	menu_code varchar(64) NOT NULL COMMENT '菜单编码',
	PRIMARY KEY (role_code, menu_code)
) COMMENT = '角色与菜单关联表';


-- 用户表
CREATE TABLE js_sys_user
(
	user_code varchar(100) NOT NULL COMMENT '用户编码',
	login_code varchar(100) NOT NULL COMMENT '登录账号',
	user_name varchar(100) NOT NULL COMMENT '用户昵称',
	password varchar(100) NOT NULL COMMENT '登录密码',
	email varchar(300) COMMENT '电子邮箱',
	mobile varchar(100) COMMENT '手机号码',
	phone varchar(100) COMMENT '办公电话',
	sex char(1) COMMENT '用户性别',
	avatar varchar(1000) COMMENT '头像路径',
	sign varchar(200) COMMENT '个性签名',
	wx_openid varchar(100) COMMENT '绑定的微信号',
	mobile_imei varchar(100) COMMENT '绑定的手机串号',
	user_type varchar(16) NOT NULL COMMENT '用户类型',
	ref_code varchar(64) COMMENT '用户类型引用编号',
	ref_name varchar(100) COMMENT '用户类型引用姓名',
	mgr_type char(1) NOT NULL COMMENT '管理员类型（0非管理员 1系统管理员  2二级管理员）',
	pwd_security_level decimal(1) COMMENT '密码安全级别（0初始 1很弱 2弱 3安全 4很安全）',
	pwd_update_date datetime COMMENT '密码最后更新时间',
	pwd_update_record varchar(1000) COMMENT '密码修改记录',
	pwd_question varchar(200) COMMENT '密保问题',
	pwd_question_answer varchar(200) COMMENT '密保问题答案',
	pwd_question_2 varchar(200) COMMENT '密保问题2',
	pwd_question_answer_2 varchar(200) COMMENT '密保问题答案2',
	pwd_question_3 varchar(200) COMMENT '密保问题3',
	pwd_question_answer_3 varchar(200) COMMENT '密保问题答案3',
	pwd_quest_update_date datetime COMMENT '密码问题修改时间',
	last_login_ip varchar(100) COMMENT '最后登陆IP',
	last_login_date datetime COMMENT '最后登陆时间',
	freeze_date datetime COMMENT '冻结时间',
	freeze_cause varchar(200) COMMENT '冻结原因',
	user_weight decimal(8) DEFAULT 0 COMMENT '用户权重（降序）',
	status char NOT NULL COMMENT '状态（0正常 1删除 2停用 3冻结）',
	create_by varchar(64) NOT NULL COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(64) NOT NULL COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(500) COMMENT '备注信息',
	corp_code varchar(64) DEFAULT '0' NOT NULL COMMENT '租户代码',
	corp_name varchar(100) DEFAULT 'JeeSite' NOT NULL COMMENT '租户名称',
	extend_s1 varchar(500) COMMENT '扩展 String 1',
	extend_s2 varchar(500) COMMENT '扩展 String 2',
	extend_s3 varchar(500) COMMENT '扩展 String 3',
	extend_s4 varchar(500) COMMENT '扩展 String 4',
	extend_s5 varchar(500) COMMENT '扩展 String 5',
	extend_s6 varchar(500) COMMENT '扩展 String 6',
	extend_s7 varchar(500) COMMENT '扩展 String 7',
	extend_s8 varchar(500) COMMENT '扩展 String 8',
	extend_i1 decimal(19) COMMENT '扩展 Integer 1',
	extend_i2 decimal(19) COMMENT '扩展 Integer 2',
	extend_i3 decimal(19) COMMENT '扩展 Integer 3',
	extend_i4 decimal(19) COMMENT '扩展 Integer 4',
	extend_f1 decimal(19,4) COMMENT '扩展 Float 1',
	extend_f2 decimal(19,4) COMMENT '扩展 Float 2',
	extend_f3 decimal(19,4) COMMENT '扩展 Float 3',
	extend_f4 decimal(19,4) COMMENT '扩展 Float 4',
	extend_d1 datetime COMMENT '扩展 Date 1',
	extend_d2 datetime COMMENT '扩展 Date 2',
	extend_d3 datetime COMMENT '扩展 Date 3',
	extend_d4 datetime COMMENT '扩展 Date 4',
	PRIMARY KEY (user_code)
) COMMENT = '用户表';


-- 用户数据权限表
CREATE TABLE js_sys_user_data_scope
(
	user_code varchar(100) NOT NULL COMMENT '控制用户编码',
	ctrl_type varchar(20) NOT NULL COMMENT '控制类型',
	ctrl_data varchar(64) NOT NULL COMMENT '控制数据',
	ctrl_permi varchar(64) NOT NULL COMMENT '控制权限',
	PRIMARY KEY (user_code, ctrl_type, ctrl_data, ctrl_permi)
) COMMENT = '用户数据权限表';


-- 用户与角色关联表
CREATE TABLE js_sys_user_role
(
	user_code varchar(100) NOT NULL COMMENT '用户编码',
	role_code varchar(64) NOT NULL COMMENT '角色编码',
	PRIMARY KEY (user_code, role_code)
) COMMENT = '用户与角色关联表';



/* Create Indexes */

CREATE INDEX idx_gen_table_ptn ON js_gen_table (parent_table_name ASC);
CREATE INDEX idx_gen_table_column_tn ON js_gen_table_column (table_name ASC);
CREATE INDEX idx_sys_area_pc ON js_sys_area (parent_code ASC);
CREATE INDEX idx_sys_area_ts ON js_sys_area (tree_sort ASC);
CREATE INDEX idx_sys_area_status ON js_sys_area (status ASC);
CREATE INDEX idx_sys_area_pcs ON js_sys_area (parent_codes ASC);
CREATE INDEX idx_sys_area_tss ON js_sys_area (tree_sorts ASC);
CREATE INDEX idx_sys_company_cc ON js_sys_company (corp_code ASC);
CREATE INDEX idx_sys_company_pc ON js_sys_company (parent_code ASC);
CREATE INDEX idx_sys_company_ts ON js_sys_company (tree_sort ASC);
CREATE INDEX idx_sys_company_status ON js_sys_company (status ASC);
CREATE INDEX idx_sys_company_vc ON js_sys_company (view_code ASC);
CREATE INDEX idx_sys_company_pcs ON js_sys_company (parent_codes ASC);
CREATE INDEX idx_sys_company_tss ON js_sys_company (tree_sorts ASC);
CREATE UNIQUE INDEX idx_sys_config_key ON js_sys_config (config_key ASC);
CREATE INDEX idx_sys_dict_data_cc ON js_sys_dict_data (corp_code ASC);
CREATE INDEX idx_sys_dict_data_dt ON js_sys_dict_data (dict_type ASC);
CREATE INDEX idx_sys_dict_data_pc ON js_sys_dict_data (parent_code ASC);
CREATE INDEX idx_sys_dict_data_status ON js_sys_dict_data (status ASC);
CREATE INDEX idx_sys_dict_data_pcs ON js_sys_dict_data (parent_codes ASC);
CREATE INDEX idx_sys_dict_data_ts ON js_sys_dict_data (tree_sort ASC);
CREATE INDEX idx_sys_dict_data_tss ON js_sys_dict_data (tree_sorts ASC);
CREATE INDEX idx_sys_dict_data_dv ON js_sys_dict_data (dict_value ASC);
CREATE INDEX idx_sys_dict_type_is ON js_sys_dict_type (is_sys ASC);
CREATE INDEX idx_sys_dict_type_status ON js_sys_dict_type (status ASC);
CREATE INDEX idx_sys_employee_cco ON js_sys_employee (company_code ASC);
CREATE INDEX idx_sys_employee_cc ON js_sys_employee (corp_code ASC);
CREATE INDEX idx_sys_employee_ud ON js_sys_employee (update_date ASC);
CREATE INDEX idx_sys_employee_oc ON js_sys_employee (office_code ASC);
CREATE INDEX idx_sys_employee_status ON js_sys_employee (status ASC);
CREATE INDEX idx_sys_file_entity_md5 ON js_sys_file_entity (file_md5 ASC);
CREATE INDEX idx_sys_file_entity_size ON js_sys_file_entity (file_size ASC);
CREATE INDEX idx_sys_file_biz_ft ON js_sys_file_upload (file_type ASC);
CREATE INDEX idx_sys_file_biz_fi ON js_sys_file_upload (file_id ASC);
CREATE INDEX idx_sys_file_biz_status ON js_sys_file_upload (status ASC);
CREATE INDEX idx_sys_file_biz_cb ON js_sys_file_upload (create_by ASC);
CREATE INDEX idx_sys_file_biz_ud ON js_sys_file_upload (update_date ASC);
CREATE INDEX idx_sys_file_biz_bt ON js_sys_file_upload (biz_type ASC);
CREATE INDEX idx_sys_file_biz_bk ON js_sys_file_upload (biz_key ASC);
CREATE INDEX idx_sys_job_status ON js_sys_job (status ASC);
CREATE INDEX idx_sys_job_log_jn ON js_sys_job_log (job_name ASC);
CREATE INDEX idx_sys_job_log_jg ON js_sys_job_log (job_group ASC);
CREATE INDEX idx_sys_job_log_t ON js_sys_job_log (job_type ASC);
CREATE INDEX idx_sys_job_log_e ON js_sys_job_log (job_event ASC);
CREATE INDEX idx_sys_job_log_ie ON js_sys_job_log (is_exception ASC);
CREATE INDEX idx_sys_lang_code ON js_sys_lang (lang_code ASC);
CREATE INDEX idx_sys_lang_type ON js_sys_lang (lang_type ASC);
CREATE INDEX idx_sys_log_cb ON js_sys_log (create_by ASC);
CREATE INDEX idx_sys_log_cc ON js_sys_log (corp_code ASC);
CREATE INDEX idx_sys_log_lt ON js_sys_log (log_type ASC);
CREATE INDEX idx_sys_log_bk ON js_sys_log (biz_key ASC);
CREATE INDEX idx_sys_log_bt ON js_sys_log (biz_type ASC);
CREATE INDEX idx_sys_log_ie ON js_sys_log (is_exception ASC);
CREATE INDEX idx_sys_log_cd ON js_sys_log (create_date ASC);
CREATE INDEX idx_sys_menu_pc ON js_sys_menu (parent_code ASC);
CREATE INDEX idx_sys_menu_ts ON js_sys_menu (tree_sort ASC);
CREATE INDEX idx_sys_menu_status ON js_sys_menu (status ASC);
CREATE INDEX idx_sys_menu_mt ON js_sys_menu (menu_type ASC);
CREATE INDEX idx_sys_menu_pss ON js_sys_menu (parent_codes ASC);
CREATE INDEX idx_sys_menu_tss ON js_sys_menu (tree_sorts ASC);
CREATE INDEX idx_sys_menu_sc ON js_sys_menu (sys_code ASC);
CREATE INDEX idx_sys_menu_is ON js_sys_menu (is_show ASC);
CREATE INDEX idx_sys_menu_mcs ON js_sys_menu (module_codes ASC);
CREATE INDEX idx_sys_menu_wt ON js_sys_menu (weight ASC);
CREATE INDEX idx_sys_module_status ON js_sys_module (status ASC);
CREATE INDEX idx_sys_msg_inner_cb ON js_sys_msg_inner (create_by ASC);
CREATE INDEX idx_sys_msg_inner_status ON js_sys_msg_inner (status ASC);
CREATE INDEX idx_sys_msg_inner_cl ON js_sys_msg_inner (content_level ASC);
CREATE INDEX idx_sys_msg_inner_sc ON js_sys_msg_inner (send_user_code ASC);
CREATE INDEX idx_sys_msg_inner_sd ON js_sys_msg_inner (send_date ASC);
CREATE INDEX idx_sys_msg_inner_r_mi ON js_sys_msg_inner_record (msg_inner_id ASC);
CREATE INDEX idx_sys_msg_inner_r_ruc ON js_sys_msg_inner_record (receive_user_code ASC);
CREATE INDEX idx_sys_msg_inner_r_status ON js_sys_msg_inner_record (read_status ASC);
CREATE INDEX idx_sys_msg_inner_r_star ON js_sys_msg_inner_record (is_star ASC);
CREATE INDEX idx_sys_msg_push_type ON js_sys_msg_push (msg_type ASC);
CREATE INDEX idx_sys_msg_push_rc ON js_sys_msg_push (receive_code ASC);
CREATE INDEX idx_sys_msg_push_uc ON js_sys_msg_push (receive_user_code ASC);
CREATE INDEX idx_sys_msg_push_suc ON js_sys_msg_push (send_user_code ASC);
CREATE INDEX idx_sys_msg_push_pd ON js_sys_msg_push (plan_push_date ASC);
CREATE INDEX idx_sys_msg_push_ps ON js_sys_msg_push (push_status ASC);
CREATE INDEX idx_sys_msg_push_rs ON js_sys_msg_push (read_status ASC);
CREATE INDEX idx_sys_msg_push_bk ON js_sys_msg_push (biz_key ASC);
CREATE INDEX idx_sys_msg_push_bt ON js_sys_msg_push (biz_type ASC);
CREATE INDEX idx_sys_msg_push_imp ON js_sys_msg_push (is_merge_push ASC);
CREATE INDEX idx_sys_msg_pushed_type ON js_sys_msg_pushed (msg_type ASC);
CREATE INDEX idx_sys_msg_pushed_rc ON js_sys_msg_pushed (receive_code ASC);
CREATE INDEX idx_sys_msg_pushed_uc ON js_sys_msg_pushed (receive_user_code ASC);
CREATE INDEX idx_sys_msg_pushed_suc ON js_sys_msg_pushed (send_user_code ASC);
CREATE INDEX idx_sys_msg_pushed_pd ON js_sys_msg_pushed (plan_push_date ASC);
CREATE INDEX idx_sys_msg_pushed_ps ON js_sys_msg_pushed (push_status ASC);
CREATE INDEX idx_sys_msg_pushed_rs ON js_sys_msg_pushed (read_status ASC);
CREATE INDEX idx_sys_msg_pushed_bk ON js_sys_msg_pushed (biz_key ASC);
CREATE INDEX idx_sys_msg_pushed_bt ON js_sys_msg_pushed (biz_type ASC);
CREATE INDEX idx_sys_msg_pushed_imp ON js_sys_msg_pushed (is_merge_push ASC);
CREATE INDEX idx_sys_msg_tpl_key ON js_sys_msg_template (tpl_key ASC);
CREATE INDEX idx_sys_msg_tpl_type ON js_sys_msg_template (tpl_type ASC);
CREATE INDEX idx_sys_msg_tpl_status ON js_sys_msg_template (status ASC);
CREATE INDEX idx_sys_office_cc ON js_sys_office (corp_code ASC);
CREATE INDEX idx_sys_office_pc ON js_sys_office (parent_code ASC);
CREATE INDEX idx_sys_office_pcs ON js_sys_office (parent_codes ASC);
CREATE INDEX idx_sys_office_status ON js_sys_office (status ASC);
CREATE INDEX idx_sys_office_ot ON js_sys_office (office_type ASC);
CREATE INDEX idx_sys_office_vc ON js_sys_office (view_code ASC);
CREATE INDEX idx_sys_office_ts ON js_sys_office (tree_sort ASC);
CREATE INDEX idx_sys_office_tss ON js_sys_office (tree_sorts ASC);
CREATE INDEX idx_sys_post_cc ON js_sys_post (corp_code ASC);
CREATE INDEX idx_sys_post_status ON js_sys_post (status ASC);
CREATE INDEX idx_sys_post_ps ON js_sys_post (post_sort ASC);
CREATE INDEX idx_sys_role_cc ON js_sys_role (corp_code ASC);
CREATE INDEX idx_sys_role_is ON js_sys_role (is_sys ASC);
CREATE INDEX idx_sys_role_status ON js_sys_role (status ASC);
CREATE INDEX idx_sys_role_rs ON js_sys_role (role_sort ASC);
CREATE INDEX idx_sys_user_lc ON js_sys_user (login_code ASC);
CREATE INDEX idx_sys_user_email ON js_sys_user (email ASC);
CREATE INDEX idx_sys_user_mobile ON js_sys_user (mobile ASC);
CREATE INDEX idx_sys_user_wo ON js_sys_user (wx_openid ASC);
CREATE INDEX idx_sys_user_imei ON js_sys_user (mobile_imei ASC);
CREATE INDEX idx_sys_user_rt ON js_sys_user (user_type ASC);
CREATE INDEX idx_sys_user_rc ON js_sys_user (ref_code ASC);
CREATE INDEX idx_sys_user_mt ON js_sys_user (mgr_type ASC);
CREATE INDEX idx_sys_user_us ON js_sys_user (user_weight ASC);
CREATE INDEX idx_sys_user_ud ON js_sys_user (update_date ASC);
CREATE INDEX idx_sys_user_status ON js_sys_user (status ASC);
CREATE INDEX idx_sys_user_cc ON js_sys_user (corp_code ASC);



