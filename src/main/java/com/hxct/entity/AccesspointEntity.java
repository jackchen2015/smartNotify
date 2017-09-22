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
public class AccesspointEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private Integer acid;
	//
	private Integer index;
	//
	private String cover;
	//
	private String uptime;
	//站点业主单位
	private String ownerunit;
	//联系人员
	private String contact;
	//联系人电话
	private String contactnumber;
	//
	private String hotspotid;
	//
	private String macaddr;
	//
	private String ipaddress;
	//
	private String name;
	//
	private String location;
	//经度
	private String longitude;
	//纬度
	private String latitude;
	//
	private Integer iskey;
	//
	private Integer uppoeport;
	//
	private Integer uppoedistance;
	//
	private String gatewayloc;
	//
	private String apsearial;
	//
	private String type;
	//
	private String model;

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
	public void setAcid(Integer acid) {
		this.acid = acid;
	}
	/**
	 * 获取：
	 */
	public Integer getAcid() {
		return acid;
	}
	/**
	 * 设置：
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}
	/**
	 * 获取：
	 */
	public Integer getIndex() {
		return index;
	}
	/**
	 * 设置：
	 */
	public void setCover(String cover) {
		this.cover = cover;
	}
	/**
	 * 获取：
	 */
	public String getCover() {
		return cover;
	}
	/**
	 * 设置：
	 */
	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	/**
	 * 获取：
	 */
	public String getUptime() {
		return uptime;
	}
	/**
	 * 设置：站点业主单位
	 */
	public void setOwnerunit(String ownerunit) {
		this.ownerunit = ownerunit;
	}
	/**
	 * 获取：站点业主单位
	 */
	public String getOwnerunit() {
		return ownerunit;
	}
	/**
	 * 设置：联系人员
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	/**
	 * 获取：联系人员
	 */
	public String getContact() {
		return contact;
	}
	/**
	 * 设置：联系人电话
	 */
	public void setContactnumber(String contactnumber) {
		this.contactnumber = contactnumber;
	}
	/**
	 * 获取：联系人电话
	 */
	public String getContactnumber() {
		return contactnumber;
	}
	/**
	 * 设置：
	 */
	public void setHotspotid(String hotspotid) {
		this.hotspotid = hotspotid;
	}
	/**
	 * 获取：
	 */
	public String getHotspotid() {
		return hotspotid;
	}
	/**
	 * 设置：
	 */
	public void setMacaddr(String macaddr) {
		this.macaddr = macaddr;
	}
	/**
	 * 获取：
	 */
	public String getMacaddr() {
		return macaddr;
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
	 * 设置：经度
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	/**
	 * 获取：经度
	 */
	public String getLongitude() {
		return longitude;
	}
	/**
	 * 设置：纬度
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	/**
	 * 获取：纬度
	 */
	public String getLatitude() {
		return latitude;
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
	/**
	 * 设置：
	 */
	public void setUppoeport(Integer uppoeport) {
		this.uppoeport = uppoeport;
	}
	/**
	 * 获取：
	 */
	public Integer getUppoeport() {
		return uppoeport;
	}
	/**
	 * 设置：
	 */
	public void setUppoedistance(Integer uppoedistance) {
		this.uppoedistance = uppoedistance;
	}
	/**
	 * 获取：
	 */
	public Integer getUppoedistance() {
		return uppoedistance;
	}
	/**
	 * 设置：
	 */
	public void setGatewayloc(String gatewayloc) {
		this.gatewayloc = gatewayloc;
	}
	/**
	 * 获取：
	 */
	public String getGatewayloc() {
		return gatewayloc;
	}
	/**
	 * 设置：
	 */
	public void setApsearial(String apsearial) {
		this.apsearial = apsearial;
	}
	/**
	 * 获取：
	 */
	public String getApsearial() {
		return apsearial;
	}
	/**
	 * 设置：
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * 获取：
	 */
	public String getType() {
		return type;
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
	
	public void copyFrom(AccesspointEntity ape){
		this.acid = ape.acid;
		this.apsearial = ape.apsearial;
		this.contact = ape.contact;
		this.contactnumber = ape.contactnumber;
		this.cover = ape.cover;
		this.gatewayloc = ape.gatewayloc;
		this.latitude = ape.latitude;
		this.longitude = ape.longitude;
		this.model = ape.model;
	}
}
