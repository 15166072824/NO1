-- 医生打卡签到表
CREATE TABLE `dakaqiandao` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yishengzhanghao` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '医生账号',
  `yishengxingming` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '医生姓名',
  `dakazhuangtai` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '打卡状态(上班、暂停、下班)',
  `dakashijian` datetime(0) DEFAULT NULL COMMENT '打卡时间',
  `beizhu` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `gongzuoshichang` int(11) DEFAULT '0' COMMENT '工作时长(分钟)',
  `zantingkaishishijian` datetime(0) DEFAULT NULL COMMENT '暂停开始时间',
  `zantingjieshushijian` datetime(0) DEFAULT NULL COMMENT '暂停结束时间',
  `zantingzongshichang` int(11) DEFAULT '0' COMMENT '暂停总时长(分钟)',
  `riqi` date DEFAULT NULL COMMENT '日期',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_yishengzhanghao` (`yishengzhanghao`),
  KEY `idx_dakazhuangtai` (`dakazhuangtai`),
  KEY `idx_riqi` (`riqi`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='医生打卡签到表' ROW_FORMAT=Dynamic;

-- 添加医生打卡功能菜单
INSERT INTO `menu` VALUES (NULL, '[{"backMenu": [{"child": [{"buttons": ["新增", "查看", "修改", "删除"], "menu": "", "menuJump": "列表", "tableName": "dakaqiandao"}], "menu": "打卡管理", "tableName": "dakaqiandao"}], "frontMenu": [{"child": [{"buttons": ["查看"], "menu": "", "menuJump": "列表", "tableName": "dakaqiandao"}], "menu": "打卡管理", "tableName": "dakaqiandao"}], "roleName": "医生", "tableName": "yisheng"}]', NOW());