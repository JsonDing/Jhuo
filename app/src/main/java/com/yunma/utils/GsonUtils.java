package com.yunma.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GsonUtils {

	private static Gson gson = null;
	static {
		if (gson == null) {
			gson = new Gson();
		}
	}

	private GsonUtils() {
	}

	/**
	 * 将jsonString转成泛型bean
	 */
	public static <T> T GsonToBean(String jsonString, Class<T> cls) throws Exception {
		T t = null;
		if (gson != null) {
			t = gson.fromJson(jsonString, cls);
		}
		return t;
	}

	/**
	 * 转成list
	 * 解决泛型问题
	 */
	public static <T> List<T> GsonToList(String json, Class<T> cls) {
		Gson gson = new Gson();
		List<T> list = new ArrayList<T>();
		JsonArray array = new JsonParser().parse(json).getAsJsonArray();
		for(final JsonElement elem : array){
			list.add(gson.fromJson(elem, cls));
		}
		return list;
	}

	/**
	 * 转成list中有map的
	 */
	public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
		List<Map<String, T>> list = null;
		if (gson != null) {
			list = gson.fromJson(gsonString,
					new TypeToken<List<Map<String, T>>>() {
					}.getType());
		}
		return list;
	}

	/**
	 * 转成map的
	 */
	public static <T> Map<String, T> GsonToMaps(String gsonString) {
		Map<String, T> map = null;
		if (gson != null) {
			map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
			}.getType());
		}
		return map;
	}


	private static String GsonTokener(String in) {
		// consume an optional byte order mark (BOM) if it exists
		if (in != null && in.startsWith("\ufeff")) {
			in = in.substring(1);
		}
		return in;
	}

}
