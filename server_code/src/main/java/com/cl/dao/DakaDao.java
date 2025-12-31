package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cl.entity.DakaEntity;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 打卡记录
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
public interface DakaDao extends BaseMapper<DakaEntity> {
    
    /**
     * 根据用户ID获取今日打卡记录
     */
    DakaEntity getTodayDakaByUserId(@Param("userId") Long userId);
    
    /**
     * 获取用户打卡统计
     */
    List<Map<String, Object>> getDakaStatistics(@Param("userId") Long userId);
    
    /**
     * 获取用户打卡记录
     */
    List<DakaEntity> getDakaRecords(@Param("userId") Long userId);
}