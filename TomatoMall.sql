-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: tomatomall
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `advertisements`
--

DROP TABLE IF EXISTS `advertisements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `advertisements` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '广告id',
  `content` varchar(500) NOT NULL COMMENT '广告内容',
  `image_url` varchar(500) NOT NULL COMMENT '广告图片url',
  `product_id` int NOT NULL COMMENT '所属商品id，不允许为空',
  `title` varchar(50) NOT NULL COMMENT '广告标题，不允许为空',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `advertisements`
--

LOCK TABLES `advertisements` WRITE;
/*!40000 ALTER TABLE `advertisements` DISABLE KEYS */;
INSERT INTO `advertisements` VALUES (4,'西游记','http://test-njshop.oss-cn-nanjing.aliyuncs.com/%E8%A5%BF%E6%B8%B8.png',31,'西游'),(6,'西游记','http://test-njshop.oss-cn-nanjing.aliyuncs.com/%E8%A5%BF%E6%B8%B8.png',31,'西游'),(7,'0','http://test-njshop.oss-cn-nanjing.aliyuncs.com/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20250427130724.png',33,'C Primer Plus');
/*!40000 ALTER TABLE `advertisements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `cart_item_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`cart_item_id`),
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `carts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `carts_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
INSERT INTO `carts` VALUES (5,11,31,1),(6,11,33,3);
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts_orders_relation`
--

DROP TABLE IF EXISTS `carts_orders_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts_orders_relation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cartitem_id` int NOT NULL,
  `order_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cartitem_id` (`cartitem_id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `carts_orders_relation_ibfk_1` FOREIGN KEY (`cartitem_id`) REFERENCES `carts` (`cart_item_id`),
  CONSTRAINT `carts_orders_relation_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts_orders_relation`
--

LOCK TABLES `carts_orders_relation` WRITE;
/*!40000 ALTER TABLE `carts_orders_relation` DISABLE KEYS */;
INSERT INTO `carts_orders_relation` VALUES (1,5,132),(2,6,132),(3,5,133),(4,6,133),(5,5,134),(6,6,134),(7,5,135),(8,6,135),(9,5,136),(10,6,136),(11,5,137),(12,6,137),(13,5,150),(14,6,150),(15,5,151),(16,6,151),(17,5,152),(18,6,152),(19,5,153),(20,6,153),(21,5,154),(22,6,154),(23,5,155),(24,6,155),(25,5,156),(26,6,156),(27,5,157),(28,6,157),(29,5,176),(30,6,176),(31,5,177),(32,6,177),(33,5,178),(34,6,178),(35,5,179),(36,6,179);
/*!40000 ALTER TABLE `carts_orders_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `content` varchar(255) NOT NULL,
  `father_id` int DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `product_id` int NOT NULL,
  `rate` int DEFAULT NULL,
  `son` json DEFAULT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (4,'awesome',NULL,NULL,31,5,'[5, 11, 12, 15]',11),(6,'good',5,NULL,31,5,'[]',11),(8,'很好的书',5,NULL,31,5,'[]',11),(10,'很好的书',5,NULL,31,5,'[]',11),(15,'awesome',4,NULL,31,5,'[]',11);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupon`
--

DROP TABLE IF EXISTS `coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupon` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `is_common` tinyint(1) DEFAULT NULL,
  `volume` decimal(20,2) DEFAULT NULL,
  `need` decimal(20,2) DEFAULT NULL,
  `deadline` date DEFAULT NULL,
  `number` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `coupon_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupon`
--

LOCK TABLES `coupon` WRITE;
/*!40000 ALTER TABLE `coupon` DISABLE KEYS */;
INSERT INTO `coupon` VALUES (22,NULL,0,40.00,50.00,'2025-05-31',5),(23,NULL,0,40.00,50.00,'2025-05-31',9),(24,NULL,1,50.00,40.00,'2025-05-31',10);
/*!40000 ALTER TABLE `coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupon_products`
--

DROP TABLE IF EXISTS `coupon_products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupon_products` (
  `coupon_id` int NOT NULL,
  `product_id` int NOT NULL,
  PRIMARY KEY (`coupon_id`,`product_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `coupon_products_ibfk_1` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`id`) ON DELETE CASCADE,
  CONSTRAINT `coupon_products_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupon_products`
--

