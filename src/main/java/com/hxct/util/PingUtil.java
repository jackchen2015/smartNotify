/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hxct.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author 0310002487
 */
public class PingUtil {
    
    public static StringBuilder ping(String hostIp, int times){

        BufferedReader br = null;
        StringBuilder sb=new StringBuilder();
        try {

            String osName = System.getProperty("os.name");//获取操作系统类型
            String command = "";
            if(osName.contains("Linux")){
                command = "ping -c "+times+" -i 1 "+hostIp; //ping参数 ping3次 times = 3
            }else{
                command = "ping -n "+times+" -w 1 "+hostIp;
            }
            Process p = Runtime.getRuntime().exec(command);
            br = new BufferedReader(new InputStreamReader(p.getInputStream(),"gbk"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return sb;
    }

    /**
     * 根据ping返回信息判断有没有ping成功
     * @param hostIp  ping返回的结果
     * @return  ping成功返回true
     * 失败返回false
     */
    public static boolean pingAcce(String hostIp, int times){
        boolean res=false;
        StringBuilder line = ping(hostIp, times);
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)|(ttl=\\d+)",Pattern.CASE_INSENSITIVE);
        Matcher matcher =pattern.matcher(line);
        int count = 0;
        while(matcher.find()){
//            res=true;
            count++;
        }
        if(count*100/times>35){
            res = true;
        }
        if(!res){
            System.out.println("hostIp "+hostIp+", ping fail");
        }
        return res;
    }
}
