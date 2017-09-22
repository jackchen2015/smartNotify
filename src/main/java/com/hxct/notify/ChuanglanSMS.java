package com.hxct.notify;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.output.ByteArrayOutputStream;

import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ChuanglanSMS {
	private CloseableHttpClient client;
	private String sendurl;
	private String account;
	private String password;
//	private static final String SEND_URL="http://222.73.117.138:7891/mt";
	private static final String QUERY_URL="http://222.73.117.138:7891/bi";
	private static final String INTERNATIONAL_URL="http://222.73.117.140:8044/mt";

	public ChuanglanSMS(String sendurl, String account,String password){
		this.sendurl = sendurl;
		this.account = account;
		this.password = password;
		client = HttpClients.createDefault();
	}
	
	/**
	 * 
	 * @param url 应用地址，类似于http://ip:port/msg/
	 * @param account 账号
	 * @param pswd 密码
	 * @param mobile 手机号码，多个号码使用","分割
	 * @param msg 短信内容
	 * @param needstatus 是否需要状态报告，需要true，不需要false
	 * @return 返回值定义参见HTTP协议文档
	 * @throws Exception
	 */
	public String send(String url, String account, String pswd, String mobile, String msg,
	boolean needstatus, String product, String extno) throws Exception {
	HttpClient client = new HttpClient();
	GetMethod method = new GetMethod();
	try {
	URI base = new URI(url, false);
	method.setURI(new URI(base, "HttpBatchSendSM", false));
	method.setQueryString(new NameValuePair[] { 
	new NameValuePair("account", account),
	new NameValuePair("pswd", pswd), 
	new NameValuePair("mobile", mobile),
	new NameValuePair("needstatus", String.valueOf(needstatus)), 
	new NameValuePair("msg", msg),
	new NameValuePair("product", product), 
	new NameValuePair("extno", extno), 
	});
	int result = client.executeMethod(method);
	if (result == HttpStatus.SC_OK) {
	InputStream in = method.getResponseBodyAsStream();
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	byte[] buffer = new byte[1024];
	int len = 0;
	while ((len = in.read(buffer)) != -1) {
	baos.write(buffer, 0, len);
	}
	return URLDecoder.decode(baos.toString(), "UTF-8");
	} else {
	throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
	}
	} finally {
	method.releaseConnection();
	}
	}
	
	public String send(String mobile, String msg) throws Exception{
		return send(sendurl,account,password,mobile,msg,true,null,null);
	}
	

	public CloseableHttpResponse sendMessage(String phone, String content) {
		String encodedContent = null;
		try {
			encodedContent = URLEncoder.encode(content, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		StringBuffer strBuf = new StringBuffer(sendurl);
		strBuf.append("?un=").append(account);
		strBuf.append("&pw=").append(password);
		strBuf.append("&da=").append(phone);
		strBuf.append("&sm=").append(encodedContent);
		strBuf.append("&dc=15&rd=1&rf=2&tf=3");
		HttpGet get = new HttpGet( strBuf.toString() );
		
		try {
			return client.execute(get);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public CloseableHttpResponse queryBalance() {
		StringBuffer strBuf = new StringBuffer(QUERY_URL);
		strBuf.append("?un=").append(account);
		strBuf.append("&pw=").append(password);
		strBuf.append("&rf=2");
		HttpGet get = new HttpGet( strBuf.toString() );
		
		try {
			return client.execute(get);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public CloseableHttpResponse sendInternationalMessage(String phone, String content) {
		String encodedContent = null;
		try {
			encodedContent = URLEncoder.encode(content, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		StringBuffer strBuf = new StringBuffer(INTERNATIONAL_URL);
		strBuf.append("?un=").append(account);
		strBuf.append("&pw=").append(password);
		strBuf.append("&da=").append(phone);
		strBuf.append("&sm=").append(encodedContent);
		strBuf.append("&dc=15&rd=1&rf=2&tf=3");
		HttpGet get = new HttpGet( strBuf.toString() );
		
		try {
			return client.execute(get);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close() {
		if(client != null){
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
