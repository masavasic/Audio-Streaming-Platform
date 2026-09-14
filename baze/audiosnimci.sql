CREATE DATABASE  IF NOT EXISTS `audiosnimci` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `audiosnimci`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: audiosnimci
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `audiosnimak`
--

DROP TABLE IF EXISTS `audiosnimak`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audiosnimak` (
  `IdAudSni` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `Trajanje` time NOT NULL,
  `DatumVreme` datetime NOT NULL,
  `IdVlasnika` int NOT NULL,
  PRIMARY KEY (`IdAudSni`),
  KEY `audiosnikad_idVla_idx` (`IdVlasnika`),
  CONSTRAINT `audiosnikad_idVla` FOREIGN KEY (`IdVlasnika`) REFERENCES `korisnik` (`IdKor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audiosnimak`
--

LOCK TABLES `audiosnimak` WRITE;
/*!40000 ALTER TABLE `audiosnimak` DISABLE KEYS */;
INSERT INTO `audiosnimak` VALUES (1,'audio1','00:25:00','2024-10-10 00:00:10',1),(2,'audio2','00:50:00','2024-10-15 10:39:00',1),(3,'audio3','00:10:10','2024-10-25 09:00:10',9),(4,'audio4','00:03:00','2024-12-12 00:50:00',8),(5,'audio5','00:05:55','2024-09-01 08:50:00',9),(6,'audio6','00:23:23','2024-08-06 11:46:00',5),(7,'audio7','00:56:00','2024-11-03 13:34:00',3),(8,'audio8','01:10:00','2024-02-05 15:44:00',2),(9,'audio9','00:45:00','2024-09-09 16:16:00',6),(10,'audio10','00:10:00','2024-10-10 10:10:00',7),(11,'audio11','00:30:00','2024-10-11 00:00:00',2),(12,'audio12','00:30:00','2024-12-26 21:00:00',2),(13,'audio13','00:46:00','2024-10-10 00:00:30',7),(14,'audio14','00:40:00','2024-10-20 00:20:00',6);
/*!40000 ALTER TABLE `audiosnimak` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kategorija`
--

