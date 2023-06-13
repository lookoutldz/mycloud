CREATE DATABASE IF NOT EXISTS `mycloud` DEFAULT CHARSET utf8mb4;

CREATE TABLE IF NOT EXISTS `user` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `username` varchar(191) NOT NULL DEFAULT '',
    `password` varchar(191) NOT NULL DEFAULT '',
    `phone` varchar(191) DEFAULT NULL,
    `phone_prefix` varchar(191) DEFAULT NULL,
    `email` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `gender` smallint unsigned NOT NULL DEFAULT '0',
    `age` smallint unsigned DEFAULT NULL,
    `description` varchar(191) DEFAULT NULL,
    `ban` tinyint(1) NOT NULL DEFAULT '0',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` tinyint(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_email` (`email`),
    UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

# @deprecated(use redis instead)
# CREATE TABLE `tb_validcode` (
#     `id` int NOT NULL AUTO_INCREMENT,
#     `email` varchar(191) NOT NULL,
#     `validcode` varchar(191) DEFAULT NULL,
#     `validcode_expire` timestamp NULL DEFAULT NULL,
#     `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
#     `update_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP,
#     PRIMARY KEY (`id`),
#     UNIQUE KEY `idx_email` (`email`)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `persistent_logins` (
    `username` varchar(64) NOT NULL,
    `series` varchar(64) NOT NULL,
    `token` varchar(64) NOT NULL,
    `last_used` timestamp NOT NULL,
    PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;