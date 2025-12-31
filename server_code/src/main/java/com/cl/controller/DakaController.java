package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.DakaEntity;
import com.cl.entity.PaibanxinxiEntity;
import com.cl.dao.DakaDao;
import com.cl.dao.PaibanxinxiDao;
import com.cl.service.CommonService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 打卡管理
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
@RestController
@RequestMapping("/daka")
public class DakaController {
    @Autowired
    private DakaDao dakaDao;
    
    @Autowired
    private PaibanxinxiDao paibanxinxiDao;
    
    @Autowired
    private CommonService commonService;
    
    /**
     * 打卡上班
     */
    @PostMapping("/start")
    public R start(@RequestBody DakaEntity daka) {
        // 检查今日是否已打卡
        DakaEntity todayDaka = dakaDao.getTodayDakaByUserId(daka.getUserId());
        if (todayDaka != null && ("已打卡".equals(todayDaka.getStatus()) || "暂停".equals(todayDaka.getStatus()))) {
            return R.error("今日已打卡上班");
        }
        
        // 获取排班信息
        PaibanxinxiEntity paiban = paibanxinxiDao.selectOne(new EntityWrapper<PaibanxinxiEntity>().eq("yishengzhanghao", daka.getUserId()));
        
        DakaEntity newDaka = new DakaEntity();
        newDaka.setUserId(daka.getUserId());
        newDaka.setUserType(daka.getUserType());
        newDaka.setStatus("已打卡");
        newDaka.setStartTime(new Date());
        newDaka.setCreateTime(new Date());
        newDaka.setUpdateTime(new Date());
        
        if (paiban != null) {
            newDaka.setTotalDuration(paiban.getShangbanshizhang().doubleValue());
        }
        
        dakaDao.insert(newDaka);
        return R.ok("打卡上班成功");
    }
    
    /**
     * 暂停工作
     */
    @PostMapping("/pause")
    public R pause(@RequestBody DakaEntity daka) {
        DakaEntity todayDaka = dakaDao.getTodayDakaByUserId(daka.getUserId());
        if (todayDaka == null || !"已打卡".equals(todayDaka.getStatus())) {
            return R.error("未打卡上班，无法暂停");
        }
        
        todayDaka.setStatus("暂停");
        todayDaka.setPauseTime(new Date());
        todayDaka.setUpdateTime(new Date());
        
        dakaDao.updateById(todayDaka);
        return R.ok("暂停工作成功");
    }
    
    /**
     * 恢复工作
     */
    @PostMapping("/resume")
    public R resume(@RequestBody DakaEntity daka) {
        DakaEntity todayDaka = dakaDao.getTodayDakaByUserId(daka.getUserId());
        if (todayDaka == null || !"暂停".equals(todayDaka.getStatus())) {
            return R.error("未暂停工作，无法恢复");
        }
        
        todayDaka.setStatus("已打卡");
        todayDaka.setResumeTime(new Date());
        todayDaka.setUpdateTime(new Date());
        
        dakaDao.updateById(todayDaka);
        return R.ok("恢复工作成功");
    }
    
    /**
     * 打卡下班
     */
    @PostMapping("/end")
    public R end(@RequestBody DakaEntity daka) {
        DakaEntity todayDaka = dakaDao.getTodayDakaByUserId(daka.getUserId());
        if (todayDaka == null || "未打卡".equals(todayDaka.getStatus())) {
            return R.error("未打卡上班，无法下班");
        }
        
        todayDaka.setStatus("已下班");
        todayDaka.setEndTime(new Date());
        todayDaka.setUpdateTime(new Date());
        
        // 计算工作时长
        long diff = todayDaka.getEndTime().getTime() - todayDaka.getStartTime().getTime();
        double hours = diff / (1000.0 * 60 * 60);
        todayDaka.setTotalDuration(hours);
        
        dakaDao.updateById(todayDaka);
        return R.ok("打卡下班成功");
    }
    
    /**
     * 获取打卡状态
     */
    @GetMapping("/status")
    public R status(@RequestParam Long userId) {
        DakaEntity todayDaka = dakaDao.getTodayDakaByUserId(userId);
        
        Map<String, Object> result = new HashMap<>();
        if (todayDaka == null) {
            result.put("status", "未打卡");
        } else {
            result.put("status", todayDaka.getStatus());
            result.put("startTime", todayDaka.getStartTime());
            result.put("pauseTime", todayDaka.getPauseTime());
            
            // 获取打卡记录
            List<DakaEntity> records = dakaDao.getDakaRecords(userId);
            result.put("records", records);
        }
        
        return R.ok().put("data", result);
    }
    
    /**
     * 打卡记录列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = commonService.queryPage(params, "daka", new String[]{});
        return R.ok().put("data", page);
    }
}