CREATE TABLE `product`
(
    `id`   varchar(36)  NOT NULL,
    `name` varchar(100) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `locale`
(
    `id`     varchar(36) NOT NULL,
    `locale` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `language`
(
    `id`       varchar(36)  NOT NULL,
    `language` varchar(100) NOT NULL,
    `iso_code` varchar(10)  NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `iso_code` (`iso_code`)
);

CREATE TABLE `locale_language`
(
    `id`          varchar(36) NOT NULL,
    `locale_id`   varchar(36) DEFAULT NULL,
    `language_id` varchar(36) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `locale_id` (`locale_id`),
    KEY `language_id` (`language_id`),
    CONSTRAINT `locale_language_ibfk_1` FOREIGN KEY (`locale_id`) REFERENCES `locale` (`id`),
    CONSTRAINT `locale_language_ibfk_2` FOREIGN KEY (`language_id`) REFERENCES `language` (`id`)
);

CREATE TABLE `consent_template`
(
    `id`                 varchar(36) NOT NULL,
    `locale_language_id` varchar(36) DEFAULT NULL,
    `template_pdf`       blob        DEFAULT NULL,
    `type`               varchar(10) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `locale_language_id` (`locale_language_id`),
    CONSTRAINT `consent_template_ibfk_1` FOREIGN KEY (`locale_language_id`) REFERENCES `locale_language` (`id`)
);

CREATE TABLE `product_template`
(
    `id`                  varchar(36) NOT NULL,
    `product_id`          varchar(36) NOT NULL,
    `consent_template_id` varchar(36) NOT NULL,
    `active`              tinyint(1)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `consent_template_id` (`consent_template_id`),
    KEY `product_id` (`product_id`),
    KEY `idx_active_product` (`active`, `product_id`),
    CONSTRAINT `product_template_ibfk_1` FOREIGN KEY (`consent_template_id`) REFERENCES `consent_template` (`id`),
    CONSTRAINT `product_template_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
);

CREATE TABLE `client_consent`
(
    `id`                  varchar(36) NOT NULL,
    `product_template_id` varchar(36)  DEFAULT NULL,
    `individual_id`       varchar(100) DEFAULT NULL,
    `date`                date         DEFAULT NULL,
    `optout_reason`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `product_template_id` (`product_template_id`),
    CONSTRAINT `client_consent_ibfk_1` FOREIGN KEY (`product_template_id`) REFERENCES `product_template` (`id`)
);