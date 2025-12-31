package com.cl.entity.view;

import com.cl.entity.DakaqiandaoEntity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 医生打卡签到 视图实体类
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
@TableName("dakaqiandaoView")
public class DakaqiandaoView extends DakaqiandaoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public DakaqiandaoView(){
	}

 	public DakaqiandaoView(DakaqiandaoEntity dakaqiandaoEntity){
 		try {
 			BeanUtils.copyProperties(this, dakaqiandaoEntity);
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 	}

	@Override
	public String toString() {
		return super.toString();
	}
}