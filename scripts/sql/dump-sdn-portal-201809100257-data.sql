-- MySQL dump 10.13  Distrib 5.7.19, for osx10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: hippo-integration
-- ------------------------------------------------------
-- Server version	5.7.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `gateway_api_define`
--

DROP TABLE IF EXISTS `gateway_api_define`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gateway_api_define` (
  `service_id` varchar(100) NOT NULL COMMENT '接口服务ID',
  `service_model` varchar(100) NOT NULL COMMENT '服务类型。httpservice,dubboservice,hessianservice,soapservice等',
  `domain` varchar(255) DEFAULT '' COMMENT '转发目标地址URL，k8s填写环境变量或cluster Ip',
  `path` varchar(255) DEFAULT '' COMMENT 'http服务的请求PATH',
  `param` varchar(255) DEFAULT '' COMMENT '接口需校验参数列表',
  `auth_code` varchar(32) DEFAULT '' COMMENT '接口对应的权限值',
  `permission` tinyint(1) DEFAULT '0' COMMENT '是否需要登录校验, 1需要验证登录,0 不需要验证登录',
  `argument` varchar(255) DEFAULT '' COMMENT 'RPC接口存在，调用目标服务的参数类型',
  `interfaze` varchar(255) DEFAULT '' COMMENT '目标地址的接口类名称',
  `method` varchar(100) DEFAULT '' COMMENT 'RPC存在，目标服务的接口方法名',
  `replenishRate` bigint(20) unsigned DEFAULT '0' COMMENT '限流： 令牌桶填充平均速率，单位：秒',
  `burstCapacity` bigint(20) unsigned DEFAULT '0' COMMENT '限流： 令牌桶上限。',
  `circuitBreakerEnabled` tinyint(1) DEFAULT '0' COMMENT ' 熔断开关,1开启，0关闭',
  `metricsRollingStatisticalWindowInMilliseconds` bigint(20) unsigned DEFAULT '0' COMMENT '熔断： 统计滚动的时间窗口',
  `circuitBreakerSleepWindowInMilliseconds` bigint(20) unsigned DEFAULT '0' COMMENT '熔断时间窗口，当熔断器打开5s后进入半开状态，允许部分流量进来重试',
  `circuitBreakerRequestVolumeThreshold` bigint(20) unsigned DEFAULT '0' COMMENT '熔断器在整个统计时间内是否开启的阀值',
  `circuitBreakerErrorThresholdPercentage` bigint(20) unsigned DEFAULT '0' COMMENT '熔断打开最大错误率',
  `executionIsolationSemaphoreMaxConcurrentRequests` bigint(20) unsigned DEFAULT '0' COMMENT '最大并发数',
  `fallbackIsolationSemaphoreMaxConcurrentRequests` bigint(20) unsigned DEFAULT '0' COMMENT '设置失败回滚调用最大并发数',
  UNIQUE KEY `gateway_api_define_UN` (`service_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gateway_api_define`
--

LOCK TABLES `gateway_api_define` WRITE;
/*!40000 ALTER TABLE `gateway_api_define` DISABLE KEYS */;
/*!40000 ALTER TABLE `gateway_api_define` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sdn_service_config`
--

