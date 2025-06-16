-- MySQL dump 10.13  Distrib 8.0.42, for Linux (x86_64)
--
-- Host: localhost    Database: Hospital
-- ------------------------------------------------------
-- Server version	8.0.42-0ubuntu0.24.04.1

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
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admins` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `surname` varchar(100) NOT NULL,
  `salt` varchar(100) NOT NULL,
  `hash` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
INSERT INTO `admins` VALUES (2,'admin',111,'STELIOS','SDOUGKOS','6TnqGqPqxcBgh6wtjkZJyA==','To4IRyonfu1iMr7SzDtjfUjUSVydU79Ppuiw8aWfoTY=');
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments` (
  `patient_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `date` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `appointments0`
--

DROP TABLE IF EXISTS `appointments0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointments0` (
  `id` int NOT NULL AUTO_INCREMENT,
  `patient_username` varchar(50) DEFAULT NULL,
  `doctor_username` varchar(50) DEFAULT NULL,
  `day` int DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `patient_username` (`patient_username`),
  KEY `doctor_username` (`doctor_username`),
  CONSTRAINT `appointments0_ibfk_1` FOREIGN KEY (`patient_username`) REFERENCES `patients` (`username`) ON DELETE CASCADE,
  CONSTRAINT `appointments0_ibfk_2` FOREIGN KEY (`doctor_username`) REFERENCES `doctors` (`username`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments0`
--

LOCK TABLES `appointments0` WRITE;
/*!40000 ALTER TABLE `appointments0` DISABLE KEYS */;
INSERT INTO `appointments0` VALUES (5,'teri','pkele',12,1),(6,'teri','mgrig',18,1),(7,'teri','panef',16,1);
/*!40000 ALTER TABLE `appointments0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `availableDays`
--

DROP TABLE IF EXISTS `availableDays`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `availableDays` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `availableDay` tinyint NOT NULL,
  `available` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `username` (`username`),
  CONSTRAINT `availableDays_ibfk_1` FOREIGN KEY (`username`) REFERENCES `doctors` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `availableDays_chk_1` CHECK ((`availableDay` between 1 and 31))
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `availableDays`
--

LOCK TABLES `availableDays` WRITE;
/*!40000 ALTER TABLE `availableDays` DISABLE KEYS */;
INSERT INTO `availableDays` VALUES (1,'pkele',9,1),(2,'pkele',12,0),(3,'pkele',17,1),(4,'pkele',19,1),(5,'pkele',25,1),(6,'mgrig',1,1),(7,'mgrig',12,0),(8,'mgrig',15,1),(9,'mgrig',18,0),(10,'mgrig',31,1),(17,'pantr',11,1),(18,'panef',15,1),(19,'panef',16,0),(20,'panef',17,1),(21,'panef',18,1),(22,'panef',19,1),(23,'panef',20,1),(24,'panef',21,1);
/*!40000 ALTER TABLE `availableDays` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctors`
--

DROP TABLE IF EXISTS `doctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `surname` varchar(100) NOT NULL,
  `speciality` varchar(100) NOT NULL,
  `salt` varchar(100) DEFAULT NULL,
  `hash` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES (3,'pkele',456,'PANAGIOTIS','KELESIDIS','DENTIST','A6ncMkP/I6nPiF4jGNxc1g==','8sd4pqFe63SMLRCfNVA5gDdpKl7kJPsFi44lfJX65Rw='),(6,'mgrig',789,'MARIA','GRIGORA','GYNECOLOGIST','loXSc3cyjWo0GShDNuonTQ==','fKugfG1+bdA2dp/wrTIX2iyc2LGIlcqqDVx1mhQck6Q='),(14,'pantr',111,'TRAPEZONTAS','PANOS','SURGEON','QmyVqhme4z5KgmwxQO13IQ==','Ee0oE5QCB/D9T9gnOaTz2Gd2GDF1hkJsiHT44lXU+/g='),(15,'panef',111,'PANOS','EF','NEUROLOGIST','XfFNiitVBWf2EHK7dZ+ZrA==','Egnj/I5Nj2myVyUn5sb8JsUT3VemNp73PieEtNN00b0=');
/*!40000 ALTER TABLE `doctors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patients` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` int NOT NULL,
  `name` varchar(100) NOT NULL,
  `surname` varchar(100) NOT NULL,
  `AMKA` int NOT NULL,
  `salt` varchar(100) DEFAULT NULL,
  `hash` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `AMKA` (`AMKA`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (7,'teri',123,'TEREZA','GRIGORASKOU',250804,'XA+Z0gg2cYHgrIN2SZXTkQ==','uN51MKd/IHCtBO2icu6A1no36G0A4OFEX7xi4eMArzc=');
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-16 22:42:03
