package com.cl.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;


import com.cl.dao.DakaqiandaoDao;
import com.cl.entity.DakaqiandaoEntity;
import com.cl.service.DakaqiandaoService;
import com.cl.entity.view.DakaqiandaoView;

@Service("dakaqiandaoService")
public class DakaqiandaoServiceImpl extends ServiceImpl<DakaqiandaoDao, DakaqiandaoEntity> implements DakaqiandaoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DakaqiandaoEntity> page = this.selectPage(
                new Query<DakaqiandaoEntity>(params).getPage(),
                new EntityWrapper<DakaqiandaoEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<DakaqiandaoEntity> wrapper) {
        Page<DakaqiandaoView> page =new Query<DakaqiandaoView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page,wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }
    
    @Override
    public List<DakaqiandaoView> selectListView(Wrapper<DakaqiandaoEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public DakaqiandaoView selectView(Wrapper<DakaqiandaoEntity> wrapper) {
        return baseMapper.selectView(wrapper);
    }

    @Override
    public List<DakaqiandaoView> selectListView2(Map<String, Object> params) {
        return baseMapper.selectListView2(params);
    }

    @Override
    public List<Map<String, Object>> selectDakaTongji(Map<String, Object> params) {
        return baseMapper.selectDakaTongji(params);
    }
}