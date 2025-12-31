package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.DakaqiandaoEntity;
import java.util.List;
import java.util.Map;
import com.cl.entity.view.DakaqiandaoView;

/**
 * 医生打卡签到
 *
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
public interface DakaqiandaoService extends IService<DakaqiandaoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    	List<DakaqiandaoView> selectListView(Wrapper<DakaqiandaoEntity> wrapper);

   	DakaqiandaoView selectView(Wrapper<DakaqiandaoEntity> wrapper);
   
   	PageUtils queryPage(Map<String, Object> params,Wrapper<DakaqiandaoEntity> wrapper);
   
   	List<DakaqiandaoView> selectListView2(Map<String,Object> params);
   	
   	List<Map<String, Object>> selectDakaTongji(Map<String, Object> params);

}