package com.hxct.util;

import java.text.SimpleDateFormat;

public class Constants {
    /**
     * @param args the command line arguments
     */
    public static final int TIMEOUT = 5000;  //设置接收数据的超时时间  
    public static final int MAXNUM = 5;      //设置重发数据的最多次数 
    public static String password = "c#6zw(qi";
    public static String iv = "salt#&@!";
//    public static String password = "xw$@ghjt";
//    public static String iv = "salt#&@!";
    public static String dePassword = "xw$@xwjm";
    public static String deIv = "pass#&@!";
    public static final char splitChar = '\u0001';
    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
}
