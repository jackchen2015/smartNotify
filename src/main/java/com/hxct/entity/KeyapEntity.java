package com.hxct.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-09 16:08:57
 */
public class KeyapEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private String apmac;
	//
	private String 热点;
	//
	private String ap安装位置描述;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：
	 */
	public void setApmac(String apmac) {
		this.apmac = apmac;
	}
	/**
	 * 获取：
	 */
	public String getApmac() {
		return apmac;
	}
	/**
	 * 设置：
	 */
	public void set热点(String 热点) {
		this.热点 = 热点;
	}
	/**
	 * 获取：
	 */
	public String get热点() {
		return 热点;
	}
	/**
	 * 设置：
	 */
	public void setAp安装位置描述(String ap安装位置描述) {
		this.ap安装位置描述 = ap安装位置描述;
	}
	/**
	 * 获取：
	 */
	public String getAp安装位置描述() {
		return ap安装位置描述;
	}
}
