CREATE TABLE IF NOT EXISTS `id_provider_status`
(
    `id`   varchar(36)  NOT NULL,
    `provider_name` varchar(100) NOT NULL,
    `status` varchar(100) NOT NULL,
    `ident_id` varchar(100) NOT NULL,
    `status_date`  timestamp NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
);