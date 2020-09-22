CREATE TABLE `distributed_lock` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `unique_mutex` varchar(255) NOT NULL COMMENT '业务防重id',
    `holder_id` varchar(255) NOT NULL COMMENT '锁持有者id',
    `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `mutex_index` (`unique_mutex`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
