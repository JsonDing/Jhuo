package com.yunma.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

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

	public static String makeIdToBase64(Context context,String id){
		String enId = "" ;
		if(!TextUtils.isEmpty(id)){
			enId = new String(Base64.encode(id.getBytes(), Base64.DEFAULT));
		} else {
			ToastUtils.showShort(context,"数据解析出错!");
		}
		return enId;
	}

    public static String getIdFromBase64(String base64Id){
        String result ="";
		if(!TextUtils.isEmpty(base64Id)){
			result = new String(Base64.decode(base64Id.getBytes(),Base64.DEFAULT));
		}
        return result;
    }
}
