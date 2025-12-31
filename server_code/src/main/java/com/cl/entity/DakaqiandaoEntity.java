package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;


/**
 * 医生打卡签到
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
@TableName("dakaqiandao")
public class DakaqiandaoEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public DakaqiandaoEntity() {
		
	}
	
	public DakaqiandaoEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 医生账号
	 */
																														
	private String yishengzhanghao;
	
	/**
	 * 医生姓名
	 */
																													
	private String yishengxingming;
	
	/**
	 * 打卡状态
	 */
																													
	private String dakazhuangtai;
	
	/**
	 * 打卡时间
	 */
					
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date dakashijian;
	
	/**
	 * 备注
	 */
																													
	private String beizhu;
	
	/**
	 * 工作时长
	 */
																													
	private Integer gongzuoshichang;
	
	/**
	 * 暂停开始时间
	 */
					
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date zantingkaishishijian;
	
	/**
	 * 暂停结束时间
	 */
					
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date zantingjieshushijian;
	
	/**
	 * 暂停总时长
	 */
																													
	private Integer zantingzongshichang;
	
	/**
	 * 日期
	 */
					
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	@DateTimeFormat 		
	private Date riqi;
	

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 设置：医生账号
	 */
	public void setYishengzhanghao(String yishengzhanghao) {
		this.yishengzhanghao = yishengzhanghao;
	}
	/**
	 * 获取：医生账号
	 */
	public String getYishengzhanghao() {
		return yishengzhanghao;
	}
	/**
	 * 设置：医生姓名
	 */
	public void setYishengxingming(String yishengxingming) {
		this.yishengxingming = yishengxingming;
	}
	/**
	 * 获取：医生姓名
	 */
	public String getYishengxingming() {
		return yishengxingming;
	}
	/**
	 * 设置：打卡状态
	 */
	public void setDakazhuangtai(String dakazhuangtai) {
		this.dakazhuangtai = dakazhuangtai;
	}
	/**
	 * 获取：打卡状态
	 */
	public String getDakazhuangtai() {
		return dakazhuangtai;
	}
	/**
	 * 设置：打卡时间
	 */
	public void setDakashijian(Date dakashijian) {
		this.dakashijian = dakashijian;
	}
	/**
	 * 获取：打卡时间
	 */
	public Date getDakashijian() {
		return dakashijian;
	}
	/**
	 * 设置：备注
	 */
	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}
	/**
	 * 获取：备注
	 */
	public String getBeizhu() {
		return beizhu;
	}
	/**
	 * 设置：工作时长
	 */
	public void setGongzuoshichang(Integer gongzuoshichang) {
		this.gongzuoshichang = gongzuoshichang;
	}
	/**
	 * 获取：工作时长
	 */
	public Integer getGongzuoshichang() {
		return gongzuoshichang;
	}
	/**
	 * 设置：暂停开始时间
	 */
	public void setZantingkaishishijian(Date zantingkaishishijian) {
		this.zantingkaishishijian = zantingkaishishijian;
	}
	/**
	 * 获取：暂停开始时间
	 */
	public Date getZantingkaishishijian() {
		return zantingkaishishijian;
	}
	/**
	 * 设置：暂停结束时间
	 */
	public void setZantingjieshushijian(Date zantingjieshushijian) {
		this.zantingjieshushijian = zantingjieshushijian;
	}
	/**
	 * 获取：暂停结束时间
	 */
	public Date getZantingjieshushijian() {
		return zantingjieshushijian;
	}
	/**
	 * 设置：暂停总时长
	 */
	public void setZantingzongshichang(Integer zantingzongshichang) {
		this.zantingzongshichang = zantingzongshichang;
	}
	/**
	 * 获取：暂停总时长
	 */
	public Integer getZantingzongshichang() {
		return zantingzongshichang;
	}
	/**
	 * 设置：日期
	 */
	public void setRiqi(Date riqi) {
		this.riqi = riqi;
	}
	/**
	 * 获取：日期
	 */
	public Date getRiqi() {
		return riqi;
	}

}