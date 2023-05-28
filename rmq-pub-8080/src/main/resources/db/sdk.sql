create database `rmq_esb`;
use rmq_esb;
/**
 * sdk环境标识
 */
DROP TABLE IF EXISTS `sdk_env_flag`;
CREATE TABLE `sdk_env_flag` (
                               `id` bigint(11) NOT NULL AUTO_INCREMENT comment '主键ID',
                               `env_flag` varchar(2) comment '环境标志',
                               `env_flag_old` varchar(2) comment '老环境标志',
                                `env_update_time` bigint(11) comment '环境更新时间',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


/**
* 业务系统配置信息
 */
-- ----------------------------
-- Table structure for sdk_business_system_conf
-- ----------------------------
DROP TABLE IF EXISTS `sdk_business_system_conf`;
CREATE TABLE `sdk_business_system_conf` (
                                            `bsc_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                            `bsc_name` varchar(50) DEFAULT NULL COMMENT '系统名称',
                                            `bsc_code` varchar(50) DEFAULT NULL COMMENT '系统编号',
                                            `bsc_manufactor` varchar(50) DEFAULT NULL COMMENT '所属厂商',
                                            `bsc_connect_pool` int(11) DEFAULT '1' COMMENT '连接池开关 （1开启，2不开启，默认为1）',
                                            `bsc_reconnect_num` int(11) DEFAULT NULL COMMENT '重连次数 （默认为3次，-1表示一直重连，0表示不重连 ）',
                                            `bsc_reconnect_time` int(11) DEFAULT NULL COMMENT '重连接时间',
                                            `bsc_split_flow` tinyint(11) DEFAULT '1' COMMENT '限流开关 （1-开启，2-不开启，默认是1）',
                                            `bsc_key` varchar(255) DEFAULT NULL COMMENT '系统标志码',
                                            `bsc_resource_pool_warnning` double DEFAULT NULL COMMENT '告警阀值',
                                            `bsc_create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                            `bsc_remarks` varchar(255) DEFAULT NULL COMMENT '备注',
                                            `bsc_client_ip` text COMMENT '客户端IP地址（多组之间，用，分隔）',
                                            PRIMARY KEY (`bsc_id`) USING BTREE,
                                            UNIQUE KEY `bsc_name` (`bsc_name`) USING BTREE,
                                            UNIQUE KEY `bsc_code` (`bsc_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='业务系统配置信息';

-- ----------------------------
-- Records of sdk_business_system_conf
-- ----------------------------
INSERT INTO `sdk_business_system_conf` VALUES ('90', 'EMR', 'S02', '医惠', '1', '-1', '3000', '1', 'f5c69c8e84194773b6cbdaaab35aed01', '0.5', '2019-06-14 08:06:00', null, '172.16.90.16,172.16.90.17,172.16.90.21,172.16.90.22,172.16.90.39,172.16.90.40,172.16.10.243,172.16.10.244,172.16.10.245,172.16.10.251,172.16.10.247,172.16.10.253,172.19.90.11,172.19.90.5,172.19.90.144,172.19.90.249,172.19.90.94,172.16.10.55,172.16.10.56,172.16.10.57,172.19.90.246,172.19.90.139,172.19.90.82,172.16.16.214,25.25.25.25,172.16.80.42,172.16.84.250,172.16.10.218,1.1.1.1,172.16.90.249,172.16.84.247,172.16.84.249,172.16.84.248,172.16.120.249,172.16.84.245,192.168.3.172,172.16.84.201,172.16.84.241,172.16.80.44');
INSERT INTO `sdk_business_system_conf` VALUES ('93', 'HIS', 'S01', '复高', '1', '-1', '3000', '1', 'a6eceabe379f4dabae5d463785c7d345', '0.5', '2019-06-17 07:24:28', null, '172.16.10.20,1.1.1.1,172.19.90.11,172.19.90.249,172.16.10.71,172.16.84.247,172.19.90.82,172.16.84.248,192.168.3.19,172.16.84.243,172.16.84.245,192.168.3.1,172.16.80.44');
INSERT INTO `sdk_business_system_conf` VALUES ('98', '主索引', 'S92', '医惠', '1', '-1', '3000', '1', '7c27316b8c5440919852a8a6ab4faa53', '0.5', '2019-06-22 06:50:58', null, '172.16.90.219,172.19.90.11,172.19.90.249,172.16.84.9,172.19.90.82');
INSERT INTO `sdk_business_system_conf` VALUES ('103', 'LIS', 'S03', '杏和', '1', '-1', '3000', '1', 'e96a97dcde0e4909b35115edb64d2a61', '0.5', '2019-06-26 02:36:28', null, '172.16.10.63,3.3.3.3,172.19.90.11,172.19.90.249,172.16.80.46,172.16.84.247,172.19.90.82,172.16.84.248,172.16.120.249,172.19.89.7,127.0.0.1,172.16.84.253');
INSERT INTO `sdk_business_system_conf` VALUES ('104', '护理', 'S05', '好智', '1', '-1', '3000', '1', '57cdbd8710ef4a9d8502c061415af59c', '0.5', '2019-06-27 01:40:06', null, '172.16.10.192,172.19.90.11,172.16.84.247,172.16.120.249,172.16.84.243');
INSERT INTO `sdk_business_system_conf` VALUES ('105', '病理', 'S10', '郎珈', '1', '-1', '3000', '1', '8ce3630a44c44f449cb2dd201e6b6d2b', '0.5', '2019-06-27 02:35:31', null, '172.16.90.47,172.16.80.46');
INSERT INTO `sdk_business_system_conf` VALUES ('107', '手麻', 'S09', '麦迪斯顿', '1', '-1', '3000', '1', 'fbcc11606f28426d81183b8826812201', '0.5', '2019-07-01 05:29:40', null, '172.16.10.185,172.16.90.245,172.19.90.11,172.19.90.249,172.19.90.82');
INSERT INTO `sdk_business_system_conf` VALUES ('109', 'PACS', 'S04', '岱嘉', '1', '-1', '3000', '1', '192b6cc240394d93a146f00cbdef2d08', '0.5', '2019-07-04 03:04:17', null, '172.16.90.145,1.1.1.1,25.25.25.25,172.16.120.249,172.19.90.11,172.16.120.231,172.16.120.232,172.19.90.249,172.19.90.82,192.168.3.9,172.19.89.7,172.16.84.245');
INSERT INTO `sdk_business_system_conf` VALUES ('110', '心电', 'S08', '麦迪克斯', '1', '-1', '3000', '1', '40291af6cd5d4bc8b4ce3c21a7d91efa', '0.5', '2019-07-04 06:59:16', null, '172.16.90.55,1.1.1.1,172.16.60.138,172.19.90.11,172.19.90.249,172.16.90.249,172.16.84.247,172.19.89.7,172.16.80.44');
INSERT INTO `sdk_business_system_conf` VALUES ('112', '危急值管理系统', 'S40', '医惠', '1', '-1', '3000', '1', '2b9b6c18b04d431aa12337600dd06bd0', '0.5', '2019-07-05 11:08:48', null, '172.16.10.215,1.1.1.1,172.16.90.16,172.16.90.17,172.16.90.21,172.16.90.22,172.16.90.39,172.16.90.40,172.16.10.243,172.16.10.244,172.16.10.245,172.16.10.251,172.16.10.247,172.16.10.253,172.19.90.11,40.40.40.40,172.16.10.55,172.16.10.56,172.16.10.57,172.16.84.250,172.16.84.239,172.19.89.7');
INSERT INTO `sdk_business_system_conf` VALUES ('115', '嵌入式门户', 'S35', '嵌入式门户', '1', '-1', '3000', '1', '404415aae1b245d483aa96f109b2ea9b', '0.5', '2019-07-12 08:59:43', null, '172.16.10.84,172.16.90.16,172.16.90.17,172.16.90.21,172.16.90.22,172.16.90.39,172.16.90.40,172.16.10.243,172.16.10.244,172.16.10.245,172.16.10.251,172.16.10.247,172.16.10.253,172.19.90.11');
INSERT INTO `sdk_business_system_conf` VALUES ('117', '审方', 'S31', '天际', '1', '-1', '3000', '1', 'd9e6b96c0375442093aaf72d87664a22', '0.5', '2019-07-18 11:05:49', null, '172.16.90.154,172.16.90.155,172.19.90.11');
INSERT INTO `sdk_business_system_conf` VALUES ('118', '智能病理诊断', 'S37', '医惠科技', '1', '-1', '3000', '1', 'da2f979f7fc648b7a0895beae70196d2', '0.5', '2019-07-22 07:40:16', null, '1.1.1.1,1.1.1.2,172.16.80.140,172.19.90.11,172.16.13.16');
INSERT INTO `sdk_business_system_conf` VALUES ('119', '内镜工作站', 'S36', '医惠', '1', '-1', '3000', '1', '7a1913b21be7482892baf6d4a74ec171', '0.5', '2019-08-12 09:23:55', null, '172.16.10.55,172.16.10.56,172.16.10.57');
INSERT INTO `sdk_business_system_conf` VALUES ('121', '营养膳食系统', 'S42', '营康', '1', '-1', '3000', '1', '5eb2ff3a4ebe4c2ca95d691c092aed76', '0.5', '2019-09-03 10:05:49', null, '1.1.1.1,192.168.137.157,172.16.16.87');
INSERT INTO `sdk_business_system_conf` VALUES ('122', '消毒供应', 'S17', '医惠', '1', '-1', '3000', '1', '3c5afed225fa4c43bc47cb6d67ade24c', '0.5', '2019-09-04 09:42:01', null, '172.16.16.28');

-- ----------------------------
-- Table structure for sdk_queue_manager
-- ----------------------------
DROP TABLE IF EXISTS `sdk_queue_manager`;
CREATE TABLE `sdk_queue_manager` (
                                     `qm_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                     `qm_pid` int(10) DEFAULT '0' COMMENT '父ID',
                                     `bsc_id` int(10) DEFAULT NULL COMMENT '外键-业务系统ID',
                                     `qm_name` varchar(60) DEFAULT NULL COMMENT '队列管理器',
                                     `qm_ip` varchar(15) DEFAULT NULL COMMENT '队列管理器IP地址',
                                     `qm_create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `qm_status` varchar(1) DEFAULT '2' COMMENT '是否可用（1-可用，2-不可用）新增且未测试的标记为不可用',
                                     `qm_use_hardware_load` tinyint(4) DEFAULT '0' COMMENT '是否启用硬负载，默认关闭(0关闭，1开启)',
                                     PRIMARY KEY (`qm_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='队列管理器表';

-- ----------------------------
-- Records of sdk_queue_manager
-- ----------------------------
INSERT INTO `sdk_queue_manager` VALUES ('168', '0', '90', 'P02', '172.16.90.31', '2019-06-18 11:12:46', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('177', '0', '98', 'P92', '172.16.90.31', '2019-06-22 06:50:58', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('199', '0', '90', 'S02', '172.16.90.31', '2019-06-25 02:38:51', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('213', '0', '103', 'S03', '172.16.90.31', '2019-06-26 02:40:02', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('214', '0', '103', 'P03', '172.16.90.31', '2019-06-26 06:14:20', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('218', '0', '104', 'P05', '172.16.90.31', '2019-06-27 01:45:12', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('219', '0', '105', 'P10', '172.16.90.31', '2019-06-27 02:37:02', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('220', '0', '104', 'S05', '172.16.90.31', '2019-06-27 05:10:49', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('234', '0', '93', 'P01', '172.16.90.31', '2019-06-29 10:16:22', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('235', '0', '105', 'S10', '172.16.90.31', '2019-07-01 01:34:11', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('242', '0', '107', 'P09', '172.16.90.31', '2019-07-01 05:31:02', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('245', '0', '109', 'P04', '172.16.90.31', '2019-07-04 03:05:21', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('246', '0', '110', 'P08', '172.16.90.31', '2019-07-04 07:00:17', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('248', '0', '112', 'P40', '172.16.90.31', '2019-07-05 11:10:24', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('249', '0', '112', 'S40', '172.16.90.31', '2019-07-05 11:12:27', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('257', '0', '115', 'S35', '172.16.90.31', '2019-07-12 09:00:43', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('264', '0', '117', 'S31', '172.16.90.31', '2019-07-18 11:06:39', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('267', '0', '117', 'P31', '172.16.90.31', '2019-07-18 11:27:19', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('268', '0', '118', 'S37', '172.16.90.31', '2019-07-22 07:41:27', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('272', '0', '115', 'P35', '172.16.90.31', '2019-07-31 07:31:27', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('273', '0', '119', 'P36', '172.16.90.31', '2019-08-12 09:25:16', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('277', '0', '121', 'P42', '172.16.90.31', '2019-09-03 10:19:34', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('278', '0', '122', 'P17', '172.16.90.31', '2019-09-04 09:44:41', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('282', '0', '122', 'S17', '172.16.90.31', '2019-09-04 11:50:00', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('283', '0', '124', 'P45', '172.16.90.31', '2019-09-05 09:16:41', '1', '1');
INSERT INTO `sdk_queue_manager` VALUES ('295', '0', '121', 'S42', '172.16.90.31', '2019-09-11 09:25:11', '1', '1');

/**
* 队列表
 */
DROP TABLE IF EXISTS `sdk_queue`;
CREATE TABLE `sdk_queue` (
                             `q_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                             `qm_id` int(10) DEFAULT NULL COMMENT '外键-队列管理器表ID',
                             `q_name_put` varchar(50) DEFAULT NULL COMMENT 'put队列名称',
                             `q_name_get` varchar(50) DEFAULT NULL COMMENT 'get队列名称',
                             `q_name_collect` varchar(50) DEFAULT NULL COMMENT 'collect队列名称',
                             `q_create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `q_get_status` int(11) DEFAULT NULL COMMENT 'get队列是否可用（1-可用，2-不可用）',
                             `q_put_status` int(11) DEFAULT NULL COMMENT 'put队列是否可用（1-可用，2-不可用）',
                             `q_collect_status` int(11) DEFAULT NULL COMMENT 'collect队列是否可用（1-可用，2-不可用）',
                             `q_join_connect_pool` int(11) DEFAULT '2' COMMENT '是否加入所属队列管理器的资源池 （1-加入，2-不加入）默认不加入',
                             PRIMARY KEY (`q_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4563 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='队列表';

-- ----------------------------
-- Records of sdk_queue
-- ----------------------------
INSERT INTO `sdk_queue` VALUES ('2782', '467', 'EQ.S01.PS02021.PUT', 'EQ.S01.PS02021.GET', 'EQ.S01.PS02021.COLLECT', '2019-07-02 06:14:25', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2783', '468', 'EQ.S01.PS02021.PUT', 'EQ.S01.PS02021.GET', 'EQ.S01.PS02021.COLLECT', '2019-07-02 06:14:25', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2784', '467', 'EQ.S01.PS02022.PUT', 'EQ.S01.PS02022.GET', 'EQ.S01.PS02022.COLLECT', '2019-06-29 10:26:36', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2785', '468', 'EQ.S01.PS02022.PUT', 'EQ.S01.PS02022.GET', 'EQ.S01.PS02022.COLLECT', '2019-06-29 10:26:36', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2786', '467', 'EQ.S01.PS02023.PUT', 'EQ.S01.PS02023.GET', 'EQ.S01.PS02023.COLLECT', '2019-07-19 08:42:28', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2787', '468', 'EQ.S01.PS02023.PUT', 'EQ.S01.PS02023.GET', 'EQ.S01.PS02023.COLLECT', '2019-07-19 08:42:28', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2788', '467', 'EQ.S01.PS02025.PUT', 'EQ.S01.PS02025.GET', 'EQ.S01.PS02025.COLLECT', '2019-08-14 08:50:21', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2789', '468', 'EQ.S01.PS02025.PUT', 'EQ.S01.PS02025.GET', 'EQ.S01.PS02025.COLLECT', '2019-08-14 08:50:21', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2790', '467', 'EQ.S01.PS02026.PUT', 'EQ.S01.PS02026.GET', 'EQ.S01.PS02026.COLLECT', '2020-03-05 08:32:47', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2791', '468', 'EQ.S01.PS02026.PUT', 'EQ.S01.PS02026.GET', 'EQ.S01.PS02026.COLLECT', '2020-03-05 08:32:47', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2792', '467', 'EQ.S01.PS02027.PUT', 'EQ.S01.PS02027.GET', 'EQ.S01.PS02027.COLLECT', '2019-07-19 08:39:24', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2793', '468', 'EQ.S01.PS02027.PUT', 'EQ.S01.PS02027.GET', 'EQ.S01.PS02027.COLLECT', '2019-07-19 08:39:24', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2794', '467', 'EQ.S01.PS02028.PUT', 'EQ.S01.PS02028.GET', 'EQ.S01.PS02028.COLLECT', '2019-07-19 06:10:32', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2795', '468', 'EQ.S01.PS02028.PUT', 'EQ.S01.PS02028.GET', 'EQ.S01.PS02028.COLLECT', '2019-07-19 06:10:32', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2796', '467', 'EQ.S01.PS02030.PUT', 'EQ.S01.PS02030.GET', 'EQ.S01.PS02030.COLLECT', '2019-08-08 02:38:00', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2797', '468', 'EQ.S01.PS02030.PUT', 'EQ.S01.PS02030.GET', 'EQ.S01.PS02030.COLLECT', '2019-08-08 02:38:00', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2798', '467', 'EQ.S01.PS02031.PUT', 'EQ.S01.PS02031.GET', 'EQ.S01.PS02031.COLLECT', '2019-07-19 08:40:50', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2799', '468', 'EQ.S01.PS02031.PUT', 'EQ.S01.PS02031.GET', 'EQ.S01.PS02031.COLLECT', '2019-07-19 08:40:50', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('2800', '467', 'EQ.S01.PS02032.PUT', 'EQ.S01.PS02032.GET', 'EQ.S01.PS02032.COLLECT', '2019-07-19 08:48:17', '1', '1', '1', '2');
INSERT INTO `sdk_queue` VALUES ('3692', '547', 'EQ.S40.DC10002.PUT', 'EQ.S40.DC10002.GET', null, '2019-07-05 11:12:27', '1', '1', null, '1');
INSERT INTO `sdk_queue` VALUES ('3693', '548', 'EQ.S40.DC10002.PUT', 'EQ.S40.DC10002.GET', null, '2019-07-05 11:12:27', '1', '1', null, '1');
INSERT INTO `sdk_queue` VALUES ('3694', '547', 'EQ.S40.DC10003.PUT', 'EQ.S40.DC10003.GET', null, '2019-07-05 11:12:27', '1', '1', null, '1');
INSERT INTO `sdk_queue` VALUES ('3695', '548', 'EQ.S40.DC10003.PUT', 'EQ.S40.DC10003.GET', null, '2019-07-05 11:12:27', '1', '1', null, '1');
INSERT INTO `sdk_queue` VALUES ('3696', '547', 'EQ.S40.DC10004.PUT', 'EQ.S40.DC10004.GET', null, '2019-07-05 11:12:27', '1', '1', null, '1');
INSERT INTO `sdk_queue` VALUES ('3697', '548', 'EQ.S40.DC10004.PUT', 'EQ.S40.DC10004.GET', null, '2019-07-05 11:12:27', '1', '1', null, '1');
INSERT INTO `sdk_queue` VALUES ('3698', '547', 'EQ.S40.DC10005.PUT', 'EQ.S40.DC10005.GET', null, '2019-07-05 11:12:27', '1', '1', null, '1');
INSERT INTO `sdk_queue` VALUES ('3699', '548', 'EQ.S40.DC10005.PUT', 'EQ.S40.DC10005.GET', null, '2019-07-05 11:12:27', '1', '1', null, '1');
