package com.hxct.notify;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class TestMain {

	public static void main(String[] args) {
		//虹信通信
		ChuanglanSMS client = new ChuanglanSMS("url","xxxxxx","xxxxxx");

		String hp = "热点11";
		String loc = "位置21";
		String mac = "xx-xx-xx-xx-xx-xx";
		String smsMsg ="AP离线告警! 位于$hotspot$,安装位置在$setuploc$,MAC为$macaddr$,请尽快修复!";
		String newStr = smsMsg.replaceAll("\\$hotspot\\$", "'"+hp+"'").replaceAll("\\$setuploc\\$", "'"+loc+"'").replaceAll("\\$macaddr\\$", "'"+mac+"'");
		try{
		client.send("18502706580,15337391976",newStr);
		}catch(Exception e)
		{
				e.printStackTrace();
		}
		CloseableHttpResponse response = null;
		try {
			//发送短信
			response = client.sendMessage("18502706580,15337391976","AP离线告警!位于'湖北省人才市场热点1',安装位置在三楼左侧大厅,MAC为8c:79:67:01:dd:22,请尽快修复!");
			
			if(response != null && response.getStatusLine().getStatusCode()==200){
				System.out.println(EntityUtils.toString(response.getEntity()));
			}
	
			//查询余额
			response = client.queryBalance();
			if(response != null && response.getStatusLine().getStatusCode()==200){
				System.out.println(EntityUtils.toString(response.getEntity()));
			}
	
			//发送国际验证码
			response = client.sendInternationalMessage("13612345678","【创蓝文化】您的验证码是：1234567");
			if(response != null && response.getStatusLine().getStatusCode()==200){
				System.out.println(EntityUtils.toString(response.getEntity()));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.close();
	}

}
