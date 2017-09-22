package com.hxct.util;

public class DataConvert {
	/**

	 * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。   
	 * @param src byte[] data   
	 * @return hex string   
	 */      
	public static String bytesToHexString(byte[] src, boolean capital, boolean space){
	    StringBuilder stringBuilder = new StringBuilder("");
	    if (src == null || src.length <= 0) {
	        return null;
	    }
	    for (int i = 0; i < src.length; i++) {
	        int v = src[i] & 0xFF;
	        String hv = Integer.toHexString(v);
	        if(capital)
	        {
	        	hv = hv.toUpperCase();
	        }
	        if (hv.length() < 2) {
	            stringBuilder.append(0);
	        }
	        stringBuilder.append(hv);
	        if(space)
	        {
	        	stringBuilder.append(" ");
	        }
	    }
	    return stringBuilder.toString();
	}
	
	/**  
	 * Convert hex string to byte[]  
	 * @param hexString the hex string  
	 * @return byte[]  
	 */
	public static byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return null;   
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	}
	/**  
	 * Convert char to byte  
	 * @param c char  
	 * @return byte  
	 */  
	 private static byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	}
	 
	//将指定byte数组以16进制的形式打印到控制台   
	 public static void printHexString( byte[] b) {     
	    for (int i = 0; i < b.length; i++) {    
	      String hex = Integer.toHexString(b[i] & 0xFF);    
	      if (hex.length() == 1) {    
	        hex = '0' + hex;    
	      }    
	      System.out.print(hex.toUpperCase() );    
	    }    
	   
	 }
	 
	 public static String getOsType(String strType)	 {
		 if("ANDROID".equalsIgnoreCase(strType))
		 {
			 return "02";
		 }
		 else if("IOS".equalsIgnoreCase(strType))
		 {
			 return "01";
		 }
		 else if("WINDOW_MOBILE".equalsIgnoreCase(strType))
		 {
			 return "04";
		 }
		 return "99";
	 }
	 
	 public static String convertMacWithSplit(String mac) {
		 if(mac!=null&&!mac.equals("")){
			 if(mac.indexOf("-")<=0){				 
				 String result = mac.substring(0,2);
				 for(int i=1;i<6;i++){
					 result += "-" + mac.substring(i*2, i*2+2);
				 }
				 return result;
			 }
		 }
		 return mac;
	 }
	 
	     public static String byteToHexString(byte[] bytes) {  
		         StringBuffer sb = new StringBuffer(bytes.length);  
		         String sTemp;  
		         for (int i = 0; i < bytes.length; i++) {  
		             sTemp = Integer.toHexString(0xFF & bytes[i]);  
		             if (sTemp.length() < 2)  
		                 sb.append(0);  
		             sb.append(sTemp.toUpperCase());  
		         }  
		         return sb.toString();  
		     }
	 
}
