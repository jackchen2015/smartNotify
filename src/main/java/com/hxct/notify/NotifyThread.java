/*
 * Copyright 2015 Hongxin Telecommunication Technologies Co, Ltd.,
 * Wuhan, Hubei, China. All rights reserved.
 */
package com.hxct.notify;

import com.hxct.entity.GateWayEntity;
import com.hxct.entity.AlertnotifyEntity;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author chenwei Created on 2017-8-16, 15:49:36
 */
public class NotifyThread implements Runnable {

    private QueueLink<GateWayEntity> allNotifys;
    private int pollTimes;
    private ChuanglanSMS client;
    private Map<String, Integer> gwTimes = new HashMap<String, Integer>();
    private Map<String, String> gwNotifyDate = new HashMap<String, String>();
    private AlertnotifyEntity altNotify;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final JTable logTable;

    public NotifyThread(JTable logTable, QueueLink<GateWayEntity> queue, int pollTimes, AlertnotifyEntity altNotify) {
        this.logTable = logTable;
        this.allNotifys = queue;
        this.pollTimes = pollTimes;
        this.altNotify = altNotify;
        client = new ChuanglanSMS(altNotify.getSmssendurl(), altNotify.getSmsaccount(), altNotify.getSmspassword());
    }

    @Override
    public void run() {
        while (true) {
            if (!allNotifys.isEmpty()) {
                Object o = allNotifys.pop();
                if(!(o instanceof GateWayEntity)){
                    continue;
                }
                GateWayEntity notify = (GateWayEntity) o;
                String gwip = notify.getIpaddress();

                try {
                    Integer time = 0;
                    if (gwTimes.containsKey(gwip)) {
                        time = gwTimes.get(gwip);
                    }
                    time++;
                    gwTimes.put(gwip, time);
                    if (time >= pollTimes) {
                        String loc = notify.getLocation();
                        String gwname = notify.getName();

                        int type = altNotify.getType();
                        String host = altNotify.getSmtpserver();
                        boolean isSSL = altNotify.getSmtpssl() == 1 ? true : false;
                        String username = altNotify.getMailsender();
                        String password = altNotify.getMailpwd();
                        String from = altNotify.getMailsender();
                        String emailTitle = altNotify.getMailtitle();
                        String emailMsg = altNotify.getMailbody();
                        String emails = altNotify.getMailrecv();

                        String mobiles = altNotify.getSmsrecvnumber();
                        String smsMsg = altNotify.getSmsmsg();

                        /**
                         * 同一天同一台设备只发一条告警
                         */
//                        String currentDate = sdf.format(new Date());
//                        String date = gwNotifyDate.get(gwip);
//                        if (date != null) {
//                            if (currentDate.equals(date)) {
//                                gwTimes.put(gwip, 0);
//                                continue;
//                            }
//                        }
//                        gwNotifyDate.put(gwip, currentDate);
                        
                        if (type == 0 || type == 2) {
                            String title = emailTitle.replaceAll("\\$spotname\\$", "'" + gwname + "'");
                            String details = "时间: "+ sdfTime.format(new Date()) + ", " + emailMsg.replaceAll("\\$spotname\\$", "'" + gwname + "'").replaceAll("\\$setuploc\\$", "'" + loc + "'").replaceAll("\\$ip\\$", "'" + gwip + "'");
                            MailUtils.sendMail(host, isSSL, username, password, from, emails, title, details, null);
                        }
                        if (type == 1 || type == 2) {
                            String newStr = smsMsg.replaceAll("\\$spotname\\$", "'" + gwname + "'").replaceAll("\\$setuploc\\$", "'" + loc + "'").replaceAll("\\$ip\\$", "'" + gwip + "'");
                            client.sendUseNew(altNotify.getSmssendurl(), altNotify.getSmsaccount(), altNotify.getSmspassword(), altNotify.getSmsrecvnumber(), newStr,
                                    true, "", "2020");
                        }
                        String typeStr = "";
                        switch(type){
                            case 0:
                                typeStr = "邮件";
                                break;
                            case 1:
                                typeStr = "短信";
                                break;
                            case 2:
                                typeStr = "短信和邮件";
                                break;
                            case 3:
                                typeStr = "无";
                                break;
                            default:
                                break;
                        }
                        ((DefaultTableModel)logTable.getModel()).addRow(new Object[]{sdfTime.format(new Date()), typeStr, gwip, loc, true});                        

                        gwTimes.put(gwip, 0);
                    }
                } catch (MessagingException ex) {
                    Logger.getLogger(NotifyThread.class.getName()).
                            log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(NotifyThread.class.getName()).
                            log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(NotifyThread.class.getName()).
                            log(Level.SEVERE, null, ex);
                }

            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(NotifyThread.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
    }

}
