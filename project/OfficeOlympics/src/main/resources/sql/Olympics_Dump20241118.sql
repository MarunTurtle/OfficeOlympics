CREATE DATABASE  IF NOT EXISTS `olympic_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `olympic_db`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: olympic_db
-- ------------------------------------------------------
-- Server version	8.0.39

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
-- Table structure for table `challenge_scores`
--

DROP TABLE IF EXISTS `challenge_scores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `challenge_scores` (
  `score_id` int NOT NULL AUTO_INCREMENT,
  `challenge_id` int DEFAULT NULL,
  `player_id` int DEFAULT NULL,
  `score` int NOT NULL,
  `reg_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`score_id`),
  KEY `challenge_id` (`challenge_id`),
  KEY `player_id` (`player_id`),
  CONSTRAINT `challenge_scores_ibfk_1` FOREIGN KEY (`challenge_id`) REFERENCES `challenges` (`challenge_id`) ON DELETE CASCADE,
  CONSTRAINT `challenge_scores_ibfk_2` FOREIGN KEY (`player_id`) REFERENCES `players` (`player_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `challenge_scores`
--

LOCK TABLES `challenge_scores` WRITE;
/*!40000 ALTER TABLE `challenge_scores` DISABLE KEYS */;
INSERT INTO `challenge_scores` VALUES (1,1,1,1000,'2024-11-18 11:21:32'),(2,1,2,2000,'2024-11-18 11:21:32'),(3,1,3,3000,'2024-11-18 11:21:32'),(4,2,1,2000,'2024-11-18 11:22:11'),(5,2,2,3000,'2024-11-18 11:22:11'),(6,2,3,4000,'2024-11-18 11:22:11'),(7,3,1,8000,'2024-11-18 11:22:21'),(8,3,2,7000,'2024-11-18 11:22:21'),(9,3,3,6000,'2024-11-18 11:22:21'),(10,1,4,40,'2024-11-18 11:40:34'),(11,2,4,38,'2024-11-18 11:40:34'),(12,3,4,35,'2024-11-18 11:40:34'),(13,1,5,37,'2024-11-18 11:40:34'),(14,2,5,35,'2024-11-18 11:40:34'),(15,3,5,33,'2024-11-18 11:40:34'),(16,1,6,30,'2024-11-18 11:40:34'),(17,2,6,33,'2024-11-18 11:40:34'),(18,3,6,32,'2024-11-18 11:40:34'),(19,1,7,45,'2024-11-18 11:40:34'),(20,2,7,42,'2024-11-18 11:40:34'),(21,3,7,40,'2024-11-18 11:40:34'),(22,1,8,40,'2024-11-18 11:40:34'),(23,2,8,37,'2024-11-18 11:40:34'),(24,3,8,38,'2024-11-18 11:40:34'),(25,1,9,35,'2024-11-18 11:40:34'),(26,2,9,32,'2024-11-18 11:40:34'),(27,3,9,33,'2024-11-18 11:40:34');
/*!40000 ALTER TABLE `challenge_scores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `challenges`
--

DROP TABLE IF EXISTS `challenges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `challenges` (
  `challenge_id` int NOT NULL AUTO_INCREMENT,
  `challenge_name` varchar(255) NOT NULL,
  `challenge_desc` text,
  `challenge_url` text,
  `reg_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`challenge_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `challenges`
--

LOCK TABLES `challenges` WRITE;
/*!40000 ALTER TABLE `challenges` DISABLE KEYS */;
INSERT INTO `challenges` VALUES (1,'스테이플러 던지기','참가자들은 스테이플러를 책상 끝으로 던져 가장 근접하게 도달한 사람이 승리합니다. 각 참가자는 3번의 기회를 가지며, 가장 좋은 점수만 기록됩니다. 안전을 위해 반드시 빈 스테이플러를 사용하세요.','https://youtu.be/dQw4w9WgXcQ','2024-11-18 11:19:41'),(2,'포스트잇 얼굴 붙이기','60초 동안 얼굴에 최대한 많은 포스트잇을 붙이는 게임입니다. 포스트잇은 반드시 한 면만 접착되어야 하며, 떨어진 포스트잇은 점수에서 제외됩니다. 가장 많은 포스트잇을 붙인 사람이 승리합니다.','https://youtu.be/a3Z7zEc7AXQ','2024-11-18 11:19:41'),(3,'서류 더미 쌓기','참가자들은 주어진 시간 동안 책상 위에 서류를 최대한 높이 쌓아야 합니다. 서류 더미가 무너지면 해당 라운드의 점수는 0점 처리됩니다. 안정성과 높이를 동시에 고려해야 합니다.','https://youtu.be/fzMJYPYVTlQ','2024-11-18 11:19:41'),(4,'의자 레이싱','사무실 의자를 타고 정해진 코스를 완주하는 속도를 겨루는 게임입니다. 손으로 바닥을 밀거나 다리로 추진력을 이용할 수 있습니다. 코스에서 벗어나면 5초 패널티가 적용됩니다.','https://youtu.be/hQ4pHp0nm50','2024-11-18 11:19:41'),(5,'클립 체인 만들기','5분 안에 클립을 연결하여 가장 긴 체인을 만들어야 합니다. 연결이 단단하지 않거나 체인이 끊어지면 점수에서 제외됩니다. 체인의 길이는 끝부터 끝까지 측정합니다.','https://youtu.be/CevxZvSJLk8','2024-11-18 11:19:41'),(6,'병뚜껑 알까기','참가자는 병뚜껑으로 다른 병뚜껑을 튕겨서 뒤집어야 합니다. 각 참가자는 5개의 병뚜껑을 던질 수 있으며, 성공적으로 뒤집은 병뚜껑 수로 점수가 결정됩니다.','https://youtu.be/L_jWHffIx5E','2024-11-18 11:19:41'),(7,'종이비행기 대회','종이비행기를 접어 가장 멀리 날리는 사람이 승리합니다. 각 참가자는 3번의 기회를 가지며, 비행기가 멈춘 지점까지의 거리를 측정합니다. 날리는 방향에 맞춰 장애물(의자, 책상)을 피해야 합니다.','https://youtu.be/EkHTsc9PU2A','2024-11-18 11:19:41'),(8,'사무용품 균형 잡기','책, 마우스, 스테이플러 등 사무용품을 머리 위에 올리고 가장 오래 균형을 유지하는 사람이 승리합니다. 물건이 떨어지거나 손으로 잡으면 실격 처리됩니다.','https://youtu.be/tgbNymZ7vqY','2024-11-18 11:19:41'),(9,'책 던지기','참가자들은 책을 던져 지정된 목표에 가장 근접하게 도달해야 합니다. 각 참가자는 3번의 기회를 가지며, 책이 목표에서 벗어날 경우 가장 가까운 점수를 기록합니다. 반드시 가벼운 책만 사용하세요.','https://youtu.be/9bZkp7q19f0','2024-11-18 11:19:41'),(10,'미니 농구','작은 공이나 동그란 물체를 사용해 쓰레기통에 가장 많이 넣는 게임입니다. 각 참가자는 60초 동안 최대한 많은 성공을 기록해야 합니다. 쓰레기통이 넘어가면 해당 라운드는 무효 처리됩니다.','https://youtu.be/3JWTaaS7LdU','2024-11-18 11:19:41'),(11,'의자 멀리 밀기','참가자는 의자에 앉아 벽을 발로 밀어 최대한 멀리 나아가야 합니다. 각 참가자는 3번의 시도를 할 수 있으며, 가장 먼 기록만 채택됩니다. 의자가 코스를 벗어나거나 뒤집히면 해당 라운드는 무효 처리됩니다.','https://youtu.be/E7A4v7jUeNE','2024-11-18 11:19:41'),(12,'스피드 타이핑','1분 동안 주어진 텍스트를 가장 빠르고 정확하게 타이핑한 사람이 승리합니다. 오타가 있을 경우 단어당 1초의 패널티가 적용됩니다. 속도와 정확도가 중요한 게임입니다.','https://youtu.be/VYOjWnS4cMY','2024-11-18 11:19:41'),(13,'딱지치기','각 참가자는 자신이 접은 딱지로 상대방의 딱지를 뒤집어야 합니다. 각 라운드마다 3번의 시도가 주어지며, 상대 딱지를 뒤집으면 승리입니다. 딱지가 찢어지거나 규칙 외의 방식으로 뒤집을 경우 실격 처리됩니다.','https://youtu.be/TkVyRx2MAYs','2024-11-18 11:19:41'),(14,'종이컵 탑 쌓기','제한된 시간 안에 종이컵을 최대한 높이 쌓는 게임입니다. 종이컵이 무너지면 점수가 0점으로 처리됩니다. 안정성과 속도를 모두 고려해야 합니다.','https://youtu.be/HyHNuVaZJ-k','2024-11-18 11:19:41'),(15,'책상 미니 볼링','작은 공을 굴려 펜이나 클립처럼 세워진 목표물을 쓰러뜨려야 합니다. 각 참가자는 2번씩 3라운드의 기회를 가지며, 쓰러뜨린 개수의 합으로 점수가 결정됩니다.','https://youtu.be/2Vv-BfVoq4g','2024-11-18 11:19:41');
/*!40000 ALTER TABLE `challenges` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `challenge_id` int DEFAULT NULL,
  `comment_text` text NOT NULL,
  `reg_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `user_id` (`user_id`),
  KEY `challenge_id` (`challenge_id`),
  CONSTRAINT `comments_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `comments_ibfk_2` FOREIGN KEY (`challenge_id`) REFERENCES `challenges` (`challenge_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
INSERT INTO `comments` VALUES (2,2,1,'도전정신이 생겼습니다.','2024-11-18 11:42:07',NULL),(3,3,1,'다음엔 더 잘할게요.','2024-11-18 11:42:07',NULL),(4,1,2,'생각보다 쉬웠네요!','2024-11-18 11:42:07',NULL),(5,2,2,'좋은 운동이었어요.','2024-11-18 11:42:07',NULL),(6,3,2,'생각보다 시간이 많이 필요했네요.','2024-11-18 11:42:07',NULL),(7,1,3,'재미있었습니다!','2024-11-18 11:42:07',NULL),(8,2,3,'이 운동은 정말 도전적이었어요.','2024-11-18 11:42:07',NULL),(9,3,3,'다음에도 참가할게요.','2024-11-18 11:42:07',NULL),(10,1,4,'생각보다 어려운 운동이었어요!','2024-11-18 11:42:07',NULL),(11,2,4,'도전할 수 있어서 기뻤어요.','2024-11-18 11:42:07',NULL),(12,3,4,'팀워크가 좋았습니다.','2024-11-18 11:42:07',NULL),(13,1,5,'운동이 부족했어요.','2024-11-18 11:42:07',NULL),(14,2,5,'다음에도 참가할게요.','2024-11-18 11:42:07',NULL),(15,3,5,'최선을 다했어요!','2024-11-18 11:42:07',NULL);
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `olympics`
--

DROP TABLE IF EXISTS `olympics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `olympics` (
  `olympics_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `olympics_name` varchar(255) NOT NULL,
  PRIMARY KEY (`olympics_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `olympics_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `olympics`
--

LOCK TABLES `olympics` WRITE;
/*!40000 ALTER TABLE `olympics` DISABLE KEYS */;
INSERT INTO `olympics` VALUES (1,1,'팀 A 올림픽'),(2,2,'팀 B 올림픽'),(3,3,'팀 C 올림픽');
/*!40000 ALTER TABLE `olympics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `players`
--

DROP TABLE IF EXISTS `players`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `players` (
  `player_id` int NOT NULL AUTO_INCREMENT,
  `olympics_id` int DEFAULT NULL,
  `player_name` varchar(255) NOT NULL,
  `total_score` int DEFAULT '0',
  `reg_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`player_id`),
  KEY `olympics_id` (`olympics_id`),
  CONSTRAINT `players_ibfk_1` FOREIGN KEY (`olympics_id`) REFERENCES `olympics` (`olympics_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `players`
--

LOCK TABLES `players` WRITE;
/*!40000 ALTER TABLE `players` DISABLE KEYS */;
INSERT INTO `players` VALUES (1,1,'플레이어1',11000,'2024-11-18 11:22:21'),(2,1,'플레이어2',12000,'2024-11-18 11:22:21'),(3,1,'플레이어3',13000,'2024-11-18 11:22:21'),(4,2,'플레이어4',11300,'2024-11-18 11:38:37'),(5,2,'플레이어5',10500,'2024-11-18 11:38:37'),(6,2,'플레이어6',9500,'2024-11-18 11:38:37'),(7,3,'플레이어7',12700,'2024-11-18 11:38:37'),(8,3,'플레이어8',11500,'2024-11-18 11:38:37'),(9,3,'플레이어9',10000,'2024-11-18 11:38:37');
/*!40000 ALTER TABLE `players` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(128) NOT NULL,
  `salt` varchar(128) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `profile_img` varchar(255) DEFAULT NULL,
  `img_src` varchar(255) DEFAULT NULL,
  `reg_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'ssafy1@ssafy.com','87e310ef0dd31cb1f3250a608df890a34aafe7d57e2a618aad59c6a8abcfff4b','8iDvseT5+vOm4ONeeixwYA==','사용자1','닉네임1','profile1.png','0ab7bf98-b57f-4594-9773-ed939eecf45f.png','2024-11-18 11:18:37'),(2,'ssafy2@ssafy.com','af16034ba4340164f0e1cc4bb3a12f10ea8331d399c4bef728acb13b6d97ff42','jfTalvGnu9+2HsGusI95kQ==','사용자2','닉네임2','profile2.png','a480e227-5ca9-4bd4-86a2-3243d5d604ee.png','2024-11-18 11:18:50'),(3,'ssafy3@ssafy.com','1a31f7b07a6e3ab336c579b2867ac2e27a6bb2126124663311afcd07e4514a9a','ujaYVCyVY+GNglJMy+gEGQ==','사용자3','닉네임3','profile3.png','0421fe1e-5b60-4970-9709-8aa28763502c.png','2024-11-18 11:19:02');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-18 20:48:48
