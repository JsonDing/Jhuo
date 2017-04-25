package com.yunma.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * MD5
 */
public class MD5Utils {
	
	public static String getMD5(String data)  {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("Md5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] md5Bytes = null;
		try {
			assert digest != null;
			md5Bytes = digest.digest(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb  = new StringBuilder();
		assert md5Bytes != null;
		for(byte b : md5Bytes){
			if((b & 0xff) < 0x10)
				sb.append("0");
			sb.append(Integer.toHexString((b & 0xff)));
		}
		return sb.toString();
	}


}