DROP TABLE IF EXISTS `sdn_service_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sdn_service_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(100) NOT NULL DEFAULT '' COMMENT '接口服务ID',
  `service_desc` varchar(255) DEFAULT '' COMMENT '接口描述',
  `service_model` varchar(100) NOT NULL DEFAULT '' COMMENT '服务类型。httpservice,dubboservice,hessianservice,soapservice等',
  `service_log` tinyint(1) DEFAULT '0' COMMENT '日志开关 1 记录 0 不记',
  `domain` varchar(255) DEFAULT '' COMMENT '转发目标地址URL，k8s填写环境变量或cluster Ip',
  `path` varchar(255) DEFAULT '' COMMENT 'http服务的请求PATH',
  `param` varchar(255) DEFAULT '' COMMENT '接口需校验参数列表',
  `mode` varchar(50) DEFAULT '' COMMENT '请求方式',
  `action_type` varchar(32) DEFAULT '' COMMENT '接口对应的权限值',
  `permission` tinyint(1) DEFAULT '1' COMMENT '是否需要登录校验, 1需要验证登录,0 不需要验证登录',
  `argument` varchar(255) DEFAULT '' COMMENT 'RPC接口存在，调用目标服务的参数类型',
  `interfaze` varchar(255) DEFAULT '' COMMENT '目标地址的接口类名称',
  `method` varchar(100) DEFAULT '' COMMENT 'RPC存在，目标服务的接口方法名',
  `replenishRate` bigint(20) unsigned DEFAULT '0' COMMENT '限流： 令牌桶填充平均速率，单位：秒',
  `burstCapacity` bigint(20) unsigned DEFAULT '0' COMMENT '限流： 令牌桶上限。',
  `circuitBreakerEnabled` tinyint(1) DEFAULT '0' COMMENT ' 熔断开关,1开启，0关闭',
  `metricsRollingStatisticalWindowInMilliseconds` bigint(20) unsigned DEFAULT '0' COMMENT '熔断： 统计滚动的时间窗口',
  `circuitBreakerSleepWindowInMilliseconds` bigint(20) unsigned DEFAULT '0' COMMENT '熔断时间窗口，当熔断器打开5s后进入半开状态，允许部分流量进来重试',
  `circuitBreakerRequestVolumeThreshold` bigint(20) unsigned DEFAULT '0' COMMENT '熔断器在整个统计时间内是否开启的阀值',
  `circuitBreakerErrorThresholdPercentage` bigint(20) unsigned DEFAULT '0' COMMENT '熔断打开最大错误率',
  `executionIsolationSemaphoreMaxConcurrentRequests` bigint(20) unsigned DEFAULT '0' COMMENT '最大并发数',
  `fallbackIsolationSemaphoreMaxConcurrentRequests` bigint(20) unsigned DEFAULT '0' COMMENT '设置失败回滚调用最大并发数',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '发布标志:0-未发布1-发布',
  `create_by` varchar(50) DEFAULT '' COMMENT '创建人',
  `update_by` varchar(50) DEFAULT '' COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sdn_service_config`
--

