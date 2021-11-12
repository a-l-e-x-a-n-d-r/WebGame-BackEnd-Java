CREATE TABLE `users`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `avatar`     varchar(255) DEFAULT NULL,
    `email`      varchar(255) DEFAULT NULL,
    `password`   varchar(255) DEFAULT NULL,
    `points`     int          DEFAULT NULL,
    `username`   varchar(255) DEFAULT NULL,
    `kingdom_id` bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK1i8ig585jq0eftxwhbspjl0st` (`kingdom_id`)
) ENGINE = MyISAM
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `kingdom`
(
    `id`   bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = MyISAM
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `building`
(
    `dtype`       varchar(31) NOT NULL,
    `id`          bigint      NOT NULL AUTO_INCREMENT,
    `finished_at` bigint       DEFAULT NULL,
    `hp`          int          DEFAULT NULL,
    `level`       int          DEFAULT NULL,
    `started_at`  bigint       DEFAULT NULL,
    `type`        varchar(255) DEFAULT NULL,
    `kingdom_id`  bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK6s58l4oxb2hntomgyhe5byvpa` (`kingdom_id`)
) ENGINE = MyISAM
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `troop`
(
    `dtype`       varchar(31) NOT NULL,
    `id`          bigint      NOT NULL AUTO_INCREMENT,
    `attack`      int         NOT NULL,
    `defence`     int         NOT NULL,
    `finished_at` bigint       DEFAULT NULL,
    `hp`          int         NOT NULL,
    `level`       int         NOT NULL,
    `started_at`  bigint       DEFAULT NULL,
    `type`        varchar(255) DEFAULT NULL,
    `kingdom_id`  bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK9c9dtfl3ehguq5d38u40c4c5w` (`kingdom_id`)
) ENGINE = MyISAM
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `resource`
(
    `dtype`      varchar(31) NOT NULL,
    `id`         bigint      NOT NULL AUTO_INCREMENT,
    `amount`     int         NOT NULL,
    `generation` int         NOT NULL,
    `type`       varchar(255) DEFAULT NULL,
    `updated_at` bigint      NOT NULL,
    `kingdom_id` bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FKs4wjv1ijb0jucakvtlupiuxqh` (`kingdom_id`)
) ENGINE = MyISAM
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;