/*
 * Copyright 2015 Hongxin Telecommunication Technologies Co, Ltd.,
 * Wuhan, Hubei, China. All rights reserved.
 */

package com.hxct.notify;

import com.sun.mail.util.MailSSLSocketFactory;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;


/**
 * Java Mail 工具类
 * 
 * @author ChenWei
 * @version 1.0
 * 
 */
public class MailUtils {
	private static String nick;
	static {
		try {
			nick = "东西湖WIFI站点运维中心";
			// nick + from 组成邮箱的发件人信息
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送邮件
	 * 
	 * @param to
	 *            收件人列表，以","分割
	 * @param subject
	 *            标题
	 * @param body
	 *            内容
	 * @param filepath
	 *            附件列表,无附件传递null
	 * @return
	 * @throws MessagingException
	 * @throws AddressException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean sendMail(String host, boolean isSSL, String username, String password, String from, String to, String subject, String body,
			List<String> filepath) throws AddressException, MessagingException,
			UnsupportedEncodingException {
		// 参数修饰
		if (body == null) {
			body = "";
		}
		if (subject == null) {
			subject = "通知";
		}
		// 创建Properties对象
		Properties props = System.getProperties();
		// 创建信件服务器
		props.setProperty("mail.transport.protocol", "smtp");// 设置传输协议
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "false"); // 通过验证
		props.put("mail.debug", "true");//便于调试
		// SSL加密  
		MailSSLSocketFactory sf = null;
		try  
		{
			sf = new MailSSLSocketFactory();
		}
		catch(GeneralSecurityException ex)
		{
			Logger.getLogger(MailUtils.class.getName()).log(Level.SEVERE, null, ex);
		}
		// 设置信任所有的主机  
		sf.setTrustAllHosts(true);  
		props.put("mail.smtp.ssl.enable", "true");  
		props.put("mail.smtp.ssl.socketFactory", sf);  
		// 得到默认的对话对象
//		Session session = Session.getDefaultInstance(props, new Authenticator(){
//		 // 认证信息，需要提供"用户账号","授权码"  
//			public PasswordAuthentication getPasswordAuthentication() {  
//			  return new PasswordAuthentication("hongxintx123@163.com", "jackchen2017");  
//			}  
//		});
		Session session = Session.getDefaultInstance(props,null);
		// 创建一个消息，并初始化该消息的各项元素
		MimeMessage msg = new MimeMessage(session);
		nick = MimeUtility.encodeText(nick);
		//msg.setFrom(new InternetAddress(from));
                msg.setFrom(new InternetAddress(nick+" <"+from+">"));
		// 创建收件人列表
		if (to != null && to.trim().length() > 0) {
			String[] arr = to.split(",");
			int receiverCount = arr.length;
			if (receiverCount > 0) {
				InternetAddress[] address = new InternetAddress[receiverCount];
				for (int i = 0; i < receiverCount; i++) {
					address[i] = new InternetAddress(arr[i]);
				}
				msg.addRecipients(Message.RecipientType.TO, address);
				msg.setSubject(subject);
				// 后面的BodyPart将加入到此处创建的Multipart中
				Multipart mp = new MimeMultipart();
				// 附件操作
				if (filepath != null && filepath.size() > 0) {
					for (String filename : filepath) {
						MimeBodyPart mbp = new MimeBodyPart();
						// 得到数据源
						FileDataSource fds = new FileDataSource(filename);
						// 得到附件本身并至入BodyPart
						mbp.setDataHandler(new DataHandler(fds));
						// 得到文件名同样至入BodyPart
						mbp.setFileName(fds.getName());
						mp.addBodyPart(mbp);
					}
					MimeBodyPart mbp = new MimeBodyPart();
					mbp.setText(body);
					mp.addBodyPart(mbp);
					// 移走集合中的所有元素
					filepath.clear();
					// Multipart加入到信件
					msg.setContent(mp);
				} else {
					// 设置邮件正文
					msg.setText(body);
				}
				// 设置信件头的发送日期
				msg.setSentDate(new Date());
				msg.saveChanges();
				// 发送信件
				Transport transport = session.getTransport("smtp");
				transport.connect(host, username, password);
				transport.sendMessage(msg,
						msg.getRecipients(Message.RecipientType.TO));
//				transport.sendMessage(msg,
//						msg.getAllRecipients());
				transport.close();
				return true;
			} else {
				System.out.println("None receiver!");
				return false;
			}
		} else {
			System.out.println("None receiver!");
			return false;
		}
	}
	
	public static void sendMail2() throws AddressException, MessagingException
	{
/*
         * 1. 得到session
         */
        Properties props = new Properties();
        props.setProperty("mail.host", "smtp.163.com");
        props.setProperty("mail.smtp.auth", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("xxxxxx", "xxx");
            }
        };

        Session session = Session.getInstance(props, auth);

        /*
         * 2. 创建MimeMessage
         */
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("hongxintx123@163.com"));//设置发件人
        msg.setRecipients(RecipientType.TO, "chenweia@hxct.com");//设置收件人
        msg.setRecipients(RecipientType.CC, "941219887@qq.com");//设置抄送
        msg.setRecipients(RecipientType.BCC, "chenw_2002@163.com");//设置暗送

        msg.setSubject("这是来自ITCAST的测试邮件");
        msg.setContent("这就是一封垃圾邮件！", "text/html;charset=utf-8");

        /*
         * 3. 发
         */
        Transport.send(msg);		
	}	

	public static void main(String[] args) throws AddressException,
			UnsupportedEncodingException, MessagingException {
		List<String> filepath = new ArrayList<>();
//		filepath.add("E:\\Resources\\Development Test\\AuctionServer\\src\\main\\java\\com\\auction\\dao\\IBaseDAO.java");
//		filepath.add("E:\\Resources\\Development Test\\AuctionServer\\src\\main\\java\\com\\auction\\dao\\IMemcacheDAO.java");
		sendMail("",true,"","","","941219887@qq.com", "作息时间调整通知", "同事按上级安排公司于2017年9月2日进行作息时间调整，具体安排如下：上午：9:00~11:30 下午：13:30~17:00",filepath);
//		sendMail2();
	}
}
