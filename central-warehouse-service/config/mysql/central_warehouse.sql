-- Adminer 4.8.1 MySQL 9.1.0 dump

SET NAMES utf8;
SET time_zone = '+00:00';
SET foreign_key_checks = 0;
SET sql_mode = 'NO_AUTO_VALUE_ON_ZERO';

SET NAMES utf8mb4;

CREATE TABLE `warehouse_order` (
                                   `id` int NOT NULL AUTO_INCREMENT,
                                   `uuid` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                   `store_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                   `customer_order_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                   `cancelled` bit NOT NULL DEFAULT 0,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `warehouse_order_article` (
                                           `warehouse_order` int NOT NULL,
                                           `article` int NOT NULL,
                                           `count` int NOT NULL,
                                           `fulfilled` int NOT NULL,
                                           `next_delivery_date` DATE,
                                           PRIMARY KEY (`warehouse_order`,`article`),
                                           KEY `warehouse_order` (`warehouse_order`),
                                           CONSTRAINT `warehouse_order_article_ibfk_2` FOREIGN KEY (`warehouse_order`) REFERENCES `warehouse_order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 2024-11-08 17:10:20