DROP TABLE IF EXISTS `kategorija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kategorija` (
  `IdKat` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`IdKat`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kategorija`
--

LOCK TABLES `kategorija` WRITE;
/*!40000 ALTER TABLE `kategorija` DISABLE KEYS */;
INSERT INTO `kategorija` VALUES (1,'kategorija1'),(2,'kategorija2'),(3,'kategorija3'),(4,'kategorija4'),(5,'kategorija5'),(6,'kategorija6'),(7,'kategorija7'),(8,'kategorija8'),(9,'kategorija9'),(10,'kategorija10'),(11,'kategorija11\n'),(12,'kategorija12'),(13,'kategorija13'),(14,'kategorija14'),(15,'kategorija15'),(16,'kategorija16'),(17,'kategorija17'),(18,'kategorija18');
/*!40000 ALTER TABLE `kategorija` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `korisnik`
--

DROP TABLE IF EXISTS `korisnik`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `korisnik` (
  `IdKor` int NOT NULL AUTO_INCREMENT,
  `Ime` varchar(45) NOT NULL,
  `Email` varchar(45) NOT NULL,
  `Godiste` int NOT NULL,
  `Pol` enum('M','Z') NOT NULL,
  `IdMes` int NOT NULL,
  PRIMARY KEY (`IdKor`),
  KEY `korisnik_idMes_idx` (`IdMes`),
  CONSTRAINT `korisnik_idMes` FOREIGN KEY (`IdMes`) REFERENCES `mesto` (`IdMes`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `korisnik`
--

LOCK TABLES `korisnik` WRITE;
/*!40000 ALTER TABLE `korisnik` DISABLE KEYS */;
INSERT INTO `korisnik` VALUES (1,'Masa','masa@gmail.com',2003,'Z',5),(2,'Tijana','tijana@gmail.com',2000,'Z',3),(3,'Nemanja','nemanja@gmail.com',1999,'M',6),(4,'Lucija','lucija@gmail.com',2005,'Z',4),(5,'Nikola','nikola@gmail.com',2000,'M',2),(6,'Marija','marija@gmail.com',2003,'Z',9),(7,'Milos','milos@gmail.com',2004,'M',5),(8,'Lana','lana@gmail.com',2001,'Z',1),(9,'Teodora','teodora@gmail.com',2005,'Z',3),(10,'Teodor','teodor15@gmail.com\n',2004,'M',10);
/*!40000 ALTER TABLE `korisnik` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mesto`
--

DROP TABLE IF EXISTS `mesto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mesto` (
  `IdMes` int NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`IdMes`),
  UNIQUE KEY `Naziv_UNIQUE` (`Naziv`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mesto`
--

LOCK TABLES `mesto` WRITE;
/*!40000 ALTER TABLE `mesto` DISABLE KEYS */;
INSERT INTO `mesto` VALUES (28,'Banja Luka'),(27,'Bar'),(19,'Bec'),(1,'Beograd'),(15,'Beograd\n'),(21,'Bern'),(20,'Budimpesta'),(3,'Gnjilane'),(16,'kikinda'),(6,'Kragujevac'),(9,'Kraljevo'),(10,'Krusevac'),(8,'Leskovac'),(23,'Madrid'),(22,'Milanovac'),(2,'Nis'),(4,'Novi Sad'),(11,'Oslo'),(26,'Podgorica'),(24,'Prizren'),(25,'Skoplje'),(18,'Sombor'),(5,'Subotica'),(7,'Topola'),(17,'valjevo'),(12,'Vranje');
/*!40000 ALTER TABLE `mesto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ocena`
--

DROP TABLE IF EXISTS `ocena`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ocena` (
  `IdOce` int NOT NULL AUTO_INCREMENT,
  `Ocena` int NOT NULL,
  `DatumVreme` datetime NOT NULL,
  `IdKor` int NOT NULL,
  `IdAudSni` int NOT NULL,
  PRIMARY KEY (`IdOce`),
  KEY `ocena_idKor_idx` (`IdKor`),
  KEY `ocena_idAudSni_idx` (`IdAudSni`),
  CONSTRAINT `ocena_idAudSni` FOREIGN KEY (`IdAudSni`) REFERENCES `audiosnimak` (`IdAudSni`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ocena_idKor` FOREIGN KEY (`IdKor`) REFERENCES `korisnik` (`IdKor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ocena`
--

LOCK TABLES `ocena` WRITE;
/*!40000 ALTER TABLE `ocena` DISABLE KEYS */;
INSERT INTO `ocena` VALUES (2,5,'2024-12-14 13:00:00',2,4),(3,4,'2024-12-05 09:45:00',2,8),(4,2,'2024-12-21 12:00:00',1,9),(6,3,'2024-12-11 14:13:44',6,6);
/*!40000 ALTER TABLE `ocena` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `omiljenisnimak`
--

DROP TABLE IF EXISTS `omiljenisnimak`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `omiljenisnimak` (
  `IdKor` int NOT NULL,
  `IdAudSni` int NOT NULL,
  `IdOmiSni` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`IdOmiSni`),
  KEY `omiljenisnimak_idKor_idx` (`IdKor`),
  KEY `omiljenisnimak_idAudSni_idx` (`IdAudSni`),
  CONSTRAINT `omiljenisnimak_idAudSni` FOREIGN KEY (`IdAudSni`) REFERENCES `audiosnimak` (`IdAudSni`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `omiljenisnimak_idKor` FOREIGN KEY (`IdKor`) REFERENCES `korisnik` (`IdKor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `omiljenisnimak`
--

LOCK TABLES `omiljenisnimak` WRITE;
/*!40000 ALTER TABLE `omiljenisnimak` DISABLE KEYS */;
INSERT INTO `omiljenisnimak` VALUES (1,2,1),(4,6,2),(3,9,3),(7,6,4),(6,5,5),(2,5,6),(2,12,7),(2,5,8),(2,9,9),(1,4,10),(1,2,11);
/*!40000 ALTER TABLE `omiljenisnimak` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paket`
--

DROP TABLE IF EXISTS `paket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paket` (
  `IdPak` int NOT NULL AUTO_INCREMENT,
  `Cena` double NOT NULL,
  `Naziv` varchar(45) NOT NULL,
  PRIMARY KEY (`IdPak`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paket`
--

LOCK TABLES `paket` WRITE;
/*!40000 ALTER TABLE `paket` DISABLE KEYS */;
INSERT INTO `paket` VALUES (1,20,'paket1'),(2,300,'paket2'),(3,450,'paket3'),(4,320,'paket4'),(5,150,'paket5'),(6,1000,'paket6'),(7,1200,'paket7'),(8,550,'paket8'),(9,770,'paket9'),(10,800,'paket10'),(11,500,'paket11\n'),(12,300,'paket12'),(13,300,'paket13'),(14,30,'paket14'),(15,900,'paket15');
/*!40000 ALTER TABLE `paket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pretplata`
--

DROP TABLE IF EXISTS `pretplata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pretplata` (
  `IdPre` int NOT NULL AUTO_INCREMENT,
  `DatumVreme` datetime NOT NULL,
  `Cena` double NOT NULL,
  `IdKor` int NOT NULL,
  `IdPak` int NOT NULL,
  PRIMARY KEY (`IdPre`),
  KEY `pretplata_idKor_idx` (`IdKor`),
  KEY `pretplata_idPak_idx` (`IdPak`),
  CONSTRAINT `pretplata_idKor` FOREIGN KEY (`IdKor`) REFERENCES `korisnik` (`IdKor`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `pretplata_idPak` FOREIGN KEY (`IdPak`) REFERENCES `paket` (`IdPak`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pretplata`
--

LOCK TABLES `pretplata` WRITE;
/*!40000 ALTER TABLE `pretplata` DISABLE KEYS */;
INSERT INTO `pretplata` VALUES (1,'2024-12-03 08:00:00',300,1,3),(2,'2024-12-04 09:00:55',450,2,5),(3,'2024-12-10 10:05:00',330,4,4),(4,'2024-12-09 14:00:30',200,5,9),(5,'2024-12-08 15:30:00',1300,7,6),(6,'2024-12-09 16:00:40',880,1,1),(7,'2024-12-01 11:33:00',300,2,3),(8,'2024-12-16 12:00:03',400,4,7),(9,'2024-12-17 14:40:00',330,5,4),(10,'2024-12-10 10:03:00',1300,8,6),(11,'2024-10-10 00:00:00',200,1,1),(12,'2024-10-24 09:45:00',500,4,6),(13,'2024-10-10 00:00:00',500,2,13),(14,'2024-10-10 00:00:00',500,2,12),(15,'2024-09-10 00:00:00',200,1,12),(16,'2021-10-10 00:00:00',300,2,2),(17,'2024-10-10 00:00:00',300,1,13),(18,'2024-10-10 00:00:00',450,2,2),(19,'2024-10-01 10:00:00',200,1,15);
/*!40000 ALTER TABLE `pretplata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pripada`
--

DROP TABLE IF EXISTS `pripada`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pripada` (
  `IdAudSni` int NOT NULL,
  `IdKat` int NOT NULL,
  `IdPri` int NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`IdPri`),
  KEY `pripada_idAudSni_idx` (`IdAudSni`),
  KEY `pripada_idKat_idx` (`IdKat`),
  CONSTRAINT `pripada_idAudSni` FOREIGN KEY (`IdAudSni`) REFERENCES `audiosnimak` (`IdAudSni`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `pripada_idKat` FOREIGN KEY (`IdKat`) REFERENCES `kategorija` (`IdKat`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pripada`
--

LOCK TABLES `pripada` WRITE;
/*!40000 ALTER TABLE `pripada` DISABLE KEYS */;
INSERT INTO `pripada` VALUES (2,5,2),(4,8,5),(4,9,6),(5,7,7),(6,6,8),(6,7,9),(7,3,10),(8,4,11),(9,9,12),(10,1,13),(5,6,14),(4,14,15),(14,14,16);
/*!40000 ALTER TABLE `pripada` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slusanje`
--

DROP TABLE IF EXISTS `slusanje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `slusanje` (
  `IdSlu` int NOT NULL AUTO_INCREMENT,
  `DatumVreme` datetime NOT NULL,
  `SekundZapoceto` int NOT NULL,
  `SekundOdslusano` int NOT NULL,
  `IdKor` int NOT NULL,
  `IdAudSni` int NOT NULL,
  PRIMARY KEY (`IdSlu`),
  KEY `slusanje_idAudSni_idx` (`IdAudSni`),
  KEY `slusanje_idKor_idx` (`IdKor`),
  CONSTRAINT `slusanje_idAudSni` FOREIGN KEY (`IdAudSni`) REFERENCES `audiosnimak` (`IdAudSni`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `slusanje_idKor` FOREIGN KEY (`IdKor`) REFERENCES `korisnik` (`IdKor`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slusanje`
--

LOCK TABLES `slusanje` WRITE;
/*!40000 ALTER TABLE `slusanje` DISABLE KEYS */;
INSERT INTO `slusanje` VALUES (2,'2024-12-13 00:00:10',0,30,2,4),(3,'2024-12-03 12:05:10',15,80,2,8),(4,'2024-12-04 15:47:05',8,20,4,5),(5,'2024-12-10 16:03:55',9,55,6,6),(6,'2024-12-05 04:55:00',0,43,7,2),(8,'2024-12-20 05:02:00',0,20,1,9),(10,'2024-12-14 17:45:00',0,63,9,7),(11,'2024-10-10 00:00:00',4,35,1,4),(12,'2024-10-10 09:09:09',0,30,2,13),(13,'2024-10-24 00:00:00',2,45,2,5),(14,'2024-10-10 00:20:20',8,36,2,9),(15,'2024-10-10 00:20:20',8,36,2,9),(16,'2014-10-10 00:00:00',0,30,4,13);
/*!40000 ALTER TABLE `slusanje` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-14 22:28:45
