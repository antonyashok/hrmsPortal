
CREATE SCHEMA `invoice` ;

--
-- Table structure for table `address`
--
use invoice;


DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `addr_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  `actv_flg` varchar(255) DEFAULT NULL,
  `addr_city_1` varchar(255) DEFAULT NULL,
  `addr_city_2` varchar(255) DEFAULT NULL,
  `addr_city_3` varchar(255) DEFAULT NULL,
  `addr_strt_1` varchar(255) DEFAULT NULL,
  `addr_strt_2` varchar(255) DEFAULT NULL,
  `addr_strt_3` varchar(255) DEFAULT NULL,
  `cntry_id` bigint(20) DEFAULT NULL,
  `pstl_cd` varchar(255) DEFAULT NULL,
  `st_prv_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`addr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alert_type`
--

DROP TABLE IF EXISTS `alert_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `alert_type` (
  `alert_typ_id` bigint(15) NOT NULL,
  `alert_typ_nm` varchar(45) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date NOT NULL,
  `last_updt_dt` date DEFAULT NULL,
  PRIMARY KEY (`alert_typ_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alert_type`
--

LOCK TABLES `alert_type` WRITE;
/*!40000 ALTER TABLE `alert_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `alert_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_cycle`
--

DROP TABLE IF EXISTS `bill_cycle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill_cycle` (
  `bill_cycle_id` bigint(15) NOT NULL,
  `bill_cycle_nm` varchar(45) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date NOT NULL,
  `last_updt_dt` date DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  PRIMARY KEY (`bill_cycle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_cycle`
--

LOCK TABLES `bill_cycle` WRITE;
/*!40000 ALTER TABLE `bill_cycle` DISABLE KEYS */;
INSERT INTO `bill_cycle` VALUES (1,'Daily','Y','2013-07-17','2013-07-17',0,'2013-07-17 18:33:55',0,'2013-07-17 18:33:55'),(2,'Daily','N','2013-07-17','2013-07-17',1,'2013-07-17 18:33:55',1,'2013-07-17 18:33:55');
/*!40000 ALTER TABLE `bill_cycle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_cycle_day`
--

DROP TABLE IF EXISTS `bill_cycle_day`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill_cycle_day` (
  `bill_cycle_day_id` bigint(15) NOT NULL,
  `bill_cycle_day_nm` varchar(3) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date NOT NULL,
  `last_updt_dt` date DEFAULT NULL,
  PRIMARY KEY (`bill_cycle_day_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_cycle_day`
--

LOCK TABLES `bill_cycle_day` WRITE;
/*!40000 ALTER TABLE `bill_cycle_day` DISABLE KEYS */;
INSERT INTO `bill_cycle_day` VALUES (1,'Day','Y','2013-07-17','2013-07-17'),(2,'Day','N','2013-07-17','2013-07-17');
/*!40000 ALTER TABLE `bill_cycle_day` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill_cycle_start_end`
--

DROP TABLE IF EXISTS `bill_cycle_start_end`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill_cycle_start_end` (
  `bill_cycle_start_end_id` bigint(15) NOT NULL,
  `bill_cycle_start_end_nm` varchar(45) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date NOT NULL,
  `last_updt_dt` date DEFAULT NULL,
  PRIMARY KEY (`bill_cycle_start_end_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill_cycle_start_end`
--

LOCK TABLES `bill_cycle_start_end` WRITE;
/*!40000 ALTER TABLE `bill_cycle_start_end` DISABLE KEYS */;
INSERT INTO `bill_cycle_start_end` VALUES (1,'Test',NULL,'2013-07-17','2013-07-17'),(2,'Test',NULL,'2013-07-17','2013-07-17');
/*!40000 ALTER TABLE `bill_cycle_start_end` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `billing_term`
--

DROP TABLE IF EXISTS `billing_term`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `billing_term` (
  `bill_term_id` bigint(15) NOT NULL,
  `bill_term_nm` varchar(45) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date NOT NULL,
  `last_updt_dt` date DEFAULT NULL,
  PRIMARY KEY (`bill_term_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `billing_term`
--

LOCK TABLES `billing_term` WRITE;
/*!40000 ALTER TABLE `billing_term` DISABLE KEYS */;
/*!40000 ALTER TABLE `billing_term` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comm_info`
--

DROP TABLE IF EXISTS `comm_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comm_info` (
  `comm_info_id` bigint(20) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  `actv_flg` varchar(255) DEFAULT NULL,
  `cntct_id` bigint(20) DEFAULT NULL,
  `cntct_info` varchar(255) DEFAULT NULL,
  `cntct_typ` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`comm_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comm_info`
--

LOCK TABLES `comm_info` WRITE;
/*!40000 ALTER TABLE `comm_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `comm_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contractor_billing_details_view`
--

DROP TABLE IF EXISTS `contractor_billing_details_view`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contractor_billing_details_view` (
  `uniqueId` double NOT NULL,
  `empl_id` bigint(20) DEFAULT NULL,
  `employee_name` varchar(255) DEFAULT NULL,
  `hoursWorked` decimal(19,2) DEFAULT NULL,
  `workingdate` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uniqueId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contractor_billing_details_view`
--

LOCK TABLES `contractor_billing_details_view` WRITE;
/*!40000 ALTER TABLE `contractor_billing_details_view` DISABLE KEYS */;
/*!40000 ALTER TABLE `contractor_billing_details_view` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contractor_tab_details`
--

DROP TABLE IF EXISTS `contractor_tab_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contractor_tab_details` (
  `contractor_tab_id` varchar(255) NOT NULL,
  `employeeId` bigint(20) DEFAULT NULL,
  `employeeName` varchar(255) DEFAULT NULL,
  `fileNumber` varchar(255) DEFAULT NULL,
  `poNumber` varchar(255) DEFAULT NULL,
  `purchaseOrderId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`contractor_tab_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contractor_tab_details`
--

LOCK TABLES `contractor_tab_details` WRITE;
/*!40000 ALTER TABLE `contractor_tab_details` DISABLE KEYS */;
INSERT INTO `contractor_tab_details` VALUES ('64c3f9d3-d6e4-432a-9739-53f09606b245',3,'BertBean','663','exp','5454033f-2cbd-4f55-ab32-39094c4a5798'),('73159de0-bc79-4306-9c03-9f8eeb34fe43',1,'AdminAdministrator','999999','exp','5454033f-2cbd-4f55-ab32-39094c4a5798'),('91ebf9cc-8287-44f8-8f92-b998e96cb144',2,'JamesBauer','1847','exp','5454033f-2cbd-4f55-ab32-39094c4a5798'),('e2cf2a16-424d-4129-ab28-28a026983c2d',3,'BertBean','663','exp','5454033f-2cbd-4f55-ab32-39094c4a5798');
/*!40000 ALTER TABLE `contractor_tab_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `contractor_tab_view`
--

DROP TABLE IF EXISTS `contractor_tab_view`;
/*!50001 DROP VIEW IF EXISTS `contractor_tab_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `contractor_tab_view` AS SELECT 
 1 AS `uniqueId`,
 1 AS `empl_id`,
 1 AS `employee_name`,
 1 AS `file_no`,
 1 AS `inv_setup_id`,
 1 AS `po_id`,
 1 AS `po_Number`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `contractor_view`
--

DROP TABLE IF EXISTS `contractor_view`;
/*!50001 DROP VIEW IF EXISTS `contractor_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `contractor_view` AS SELECT 
 1 AS `inv_setup_id`,
 1 AS `empl_id`,
 1 AS `po_id`,
 1 AS `po_Number`,
 1 AS `first_nm`,
 1 AS `last_nm`,
 1 AS `file_no`*/;
SET character_set_client = @saved_cs_client;

--


DROP TABLE IF EXISTS `delivery_mode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `delivery_mode` (
  `delivery_mode_id` bigint(15) NOT NULL,
  `delivery_mode_nm` varchar(45) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date NOT NULL,
  `last_updt_dt` date DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  PRIMARY KEY (`delivery_mode_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--


--
-- Dumping data for table `delivery_mode`
--

LOCK TABLES `delivery_mode` WRITE;
/*!40000 ALTER TABLE `delivery_mode` DISABLE KEYS */;
INSERT INTO `delivery_mode` VALUES (1,'Email',NULL,'2013-07-17','2013-07-17',0,CURRENT_TIMESTAMP,0,CURRENT_TIMESTAMP),(2,'PDF',NULL,'2013-07-17','2013-07-17',0,CURRENT_TIMESTAMP,0,CURRENT_TIMESTAMP);
/*!40000 ALTER TABLE `delivery_mode` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Temporary view structure for view `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!50001 DROP VIEW IF EXISTS `employee`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `employee` AS SELECT 
 1 AS `empl_id`,
 1 AS `emp_rl_id`,
 1 AS `first_nm`,
 1 AS `last_nm`,
 1 AS `middle_initials`,
 1 AS `tin`,
 1 AS `portal_userid`,
 1 AS `file_no`,
 1 AS `addr_id`,
 1 AS `prmry_email_comm_info_id`,
 1 AS `scndry_email_comm_info_id`,
 1 AS `prmry_phn_comm_info_id`,
 1 AS `scndry_phn_comm_info_id`,
 1 AS `mgr_empl_id`,
 1 AS `ofc_id`,
 1 AS `profile_id`,
 1 AS `esf_id`,
 1 AS `empl_typ`,
 1 AS `empl_strt_dt`,
 1 AS `hourly_flg`,
 1 AS `actv_flg`,
 1 AS `create_dt`,
 1 AS `last_updt_dt`,
 1 AS `usrtype`,
 1 AS `created_by`,
 1 AS `created_on`,
 1 AS `updated_by`,
 1 AS `updated_on`,
 1 AS `end_dt`,
 1 AS `start_dt`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `employee_address`
--

DROP TABLE IF EXISTS `employee_address`;
/*!50001 DROP VIEW IF EXISTS `employee_address`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `employee_address` AS SELECT 
 1 AS `addr_id`,
 1 AS `st_prv_id`,
 1 AS `cntry_id`,
 1 AS `addr_strt_1`,
 1 AS `addr_strt_2`,
 1 AS `addr_strt_3`,
 1 AS `addr_city_1`,
 1 AS `addr_city_2`,
 1 AS `addr_city_3`,
 1 AS `pstl_cd`,
 1 AS `actv_flg`,
 1 AS `create_dt`,
 1 AS `last_updt_dt`,
 1 AS `county`,
 1 AS `addr_unit`,
 1 AS `addr_typ`,
 1 AS `created_by`,
 1 AS `created_on`,
 1 AS `updated_by`,
 1 AS `updated_on`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `employee_comm_info`
--

DROP TABLE IF EXISTS `employee_comm_info`;
/*!50001 DROP VIEW IF EXISTS `employee_comm_info`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `employee_comm_info` AS SELECT 
 1 AS `comm_info_id`,
 1 AS `cntct_id`,
 1 AS `cntct_typ`,
 1 AS `cntct_info`,
 1 AS `actv_flg`,
 1 AS `create_dt`,
 1 AS `last_updt_dt`,
 1 AS `created_by`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `employee_role`
--

DROP TABLE IF EXISTS `employee_role`;
/*!50001 DROP VIEW IF EXISTS `employee_role`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `employee_role` AS SELECT 
 1 AS `emp_rl_id`,
 1 AS `emp_rl_nm`,
 1 AS `emp_rl_desc`,
 1 AS `actv_flg`,
 1 AS `create_dt`,
 1 AS `last_updt_dt`,
 1 AS `created_by`,
 1 AS `created_on`,
 1 AS `updated_by`,
 1 AS `updated_on`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `employee_task`
--

DROP TABLE IF EXISTS `employee_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `employee_task` (
  `task_id` char(36) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  `bill_type` varchar(255) DEFAULT NULL,
  `contract_id` bigint(20) DEFAULT NULL,
  `empl_id` bigint(20) DEFAULT NULL,
  `engm_id` bigint(20) DEFAULT NULL,
  `task_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_task`
--

LOCK TABLES `employee_task` WRITE;
/*!40000 ALTER TABLE `employee_task` DISABLE KEYS */;
INSERT INTO `employee_task` VALUES ('132b84e8-d7be-4f90-b0ec-af5ad5fa464c',0,'2016-10-24 15:16:36',0,'2016-10-24 15:16:36','Unit Based',22334675666774333,22334455666774333,NULL,'task09'),('16aed1eb-7689-4251-a31b-50feef853a86',0,'2017-03-15 16:46:56',0,'2017-03-15 16:46:56','',82,190,NULL,'task1'),('1966d8df-607e-47f9-b2a4-de3aed6b798a',0,'2016-10-24 15:15:37',0,'2016-10-24 15:15:37','Unit Based',22334675666774333,22334455666774333,NULL,'task04'),('2177251e-383e-4ee8-ac00-f161db6d172d',0,'2016-10-20 17:56:58',0,'2016-10-20 17:56:58','Unit Based',22334675666774333,22334455666774333,NULL,'df'),('72ef624a-6b66-419b-a442-a11b3314d6a0',0,'2017-03-13 16:45:45',0,'2017-03-13 16:45:45','Unit Based',22334675666774333,22334455666774333,NULL,'invoice-development'),('7b2b07f0-41d0-45eb-a046-63d8dfcf22b6',0,'2016-10-25 12:26:22',0,'2016-10-25 12:26:22','Unit Based',22334675666774333,22334455666774333,NULL,'task11'),('7dd056f5-09b0-493e-8662-74324f582366',0,'2016-10-20 16:22:02',0,'2016-10-20 16:22:02','Unit Based',22334675666774333,22334455666774333,NULL,'meenu'),('97b9d383-654a-44b1-a7d5-200e49680db4',0,'2017-03-13 16:39:40',0,'2017-03-13 16:39:40','Unit Based',22334675666774333,22334455666774333,NULL,'task01'),('9a8560dc-0c42-46ee-b1aa-ec8ded04337c',0,'2016-11-01 15:54:59',0,'2016-11-01 15:54:59','Unit Based',22334675666774333,22334455666774333,NULL,'tas00k11'),('a8811f5b-d648-43ff-803a-b4dce209db89',0,'2016-10-24 15:16:18',0,'2016-10-24 15:16:18','Unit Based',22334675666774333,22334455666774333,NULL,'task07'),('b2f0cba0-c6d3-4112-9a94-13d60098a8cd',0,'2016-10-24 15:16:01',0,'2016-10-24 15:16:01','Unit Based',22334675666774333,22334455666774333,NULL,'task06'),('ba4ec0ec-73cf-4da5-87ae-d02d92171f83',0,'2017-03-15 18:33:04',0,'2017-03-15 18:33:04','',1,NULL,NULL,'FarBeyond'),('d49b2ec6-ee72-41d8-a5ab-db1bc3d71b50',0,'2016-10-20 17:50:38',0,'2016-10-20 17:50:38','Unit Based',22334675666774333,22334455666774333,NULL,'jh'),('da40d3f3-1f64-4131-a85f-93686f84470b',0,'2016-10-20 17:57:05',0,'2016-10-20 17:57:05','Unit Based',22334675666774333,22334455666774333,NULL,'dfgf'),('ecc080b5-e5fc-42e9-a9de-bcffccde52c8',0,'2016-10-24 15:16:58',0,'2016-10-24 15:16:58','Unit Based',22334675666774333,22334455666774333,NULL,'task10'),('ee2dbbe7-c62b-47b5-9523-ff626da1d905',0,'2016-10-24 15:16:27',0,'2016-10-24 15:16:27','Unit Based',22334675666774333,22334455666774333,NULL,'task08');
/*!40000 ALTER TABLE `employee_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (24),(24),(24);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice` (
  `inv_id` bigint(15) NOT NULL,
  `inv_number` varchar(45) NOT NULL,
  `inv_dt` date NOT NULL,
  `bill_to_org_id` bigint(15) DEFAULT NULL,
  `end_org_id` bigint(15) DEFAULT NULL,
  `engm_id` bigint(15) DEFAULT NULL,
  `ofc_id` bigint(15) DEFAULT NULL,
  `lob_id` bigint(15) DEFAULT NULL,
  `inv_template_id` bigint(15) DEFAULT NULL,
  `inv_amt` decimal(10,2) NOT NULL,
  `po_number` varchar(45) DEFAULT NULL,
  `delivery_mode_id` bigint(15) DEFAULT NULL,
  `bill_to_address` varchar(200) DEFAULT NULL,
  `attention` varchar(45) DEFAULT NULL,
  `receipient_email` varchar(200) DEFAULT NULL,
  `status` varchar(45) NOT NULL,
  `rejection_reason` varchar(45) DEFAULT NULL,
  `created_by` bigint(15) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`inv_id`),
  KEY `inv_inv_template_id_idx` (`inv_template_id`),
  KEY `inv_bill_to_org_id_idx` (`bill_to_org_id`),
  KEY `inv_end_org_id_idx` (`end_org_id`),
  KEY `inv_engm_id_idx` (`engm_id`),
  KEY `inv_ofc_id_idx` (`ofc_id`),
  KEY `inv_lob_id_idx` (`lob_id`),
  KEY `inv_delivery_mode_id_idx` (`delivery_mode_id`)
 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
INSERT INTO `invoice` VALUES (1,'Test','2013-07-17',255,256,1,1,1,1,100.00,'PO_001',1,'Test','Test','antonyashok110@yahoo.in','Pending','Test',1,'2016-10-15 05:42:24',1,'2013-07-17 13:03:55'),(2,'Test','2013-07-17',255,256,1,1,1,1,100.00,'1',1,'Test','Test','antonyashok110@yahoo.in','Pending','Test',1,'2013-07-17 13:03:55',1,'2013-07-17 13:03:55'),(3,'Test','2013-07-17',255,256,1,1,1,1,100.00,'1',1,'Test','Test','antonyashok110@yahoo.in','Pending','Test',1,'2013-07-17 13:03:55',1,'2013-07-17 13:03:55');
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_assign_bill`
--

DROP TABLE IF EXISTS `invoice_assign_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_assign_bill` (
  `inv_setup_id` bigint(15) NOT NULL,
  `mgr_name` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `mgrid` bigint(15) NOT NULL,
  `assigned_flg` varchar(45) DEFAULT NULL,
  `assignedFlag` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`mgrid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_assign_bill`
--

LOCK TABLES `invoice_assign_bill` WRITE;
/*!40000 ALTER TABLE `invoice_assign_bill` DISABLE KEYS */;
INSERT INTO `invoice_assign_bill` VALUES (123456,'sfdsfdg','xcxvxcvbc',1,NULL,NULL),(11111,'prebill','xcxvxcvbc',7,NULL,NULL),(22222,'Regular','xcxvxcvbc',8,NULL,NULL),(2,'dgfdgd','dfhfghj',9,'Y',NULL),(22222,'Regular','xcxvxcvbc',10,NULL,NULL),(22222,'Regular','xcxvxcvbc',11,NULL,NULL),(22222,'Regular','xcxvxcvbc',12,NULL,'N');
/*!40000 ALTER TABLE `invoice_assign_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_attribute`
--

DROP TABLE IF EXISTS `invoice_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_attribute` (
  `inv_attr_id` bigint(15) NOT NULL,
  `inv_attr_nm` varchar(32) NOT NULL,
  `field_type` char(36) NOT NULL,
  `actv_flg` enum('Y','N') NOT NULL,
  `parent_id` bigint(15) NOT NULL,
  `created_by` bigint(15) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` bigint(15) NOT NULL,
  `updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`inv_attr_id`),
  UNIQUE KEY `inv_attribute_nm_UNIQUE` (`inv_attr_nm`),
  UNIQUE KEY `id_UNIQUE` (`inv_attr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_attribute`
--

LOCK TABLES `invoice_attribute` WRITE;
/*!40000 ALTER TABLE `invoice_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_attribute_value`
--

DROP TABLE IF EXISTS `invoice_attribute_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_attribute_value` (
  `inv_attr_val_id` bigint(15) NOT NULL,
  `inv_id` bigint(15) DEFAULT NULL,
  `inv_attr_id` bigint(15) DEFAULT NULL,
  `inv_attr_val` varchar(45) NOT NULL,
  `created_by` bigint(15) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`inv_attr_val_id`),
  KEY `inv_attr_val_inv_id_fk_idx` (`inv_id`),
  KEY `inv_attr_val_inv_attr_id_fk_idx` (`inv_attr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_attribute_value`
--

LOCK TABLES `invoice_attribute_value` WRITE;
/*!40000 ALTER TABLE `invoice_attribute_value` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_attribute_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_contractor`
--

DROP TABLE IF EXISTS `invoice_contractor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_contractor` (
  `inv_id` bigint(15) NOT NULL,
  `empl_id` bigint(15) NOT NULL,
  `file_no` varchar(45) DEFAULT NULL,
  `remove_flg` enum('Y','N') DEFAULT 'N',
  `created_ by` bigint(15) DEFAULT NULL,
  `created_on` timestamp NULL DEFAULT NULL,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`inv_id`,`empl_id`),
  KEY `ic_empl_id_idx` (`empl_id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_contractor`
--

LOCK TABLES `invoice_contractor` WRITE;
/*!40000 ALTER TABLE `invoice_contractor` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_contractor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_expense`
--

DROP TABLE IF EXISTS `invoice_expense`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_expense` (
  `inv_exp_id` bigint(15) NOT NULL,
  `inv_id` bigint(15) DEFAULT NULL,
  `empl_id` bigint(15) DEFAULT NULL,
  `exp_report_id` char(36) DEFAULT NULL,
  `rpt_name` varchar(45) NOT NULL,
  `billed_exp` decimal(10,2) NOT NULL,
  `submitted_on` date NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  `acts_flg` enum('Y','N') DEFAULT NULL,
  `created_by` bigint(15) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`inv_exp_id`),
  KEY `inv_exp_inv_id_fk_idx` (`inv_id`),
  KEY `inv_exp_empl_id_fk_idx` (`empl_id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_expense`
--

LOCK TABLES `invoice_expense` WRITE;
/*!40000 ALTER TABLE `invoice_expense` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_expense` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_organization_template`
--

DROP TABLE IF EXISTS `invoice_organization_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_organization_template` (
  `bill_to_org_id` bigint(15) NOT NULL,
  `inv_template_id` bigint(15) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date NOT NULL,
  PRIMARY KEY (`bill_to_org_id`,`inv_template_id`),
  KEY `inv_org_template_inv_template_id_fk_idx` (`inv_template_id`)
 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_organization_template`
--

LOCK TABLES `invoice_organization_template` WRITE;
/*!40000 ALTER TABLE `invoice_organization_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_organization_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_rate`
--

DROP TABLE IF EXISTS `invoice_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_rate` (
  `id` varchar(36) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  `bill_Rate_DT` decimal(19,2) DEFAULT NULL,
  `bill_Rate_OT` decimal(19,2) DEFAULT NULL,
  `bill_Rate_ST` decimal(19,2) DEFAULT NULL,
  `effective_Date` date DEFAULT NULL,
  `end_Date` date DEFAULT NULL,
  `end_Rate_DT` decimal(19,2) DEFAULT NULL,
  `end_Rate_OT` decimal(19,2) DEFAULT NULL,
  `end_Rate_ST` decimal(19,2) DEFAULT NULL,
  `invoice_id` varchar(255) DEFAULT NULL,
  `task_Name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_rate`
--

LOCK TABLES `invoice_rate` WRITE;
/*!40000 ALTER TABLE `invoice_rate` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_rate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_return`
--

DROP TABLE IF EXISTS `invoice_return`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_return` (
  `inv_rt_id` char(36) NOT NULL,
  `inv_id` bigint(15) DEFAULT NULL,
  `inv_amt` decimal(10,2) DEFAULT NULL,
  `ret_comments` varchar(200) DEFAULT NULL,
  `filename` varchar(200) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `status_comments` varchar(45) DEFAULT NULL,
  `created_by` bigint(15) DEFAULT NULL,
  `created_on` timestamp NULL DEFAULT NULL,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`inv_rt_id`),
  KEY `ir_inv_id_idx` (`inv_id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_return`
--

LOCK TABLES `invoice_return` WRITE;
/*!40000 ALTER TABLE `invoice_return` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_return` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_setup`
--

DROP TABLE IF EXISTS `invoice_setup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_setup` (
  `inv_setup_id` bigint(15) NOT NULL,
  `inv_setup_nm` varchar(45) NOT NULL,
  `delivery_mode_id` bigint(15) DEFAULT NULL,
  `inv_desc` varchar(128) NOT NULL,
  `receipient_email` varchar(128) DEFAULT NULL,
  `inv_typ_id` bigint(15) DEFAULT NULL,
  `inv_template_id` bigint(15) DEFAULT NULL,
  `mail_subject` varchar(45) NOT NULL,
  `include_tm_same_inv` enum('Y','N') DEFAULT NULL,
  `allow_exp_same_inv` enum('Y','N') DEFAULT NULL,
  `separate_inv_for_ot` enum('Y','N') DEFAULT NULL,
  `notes` text NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `created_by` bigint(15) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  `inv_setup_bill_cycle_id` bigint(20) DEFAULT NULL,
  `accruing_flg` varchar(255) DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `amount_remaining` decimal(19,2) DEFAULT NULL,
  `amount_used` decimal(19,2) DEFAULT NULL,
  `bill_cycle_day_id` bigint(20) DEFAULT NULL,
  `bill_cycle_id` bigint(20) DEFAULT NULL,
  `bill_cycle_strt_end_id` bigint(20) DEFAULT NULL,
  `cons_inv_flg` varchar(255) DEFAULT NULL,
  `end_dt` datetime DEFAULT NULL,
  `initial_prebill_amt` decimal(19,2) DEFAULT NULL,
  `number_of_hrs` decimal(19,2) DEFAULT NULL,
  `prebill_without_contractor_flg` varchar(255) DEFAULT NULL,
  `sales_tax_flg` varchar(255) DEFAULT NULL,
  `strt_dt` datetime DEFAULT NULL,
  `threshold_amt` decimal(19,2) DEFAULT NULL,
  `timeframe_exceed_flg` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`inv_setup_id`),
  KEY `is_delivery_mode_id_idx` (`delivery_mode_id`),
  KEY `is_inv_typ_id_idx` (`inv_typ_id`),
  KEY `is_inv_template_id_idx` (`inv_template_id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_setup`
--

LOCK TABLES `invoice_setup` WRITE;
/*!40000 ALTER TABLE `invoice_setup` DISABLE KEYS */;
INSERT INTO `invoice_setup` VALUES (1,'Amigos',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','active','Y',1,'2016-11-17 07:11:56',1,'2013-07-17 13:03:55',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,'NetMagnus',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','Active','Y',1,'2016-11-17 07:11:56',1,'2013-07-17 13:03:55',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,'SMI',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','Active','Y',1,'2016-11-17 07:11:56',1,'2016-10-17 12:13:58',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'USSG',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','Active','Y',1,'2016-11-17 07:11:56',1,'2016-10-17 12:13:58',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,'Apple',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','Active','Y',1,'2016-11-17 07:11:56',1,'2016-10-17 12:13:58',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(100,'ExistingSetup_001',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','Active','Y',1,'2016-11-17 07:11:56',1,'2013-07-17 13:03:55',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(101,'ExistingSetup_002',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','active','Y',1,'2016-11-17 07:11:56',1,'2013-07-17 13:03:55',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(102,'ExistingSetup_003',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','active','Y',1,'2016-11-17 07:11:56',1,'2013-07-17 13:03:55',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(103,'ExistingSetup_004',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','active','Y',1,'2016-11-17 07:11:56',1,'2013-07-17 13:03:55',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(104,'ExistingSetup_005',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','active','Y',1,'2016-11-17 07:11:56',1,'2013-07-17 13:03:55',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(105,'ExistingSetup_005',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','active','Y',1,'2016-11-17 07:11:56',1,'2013-07-17 13:03:55',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2000,'PendingForApproval',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','PendingApproval','Y',1,'2016-11-17 07:11:56',1,'2013-07-17 13:03:55',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2423,'Atlis_Invoice',NULL,'Atlis_Invoice',NULL,NULL,NULL,'ClonedSetup',NULL,NULL,NULL,'ClonedSetup','Active','Y',0,'2016-11-18 05:28:16',0,'2016-11-18 05:28:16',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2552,'Atlis_Invoice',NULL,'Atlis_Invoice',NULL,NULL,NULL,'ClonedSetup',NULL,NULL,NULL,'ClonedSetup','Active','Y',0,'2016-11-18 05:18:53',0,'2016-11-18 05:18:53',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3000,'DraftSetup',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','Draft','Y',1,'2016-11-11 10:04:40',1,'2013-07-17 13:03:55',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4000,'InActiveSetup',1,'Test','antonyashok110@yahoo.in',1,1,'Test',NULL,'Y',NULL,'Test','inactive','Y',1,'2016-11-11 10:04:40',1,'2013-07-17 13:03:55',1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4535,'',NULL,'',NULL,NULL,NULL,'ClonedSetup',NULL,NULL,NULL,'ClonedSetup','cloned','Y',0,'2017-03-15 13:09:05',0,'2017-03-15 13:09:05',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4963,'Atlis_Invoice',NULL,'Atlis_Invoice',NULL,NULL,NULL,'ClonedSetup',NULL,NULL,NULL,'ClonedSetup','Active','Y',0,'2016-11-18 05:10:54',0,'2016-11-18 05:10:54',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6725,'Atlis_Invoice',NULL,'Atlis_Invoice',NULL,NULL,NULL,'ClonedSetup',NULL,NULL,NULL,'ClonedSetup','Active','Y',0,'2016-11-18 05:08:34',0,'2016-11-18 05:08:34',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9156,'Atlis_Invoice',NULL,'Atlis_Invoice',NULL,NULL,NULL,'ClonedSetup',NULL,NULL,NULL,'ClonedSetup','Active','Y',0,'2016-11-18 05:16:19',0,'2016-11-18 05:16:19',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9738,'Atlis Invoice21',NULL,'Atlis Invoice21',NULL,NULL,NULL,'ClonedSetup',NULL,NULL,NULL,'ClonedSetup','Active','Y',0,'2016-11-17 04:47:50',0,'2016-11-17 04:47:50',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9911,'Atlis_Invoice',NULL,'Atlis_Invoice',NULL,NULL,NULL,'ClonedSetup',NULL,NULL,NULL,'ClonedSetup','Active','Y',0,'2016-11-18 05:24:44',0,'2016-11-18 05:24:44',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9926,'Atlis_Invoice',NULL,'Atlis_Invoice',NULL,NULL,NULL,'ClonedSetup',NULL,NULL,NULL,'ClonedSetup','Active','Y',0,'2016-11-18 05:12:29',0,'2016-11-18 05:12:29',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `invoice_setup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_setup_assign_bill`
--

DROP TABLE IF EXISTS `invoice_setup_assign_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_setup_assign_bill` (
  `inv_setup_id` varchar(45) NOT NULL,
  `mgr_name` varchar(45) NOT NULL,
  `address` varchar(200) NOT NULL,
  `assigned_flg` enum('Y','N') DEFAULT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` bigint(20) NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  `inv_manager_id` bigint(20) NOT NULL,
  PRIMARY KEY (`inv_manager_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_setup_assign_bill`
--

LOCK TABLES `invoice_setup_assign_bill` WRITE;
/*!40000 ALTER TABLE `invoice_setup_assign_bill` DISABLE KEYS */;
INSERT INTO `invoice_setup_assign_bill` VALUES ('1','Jo','223,K.Puthur,Madurai-625001.','Y','2016-11-03 05:22:07',1,1,'2013-07-17 18:33:55',1),('2','babu','17/9,kk nagar,Mdu-625001','N','2016-10-17 14:50:10',2,2,'2013-07-17 18:33:55',2),('3','Raju','123,Happy Street,ooty-09','Y','2016-10-17 14:50:10',3,3,'2016-10-17 20:20:10',3),('4','ramu','17/9,kk nagar,Mdu-625001','N','2016-10-17 14:50:10',3,3,'2016-10-17 20:20:10',4),('4','ramu','17/9,kk nagar,Mdu-625001','N','2016-10-17 14:50:10',3,3,'2016-10-17 20:20:10',5),('2','babu','17/9,kk nagar,Mdu-625001','N','2016-10-17 14:50:10',2,2,'2013-07-17 18:33:55',6),('1','padma','17/9,kk nagar,Mdu-625001','Y','2013-07-17 13:03:55',2,2,'2013-07-17 18:33:55',7),('3','Viveka','17/9,kk nagar,Mdu-625001','N','2013-07-17 13:03:55',1,1,'2013-07-17 18:33:55',8),('5','poornima','223,K.Puthur,Madurai-625001.','Y','2013-07-17 13:03:55',3,3,'2013-07-17 18:33:55',9),('5','Ram','223,K.Puthur,Madurai-625001.','Y','2013-07-17 13:03:55',3,3,'2013-07-17 18:33:55',10),('1','kevin','xgdfhfgnj','N','2017-03-15 11:23:55',0,0,'2017-03-15 16:53:55',11);
/*!40000 ALTER TABLE `invoice_setup_assign_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_setup_bill_cycle`
--

DROP TABLE IF EXISTS `invoice_setup_bill_cycle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_setup_bill_cycle` (
  `inv_setup_bill_cycle_id` bigint(15) NOT NULL,
  `inv_setup_id` bigint(15) DEFAULT NULL,
  `bill_cycle_id` bigint(15) DEFAULT NULL,
  `bill_cycle_strt_end_id` bigint(15) DEFAULT NULL,
  `bill_cycle_day_id` bigint(15) DEFAULT NULL,
  `cons_inv_flg` enum('Y','N') DEFAULT NULL,
  `strt_dt` date DEFAULT NULL,
  `threshold_amt` decimal(10,2) DEFAULT NULL,
  `end_dt` date DEFAULT NULL,
  `number_of_hrs` decimal(10,2) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `initial_prebill_amt` decimal(10,2) DEFAULT NULL,
  `amount_used` decimal(10,2) DEFAULT NULL,
  `amount_remaining` decimal(10,2) DEFAULT NULL,
  `accuring_flg` enum('Y','N') DEFAULT NULL,
  `sales_tax_flg` enum('Y','N') DEFAULT NULL,
  `timeframe_exceed_flg` enum('Y','N') DEFAULT NULL,
  `prebill_without_contractor_flg` enum('Y','N') DEFAULT NULL,
  `notes` text,
  `created_by` bigint(15) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  `accruing_flg` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`inv_setup_bill_cycle_id`),
  KEY `isbc_inv_setup_id_idx` (`inv_setup_id`),
  KEY `isbc_bill_cycle_id_idx` (`bill_cycle_id`),
  KEY `isbc_bill_cycle_strt_end_id_idx` (`bill_cycle_strt_end_id`),
  KEY `isbc_bill_cycle_day_id_idx` (`bill_cycle_day_id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_setup_bill_cycle`
--

LOCK TABLES `invoice_setup_bill_cycle` WRITE;
/*!40000 ALTER TABLE `invoice_setup_bill_cycle` DISABLE KEYS */;
INSERT INTO `invoice_setup_bill_cycle` VALUES (1,1,1,1,1,NULL,'2013-07-17',100.00,'2013-07-17',11.00,100.00,100.00,10.00,90.00,NULL,NULL,NULL,NULL,'test',1,'2013-07-17 13:03:55',1,'2013-07-17 13:03:55',NULL),(2,2,2,2,2,NULL,'2013-07-17',100.00,'2013-07-17',11.00,100.00,100.00,10.00,90.00,NULL,NULL,NULL,NULL,NULL,2,'2013-07-17 13:03:55',2,'2013-07-17 13:03:55',NULL);
/*!40000 ALTER TABLE `invoice_setup_bill_cycle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_setup_bill_cycle_detail`
--

DROP TABLE IF EXISTS `invoice_setup_bill_cycle_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_setup_bill_cycle_detail` (
  `inv_setup_bill_cycle_det_id` bigint(15) NOT NULL,
  `inv_setup_bill_cycle_id` bigint(15) DEFAULT NULL,
  `mature_dt` date DEFAULT NULL,
  `mature_day` varchar(3) DEFAULT NULL,
  `milestone_amt` decimal(10,2) DEFAULT NULL,
  `milestone_dt` date DEFAULT NULL,
  `created_by` bigint(15) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`inv_setup_bill_cycle_det_id`),
  KEY `inv_conf_bill_cycle_id_fk_idx` (`inv_setup_bill_cycle_id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_setup_bill_cycle_detail`
--

LOCK TABLES `invoice_setup_bill_cycle_detail` WRITE;
/*!40000 ALTER TABLE `invoice_setup_bill_cycle_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_setup_bill_cycle_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_setup_billing_info`
--

DROP TABLE IF EXISTS `invoice_setup_billing_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_setup_billing_info` (
  `inv_setup_bill_info_id` bigint(15) NOT NULL,
  `ap_contact` varchar(45) NOT NULL,
  `hiring_mgr_nm` varchar(45) NOT NULL,
  `inv_specialist_empl_id` bigint(15) DEFAULT NULL,
  `ar_specialist` varchar(45) NOT NULL,
  `payment_terms` varchar(45) NOT NULL,
  `bill_term_id` bigint(15) DEFAULT NULL,
  `address` varchar(200) NOT NULL,
  `created_by` bigint(15) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`inv_setup_bill_info_id`),
  KEY `isbi_inv_specialist_empl_id_idx` (`inv_specialist_empl_id`),
  KEY `isbi_bill_term_id_idx` (`bill_term_id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_setup_billing_info`
--

LOCK TABLES `invoice_setup_billing_info` WRITE;
/*!40000 ALTER TABLE `invoice_setup_billing_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_setup_billing_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_setup_contractor`
--

DROP TABLE IF EXISTS `invoice_setup_contractor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_setup_contractor` (
  `invsetup_id` varchar(45) NOT NULL,
  `contractor_id` bigint(15) NOT NULL,
  PRIMARY KEY (`contractor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_setup_contractor`
--

LOCK TABLES `invoice_setup_contractor` WRITE;
/*!40000 ALTER TABLE `invoice_setup_contractor` DISABLE KEYS */;
INSERT INTO `invoice_setup_contractor` VALUES ('474fa777-2dd3-843a-5af3-ac08a145fd94',7887878);
/*!40000 ALTER TABLE `invoice_setup_contractor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_setup_engagement`
--

DROP TABLE IF EXISTS `invoice_setup_engagement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_setup_engagement` (
  `inv_setup_id` bigint(15) NOT NULL,
  `engm_id` bigint(15) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date NOT NULL,
  `last_updt_dt` date DEFAULT NULL,
  PRIMARY KEY (`inv_setup_id`,`engm_id`),
  KEY `inv_conf_engm_engm_id_fk_idx` (`engm_id`)
 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_setup_engagement`
--

LOCK TABLES `invoice_setup_engagement` WRITE;
/*!40000 ALTER TABLE `invoice_setup_engagement` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_setup_engagement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_setup_notes`
--

DROP TABLE IF EXISTS `invoice_setup_notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_setup_notes` (
  `inv_setup_note_id` bigint(15) NOT NULL,
  `inv_setup_id` bigint(15) DEFAULT NULL,
  `empl_id` bigint(15) DEFAULT NULL,
  `notes` text,
  `created_on` timestamp NULL DEFAULT NULL,
  `created_by` bigint(20) NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  PRIMARY KEY (`inv_setup_note_id`),
  KEY `isn_inv_setup_id_idx` (`inv_setup_id`),
  KEY `isn_empl_id_idx` (`empl_id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_setup_notes`
--

LOCK TABLES `invoice_setup_notes` WRITE;
/*!40000 ALTER TABLE `invoice_setup_notes` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_setup_notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_setup_org_info`
--

DROP TABLE IF EXISTS `invoice_setup_org_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_setup_org_info` (
  `inv_setup_org_info_id` bigint(15) NOT NULL,
  `inv_setup_id` bigint(15) DEFAULT NULL,
  `bill_to_org_id` bigint(15) DEFAULT NULL,
  `end_org_id` bigint(15) DEFAULT NULL,
  `ofc_id` bigint(15) DEFAULT NULL,
  `am_empl_id` bigint(15) DEFAULT NULL,
  `nm_empl_id` bigint(15) DEFAULT NULL,
  `fein` varchar(45) NOT NULL,
  `cost_center` varchar(45) NOT NULL,
  `bill_currcy_id` bigint(15) DEFAULT NULL,
  `pay_currcy_id` bigint(15) DEFAULT NULL,
  `tm_typ_id` bigint(15) DEFAULT NULL,
  `lob_id` bigint(15) DEFAULT NULL,
  `dept_number` varchar(45) NOT NULL,
  `contract_id` varchar(45) NOT NULL,
  `created_by` bigint(15) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`inv_setup_org_info_id`),
  KEY `inv_conf_org_info_inv_conf_id_fk_idx` (`inv_setup_id`),
  KEY `inv_conf_org_info_ofc_id_fk_idx` (`bill_to_org_id`),
  KEY `inv_conf_org_info_am_empl_id_fk_idx` (`am_empl_id`),
  KEY `inv_conf_org_info_nm_empl_id_fk_idx` (`nm_empl_id`),
  KEY `isoi_end_org_id_idx` (`end_org_id`),
  KEY `isoi_ofc_id_idx` (`ofc_id`),
  KEY `isoi_bill_curr_id_idx` (`bill_currcy_id`),
  KEY `isoi_pay_curr_id_idx` (`pay_currcy_id`),
  KEY `isoi_tm_typ_id_idx` (`tm_typ_id`),
  KEY `isoi_lob_id_idx` (`lob_id`)
 
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_setup_org_info`
--

LOCK TABLES `invoice_setup_org_info` WRITE;
/*!40000 ALTER TABLE `invoice_setup_org_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_setup_org_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_template`
--

DROP TABLE IF EXISTS `invoice_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_template` (
  `inv_template_id` bigint(15) NOT NULL,
  `inv_template_nm` varchar(45) NOT NULL,
  `inv_template_desc` varchar(128) NOT NULL,
  `mongo_template_ref_id` varchar(36) NOT NULL,
  `logofilename` varchar(200) NOT NULL,
  `std_template_flg` enum('Y','N') DEFAULT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `created_by` bigint(15) DEFAULT NULL,
  `created_on` timestamp NULL DEFAULT NULL,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`inv_template_id`),
  UNIQUE KEY `created_by_UNIQUE` (`created_by`),
  UNIQUE KEY `created_on_UNIQUE` (`created_on`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_template`
--

LOCK TABLES `invoice_template` WRITE;
/*!40000 ALTER TABLE `invoice_template` DISABLE KEYS */;
INSERT INTO `invoice_template` VALUES (1,'Test','Test','101','Test','Y',NULL,1,'2013-07-17 13:03:55',1,'2013-07-17 13:03:55');
/*!40000 ALTER TABLE `invoice_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_template_attributes`
--

DROP TABLE IF EXISTS `invoice_template_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_template_attributes` (
  `inv_template_id` bigint(15) NOT NULL,
  `inv_attr_id` bigint(15) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date DEFAULT NULL,
  PRIMARY KEY (`inv_template_id`,`inv_attr_id`),
  KEY `inv_template_attr_inv_attr_id_idx` (`inv_attr_id`),
  CONSTRAINT `inv_template_attr_inv_attr_id` FOREIGN KEY (`inv_attr_id`) REFERENCES `invoice_attribute` (`inv_attr_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `inv_template_attr_inv_template_id` FOREIGN KEY (`inv_template_id`) REFERENCES `invoice_template` (`inv_template_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_template_attributes`
--

LOCK TABLES `invoice_template_attributes` WRITE;
/*!40000 ALTER TABLE `invoice_template_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_template_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_timesheet`
--

DROP TABLE IF EXISTS `invoice_timesheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_timesheet` (
  `inv_tmsht_id` bigint(15) NOT NULL,
  `inv_id` bigint(15) DEFAULT NULL,
  `empl_id` bigint(15) DEFAULT NULL,
  `week_st_dt` date NOT NULL,
  `week_end_dt` date NOT NULL,
  `billed_hrs` float(10,2) NOT NULL,
  `billed_rate` int(5) DEFAULT NULL,
  `total_amt` float(10,2) DEFAULT NULL,
  `submitted_on` date NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `created_by` bigint(15) NOT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`inv_tmsht_id`),
  KEY `inv_tmsht_inv_id_fk_idx` (`inv_id`),
  KEY `inv_tmsht_empl_id_fk_idx` (`empl_id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_timesheet`
--

LOCK TABLES `invoice_timesheet` WRITE;
/*!40000 ALTER TABLE `invoice_timesheet` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice_timesheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice_type`
--

DROP TABLE IF EXISTS `invoice_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice_type` (
  `inv_typ_id` bigint(15) NOT NULL,
  `inv_typ_nm` varchar(45) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date NOT NULL,
  `last_updt_dt` date DEFAULT NULL,
  PRIMARY KEY (`inv_typ_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice_type`
--

LOCK TABLES `invoice_type` WRITE;
/*!40000 ALTER TABLE `invoice_type` DISABLE KEYS */;
INSERT INTO `invoice_type` VALUES (1,'Weekly','Y','2013-07-17','2013-07-17'),(2,'Monthly','Y','2013-07-17','2013-07-17'),(3,'Yearly','Y','2013-07-17','2013-07-17');
/*!40000 ALTER TABLE `invoice_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoicesetup_contractor_details`
--

DROP TABLE IF EXISTS `invoicesetup_contractor_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoicesetup_contractor_details` (
  `empl_id` bigint(15) NOT NULL,
  `file_no` varchar(45) DEFAULT NULL,
  `remove_flg` enum('Y','N') NOT NULL DEFAULT 'N',
  `created_ by` bigint(15) DEFAULT NULL,
  `created_on` timestamp NULL DEFAULT NULL,
  `updated_by` bigint(15) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  `inv_setup_id` bigint(15) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `inv_con_Id` bigint(20) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`inv_setup_id`,`empl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoicesetup_contractor_details`
--

LOCK TABLES `invoicesetup_contractor_details` WRITE;
/*!40000 ALTER TABLE `invoicesetup_contractor_details` DISABLE KEYS */;
INSERT INTO `invoicesetup_contractor_details` VALUES (2,'2000N','N',1,'2013-07-17 13:03:55',1,'2013-07-17 13:03:55',1,0,0,NULL),(3,'3000N','N',1,'2013-07-17 13:03:55',1,'2013-07-17 13:03:55',1,0,0,NULL),(4,'4000N','Y',1,'2013-07-17 13:03:55',1,'2013-07-17 13:03:55',1,0,0,NULL),(5,'5000N','Y',1,'2013-07-17 13:03:55',1,'2013-07-17 13:03:55',1,1,0,NULL),(100002,'100002N','Y',1,'2013-07-17 13:03:55',1,'2013-07-17 13:03:55',1,1,0,NULL),(1,'1000N','N',1,'2013-07-17 13:03:55',1,'2013-07-17 13:03:55',2,0,0,NULL),(122321,'123456','N',NULL,'2016-11-18 05:28:16',0,'2016-11-18 05:28:16',2423,0,23,'Active'),(122321,'123456','N',NULL,'2016-11-18 05:18:53',0,'2016-11-18 05:18:53',2552,0,21,'Active'),(122321,'123456','N',NULL,'2016-11-18 05:11:00',0,'2016-11-18 05:11:00',4963,0,18,'Active'),(122321,'123456','N',NULL,'2016-11-18 05:08:34',0,'2016-11-18 05:08:34',6725,0,17,'Active'),(122321,'123456','N',NULL,'2016-11-18 05:16:19',0,'2016-11-18 05:16:19',9156,0,20,'Active'),(122321,'123456','N',NULL,'2016-11-17 04:47:52',0,'2016-11-17 04:47:52',9738,0,16,'Active'),(122321,'123456','N',NULL,'2016-11-18 05:24:44',0,'2016-11-18 05:24:44',9911,0,22,'Active'),(122321,'123456','N',NULL,'2016-11-18 05:12:29',0,'2016-11-18 05:12:29',9926,0,19,'Active');
/*!40000 ALTER TABLE `invoicesetup_contractor_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `line_of_business`
--

DROP TABLE IF EXISTS `line_of_business`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `line_of_business` (
  `lob_id` bigint(15) NOT NULL,
  `lob_nm` varchar(45) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date DEFAULT NULL,
  `last_updt_dt` date DEFAULT NULL,
  PRIMARY KEY (`lob_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `line_of_business`
--

LOCK TABLES `line_of_business` WRITE;
/*!40000 ALTER TABLE `line_of_business` DISABLE KEYS */;
INSERT INTO `line_of_business` VALUES (1,'Test',NULL,'2013-07-17','2013-07-17');
/*!40000 ALTER TABLE `line_of_business` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order`
--

DROP TABLE IF EXISTS `purchase_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_order` (
  `po_id` char(36) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  `actv_flg` varchar(255) DEFAULT NULL,
  `bill_person` varchar(255) DEFAULT NULL,
  `bill_to_org_id` binary(255) DEFAULT NULL,
  `enddate_alert` int(11) DEFAULT NULL,
  `end_org_id` binary(255) DEFAULT NULL,
  `exp_add_funds` decimal(19,2) DEFAULT NULL,
  `exp_alert` varchar(255) DEFAULT NULL,
  `exp_amt` decimal(19,2) DEFAULT NULL,
  `exp_deduct_funds` decimal(19,2) DEFAULT NULL,
  `exp_end_dt` datetime DEFAULT NULL,
  `exp_separate` varchar(255) DEFAULT NULL,
  `exp_strt_dt` datetime DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `first_deduct_dt` datetime DEFAULT NULL,
  `grp_po_flg` varchar(255) DEFAULT NULL,
  `hours_add_funds` decimal(19,2) DEFAULT NULL,
  `hours_alert` varchar(255) DEFAULT NULL,
  `hours_amt` decimal(19,2) DEFAULT NULL,
  `hours_deduct_funds` decimal(19,2) DEFAULT NULL,
  `inv_setup_id` varchar(255) DEFAULT NULL,
  `last_repl_dt` datetime DEFAULT NULL,
  `low_amount_alert` varchar(45) DEFAULT NULL,
  `no_of_contractors` int(11) DEFAULT NULL,
  `overTimePO` int(11) DEFAULT NULL,
  `overtime_po_flg` varchar(255) DEFAULT NULL,
  `po_dt` datetime DEFAULT NULL,
  `po_desc` varchar(255) NOT NULL,
  `po_end_dt` datetime DEFAULT NULL,
  `po_Number` varchar(45) NOT NULL,
  `po_strt_dt` datetime DEFAULT NULL,
  `remaining_amt` decimal(19,2) DEFAULT NULL,
  `remaining_amt_expense` decimal(19,2) DEFAULT NULL,
  `remaining_fund_week` int(11) DEFAULT NULL,
  `remaining_hrs` decimal(19,2) DEFAULT NULL,
  `revenue_amount` decimal(19,2) NOT NULL,
  `revenue_end_dt` datetime DEFAULT NULL,
  `revenue_strt_dt` datetime DEFAULT NULL,
  `time_exp_separate` varchar(255) DEFAULT NULL,
  `total_amount` decimal(19,2) DEFAULT NULL,
  `total_amt_expense` decimal(19,2) DEFAULT NULL,
  `total_hrs` decimal(19,2) DEFAULT NULL,
  `employee_Id` bigint(20) DEFAULT NULL,
  `po_parent_id` char(36) DEFAULT NULL,
  `roll_over_flg` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`po_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order`
--

LOCK TABLES `purchase_order` WRITE;
/*!40000 ALTER TABLE `purchase_order` DISABLE KEYS */;
INSERT INTO `purchase_order` VALUES ('2b7563da-559b-46b8-815b-1051fbd8b1c7',0,'2017-03-14 19:40:39',0,'2017-03-14 19:40:39',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','download.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'784675676',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','ertertyrtuu','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,NULL,NULL),('350c421e-c4e6-4a14-8068-b01d37eecae6',0,'2017-03-15 10:11:03',0,'2017-03-15 10:11:03',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','download.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'784675676',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','rtyu','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,NULL,NULL),('7e677cfc-2c03-4041-b965-04dbeab58499',0,'2017-03-13 18:32:30',0,'2017-03-13 18:32:30',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'784675676',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','eerrtyrtytu','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,NULL,NULL),('8e28f5dd-44d1-4f3d-8b7c-8fe857df779a',0,'2017-03-14 16:13:01',0,'2017-03-14 16:13:01',NULL,NULL,NULL,0,NULL,NULL,'Y',23.00,NULL,'2017-02-25 05:30:00',NULL,'2017-02-22 05:30:00','download.jpg',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'N',0,NULL,NULL,NULL,'sdsdada','2017-02-16 05:30:00','2222','2017-02-09 05:30:00',NULL,NULL,0,NULL,32.00,'2017-02-23 05:30:00','2017-02-14 05:30:00',NULL,NULL,NULL,NULL,15,NULL,NULL),('9371a643-191b-41de-8107-f25ab284bfc8',0,'2016-11-16 09:59:52',0,'2016-11-16 09:59:52',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'784675676',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,'',NULL),('99afe36c-9eb6-4510-b1a0-cc3b782f6305',0,'2016-11-03 11:09:09',0,'2016-11-03 11:09:09',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'2',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,'',NULL),('9c5e5d03-85fc-4f0b-9238-afa050f875d1',0,'2016-11-03 15:48:38',0,'2016-11-03 15:48:38',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'2',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,'',NULL),('be135868-2162-43e6-9e19-0443ab9b00c4',0,'2016-11-03 11:54:32',0,'2016-11-03 11:54:32',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'2',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,'',NULL),('bf18fa31-b1d3-44fd-9f8e-da1ff687c8cf',0,'2016-11-03 11:44:38',0,'2016-11-03 11:44:38',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'2',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,'',NULL),('c3fed50a-7af7-4494-9e01-b5299aa8bc46',0,'2016-11-02 14:57:19',0,'2016-11-02 14:57:19',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'784675676',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,'',NULL),('cb8bc0a9-5c2b-429b-960c-1107a7733f15',0,'2016-11-16 09:55:47',0,'2016-11-16 09:55:47',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'784675676',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,'',NULL),('d5205d46-160f-4ec2-b7a3-119821789d3a',0,'2016-11-03 11:14:43',0,'2016-11-03 11:14:43',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'2',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,'',NULL),('d7bf3dd0-44b7-4700-b71e-a5d62ca47840',0,'2016-10-31 18:28:05',0,'2016-10-31 18:28:05',NULL,NULL,NULL,NULL,NULL,NULL,NULL,54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'784675676',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,NULL,'',NULL),('d8cf3583-14f3-42f4-a5f9-72d88077383a',0,'2016-11-01 16:13:29',0,'2016-11-01 16:13:29',NULL,NULL,NULL,NULL,NULL,NULL,NULL,54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'22222',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,NULL,'',NULL),('de9ff457-e350-41d9-825f-b4a652c99fba',0,'2016-10-31 16:16:30',0,'2016-10-31 16:16:30',NULL,NULL,NULL,NULL,NULL,NULL,NULL,54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'784675676',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','233','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,NULL,'',NULL),('ea4b3678-e131-4e42-90c5-a9cbf05d90db',0,'2016-11-03 11:22:18',0,'2016-11-03 11:22:18',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'2',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,'',NULL),('f22e0d10-6969-4224-8966-e32f8e41ca9e',0,'2016-11-03 11:23:07',0,'2016-11-03 11:23:07',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'2',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,'',NULL),('f906b499-3a75-4045-a0e6-27087d83ff03',0,'2016-11-03 11:09:50',0,'2016-11-03 11:09:50',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'2',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,'',NULL),('fa266b93-47b1-41bb-94d5-c2a0ba5344b6',0,'2017-03-13 18:02:21',0,'2017-03-13 18:02:21',NULL,NULL,NULL,NULL,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','download.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'784675676',NULL,NULL,0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','xxxx','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,43435545656,NULL,NULL),('ff803c07-ecd3-4f1d-b22f-e9756211d034',0,'2016-11-02 11:39:50',0,'2016-11-02 11:39:50',NULL,NULL,NULL,0,NULL,NULL,'Y',54657567.00,NULL,'2017-04-20 18:30:00',NULL,'2017-04-20 18:30:00','img.jpg',NULL,NULL,NULL,NULL,NULL,NULL,'784675676',NULL,'Y',0,NULL,NULL,NULL,'sfdrgdfgfgdf','2017-04-20 18:30:00','exp','2017-04-20 18:30:00',NULL,NULL,0,NULL,34353454.00,'2017-04-20 18:30:00','2017-04-20 18:30:00',NULL,NULL,NULL,NULL,NULL,'',NULL);
/*!40000 ALTER TABLE `purchase_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order_contractor`
--

DROP TABLE IF EXISTS `purchase_order_contractor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_order_contractor` (
  `purchase_order_contractor_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  `actv_flg` varchar(255) DEFAULT NULL,
  `contractor_id` bigint(20) DEFAULT NULL,
  `empl_id` bigint(20) NOT NULL,
  `po_id` char(36) NOT NULL,
  PRIMARY KEY (`purchase_order_contractor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order_contractor`
--

LOCK TABLES `purchase_order_contractor` WRITE;
/*!40000 ALTER TABLE `purchase_order_contractor` DISABLE KEYS */;
INSERT INTO `purchase_order_contractor` VALUES (1,0,'2017-03-13 18:02:27',0,'2017-03-13 18:02:27',NULL,NULL,43435545656,'fa266b93-47b1-41bb-94d5-c2a0ba5344b6'),(2,0,'2017-03-13 18:32:30',0,'2017-03-13 18:32:30',NULL,NULL,43435545656,'7e677cfc-2c03-4041-b965-04dbeab58499'),(3,0,'2017-03-14 16:13:01',0,'2017-03-14 16:13:01',NULL,NULL,15,'8e28f5dd-44d1-4f3d-8b7c-8fe857df779a'),(4,0,'2017-03-14 19:40:39',0,'2017-03-14 19:40:39',NULL,NULL,43435545656,'2b7563da-559b-46b8-815b-1051fbd8b1c7'),(5,0,'2017-03-15 10:11:03',0,'2017-03-15 10:11:03',NULL,NULL,43435545656,'350c421e-c4e6-4a14-8068-b01d37eecae6');
/*!40000 ALTER TABLE `purchase_order_contractor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order_notes`
--

DROP TABLE IF EXISTS `purchase_order_notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_order_notes` (
  `po_note_id` char(36) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  `employee_id` bigint(20) NOT NULL,
  `notes` varchar(255) NOT NULL,
  `po_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`po_note_id`),
  KEY `FKsa2lg27q4o852mg5jpu5uend5` (`po_id`)
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order_notes`
--

LOCK TABLES `purchase_order_notes` WRITE;
/*!40000 ALTER TABLE `purchase_order_notes` DISABLE KEYS */;
INSERT INTO `purchase_order_notes` VALUES ('2a4b31a2-bc8a-41bf-8012-567cdd369e55',0,'2017-03-14 19:40:39',0,'2017-03-14 19:40:39',43435545656,'sdffdsfdgfdsg','2b7563da-559b-46b8-815b-1051fbd8b1c7'),('2ce70006-a6b4-403a-a992-828265ca7f44',0,'2017-03-13 18:32:30',0,'2017-03-13 18:32:30',43435545656,'sdffdsfdgfdsg','7e677cfc-2c03-4041-b965-04dbeab58499'),('901fdc52-7c00-49f3-86d9-a577863beaec',0,'2017-03-14 16:13:01',0,'2017-03-14 16:13:01',15,'gfgsfgsfg','8e28f5dd-44d1-4f3d-8b7c-8fe857df779a'),('babd4126-2600-4b43-8b7b-4452d42bdfc6',0,'2017-03-15 10:11:03',0,'2017-03-15 10:11:03',43435545656,'sdffdsfdgfdsg','350c421e-c4e6-4a14-8068-b01d37eecae6'),('bd7e4098-f8d2-4ac0-8436-6e83d548eb1d',0,'2017-03-13 18:02:21',0,'2017-03-13 18:02:21',43435545656,'sdffdsfdgfdsg','fa266b93-47b1-41bb-94d5-c2a0ba5344b6');
/*!40000 ALTER TABLE `purchase_order_notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_type`
--

DROP TABLE IF EXISTS `report_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_type` (
  `rpt_typ_id` bigint(15) NOT NULL,
  `rpt_typ_nm` varchar(45) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date NOT NULL,
  `last_updt_dt` date DEFAULT NULL,
  PRIMARY KEY (`rpt_typ_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_type`
--

LOCK TABLES `report_type` WRITE;
/*!40000 ALTER TABLE `report_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task_rate`
--

DROP TABLE IF EXISTS `task_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task_rate` (
  `task_rate_id` varchar(255) NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `created_on` datetime NOT NULL,
  `updated_by` bigint(20) NOT NULL,
  `updated_on` datetime NOT NULL,
  `bill_Rate_DT` decimal(19,2) DEFAULT NULL,
  `bill_Rate_OT` decimal(19,2) DEFAULT NULL,
  `bill_Rate_ST` decimal(19,2) DEFAULT NULL,
  `bill_to_client_rate` decimal(19,2) DEFAULT NULL,
  `effective_date` datetime NOT NULL,
  `End_client_rate` decimal(19,2) DEFAULT NULL,
  `end_Rate_DT` decimal(19,2) DEFAULT NULL,
  `end_Rate_OT` decimal(19,2) DEFAULT NULL,
  `end_Rate_ST` decimal(19,2) DEFAULT NULL,
  `task_id` char(36) DEFAULT NULL,
  PRIMARY KEY (`task_rate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task_rate`
--

LOCK TABLES `task_rate` WRITE;
/*!40000 ALTER TABLE `task_rate` DISABLE KEYS */;
/*!40000 ALTER TABLE `task_rate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `timesheet_type`
--

DROP TABLE IF EXISTS `timesheet_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `timesheet_type` (
  `tm_typ_id` bigint(15) NOT NULL,
  `tm_typ_nm` varchar(45) NOT NULL,
  `actv_flg` enum('Y','N') DEFAULT NULL,
  `create_dt` date DEFAULT NULL,
  `last_updt_dt` date DEFAULT NULL,
  PRIMARY KEY (`tm_typ_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `timesheet_type`
--

LOCK TABLES `timesheet_type` WRITE;
/*!40000 ALTER TABLE `timesheet_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `timesheet_type` ENABLE KEYS */;
UNLOCK TABLES;


ALTER TABLE invoice  ADD CONSTRAINT inv_delivery_mode_id FOREIGN KEY (delivery_mode_id) REFERENCES delivery_mode(delivery_mode_id);

ALTER TABLE invoice  ADD CONSTRAINT inv_inv_template_id FOREIGN KEY (inv_template_id) REFERENCES invoice_template(inv_template_id);

ALTER TABLE invoice  ADD CONSTRAINT inv_lob_id FOREIGN KEY (lob_id) REFERENCES line_of_business(lob_id);


ALTER TABLE invoice_attribute_value  ADD CONSTRAINT inv_attr_val_inv_attr_id_fk FOREIGN KEY (inv_attr_id) REFERENCES invoice_attribute(inv_attr_id);

ALTER TABLE invoice_attribute_value  ADD CONSTRAINT inv_attr_val_inv_id_fk FOREIGN KEY (inv_id) REFERENCES invoice(inv_id);


ALTER TABLE invoice_contractor  ADD CONSTRAINT ic_inv_id FOREIGN KEY (inv_id) REFERENCES invoice(inv_id);


ALTER TABLE invoice_expense  ADD CONSTRAINT inv_exp_inv_id_fk FOREIGN KEY (inv_id) REFERENCES invoice(inv_id);


ALTER TABLE invoice_organization_template  ADD CONSTRAINT inv_org_template_inv_template_id_fk FOREIGN KEY (inv_template_id) REFERENCES invoice_template(inv_template_id);


ALTER TABLE invoice_return  ADD CONSTRAINT ir_inv_id FOREIGN KEY (inv_id) REFERENCES invoice(inv_id);


ALTER TABLE invoice_setup  ADD CONSTRAINT delivery_mode_id FOREIGN KEY (delivery_mode_id) REFERENCES delivery_mode(delivery_mode_id);


ALTER TABLE invoice_setup  ADD CONSTRAINT is_inv_template_id FOREIGN KEY (inv_template_id) REFERENCES invoice_template(inv_template_id);


ALTER TABLE invoice_setup  ADD CONSTRAINT is_inv_typ_id FOREIGN KEY (inv_typ_id) REFERENCES invoice_type(inv_typ_id);


ALTER TABLE invoice_setup_bill_cycle  ADD CONSTRAINT isbc_bill_cycle_day_id FOREIGN KEY (bill_cycle_day_id) REFERENCES bill_cycle_day(bill_cycle_day_id);


ALTER TABLE invoice_setup_bill_cycle  ADD CONSTRAINT isbc_bill_cycle_id FOREIGN KEY (bill_cycle_id) REFERENCES bill_cycle(bill_cycle_id);

ALTER TABLE `invoice`.`invoice_setup_bill_cycle` 
ADD CONSTRAINT `isbc_bill_cycle_strt_end_id`
  FOREIGN KEY (`bill_cycle_strt_end_id`)
  REFERENCES `invoice`.`bill_cycle_start_end` (`bill_cycle_start_end_id`);


ALTER TABLE invoice_setup_bill_cycle  ADD CONSTRAINT isbc_inv_setup_id FOREIGN KEY (inv_setup_id) REFERENCES invoice_setup(inv_setup_id);

ALTER TABLE invoice_setup_bill_cycle_detail  ADD CONSTRAINT inv_setup_bill_cycle_id_fk FOREIGN KEY (inv_setup_bill_cycle_id) REFERENCES invoice_setup_bill_cycle(inv_setup_bill_cycle_id);


ALTER TABLE invoice_setup_billing_info  ADD CONSTRAINT isbi_bill_term_id FOREIGN KEY (bill_term_id) REFERENCES billing_term(bill_term_id);

ALTER TABLE invoice_setup_engagement  ADD CONSTRAINT inv_conf_engm_inv_conf_id_fk FOREIGN KEY (inv_setup_id) REFERENCES invoice_setup(inv_setup_id);


ALTER TABLE invoice_setup_notes  ADD CONSTRAINT isn_inv_setup_id FOREIGN KEY (inv_setup_id) REFERENCES invoice_setup(inv_setup_id);

ALTER TABLE invoice_setup_org_info  ADD CONSTRAINT isoi_inv_setup_id FOREIGN KEY (inv_setup_id) REFERENCES invoice_setup(inv_setup_id);


ALTER TABLE invoice_setup_org_info  ADD CONSTRAINT isoi_lob_id FOREIGN KEY (lob_id) REFERENCES line_of_business(lob_id);


ALTER TABLE invoice_setup_org_info  ADD CONSTRAINT isoi_tm_typ_id FOREIGN KEY (tm_typ_id) REFERENCES timesheet_type(tm_typ_id);


ALTER TABLE invoice_timesheet  ADD CONSTRAINT inv_tmsht_inv_id_fk FOREIGN KEY (inv_id) REFERENCES invoice(inv_id);

ALTER TABLE purchase_order_notes  ADD CONSTRAINT po_id_fk FOREIGN KEY (po_id) REFERENCES purchase_order(po_id);


CREATE OR REPLACE VIEW `contractor_tab_view` AS 
select
   round((rand() * 10000), 0) AS `unique_id`,
   `common`.`employee`.`empl_id` AS `employee_id`,
   concat(`common`.`employee`.`first_nm`, `common`.`employee`.`last_nm`) AS `employee_name`,
   `common`.`employee`.`file_no` AS `file_no`,
   `icd`.`inv_setup_id` AS `inv_setup_id`,
   `poc`.`po_id` AS `po_id`,
   `po`.`po_Number` AS `po_number`,
   `po`.`po_strt_dt` AS `po_start_date` 
from
   (
((`invoice`.`invoicesetup_contractor_details` `icd` 
      join
         `common`.`employee` 
         on(((`common`.`employee`.`empl_id` = `icd`.`empl_id`) 
         and 
         (
            `icd`.`remove_flg` = 'N'
         )
))) 
      left join
         `invoice`.`purchase_order_contractor` `poc` 
         on((`common`.`employee`.`empl_id` = `poc`.`empl_id`))) 
      left join
         `invoice`.`purchase_order` `po` 
         on((`poc`.`po_id` = `po`.`po_id`))
   )
;


DROP view invoice.contractor_view;


CREATE OR REPLACE VIEW `employee` AS 
select
   `common`.`employee`.`empl_id` AS `empl_id`,
   `common`.`employee`.`emp_rl_id` AS `emp_rl_id`,
   `common`.`employee`.`first_nm` AS `first_nm`,
   `common`.`employee`.`last_nm` AS `last_nm`,
   `common`.`employee`.`middle_initials` AS `middle_initials`,
   `common`.`employee`.`tin` AS `tin`,
   `common`.`employee`.`portal_userid` AS `portal_userid`,
   `common`.`employee`.`file_no` AS `file_no`,
   `common`.`employee`.`addr_id` AS `addr_id`,
   `common`.`employee`.`prmry_email_comm_info_id` AS `prmry_email_comm_info_id`,
   `common`.`employee`.`scndry_email_comm_info_id` AS `scndry_email_comm_info_id`,
   `common`.`employee`.`prmry_phn_comm_info_id` AS `prmry_phn_comm_info_id`,
   `common`.`employee`.`scndry_phn_comm_info_id` AS `scndry_phn_comm_info_id`,
   `common`.`employee`.`mgr_empl_id` AS `mgr_empl_id`,
   `common`.`employee`.`ofc_id` AS `ofc_id`,
   `common`.`employee`.`profile_id` AS `profile_id`,
   `common`.`employee`.`esf_id` AS `esf_id`,
   `common`.`employee`.`empl_typ` AS `empl_typ`,
   `common`.`employee`.`empl_strt_dt` AS `empl_strt_dt`,
   `common`.`employee`.`hourly_flg` AS `hourly_flg`,
   `common`.`employee`.`actv_flg` AS `actv_flg`,
   `common`.`employee`.`create_dt` AS `create_dt`,
   `common`.`employee`.`last_updt_dt` AS `last_updt_dt`,
   `common`.`employee`.`usrtype` AS `usrtype` 
from
   `common`.`employee`;


CREATE OR REPLACE VIEW `employee_address` AS 
select
   `common`.`address`.`addr_id` AS `addr_id`,
   `common`.`address`.`st_prv_id` AS `st_prv_id`,
   `common`.`address`.`cntry_id` AS `cntry_id`,
   `common`.`address`.`addr_strt_1` AS `addr_strt_1`,
   `common`.`address`.`addr_strt_2` AS `addr_strt_2`,
   `common`.`address`.`addr_strt_3` AS `addr_strt_3`,
   `common`.`address`.`addr_city_1` AS `addr_city_1`,
   `common`.`address`.`addr_city_2` AS `addr_city_2`,
   `common`.`address`.`addr_city_3` AS `addr_city_3`,
   `common`.`address`.`pstl_cd` AS `pstl_cd`,
   `common`.`address`.`actv_flg` AS `actv_flg`,
   `common`.`address`.`create_dt` AS `create_dt`,
   `common`.`address`.`last_updt_dt` AS `last_updt_dt`,
   `common`.`address`.`county` AS `county`,
   `common`.`address`.`addr_unit` AS `addr_unit`,
   `common`.`address`.`addr_typ` AS `addr_typ`
from
   `common`.`address`;


CREATE OR REPLACE VIEW `employee_comm_info` AS 
select
   `common`.`comm_info`.`comm_info_id` AS `comm_info_id`,
   `common`.`comm_info`.`cntct_id` AS `cntct_id`,
   `common`.`comm_info`.`cntct_typ` AS `cntct_typ`,
   `common`.`comm_info`.`cntct_info` AS `cntct_info`,
   `common`.`comm_info`.`actv_flg` AS `actv_flg`
from
   `common`.`comm_info`;


CREATE OR REPLACE VIEW `employee_role` AS 
select
   `common`.`employee_role`.`emp_rl_id` AS `emp_rl_id`,
   `common`.`employee_role`.`emp_rl_nm` AS `emp_rl_nm`,
   `common`.`employee_role`.`emp_rl_desc` AS `emp_rl_desc`,
   `common`.`employee_role`.`actv_flg` AS `actv_flg`
from
   `common`.`employee_role`;


TRUNCATE TABLE invoice.purchase_order_notes;

TRUNCATE TABLE invoice.purchase_order_contractor;




DELETE FROM invoice.purchase_order;