LOCK TABLES `coupon_products` WRITE;
/*!40000 ALTER TABLE `coupon_products` DISABLE KEYS */;
INSERT INTO `coupon_products` VALUES (23,31),(23,32),(22,34);
/*!40000 ALTER TABLE `coupon_products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `payment_method` varchar(50) NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'PENDING',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `lock_time` datetime DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,6,5967.00,'alipay','TIMEOUT','2025-04-17 20:50:48','2025-04-18 04:50:48'),(2,6,5967.00,'alipay','TIMEOUT','2025-04-17 20:50:55','2025-04-18 04:50:55'),(3,6,5967.00,'alipay','TIMEOUT','2025-04-17 21:06:10','2025-04-18 05:06:10'),(4,6,5967.00,'alipay','TIMEOUT','2025-04-17 21:26:36','2025-04-18 05:26:36'),(5,6,5967.00,'alipay','TIMEOUT','2025-04-17 21:26:39','2025-04-18 05:26:39'),(6,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:40:28','2025-04-18 06:40:28'),(7,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:41:25','2025-04-18 06:41:25'),(8,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:42:35','2025-04-18 06:42:35'),(9,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:43:26','2025-04-18 06:43:26'),(10,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:43:32','2025-04-18 06:43:32'),(11,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:44:28','2025-04-18 06:44:28'),(12,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:47:45','2025-04-18 06:47:45'),(13,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:48:09','2025-04-18 06:48:09'),(14,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:49:41','2025-04-18 06:49:41'),(15,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:51:32','2025-04-18 06:51:32'),(16,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:52:32','2025-04-18 06:52:32'),(17,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:52:49','2025-04-18 06:52:49'),(18,6,5967.00,'alipay','TIMEOUT','2025-04-17 22:53:10','2025-04-18 06:53:10'),(19,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:03:04','2025-04-18 07:03:04'),(20,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:03:28','2025-04-18 07:03:28'),(21,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:10:40','2025-04-18 07:10:40'),(22,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:11:39','2025-04-18 07:11:39'),(23,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:14:55','2025-04-18 07:14:55'),(24,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:19:25','2025-04-18 07:19:25'),(25,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:22:38','2025-04-18 07:22:38'),(26,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:23:20','2025-04-18 07:23:20'),(27,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:23:40','2025-04-18 07:23:40'),(28,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:25:03','2025-04-18 07:25:03'),(29,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:25:21','2025-04-18 07:25:21'),(30,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:26:35','2025-04-18 07:26:35'),(31,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:28:39','2025-04-18 07:28:39'),(32,6,5967.00,'wechat','TIMEOUT','2025-04-17 23:29:03','2025-04-18 07:29:03'),(33,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:46:18','2025-04-18 07:46:18'),(34,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:51:45','2025-04-18 07:51:45'),(35,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:58:48','2025-04-18 07:58:48'),(36,6,5967.00,'alipay','TIMEOUT','2025-04-17 23:59:00','2025-04-18 07:59:00'),(37,6,5967.00,'alipay','TIMEOUT','2025-04-18 00:03:33','2025-04-18 08:03:33'),(38,6,5967.00,'alipay','TIMEOUT','2025-04-18 00:04:27','2025-04-18 08:04:27'),(39,6,5967.00,'alipay','TIMEOUT','2025-04-18 00:06:59','2025-04-18 08:06:59'),(40,6,5967.00,'alipay','TIMEOUT','2025-04-18 00:07:44','2025-04-18 08:07:44'),(41,6,5967.00,'alipay','TIMEOUT','2025-04-18 00:08:28','2025-04-18 08:08:28'),(42,6,5967.00,'alipay','TIMEOUT','2025-04-18 00:08:41','2025-04-18 08:08:41'),(43,6,5967.00,'alipay','TIMEOUT','2025-04-18 00:37:25','2025-04-18 08:37:25'),(44,6,5967.00,'alipay','TIMEOUT','2025-04-18 00:49:06','2025-04-18 08:49:06'),(45,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:06:59','2025-04-18 09:06:59'),(46,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:09:44','2025-04-18 09:09:44'),(47,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:10:16','2025-04-18 09:10:16'),(48,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:12:16','2025-04-18 09:12:16'),(49,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:14:40','2025-04-18 09:14:40'),(50,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:20:40','2025-04-18 09:20:40'),(51,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:22:23','2025-04-18 09:22:23'),(52,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:23:36','2025-04-18 09:23:36'),(53,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:25:30','2025-04-18 09:25:30'),(54,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:25:58','2025-04-18 09:25:58'),(55,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:28:23','2025-04-18 09:28:23'),(56,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:35:47','2025-04-18 09:35:47'),(57,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:36:03','2025-04-18 09:36:03'),(58,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:39:34','2025-04-18 09:39:34'),(59,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:40:12','2025-04-18 09:40:12'),(60,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:40:28','2025-04-18 09:40:28'),(61,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:43:55','2025-04-18 09:43:55'),(62,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:45:08','2025-04-18 09:45:08'),(63,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:45:34','2025-04-18 09:45:34'),(64,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:45:43','2025-04-18 09:45:43'),(65,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:46:23','2025-04-18 09:46:23'),(66,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:46:38','2025-04-18 09:46:38'),(67,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:47:21','2025-04-18 09:47:21'),(68,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:48:12','2025-04-18 09:48:12'),(69,6,5967.00,'alipay','TIMEOUT','2025-04-18 01:53:24','2025-04-18 09:53:24'),(70,6,5967.00,'alipay','TIMEOUT','2025-04-18 02:03:27','2025-04-18 10:03:27'),(71,6,5967.00,'wechat','TIMEOUT','2025-04-18 04:44:26','2025-04-18 12:44:26'),(72,6,5967.00,'alipay','TIMEOUT','2025-04-18 04:49:29','2025-04-18 12:49:29'),(73,6,5967.00,'alipay','TIMEOUT','2025-04-18 05:15:23','2025-04-18 13:15:23'),(74,6,5967.00,'alipay','TIMEOUT','2025-04-18 05:19:54','2025-04-18 13:19:54'),(75,6,5967.00,'alipay','TIMEOUT','2025-04-18 05:22:49','2025-04-18 13:22:49'),(76,6,5967.00,'alipay','TIMEOUT','2025-04-18 05:23:54','2025-04-18 13:23:54'),(77,6,5967.00,'alipay','TIMEOUT','2025-04-18 05:49:23','2025-04-18 13:49:23'),(78,6,5967.00,'alipay','TIMEOUT','2025-04-18 06:17:43','2025-04-18 14:17:43'),(79,6,5967.00,'alipay','TIMEOUT','2025-04-18 07:27:28','2025-04-18 15:27:28'),(80,6,5967.00,'alipay','TIMEOUT','2025-04-18 07:36:38','2025-04-18 15:36:38'),(81,6,5967.00,'alipay','TIMEOUT','2025-04-18 07:37:38','2025-04-18 15:37:38'),(82,6,5967.00,'alipay','TIMEOUT','2025-04-18 18:50:18','2025-04-19 02:50:18'),(83,6,5967.00,'alipay','TIMEOUT','2025-04-18 18:58:42','2025-04-19 02:58:42'),(84,6,5967.00,'alipay','TIMEOUT','2025-04-18 19:02:05','2025-04-19 03:02:05'),(85,6,5967.00,'alipay','TIMEOUT','2025-04-18 19:04:16','2025-04-19 03:04:16'),(86,6,5967.00,'alipay','TIMEOUT','2025-04-18 19:09:58','2025-04-19 03:09:58'),(87,6,5967.00,'alipay','TIMEOUT','2025-04-18 19:12:13','2025-04-19 03:12:13'),(88,6,5967.00,'alipay','TIMEOUT','2025-04-18 19:14:16','2025-04-19 03:14:16'),(89,6,5967.00,'alipay','TIMEOUT','2025-04-18 19:14:43','2025-04-19 03:14:43'),(90,6,5967.00,'alipay','TIMEOUT','2025-04-18 19:25:53','2025-04-19 03:25:53'),(91,6,5967.00,'alipay','TIMEOUT','2025-04-18 19:28:25','2025-04-19 03:28:25'),(92,6,5967.00,'alipay','TIMEOUT','2025-04-18 19:31:27','2025-04-19 03:31:27'),(93,6,5967.00,'alipay','TIMEOUT','2025-04-18 19:34:05','2025-04-19 03:34:05'),(94,6,5967.00,'alipay','TIMEOUT','2025-04-18 20:29:37','2025-04-19 04:29:37'),(95,6,5967.00,'alipay','TIMEOUT','2025-04-18 20:32:46','2025-04-19 04:32:46'),(96,6,5967.00,'alipay','TIMEOUT','2025-04-18 20:34:35','2025-04-19 04:34:35'),(97,6,5967.00,'alipay','TIMEOUT','2025-04-18 20:36:17','2025-04-19 04:36:17'),(98,6,5967.00,'alipay','TIMEOUT','2025-04-18 20:38:49','2025-04-19 04:38:49'),(99,6,5967.00,'alipay','TIMEOUT','2025-04-18 20:44:37','2025-04-19 04:44:37'),(100,6,5967.00,'alipay','TIMEOUT','2025-04-18 20:47:02','2025-04-19 04:47:02'),(101,6,5967.00,'alipay','TIMEOUT','2025-04-18 20:49:20','2025-04-19 04:49:20'),(102,6,5967.00,'alipay','TIMEOUT','2025-04-18 21:20:44','2025-04-19 05:20:44'),(103,6,5967.00,'alipay','TIMEOUT','2025-04-18 21:21:53','2025-04-19 05:21:53'),(104,6,5967.00,'alipay','TIMEOUT','2025-04-18 21:23:36','2025-04-19 05:23:36'),(105,6,5967.00,'alipay','TIMEOUT','2025-04-18 21:25:42','2025-04-19 05:25:42'),(106,6,5967.00,'alipay','TIMEOUT','2025-04-18 21:30:24','2025-04-19 05:30:24'),(107,6,5967.00,'alipay','TIMEOUT','2025-04-18 21:32:47','2025-04-19 05:32:47'),(108,6,5967.00,'alipay','TIMEOUT','2025-04-18 21:34:05','2025-04-19 05:34:05'),(109,6,5967.00,'alipay','TIMEOUT','2025-04-18 21:40:34','2025-04-19 05:40:34'),(110,6,5967.00,'alipay','TIMEOUT','2025-04-18 21:52:45','2025-04-19 05:52:45'),(111,6,5967.00,'alipay','TIMEOUT','2025-04-18 21:55:18','2025-04-19 05:55:18'),(112,6,5967.00,'alipay','TIMEOUT','2025-04-18 21:56:53','2025-04-19 05:56:53'),(113,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:13:41','2025-04-19 06:13:41'),(114,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:15:36','2025-04-19 06:15:36'),(115,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:18:40','2025-04-19 06:18:40'),(116,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:20:39','2025-04-19 06:20:39'),(117,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:26:57','2025-04-19 06:26:57'),(118,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:27:58','2025-04-19 06:27:58'),(119,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:31:45','2025-04-19 06:31:45'),(120,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:35:57','2025-04-19 06:35:57'),(121,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:38:08','2025-04-19 06:38:08'),(122,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:39:16','2025-04-19 06:39:16'),(123,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:41:16','2025-04-19 06:41:16'),(124,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:45:20','2025-04-19 06:45:20'),(125,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:48:24','2025-04-19 06:48:24'),(126,6,5967.00,'alipay','TIMEOUT','2025-04-18 22:49:17','2025-04-19 06:49:17'),(127,6,5967.00,'alipay','TIMEOUT','2025-04-21 05:57:27','2025-04-21 13:57:27'),(128,6,5967.00,'alipay','TIMEOUT','2025-04-21 05:57:52','2025-04-21 13:57:52'),(129,6,5967.00,'alipay','PAID','2025-04-21 06:00:34','2025-04-21 14:00:34'),(130,6,5967.00,'alipay','PAID','2025-04-21 06:05:17','2025-04-21 14:05:17'),(131,6,5967.00,'alipay','PAID','2025-04-21 06:09:51','2025-04-21 14:09:51'),(132,11,3054.00,'alipay','TIMEOUT','2025-04-26 23:41:59','2025-04-27 07:41:59'),(133,11,3054.00,'alipay','TIMEOUT','2025-04-26 23:42:07','2025-04-27 07:42:07'),(134,11,3054.00,'alipay','TIMEOUT','2025-04-26 23:44:09','2025-04-27 07:44:09'),(135,11,3054.00,'alipay','PAID','2025-04-26 23:45:34','2025-04-27 07:45:34'),(136,11,3054.00,'alipay','TIMEOUT','2025-04-27 00:07:15','2025-04-27 08:07:15'),(137,11,3054.00,'alipay','TIMEOUT','2025-04-27 02:29:26','2025-04-27 10:29:26'),(138,11,25.00,'online','PAID','2025-05-17 19:44:51','2025-05-18 03:44:51'),(139,11,25.00,'online','PAID','2025-05-17 19:45:02','2025-05-18 03:45:02'),(140,11,25.00,'alipay','PAID','2025-05-17 19:47:32','2025-05-18 03:47:32'),(141,11,25.00,'alipay','PAID','2025-05-17 19:47:40','2025-05-18 03:47:40'),(142,11,25.00,'wechat','PAID','2025-05-17 19:56:55','2025-05-18 03:56:55'),(143,11,25.00,'alipay','PAID','2025-05-17 19:57:18','2025-05-18 03:57:18'),(144,11,40.00,'wechat','PAID','2025-05-17 19:58:16','2025-05-18 03:58:16'),(145,11,40.00,'alipay','PAID','2025-05-17 19:58:41','2025-05-18 03:58:41'),(146,11,40.00,'wechat','PAID','2025-05-17 20:00:08','2025-05-18 04:00:08'),(147,11,40.00,'alipay','PAID','2025-05-17 20:01:00','2025-05-18 04:01:00'),(148,11,40.00,'alipay','PAID','2025-05-17 20:01:57','2025-05-18 04:01:57'),(149,11,40.00,'wechat','PAID','2025-05-17 20:10:21','2025-05-18 04:10:21'),(150,11,3054.00,'alipay','TIMEOUT','2025-05-17 20:11:56','2025-05-18 04:11:56'),(151,11,3054.00,'alipay','TIMEOUT','2025-05-17 20:12:02','2025-05-18 04:12:02'),(152,11,3054.00,'alipay','TIMEOUT','2025-05-17 20:12:06','2025-05-18 04:12:06'),(153,11,3054.00,'alipay','TIMEOUT','2025-05-17 20:12:12','2025-05-18 04:12:12'),(154,11,3054.00,'alipay','TIMEOUT','2025-05-17 20:12:18','2025-05-18 04:12:18'),(155,11,3054.00,'alipay','TIMEOUT','2025-05-17 20:22:11','2025-05-18 04:22:11'),(156,11,3054.00,'alipay','TIMEOUT','2025-05-17 20:22:18','2025-05-18 04:22:18'),(157,11,3014.00,'alipay','TIMEOUT','2025-05-17 22:29:09','2025-05-18 06:29:09'),(158,11,100.00,'Alipay','PAID','2025-05-20 21:20:14','2025-05-21 05:20:14'),(159,11,400.00,'Alipay','PAID','2025-05-20 21:21:11','2025-05-21 05:21:11'),(160,11,100.00,'Alipay','PAID','2025-05-20 21:24:23','2025-05-21 05:24:23'),(161,11,100.00,'Alipay','PAID','2025-05-20 21:24:36','2025-05-21 05:24:36'),(162,11,100.00,'Alipay','PAID','2025-05-20 21:26:18','2025-05-21 05:26:18'),(163,11,0.00,'钱包支付','PAID','2025-05-20 21:26:19','2025-05-21 05:26:19'),(164,11,100.00,'Alipay','PAID','2025-05-20 21:26:27','2025-05-21 05:26:27'),(165,11,0.00,'钱包支付','PAID','2025-05-20 21:26:28','2025-05-21 05:26:28'),(166,11,0.00,'钱包支付','PAID','2025-05-20 21:28:39','2025-05-21 05:28:39'),(167,11,0.00,'钱包支付','PAID','2025-05-20 21:28:41','2025-05-21 05:28:41'),(168,11,100.00,'Alipay','PAID','2025-05-20 21:28:45','2025-05-21 05:28:45'),(169,11,0.00,'钱包支付','PAID','2025-05-20 21:28:46','2025-05-21 05:28:46'),(170,11,100.00,'Alipay','PAID','2025-05-20 21:29:15','2025-05-21 05:29:15'),(171,11,0.00,'钱包支付','PAID','2025-05-20 21:29:16','2025-05-21 05:29:16'),(172,11,100.00,'Alipay','PAID','2025-05-20 21:29:39','2025-05-21 05:29:39'),(173,11,0.00,'钱包支付','PAID','2025-05-20 21:29:40','2025-05-21 05:29:40'),(174,11,100.00,'Alipay','PAID','2025-05-20 21:33:57','2025-05-21 05:33:57'),(175,11,0.00,'钱包支付','PAID','2025-05-20 21:33:59','2025-05-21 05:33:59'),(176,11,4752.00,'wallet','PENDING','2025-05-20 22:12:53','2025-05-21 06:12:53'),(177,11,4752.00,'wallet','PENDING','2025-05-20 22:13:05','2025-05-21 06:13:05'),(178,11,4752.00,'wallet','PENDING','2025-05-20 22:14:38','2025-05-21 06:14:38'),(179,11,4752.00,'wallet','PENDING','2025-05-20 22:16:28','2025-05-21 06:16:28');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `price` decimal(20,2) NOT NULL,
  `rate` float NOT NULL,
  `description` text,
  `cover` varchar(255) DEFAULT NULL,
  `detail` text,
  `sales` int DEFAULT '0',
  `rate_list` varchar(255) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (31,'西游记',698.00,4,'西游','http://test-njshop.oss-cn-nanjing.aliyuncs.com/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20250520193150.png','nothing',1,'[]','都市','吴承恩'),(32,'红楼梦',648.00,5,'作者：曹雪芹','http://test-njshop.oss-cn-nanjing.aliyuncs.com/%E7%BA%A2%E6%A5%BC.png','nothing',10,NULL,NULL,NULL),(33,'C Primer Plus',188.00,0,'0','http://test-njshop.oss-cn-nanjing.aliyuncs.com/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20250427130724.png','暂无',15,NULL,NULL,NULL),(34,'计算机组成：结构化方法（原书第6版）',648.00,5,'一本还可以的书','http://test-njshop.oss-cn-nanjing.aliyuncs.com/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20250427130900.png','还行',5,NULL,NULL,NULL),(35,'计算机组成原理',548.00,5,'详细介绍了计算机的组成原理','http://test-njshop.oss-cn-nanjing.aliyuncs.com/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20250427131021.png','18岁婴儿程序员必读书目',6,NULL,NULL,NULL);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `specification`
--

DROP TABLE IF EXISTS `specification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `specification` (
  `id` int NOT NULL AUTO_INCREMENT,
  `item` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `productId` int NOT NULL,
  `product_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`productId`),
  KEY `FKgjslnwgmkr6xtl6rwfpwl8hm5` (`product_id`),
  CONSTRAINT `FKgjslnwgmkr6xtl6rwfpwl8hm5` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `specification_ibfk_1` FOREIGN KEY (`productId`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `specification`
--

LOCK TABLES `specification` WRITE;
/*!40000 ALTER TABLE `specification` DISABLE KEYS */;
/*!40000 ALTER TABLE `specification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stockpile`
--

DROP TABLE IF EXISTS `stockpile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stockpile` (
  `id` int NOT NULL AUTO_INCREMENT,
  `amount` int NOT NULL,
  `frozen` int NOT NULL,
  `product_id` int DEFAULT NULL,
  `lock_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm7ls1s5irhxxw8juw4gmeg6iy` (`product_id`),
  CONSTRAINT `FKm7ls1s5irhxxw8juw4gmeg6iy` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stockpile`
--

LOCK TABLES `stockpile` WRITE;
/*!40000 ALTER TABLE `stockpile` DISABLE KEYS */;
INSERT INTO `stockpile` VALUES (22,71,24,31,'2025-05-21 06:16:28'),(23,85,12,33,'2025-05-21 06:16:28');
/*!40000 ALTER TABLE `stockpile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `isVIP` int DEFAULT '0',
  `wallet` double DEFAULT '0',
  `point` double DEFAULT '0',
  `collect` text,
  `like_author` text,
  `like_tag` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (6,'','1421342050@qq.com','','xie','$2a$10$uXqb0a8HH/1miGJnbHLfLekWAJRKuAV3OcsAJxGIWLoKBmL81sJfW','user','13255300389','xie',NULL,0,0,0,NULL,NULL,NULL),(7,NULL,'1421342050@qq.com','汉口路','kira','123456','admin','13255300389','kira',NULL,0,0,0,NULL,NULL,NULL),(11,'http://test-njshop.oss-cn-nanjing.aliyuncs.com/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20250426154238.png','1421342050@qq.com','仙林区','谢','$2a$10$/5BqbeGUH0e.WeEx0s4Z8OzrAfqiJ36BSz7Ghq9zBEZ4rGGQQYK76','admin','13255300389','Lewis',NULL,1,3600,14500,'[31,33,32,34,35]','null','null');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-21 14:40:05
