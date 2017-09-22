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
public class AcEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private String ipaddr;
	//
	private Integer snmpport;
	//
	private String readco;
	//
	private String writeco;
	//
	private String name;
	//
	private String model;
	//
	private String sysoid;

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
	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	/**
	 * 获取：
	 */
	public String getIpaddr() {
		return ipaddr;
	}
	/**
	 * 设置：
	 */
	public void setSnmpport(Integer snmpport) {
		this.snmpport = snmpport;
	}
	/**
	 * 获取：
	 */
	public Integer getSnmpport() {
		return snmpport;
	}
	/**
	 * 设置：
	 */
	public void setReadco(String readco) {
		this.readco = readco;
	}
	/**
	 * 获取：
	 */
	public String getReadco() {
		return readco;
	}
	/**
	 * 设置：
	 */
	public void setWriteco(String writeco) {
		this.writeco = writeco;
	}
	/**
	 * 获取：
	 */
	public String getWriteco() {
		return writeco;
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
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * 获取：
	 */
	public String getModel() {
		return model;
	}
	/**
	 * 设置：
	 */
	public void setSysoid(String sysoid) {
		this.sysoid = sysoid;
	}
	/**
	 * 获取：
	 */
	public String getSysoid() {
		return sysoid;
	}
	
	public void copyFrom(AcEntity ace){
		this.readco = ace.readco;
		this.writeco = ace.writeco;
		this.snmpport = ace.snmpport;
		this.sysoid = ace.sysoid;
	}
	
	public String toString(){
		return name;
	}
}