LOCK TABLES `sdn_service_config` WRITE;
/*!40000 ALTER TABLE `sdn_service_config` DISABLE KEYS */;
INSERT INTO `sdn_service_config` VALUES (1,'UC801001','免密登录','httpServiceConverter',1,'用户中心','/data/api/auth/freeLogin','[sign,encodeString]','POST','0',0,'RPC 参数类型','RPC 接口类名','RPC 接口方法名',1,2,0,3,4,5,6,7,8,1,'','admin',NULL,'2018-07-12 11:55:30'),(3,'UC801002','免密登录','httpServiceConverter',0,'用户中心','/data/api/auth/freeLogin','[sign,encodeString]','POST','0',1,'','','',0,0,0,0,0,0,0,0,0,1,'admin','admin','2018-07-11 17:19:19','2018-07-12 11:54:08');
/*!40000 ALTER TABLE `sdn_service_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sdn_service_domian`
--

DROP TABLE IF EXISTS `sdn_service_domian`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sdn_service_domian` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain` varchar(255) NOT NULL DEFAULT '' COMMENT 'demain',
  `name` varchar(50) DEFAULT NULL COMMENT '应用名',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '软删除标志:0-已删除;1-正常',
  `create_by` varchar(50) DEFAULT '' COMMENT '创建人',
  `update_by` varchar(50) DEFAULT '' COMMENT '更新人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sdn_service_domian`
--

LOCK TABLES `sdn_service_domian` WRITE;
/*!40000 ALTER TABLE `sdn_service_domian` DISABLE KEYS */;
INSERT INTO `sdn_service_domian` VALUES (1,'http://{TERMINALMANAGER_MASTER_SERVICE_HOST}:{TERMINALMANAGER_MASTER_SERVICE_PORT}','下挂终端',1,'admin','admin','2018-07-03 15:17:51','2018-07-10 16:36:36'),(2,'http://${USERCENTER_MASTER_SERVICE_HOST}:${USERCENTER_MASTER_SERVICE_PORT}','用户中心',1,'admin',NULL,'2018-07-11 14:45:10',NULL);
/*!40000 ALTER TABLE `sdn_service_domian` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级部门ID，一级部门为0',
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='部门管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (6,0,'研发部',1,1),(7,6,'研發一部',1,1),(8,6,'研发二部',2,1),(9,0,'销售部',2,1),(10,9,'销售一部',1,1),(11,0,'产品部',3,1),(12,11,'产品一部',1,1),(13,0,'测试部',5,1),(14,13,'测试一部',1,1),(15,13,'测试二部',2,1);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict`
--

DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dict` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '标签名',
  `value` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '数据值',
  `type` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '类型',
  `description` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `sort` decimal(10,0) DEFAULT NULL COMMENT '排序（升序）',
  `parent_id` bigint(64) DEFAULT '0' COMMENT '父级编号',
  `create_by` int(64) DEFAULT NULL COMMENT '创建者',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) DEFAULT NULL COMMENT '更新者',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) COLLATE utf8_bin DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_dict_value` (`value`),
  KEY `sys_dict_label` (`name`),
  KEY `sys_dict_del_flag` (`del_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict`
--

LOCK TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict` VALUES (1,'正常','0','del_flag','删除标记',10,0,1,NULL,1,NULL,NULL,'0'),(3,'显示','1','show_hide','显示/隐藏',10,0,1,NULL,1,NULL,NULL,'0'),(4,'隐藏','0','show_hide','显示/隐藏',20,0,1,NULL,1,NULL,NULL,'0'),(5,'是','1','yes_no','是/否',10,0,1,NULL,1,NULL,NULL,'0'),(6,'否','0','yes_no','是/否',20,0,1,NULL,1,NULL,NULL,'0'),(7,'红色','red','color','颜色值',10,0,1,NULL,1,NULL,NULL,'0'),(8,'绿色','green','color','颜色值',20,0,1,NULL,1,NULL,NULL,'0'),(9,'蓝色','blue','color','颜色值',30,0,1,NULL,1,NULL,NULL,'0'),(10,'黄色','yellow','color','颜色值',40,0,1,NULL,1,NULL,NULL,'0'),(11,'橙色','orange','color','颜色值',50,0,1,NULL,1,NULL,NULL,'0'),(12,'默认主题','default','theme','主题方案',10,0,1,NULL,1,NULL,NULL,'0'),(13,'天蓝主题','cerulean','theme','主题方案',20,0,1,NULL,1,NULL,NULL,'0'),(14,'橙色主题','readable','theme','主题方案',30,0,1,NULL,1,NULL,NULL,'0'),(15,'红色主题','united','theme','主题方案',40,0,1,NULL,1,NULL,NULL,'0'),(16,'Flat主题','flat','theme','主题方案',60,0,1,NULL,1,NULL,NULL,'0'),(21,'公司','1','sys_office_type','机构类型',60,0,1,NULL,1,NULL,NULL,'0'),(22,'部门','2','sys_office_type','机构类型',70,0,1,NULL,1,NULL,NULL,'0'),(23,'小组','3','sys_office_type','机构类型',80,0,1,NULL,1,NULL,NULL,'0'),(24,'其它','4','sys_office_type','机构类型',90,0,1,NULL,1,NULL,NULL,'0'),(25,'综合部','1','sys_office_common','快捷通用部门',30,0,1,NULL,1,NULL,NULL,'0'),(26,'开发部','2','sys_office_common','快捷通用部门',40,0,1,NULL,1,NULL,NULL,'0'),(27,'人力部','3','sys_office_common','快捷通用部门',50,0,1,NULL,1,NULL,NULL,'0'),(28,'一级','1','sys_office_grade','机构等级',10,0,1,NULL,1,NULL,NULL,'0'),(29,'二级','2','sys_office_grade','机构等级',20,0,1,NULL,1,NULL,NULL,'0'),(30,'三级','3','sys_office_grade','机构等级',30,0,1,NULL,1,NULL,NULL,'0'),(31,'四级','4','sys_office_grade','机构等级',40,0,1,NULL,1,NULL,NULL,'0'),(32,'所有数据','1','sys_data_scope','数据范围',10,0,1,NULL,1,NULL,NULL,'0'),(33,'所在公司及以下数据','2','sys_data_scope','数据范围',20,0,1,NULL,1,NULL,NULL,'0'),(34,'所在公司数据','3','sys_data_scope','数据范围',30,0,1,NULL,1,NULL,NULL,'0'),(35,'所在部门及以下数据','4','sys_data_scope','数据范围',40,0,1,NULL,1,NULL,NULL,'0'),(36,'所在部门数据','5','sys_data_scope','数据范围',50,0,1,NULL,1,NULL,NULL,'0'),(37,'仅本人数据','8','sys_data_scope','数据范围',90,0,1,NULL,1,NULL,NULL,'0'),(39,'系统管理','1','sys_user_type','用户类型',10,0,1,NULL,1,NULL,NULL,'0'),(40,'部门经理','2','sys_user_type','用户类型',20,0,1,NULL,1,NULL,NULL,'0'),(41,'普通用户','3','sys_user_type','用户类型',30,0,1,NULL,1,NULL,NULL,'0'),(42,'基础主题','basic','cms_theme','站点主题',10,0,1,NULL,1,NULL,NULL,'0'),(43,'蓝色主题','blue','cms_theme','站点主题',20,0,1,NULL,1,NULL,NULL,'1'),(44,'红色主题','red','cms_theme','站点主题',30,0,1,NULL,1,NULL,NULL,'1'),(54,'删除','1','cms_del_flag','内容状态',20,0,1,NULL,1,NULL,NULL,'0'),(67,'接入日志','1','sys_log_type','日志类型',30,0,1,NULL,1,NULL,NULL,'0'),(68,'异常日志','2','sys_log_type','日志类型',40,0,1,NULL,1,NULL,NULL,'0'),(71,'分类1','1','act_category','流程分类',10,0,1,NULL,1,NULL,NULL,'0'),(72,'分类2','2','act_category','流程分类',20,0,1,NULL,1,NULL,NULL,'0'),(73,'增删改查','crud','gen_category','代码生成分类',10,0,1,NULL,1,NULL,NULL,'1'),(74,'增删改查（包含从表）','crud_many','gen_category','代码生成分类',20,0,1,NULL,1,NULL,NULL,'1'),(75,'树结构','tree','gen_category','代码生成分类',30,0,1,NULL,1,NULL,NULL,'1'),(76,'=','=','gen_query_type','查询方式',10,0,1,NULL,1,NULL,NULL,'1'),(77,'!=','!=','gen_query_type','查询方式',20,0,1,NULL,1,NULL,NULL,'1'),(78,'&gt;','&gt;','gen_query_type','查询方式',30,0,1,NULL,1,NULL,NULL,'1'),(79,'&lt;','&lt;','gen_query_type','查询方式',40,0,1,NULL,1,NULL,NULL,'1'),(80,'Between','between','gen_query_type','查询方式',50,0,1,NULL,1,NULL,NULL,'1'),(81,'Like','like','gen_query_type','查询方式',60,0,1,NULL,1,NULL,NULL,'1'),(82,'Left Like','left_like','gen_query_type','查询方式',70,0,1,NULL,1,NULL,NULL,'1'),(83,'Right Like','right_like','gen_query_type','查询方式',80,0,1,NULL,1,NULL,NULL,'1'),(84,'文本框','input','gen_show_type','字段生成方案',10,0,1,NULL,1,NULL,NULL,'1'),(85,'文本域','textarea','gen_show_type','字段生成方案',20,0,1,NULL,1,NULL,NULL,'1'),(86,'下拉框','select','gen_show_type','字段生成方案',30,0,1,NULL,1,NULL,NULL,'1'),(87,'复选框','checkbox','gen_show_type','字段生成方案',40,0,1,NULL,1,NULL,NULL,'1'),(88,'单选框','radiobox','gen_show_type','字段生成方案',50,0,1,NULL,1,NULL,NULL,'1'),(89,'日期选择','dateselect','gen_show_type','字段生成方案',60,0,1,NULL,1,NULL,NULL,'1'),(90,'人员选择','userselect','gen_show_type','字段生成方案',70,0,1,NULL,1,NULL,NULL,'1'),(91,'部门选择','officeselect','gen_show_type','字段生成方案',80,0,1,NULL,1,NULL,NULL,'1'),(93,'String','String','gen_java_type','Java类型',10,0,1,NULL,1,NULL,NULL,'1'),(94,'Long','Long','gen_java_type','Java类型',20,0,1,NULL,1,NULL,NULL,'1'),(95,'仅持久层','dao','gen_category','代码生成分类',40,0,1,NULL,1,NULL,NULL,'1'),(98,'Integer','Integer','gen_java_type','Java类型',30,0,1,NULL,1,NULL,NULL,'1'),(99,'Double','Double','gen_java_type','Java类型',40,0,1,NULL,1,NULL,NULL,'1'),(100,'Date','java.util.Date','gen_java_type','Java类型',50,0,1,NULL,1,NULL,NULL,'1'),(104,'Custom','Custom','gen_java_type','Java类型',90,0,1,NULL,1,NULL,NULL,'1'),(105,'dubbo','dubbo','service_type','服务类型',5,NULL,NULL,NULL,1,'2018-07-09 10:52:44','',NULL),(106,'hessian','hessian','service_type','服务类型',4,NULL,NULL,NULL,1,'2018-07-09 10:52:40','',NULL),(107,'HTTP','httpServiceConverter','service_type','服务类型',0,NULL,NULL,NULL,1,'2018-07-11 14:46:21','',NULL),(108,'mdp','mdp','service_type','服务类型',3,NULL,NULL,NULL,1,'2018-07-09 10:52:34','',NULL),(109,'soap','soap','service_type','服务类型',2,NULL,NULL,NULL,1,'2018-07-09 10:52:28','',NULL),(110,'thunder','thunder','service_type','服务类型',1,NULL,NULL,NULL,1,'2018-07-09 10:52:14','',NULL),(111,'POST','POST','request_type','请求方式',NULL,NULL,NULL,NULL,1,'2018-07-09 10:58:00','',NULL),(112,'GET','GET','request_type','请求方式',NULL,NULL,NULL,NULL,1,'2018-07-09 10:51:33','',NULL),(113,'PUT','PUT','request_type','请求方式',NULL,NULL,NULL,NULL,1,'2018-07-09 10:51:40','',NULL),(114,'DELETE','DELETE','request_type','请求方式',NULL,NULL,NULL,NULL,1,'2018-07-09 10:51:47','',NULL),(115,'管理员','0','action_type','app接口权限',0,NULL,1,'2018-07-09 11:01:06',1,'2018-07-09 13:52:52','',NULL),(116,'管理终端','1','action_type','app接口权限',1,NULL,1,'2018-07-09 11:01:48',1,'2018-07-09 13:50:48','',NULL),(117,'管理WiFi','2','action_type','app接口权限',2,NULL,1,'2018-07-09 11:02:33',1,'2018-07-09 13:50:39','',NULL),(118,'订购业务','3','action_type','app接口权限',3,NULL,1,'2018-07-09 11:03:02',1,'2018-07-09 13:50:28','',NULL),(119,'空','','action_type','app接口权限',0,NULL,1,'2018-07-09 13:51:22',1,'2018-07-09 13:51:53','',NULL),(120,'空','','request_type','请求方式',NULL,NULL,1,'2018-07-08 10:18:33',NULL,NULL,'',NULL);
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `time` int(11) DEFAULT NULL COMMENT '响应时间',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8 COMMENT='菜单管理';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,0,'基础管理','','',0,'fa fa-bars',0,'2017-08-09 22:49:47',NULL),(2,3,'系统菜单','sys/menu/','sys:menu:menu',1,'fa fa-th-list',3,'2017-08-09 22:55:15',NULL),(3,0,'系统管理',NULL,NULL,0,'fa fa-desktop',1,'2017-08-09 23:06:55','2017-08-14 14:13:43'),(6,3,'用户管理','sys/user/','sys:user:user',1,'fa fa-user',0,'2017-08-10 14:12:11',NULL),(7,3,'角色管理','sys/role','sys:role:role',1,'fa fa-paw',1,'2017-08-10 14:13:19',NULL),(12,6,'新增','','sys:user:add',2,'',0,'2017-08-14 10:51:35',NULL),(13,6,'编辑','','sys:user:edit',2,'',0,'2017-08-14 10:52:06',NULL),(14,6,'删除',NULL,'sys:user:remove',2,NULL,0,'2017-08-14 10:52:24',NULL),(15,7,'新增','','sys:role:add',2,'',0,'2017-08-14 10:56:37',NULL),(20,2,'新增','','sys:menu:add',2,'',0,'2017-08-14 10:59:32',NULL),(21,2,'编辑','','sys:menu:edit',2,'',0,'2017-08-14 10:59:56',NULL),(22,2,'删除','','sys:menu:remove',2,'',0,'2017-08-14 11:00:26',NULL),(24,6,'批量删除','','sys:user:batchRemove',2,'',0,'2017-08-14 17:27:18',NULL),(25,6,'停用',NULL,'sys:user:disable',2,NULL,0,'2017-08-14 17:27:43',NULL),(26,6,'重置密码','','sys:user:resetPwd',2,'',0,'2017-08-14 17:28:34',NULL),(27,91,'系统日志','common/log','common:log',1,'fa fa-warning',0,'2017-08-14 22:11:53',NULL),(28,27,'刷新',NULL,'sys:log:list',2,NULL,0,'2017-08-14 22:30:22',NULL),(29,27,'删除',NULL,'sys:log:remove',2,NULL,0,'2017-08-14 22:30:43',NULL),(30,27,'清空',NULL,'sys:log:clear',2,NULL,0,'2017-08-14 22:31:02',NULL),(55,7,'编辑','','sys:role:edit',2,'',NULL,NULL,NULL),(56,7,'删除','','sys:role:remove',2,NULL,NULL,NULL,NULL),(61,2,'批量删除','','sys:menu:batchRemove',2,NULL,NULL,NULL,NULL),(62,7,'批量删除','','sys:role:batchRemove',2,NULL,NULL,NULL,NULL),(73,3,'部门管理','/system/sysDept','system:sysDept:sysDept',1,'fa fa-users',2,NULL,NULL),(74,73,'增加','/system/sysDept/add','system:sysDept:add',2,NULL,1,NULL,NULL),(75,73,'刪除','system/sysDept/remove','system:sysDept:remove',2,NULL,2,NULL,NULL),(76,73,'编辑','/system/sysDept/edit','system:sysDept:edit',2,NULL,3,NULL,NULL),(78,1,'数据字典','/common/dict','common:dict:dict',1,'fa fa-book',1,NULL,NULL),(79,78,'增加','/common/dict/add','common:dict:add',2,NULL,2,NULL,NULL),(80,78,'编辑','/common/dict/edit','common:dict:edit',2,NULL,2,NULL,NULL),(81,78,'删除','/common/dict/remove','common:dict:remove',2,'',3,NULL,NULL),(83,78,'批量删除','/common/dict/batchRemove','common:dict:batchRemove',2,'',4,NULL,NULL),(91,0,'系统监控','','',0,'fa fa-video-camera',3,NULL,NULL),(93,0,'接口管理','','',0,'fa fa-print',2,NULL,NULL),(108,93,'接口配置','/common/service','common:service:service',1,'fa fa-at',1,NULL,NULL),(115,119,'增加','/common/domain/add','common:domain:add',2,'',NULL,NULL,NULL),(116,119,'编辑','/common/domain/edit','common:domain:edit',2,'',NULL,NULL,NULL),(117,119,'删除','common/domain/remove','common:domain:remove',2,'',NULL,NULL,NULL),(118,119,'批量删除','common/domain/batchRemove','common:domain:batchRemove',2,'',NULL,NULL,NULL),(119,93,'domain','/common/domain','common:domain:domain',1,'fa fa-anchor',NULL,NULL,NULL),(120,108,'增加','/common/service/add','common:service:add',2,'',NULL,NULL,NULL),(121,108,'编辑','/common/service/edit','common:service:edit',2,'',NULL,NULL,NULL),(122,108,'删除','/common/service/remove','common:service:remove',2,'',NULL,NULL,NULL),(123,108,'批量删除','/common/service/batchRemove','common:service:batchRemove',2,'',NULL,NULL,NULL),(124,108,'发布','/common/service/publish','common:service:publish',2,'fa fa-feed',NULL,NULL,NULL),(125,108,'查看','/common/service/find','common:service:find',2,'fa fa-search',NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `role_sign` varchar(100) DEFAULT NULL COMMENT '角色标识',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `user_id_create` bigint(255) DEFAULT NULL COMMENT '创建用户id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 COMMENT='角色';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (60,'超级用户角色',NULL,'拥有最高权限',NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3469 DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (367,44,1),(368,44,32),(369,44,33),(370,44,34),(371,44,35),(372,44,28),(373,44,29),(374,44,30),(375,44,38),(376,44,4),(377,44,27),(378,45,38),(379,46,3),(380,46,20),(381,46,21),(382,46,22),(383,46,23),(384,46,11),(385,46,12),(386,46,13),(387,46,14),(388,46,24),(389,46,25),(390,46,26),(391,46,15),(392,46,2),(393,46,6),(394,46,7),(598,50,38),(632,38,42),(737,51,38),(738,51,39),(739,51,40),(740,51,41),(741,51,4),(742,51,32),(743,51,33),(744,51,34),(745,51,35),(746,51,27),(747,51,28),(748,51,29),(749,51,30),(750,51,1),(1064,54,53),(1095,55,2),(1096,55,6),(1097,55,7),(1098,55,3),(1099,55,50),(1100,55,49),(1101,55,1),(1856,53,28),(1857,53,29),(1858,53,30),(1859,53,27),(1860,53,57),(1861,53,71),(1862,53,48),(1863,53,72),(1864,53,1),(1865,53,7),(1866,53,55),(1867,53,56),(1868,53,62),(1869,53,15),(1870,53,2),(1871,53,61),(1872,53,20),(1873,53,21),(1874,53,22),(2084,56,68),(2085,56,60),(2086,56,59),(2087,56,58),(2088,56,51),(2089,56,50),(2090,56,49),(2243,48,72),(2247,63,-1),(2248,63,84),(2249,63,85),(2250,63,88),(2251,63,87),(2252,64,84),(2253,64,89),(2254,64,88),(2255,64,87),(2256,64,86),(2257,64,85),(2258,65,89),(2259,65,88),(2260,65,86),(2262,67,48),(2263,68,88),(2264,68,87),(2265,69,89),(2266,69,88),(2267,69,86),(2268,69,87),(2269,69,85),(2270,69,84),(2271,70,85),(2272,70,89),(2273,70,88),(2274,70,87),(2275,70,86),(2276,70,84),(2277,71,87),(2278,72,59),(2279,73,48),(2280,74,88),(2281,74,87),(2282,75,88),(2283,75,87),(2284,76,85),(2285,76,89),(2286,76,88),(2287,76,87),(2288,76,86),(2289,76,84),(2292,78,88),(2293,78,87),(2294,78,NULL),(2295,78,NULL),(2296,78,NULL),(2308,80,87),(2309,80,86),(2310,80,-1),(2311,80,84),(2312,80,85),(2328,79,72),(2329,79,48),(2330,79,77),(2331,79,84),(2332,79,89),(2333,79,88),(2334,79,87),(2335,79,86),(2336,79,85),(2337,79,-1),(2338,77,89),(2339,77,88),(2340,77,87),(2341,77,86),(2342,77,85),(2343,77,84),(2344,77,72),(2345,77,-1),(2346,77,77),(2974,57,93),(2975,57,99),(2976,57,95),(2977,57,101),(2978,57,96),(2979,57,94),(2980,57,-1),(2981,58,93),(2982,58,99),(2983,58,95),(2984,58,101),(2985,58,96),(2986,58,94),(2987,58,-1),(2988,59,97),(2989,59,98),(2990,59,91),(2991,59,30),(2992,59,29),(2993,59,28),(2994,59,92),(2995,59,57),(2996,59,27),(2997,59,-1),(3422,60,118),(3423,60,117),(3424,60,116),(3425,60,115),(3426,60,125),(3427,60,124),(3428,60,123),(3429,60,122),(3430,60,121),(3431,60,120),(3432,60,30),(3433,60,29),(3434,60,28),(3435,60,76),(3436,60,75),(3437,60,74),(3438,60,62),(3439,60,56),(3440,60,55),(3441,60,15),(3442,60,26),(3443,60,25),(3444,60,24),(3445,60,14),(3446,60,13),(3447,60,12),(3448,60,61),(3449,60,22),(3450,60,21),(3451,60,20),(3452,60,83),(3453,60,81),(3454,60,80),(3455,60,79),(3456,60,119),(3457,60,108),(3458,60,93),(3459,60,27),(3460,60,91),(3461,60,73),(3462,60,7),(3463,60,6),(3464,60,2),(3465,60,3),(3466,60,78),(3467,60,1),(3468,60,-1);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `dept_id` bigint(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(255) DEFAULT NULL COMMENT '状态 0:禁用，1:正常',
  `user_id_create` bigint(255) DEFAULT NULL COMMENT '创建用户id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `live_address` varchar(500) DEFAULT NULL COMMENT '现居住地',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','超级管理员','27bd386e70f280e24c2f4f2a549b82cf',6,'admin@example.com','17699999999',1,1,'2017-08-15 21:40:39','2017-08-15 21:41:00','ccc'),(2,'test','临时用户','6cf3bb3deba2aadbd41ec9a22511084e',6,'test@bootdo.com',NULL,1,1,'2017-08-14 13:43:05','2017-08-14 21:15:36',NULL),(123,'zxy','张学友','35174ba93f5fe7267f1fb3c1bf903781',6,'zxy@bootdo',NULL,0,NULL,NULL,NULL,NULL),(124,'wyf','吴亦凡','e179e6f687bbd57b9d7efc4746c8090a',6,'wyf@bootdo.com',NULL,1,NULL,NULL,NULL,NULL),(130,'lh','鹿晗','7924710cd673f68967cde70e188bb097',9,'lh@bootdo.com',NULL,1,NULL,NULL,NULL,NULL),(131,'lhc','令狐冲','d515538e17ecb570ba40344b5618f5d4',6,'lhc@bootdo.com',NULL,0,NULL,NULL,NULL,NULL),(132,'lyf','刘亦菲','7fdb1d9008f45950c1620ba0864e5fbd',13,'lyf@bootdo.com',NULL,1,NULL,NULL,NULL,NULL),(134,'lyh','李彦宏','dc26092b3244d9d432863f2738180e19',8,'lyh@bootdo.com',NULL,1,NULL,NULL,NULL,NULL),(135,'wjl','王健林','3967697dfced162cf6a34080259b83aa',6,'wjl@bootod.com',NULL,1,NULL,NULL,NULL,NULL),(136,'gdg','郭德纲','3bb1bda86bc02bf6478cd91e42135d2f',9,'gdg@bootdo.com',NULL,1,NULL,NULL,NULL,NULL),(137,'test2','测试用户2','649169898e69272c0e5bc899baf1e904',8,'test2@bootdo.com',NULL,1,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (73,30,48),(74,30,49),(75,30,50),(76,31,48),(77,31,49),(78,31,52),(79,32,48),(80,32,49),(81,32,50),(82,32,51),(83,32,52),(84,33,38),(85,33,49),(86,33,52),(87,34,50),(88,34,51),(89,34,52),(113,131,48),(121,134,48),(124,NULL,48),(125,132,52),(126,132,49),(127,123,48),(134,1,60);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'hippo-integration'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-10  2:57:11
