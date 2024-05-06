CREATE TABLE `USERS` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255) UNIQUE,
  `name` varchar(255),
  `password` varchar(255),
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `RENTALS` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `surface` DECIMAL(10, 2),
  `price` DECIMAL(10, 2),
  `picture` varchar(255),
  `description` varchar(2000),
  `owner_id` bigint NOT NULL,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`owner_id`) REFERENCES `USERS` (`id`)
);

CREATE TABLE `MESSAGES` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `rental_id` bigint,
  `user_id` bigint,
  `message` varchar(2000),
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`rental_id`) REFERENCES `RENTALS` (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `USERS` (`id`)
);

CREATE UNIQUE INDEX `USERS_index` ON `USERS` (`email`);
