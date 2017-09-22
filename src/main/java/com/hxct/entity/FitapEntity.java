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
public class FitapEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private String 可用状态;
	//
	private String ap名称;
	//
	private String ip地址;
	//
	private String mac地址;
	//
	private String 所属ac;
	//
	private String 厂商;
	//
	private String 型号;
	//
	private String 序列号;
	//
	private String 位置信息;
	//
	private String 当前在线用户数;
	//
	private String 退服率;

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
	public void set可用状态(String 可用状态) {
		this.可用状态 = 可用状态;
	}
	/**
	 * 获取：
	 */
	public String get可用状态() {
		return 可用状态;
	}
	/**
	 * 设置：
	 */
	public void setAp名称(String ap名称) {
		this.ap名称 = ap名称;
	}
	/**
	 * 获取：
	 */
	public String getAp名称() {
		return ap名称;
	}
	/**
	 * 设置：
	 */
	public void setIp地址(String ip地址) {
		this.ip地址 = ip地址;
	}
	/**
	 * 获取：
	 */
	public String getIp地址() {
		return ip地址;
	}
	/**
	 * 设置：
	 */
	public void setMac地址(String mac地址) {
		this.mac地址 = mac地址;
	}
	/**
	 * 获取：
	 */
	public String getMac地址() {
		return mac地址;
	}
	/**
	 * 设置：
	 */
	public void set所属ac(String 所属ac) {
		this.所属ac = 所属ac;
	}
	/**
	 * 获取：
	 */
	public String get所属ac() {
		return 所属ac;
	}
	/**
	 * 设置：
	 */
	public void set厂商(String 厂商) {
		this.厂商 = 厂商;
	}
	/**
	 * 获取：
	 */
	public String get厂商() {
		return 厂商;
	}
	/**
	 * 设置：
	 */
	public void set型号(String 型号) {
		this.型号 = 型号;
	}
	/**
	 * 获取：
	 */
	public String get型号() {
		return 型号;
	}
	/**
	 * 设置：
	 */
	public void set序列号(String 序列号) {
		this.序列号 = 序列号;
	}
	/**
	 * 获取：
	 */
	public String get序列号() {
		return 序列号;
	}
	/**
	 * 设置：
	 */
	public void set位置信息(String 位置信息) {
		this.位置信息 = 位置信息;
	}
	/**
	 * 获取：
	 */
	public String get位置信息() {
		return 位置信息;
	}
	/**
	 * 设置：
	 */
	public void set当前在线用户数(String 当前在线用户数) {
		this.当前在线用户数 = 当前在线用户数;
	}
	/**
	 * 获取：
	 */
	public String get当前在线用户数() {
		return 当前在线用户数;
	}
	/**
	 * 设置：
	 */
	public void set退服率(String 退服率) {
		this.退服率 = 退服率;
	}
	/**
	 * 获取：
	 */
	public String get退服率() {
		return 退服率;
	}
}
