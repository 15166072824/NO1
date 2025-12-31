CREATE TABLE `daka` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `user_type` varchar(255) DEFAULT NULL COMMENT '用户类型',
  `status` varchar(255) DEFAULT NULL COMMENT '打卡状态',
  `start_time` datetime DEFAULT NULL COMMENT '上班时间',
  `end_time` datetime DEFAULT NULL COMMENT '下班时间',
  `pause_time` datetime DEFAULT NULL COMMENT '暂停时间',
  `resume_time` datetime DEFAULT NULL COMMENT '恢复时间',
  `total_duration` double DEFAULT NULL COMMENT '总工作时长',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='打卡记录';