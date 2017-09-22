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
public class AltnotifyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//
	private Integer type;
	//
	private String smtpserver;
	//
	private Integer smtpport;
	//
	private String mailsender;
	//
	private String senderalias;
	//
	private String mailuser;
	//
	private String mailpwd;
	//
	private Integer smtpssl;
	//
	private Integer smtpauth;
	//
	private String smsserverip;
	//
	private Integer smsserverport;
	//
	private String smsaccountsid;
	//
	private String smsaccounttoken;
	//
	private String smsappid;

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
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：
	 */
	public void setSmtpserver(String smtpserver) {
		this.smtpserver = smtpserver;
	}
	/**
	 * 获取：
	 */
	public String getSmtpserver() {
		return smtpserver;
	}
	/**
	 * 设置：
	 */
	public void setSmtpport(Integer smtpport) {
		this.smtpport = smtpport;
	}
	/**
	 * 获取：
	 */
	public Integer getSmtpport() {
		return smtpport;
	}
	/**
	 * 设置：
	 */
	public void setMailsender(String mailsender) {
		this.mailsender = mailsender;
	}
	/**
	 * 获取：
	 */
	public String getMailsender() {
		return mailsender;
	}
	/**
	 * 设置：
	 */
	public void setSenderalias(String senderalias) {
		this.senderalias = senderalias;
	}
	/**
	 * 获取：
	 */
	public String getSenderalias() {
		return senderalias;
	}
	/**
	 * 设置：
	 */
	public void setMailuser(String mailuser) {
		this.mailuser = mailuser;
	}
	/**
	 * 获取：
	 */
	public String getMailuser() {
		return mailuser;
	}
	/**
	 * 设置：
	 */
	public void setMailpwd(String mailpwd) {
		this.mailpwd = mailpwd;
	}
	/**
	 * 获取：
	 */
	public String getMailpwd() {
		return mailpwd;
	}
	/**
	 * 设置：
	 */
	public void setSmtpssl(Integer smtpssl) {
		this.smtpssl = smtpssl;
	}
	/**
	 * 获取：
	 */
	public Integer getSmtpssl() {
		return smtpssl;
	}
	/**
	 * 设置：
	 */
	public void setSmtpauth(Integer smtpauth) {
		this.smtpauth = smtpauth;
	}
	/**
	 * 获取：
	 */
	public Integer getSmtpauth() {
		return smtpauth;
	}
	/**
	 * 设置：
	 */
	public void setSmsserverip(String smsserverip) {
		this.smsserverip = smsserverip;
	}
	/**
	 * 获取：
	 */
	public String getSmsserverip() {
		return smsserverip;
	}
	/**
	 * 设置：
	 */
	public void setSmsserverport(Integer smsserverport) {
		this.smsserverport = smsserverport;
	}
	/**
	 * 获取：
	 */
	public Integer getSmsserverport() {
		return smsserverport;
	}
	/**
	 * 设置：
	 */
	public void setSmsaccountsid(String smsaccountsid) {
		this.smsaccountsid = smsaccountsid;
	}
	/**
	 * 获取：
	 */
	public String getSmsaccountsid() {
		return smsaccountsid;
	}
	/**
	 * 设置：
	 */
	public void setSmsaccounttoken(String smsaccounttoken) {
		this.smsaccounttoken = smsaccounttoken;
	}
	/**
	 * 获取：
	 */
	public String getSmsaccounttoken() {
		return smsaccounttoken;
	}
	/**
	 * 设置：
	 */
	public void setSmsappid(String smsappid) {
		this.smsappid = smsappid;
	}
	/**
	 * 获取：
	 */
	public String getSmsappid() {
		return smsappid;
	}
}
