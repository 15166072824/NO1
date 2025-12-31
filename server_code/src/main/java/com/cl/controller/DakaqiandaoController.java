package com.cl.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.DakaqiandaoEntity;
import com.cl.entity.PaibanxinxiEntity;
import com.cl.entity.YishengEntity;
import com.cl.entity.view.DakaqiandaoView;
import com.cl.service.DakaqiandaoService;
import com.cl.service.PaibanxinxiService;
import com.cl.service.YishengService;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;

/**
 * 医生打卡签到
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
@RestController
@RequestMapping("/dakaqiandao")
public class DakaqiandaoController {
    @Autowired
    private DakaqiandaoService dakaqiandaoService;
    
    @Autowired
    private YishengService yishengService;
    
    @Autowired
    private PaibanxinxiService paibanxinxiService;

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,DakaqiandaoEntity dakaqiandao,
					HttpServletRequest request){
		EntityWrapper<DakaqiandaoEntity> ew = new EntityWrapper<DakaqiandaoEntity>();
		PageUtils page = dakaqiandaoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dakaqiandao), params), params));
        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,DakaqiandaoEntity dakaqiandao, 
					HttpServletRequest request){
		EntityWrapper<DakaqiandaoEntity> ew = new EntityWrapper<DakaqiandaoEntity>();
		PageUtils page = dakaqiandaoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, dakaqiandao), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( DakaqiandaoEntity dakaqiandao){
       	EntityWrapper<DakaqiandaoEntity> ew = new EntityWrapper<DakaqiandaoEntity>();
      	ew.allEq(MPUtil.allEQMapPre( dakaqiandao, "dakaqiandao")); 
        return R.ok().put("data", dakaqiandaoService.selectListView(ew));
    }
	
	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(DakaqiandaoEntity dakaqiandao){
        EntityWrapper< DakaqiandaoEntity> ew = new EntityWrapper< DakaqiandaoEntity>();
 		ew.allEq(MPUtil.allEQMapPre( dakaqiandao, "dakaqiandao")); 
		DakaqiandaoView dakaqiandaoView =  dakaqiandaoService.selectView(ew);
		return R.ok("查询医生打卡签到成功").put("data", dakaqiandaoView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        DakaqiandaoEntity dakaqiandao = dakaqiandaoService.selectById(id);
		dakaqiandao = dakaqiandaoService.selectView(new EntityWrapper<DakaqiandaoEntity>().eq("id", id));
        return R.ok().put("data", dakaqiandao);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        DakaqiandaoEntity dakaqiandao = dakaqiandaoService.selectById(id);
		dakaqiandao = dakaqiandaoService.selectView(new EntityWrapper<DakaqiandaoEntity>().eq("id", id));
        return R.ok().put("data", dakaqiandao);
    }
    

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody DakaqiandaoEntity dakaqiandao, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(dakaqiandao);
        dakaqiandaoService.insert(dakaqiandao);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody DakaqiandaoEntity dakaqiandao, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(dakaqiandao);
        dakaqiandaoService.insert(dakaqiandao);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody DakaqiandaoEntity dakaqiandao, HttpServletRequest request){
        //ValidatorUtils.validateEntity(dakaqiandao);
        dakaqiandaoService.updateById(dakaqiandao);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        dakaqiandaoService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 医生上班打卡
     */
    @RequestMapping("/shangbanDaka")
    public R shangbanDaka(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String yishengzhanghao = params.get("yishengzhanghao");
        
        // 检查今天是否已上班打卡
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        
        EntityWrapper<DakaqiandaoEntity> ew = new EntityWrapper<>();
        ew.eq("yishengzhanghao", yishengzhanghao)
          .eq("riqi", today)
          .eq("dakazhuangtai", "上班");
        
        List<DakaqiandaoEntity> todayDaka = dakaqiandaoService.selectList(ew);
        if (!todayDaka.isEmpty()) {
            return R.error("今天已经上班打卡了");
        }
        
        // 获取医生信息
        YishengEntity yisheng = yishengService.selectOne(new EntityWrapper<YishengEntity>().eq("yishengzhanghao", yishengzhanghao));
        if (yisheng == null) {
            return R.error("医生信息不存在");
        }
        
        // 创建打卡记录
        DakaqiandaoEntity daka = new DakaqiandaoEntity();
        daka.setYishengzhanghao(yishengzhanghao);
        daka.setYishengxingming(yisheng.getYishengxingming());
        daka.setDakazhuangtai("上班");
        daka.setDakashijian(new Date());
        daka.setGongzuoshichang(0);
        daka.setZantingzongshichang(0);
        daka.setRiqi(new Date());
        daka.setAddtime(new Date());
        
        dakaqiandaoService.insert(daka);
        
        return R.ok("上班打卡成功").put("data", daka);
    }
    
    /**
     * 医生暂停打卡
     */
    @RequestMapping("/zantingDaka")
    public R zantingDaka(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String yishengzhanghao = params.get("yishengzhanghao");
        String zhuangtai = params.get("zhuangtai"); // "暂停开始" 或 "暂停结束"
        
        // 获取今天的上班打卡记录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        
        EntityWrapper<DakaqiandaoEntity> ew = new EntityWrapper<>();
        ew.eq("yishengzhanghao", yishengzhanghao)
          .eq("riqi", today)
          .eq("dakazhuangtai", "上班");
        
        DakaqiandaoEntity shangbanDaka = dakaqiandaoService.selectOne(ew);
        if (shangbanDaka == null) {
            return R.error("今天还未上班打卡");
        }
        
        if ("暂停开始".equals(zhuangtai)) {
            // 检查是否已有暂停开始但未结束的记录
            EntityWrapper<DakaqiandaoEntity> zantingEw = new EntityWrapper<>();
            zantingEw.eq("yishengzhanghao", yishengzhanghao)
                    .eq("riqi", today)
                    .eq("dakazhuangtai", "暂停");
            
            List<DakaqiandaoEntity> zantingList = dakaqiandaoService.selectList(zantingEw);
            for (DakaqiandaoEntity zanting : zantingList) {
                if (zanting.getZantingkaishishijian() != null && zanting.getZantingjieshushijian() == null) {
                    return R.error("已有暂停未结束");
                }
            }
            
            // 创建暂停开始记录
            DakaqiandaoEntity zanting = new DakaqiandaoEntity();
            zanting.setYishengzhanghao(yishengzhanghao);
            zanting.setYishengxingming(shangbanDaka.getYishengxingming());
            zanting.setDakazhuangtai("暂停");
            zanting.setDakashijian(new Date());
            zanting.setZantingkaishishijian(new Date());
            zanting.setRiqi(new Date());
            zanting.setAddtime(new Date());
            
            dakaqiandaoService.insert(zanting);
            
            return R.ok("暂停开始成功").put("data", zanting);
            
        } else if ("暂停结束".equals(zhuangtai)) {
            // 查找暂停开始但未结束的记录
            EntityWrapper<DakaqiandaoEntity> zantingEw = new EntityWrapper<>();
            zantingEw.eq("yishengzhanghao", yishengzhanghao)
                    .eq("riqi", today)
                    .eq("dakazhuangtai", "暂停")
                    .isNull("zantingjieshushijian");
            
            DakaqiandaoEntity zanting = dakaqiandaoService.selectOne(zantingEw);
            if (zanting == null) {
                return R.error("没有暂停中的记录");
            }
            
            // 更新暂停结束时间
            zanting.setZantingjieshushijian(new Date());
            
            // 计算暂停时长（分钟）
            long pauseTime = (new Date().getTime() - zanting.getZantingkaishishijian().getTime()) / (1000 * 60);
            zanting.setZantingzongshichang((int) pauseTime);
            
            dakaqiandaoService.updateById(zanting);
            
            // 更新总暂停时长到上班记录
            EntityWrapper<DakaqiandaoEntity> allZantingEw = new EntityWrapper<>();
            allZantingEw.eq("yishengzhanghao", yishengzhanghao)
                        .eq("riqi", today)
                        .eq("dakazhuangtai", "暂停");
            
            List<DakaqiandaoEntity> allZanting = dakaqiandaoService.selectList(allZantingEw);
            int totalPauseTime = 0;
            for (DakaqiandaoEntity pause : allZanting) {
                if (pause.getZantingzongshichang() != null) {
                    totalPauseTime += pause.getZantingzongshichang();
                }
            }
            
            shangbanDaka.setZantingzongshichang(totalPauseTime);
            dakaqiandaoService.updateById(shangbanDaka);
            
            return R.ok("暂停结束成功").put("data", zanting);
        }
        
        return R.error("无效的暂停状态");
    }
    
    /**
     * 医生下班打卡
     */
    @RequestMapping("/xiabanDaka")
    public R xiabanDaka(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String yishengzhanghao = params.get("yishengzhanghao");
        
        // 获取今天的上班打卡记录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        
        EntityWrapper<DakaqiandaoEntity> ew = new EntityWrapper<>();
        ew.eq("yishengzhanghao", yishengzhanghao)
          .eq("riqi", today)
          .eq("dakazhuangtai", "上班");
        
        DakaqiandaoEntity shangbanDaka = dakaqiandaoService.selectOne(ew);
        if (shangbanDaka == null) {
            return R.error("今天还未上班打卡");
        }
        
        // 检查是否已有下班打卡
        EntityWrapper<DakaqiandaoEntity> xiabanEw = new EntityWrapper<>();
        xiabanEw.eq("yishengzhanghao", yishengzhanghao)
                .eq("riqi", today)
                .eq("dakazhuangtai", "下班");
        
        DakaqiandaoEntity xiabanDaka = dakaqiandaoService.selectOne(xiabanEw);
        if (xiabanDaka != null) {
            return R.error("今天已经下班打卡了");
        }
        
        // 获取医生信息
        YishengEntity yisheng = yishengService.selectOne(new EntityWrapper<YishengEntity>().eq("yishengzhanghao", yishengzhanghao));
        
        // 创建下班打卡记录
        DakaqiandaoEntity xiaban = new DakaqiandaoEntity();
        xiaban.setYishengzhanghao(yishengzhanghao);
        xiaban.setYishengxingming(yisheng.getYishengxingming());
        xiaban.setDakazhuangtai("下班");
        xiaban.setDakashijian(new Date());
        xiaban.setRiqi(new Date());
        xiaban.setAddtime(new Date());
        
        // 计算工作时长（分钟）
        long workTime = (new Date().getTime() - shangbanDaka.getDakashijian().getTime()) / (1000 * 60);
        int pauseTime = shangbanDaka.getZantingzongshichang() != null ? shangbanDaka.getZantingzongshichang() : 0;
        int actualWorkTime = (int) (workTime - pauseTime);
        
        xiaban.setGongzuoshichang(actualWorkTime);
        
        dakaqiandaoService.insert(xiaban);
        
        // 更新上班记录的工作时长
        shangbanDaka.setGongzuoshichang(actualWorkTime);
        dakaqiandaoService.updateById(shangbanDaka);
        
        // 更新排班信息中的上班时长
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date todayDate = dateFormat.parse(today);
            
            EntityWrapper<PaibanxinxiEntity> paibanEw = new EntityWrapper<>();
            paibanEw.eq("yishengzhanghao", yishengzhanghao)
                    .eq("shangbanshijian", todayDate);
            
            PaibanxinxiEntity paiban = paibanxinxiService.selectOne(paibanEw);
            if (paiban != null) {
                // 将分钟转换为小时，保留一位小数
                double workHours = Math.round(actualWorkTime / 60.0 * 10) / 10.0;
                paiban.setShangbanshizhang((int) workHours);
                paibanxinxiService.updateById(paiban);
            }
        } catch (Exception e) {
            // 如果排班更新失败，不影响打卡流程
            System.err.println("更新排班信息失败: " + e.getMessage());
        }
        
        return R.ok("下班打卡成功，今日工作时长：" + actualWorkTime + "分钟").put("data", xiaban);
    }
    
    /**
     * 获取医生今日打卡状态
     */
    @RequestMapping("/getTodayDakaStatus")
    public R getTodayDakaStatus(@RequestParam String yishengzhanghao) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        
        Map<String, Object> result = new HashMap<>();
        
        // 检查上班打卡
        EntityWrapper<DakaqiandaoEntity> shangbanEw = new EntityWrapper<>();
        shangbanEw.eq("yishengzhanghao", yishengzhanghao)
                  .eq("riqi", today)
                  .eq("dakazhuangtai", "上班");
        
        DakaqiandaoEntity shangbanDaka = dakaqiandaoService.selectOne(shangbanEw);
        result.put("shangban", shangbanDaka != null);
        result.put("shangbanData", shangbanDaka);
        
        // 检查暂停状态
        if (shangbanDaka != null) {
            EntityWrapper<DakaqiandaoEntity> zantingEw = new EntityWrapper<>();
            zantingEw.eq("yishengzhanghao", yishengzhanghao)
                    .eq("riqi", today)
                    .eq("dakazhuangtai", "暂停")
                    .isNull("zantingjieshushijian");
            
            DakaqiandaoEntity zantingDaka = dakaqiandaoService.selectOne(zantingEw);
            result.put("zantingzhong", zantingDaka != null);
            result.put("zantingData", zantingDaka);
        }
        
        // 检查下班打卡
        EntityWrapper<DakaqiandaoEntity> xiabanEw = new EntityWrapper<>();
        xiabanEw.eq("yishengzhanghao", yishengzhanghao)
                .eq("riqi", today)
                .eq("dakazhuangtai", "下班");
        
        DakaqiandaoEntity xiabanDaka = dakaqiandaoService.selectOne(xiabanEw);
        result.put("xiaban", xiabanDaka != null);
        result.put("xiabanData", xiabanDaka);
        
        return R.ok().put("data", result);
    }
}