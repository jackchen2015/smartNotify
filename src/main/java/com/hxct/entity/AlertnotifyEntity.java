package com.hxct.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-29 16:43:14
 */
public class AlertnotifyEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//类型(0:email 1:sms 2:both 3:none)
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
	private String mailrecv;
	//
	private String mailtitle;
	//
	private String mailbody;
	//
	private String smsserverip;
	//
	private Integer smsserverport;
	//
	private String smssendurl;
	//
	private String smsaccount;
	//
	private String smspassword;
	//
	private String smsrecvnumber;
	//
	private String smsmsg;

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
	public void setMailrecv(String mailrecv) {
		this.mailrecv = mailrecv;
	}
	/**
	 * 获取：
	 */
	public String getMailrecv() {
		return mailrecv;
	}
	/**
	 * 设置：
	 */
	public void setMailtitle(String mailtitle) {
		this.mailtitle = mailtitle;
	}
	/**
	 * 获取：
	 */
	public String getMailtitle() {
		return mailtitle;
	}
	/**
	 * 设置：
	 */
	public void setMailbody(String mailbody) {
		this.mailbody = mailbody;
	}
	/**
	 * 获取：
	 */
	public String getMailbody() {
		return mailbody;
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
	public void setSmssendurl(String smssendurl) {
		this.smssendurl = smssendurl;
	}
	/**
	 * 获取：
	 */
	public String getSmssendurl() {
		return smssendurl;
	}
	/**
	 * 设置：
	 */
	public void setSmsaccount(String smsaccount) {
		this.smsaccount = smsaccount;
	}
	/**
	 * 获取：
	 */
	public String getSmsaccount() {
		return smsaccount;
	}
	/**
	 * 设置：
	 */
	public void setSmspassword(String smspassword) {
		this.smspassword = smspassword;
	}
	/**
	 * 获取：
	 */
	public String getSmspassword() {
		return smspassword;
	}
	/**
	 * 设置：
	 */
	public void setSmsrecvnumber(String smsrecvnumber) {
		this.smsrecvnumber = smsrecvnumber;
	}
	/**
	 * 获取：
	 */
	public String getSmsrecvnumber() {
		return smsrecvnumber;
	}
	/**
	 * 设置：
	 */
	public void setSmsmsg(String smsmsg) {
		this.smsmsg = smsmsg;
	}
	/**
	 * 获取：
	 */
	public String getSmsmsg() {
		return smsmsg;
	}
}
