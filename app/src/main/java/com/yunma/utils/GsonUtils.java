package com.yunma.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonUtils {
	
	 public static <T> T getObject(String jsonString, Class<T> cls) throws Exception {
	        T t;
	            Gson gson = new Gson();
	            t = gson.fromJson(jsonString, cls);
	        return t;
	    }

	public static <T> List<T> getList(String jsonString, Class<T> cls) {
	        List<T> list = new ArrayList<>();
	        try {
	            Gson gson = new Gson();
	            list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
	            }.getType());
	        } catch (Exception e) {
	        }
	        return list;
	    }
	public static List<Map<String, Object>> listKeyMaps(String jsonString) {
	        List<Map<String, Object>> list = new ArrayList<>();
	        try {
	            Gson gson = new Gson();
	            list = gson.fromJson(jsonString,
	                    new TypeToken<List<Map<String, Object>>>() {
	                    }.getType());
	        } catch (Exception e) {
	            // TODO: handle exception
	        }
	        return list;
	    }
	/**
	 * 将实体类转换成json字符串对象            注意此方法需要第三方gson  jar包
	 * @param obj  对象
	 * @return  map
	 */
	public static String toJson(Object obj,int method) {
		// TODO Auto-generated method stub
		if (method==1) {
			Gson gson = new Gson();
			String obj2 = gson.toJson(obj);
			return obj2;
		}else if(method==2){
			Gson gson2=new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
			String obj2=gson2.toJson(obj);
			return obj2;
		}
		return "";
	}

}
