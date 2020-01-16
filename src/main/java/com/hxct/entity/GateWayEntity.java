package com.hxct.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-04 10:50:18
 */
public class GateWayEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;

	private String ipaddress;
	//
	private String name;
	//
	private String location;

	private Integer iskey;


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
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	/**
	 * 获取：
	 */
	public String getIpaddress() {
		return ipaddress;
	}
	/**
	 * 设置：
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * 获取：
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * 设置：
	 */
	public void setIskey(Integer iskey) {
		this.iskey = iskey;
	}
	/**
	 * 获取：
	 */
	public Integer getIskey() {
		if(iskey==null)
		{
			return 0;
		}
		return iskey;
	}

}
