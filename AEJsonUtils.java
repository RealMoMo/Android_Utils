package com.realmo.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * JsonUtils 用于json的解析和数据提取
 * 
 * 将字符串转换成JSONObject格式 从JSONObject中读取和json字符串中读取各种数据类型�?
 * 整型（integer），浮点型（double）， 布尔型（boolean），字符串（string），
 * 数组（JSONArray），字典（JSONObject�?
 * 
 */
public class AEJsonUtils {

	public static final int NULLJSONLEN = 2;



	/**
	 * 将json字符串转换成JSONObject格式
	 * 
	 * @return JSONObject
	 */
	static public JSONObject getJsonFromString(String data) {

		JSONObject jsonObject = null;
		if (null != data && !"".equals(data) && data.length() > NULLJSONLEN) {
			try {
				jsonObject = new JSONObject(data);

			} catch (JSONException e) {
				// key not found
				System.out.println("返回值不是json");
			}
		}

		return jsonObject;

	}

	/**
	 * 根据key从json中获取相应的String
	 * 
	 * @param data
	 *            json数据�?
	 * @param key
	 *            键字
	 * 
	 * @return String
	 */
	static public String getString(String data, String key) {

		// 将字符串解析成JSONObject
		JSONObject jsonObject = getJsonFromString(data);
		return getString(jsonObject,key);

	}
	static public String getString(JSONObject jsonObject, String key) {

		String json = null;
		// 将字符串解析成JSONObject
		
		if (jsonObject != null) { // 判断jsonObject是否为空
			try {
				json = jsonObject.getString(key).trim();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("该key值解析成字符串有错误�?");
				e.printStackTrace();
			}
		}

		return json;

	}

	/**
	 * 根据key从json中获取相应的double 类型values
	 * 
	 * @param data
	 *            json数据�?
	 * @param key
	 *            要获取的key关键�?
	 * 
	 * @return double
	 */
	static public double getDouble(String data, String key) {

		// 将字符串解析成JSONObject
		JSONObject jsonObject = getJsonFromString(data);
		return getDouble(jsonObject,key);

	}
	static public double getDouble(JSONObject jsonObject, String key) {

		double json = 0.0f;
		// 将字符串解析成JSONObject
		
		if (jsonObject != null) { // 判断jsonObject是否为空
			try {
				json = jsonObject.getDouble(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("该key值解析成double有错误�??");
				e.printStackTrace();
			}
		}

		return json;

	}

	/**
	 * 根据key从json中获取相应的int 类型values
	 * 
	 * @param data
	 *            json数据�?
	 * @param key
	 *            要获取的key关键�?
	 * 
	 * @return int
	 */
	static public int getInt(String data, String key) {

		// 将字符串解析成JSONObject
		JSONObject jsonObject = getJsonFromString(data);
		return getInt(jsonObject,key);

	}
	static public int getInt(JSONObject jsonObject, String key) {

		int json = 0;
		// 将字符串解析成JSONObject
		
		if (jsonObject != null) { // 判断jsonObject是否为空
			try {
				json = jsonObject.getInt(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("该key值解析成int有错误�??");
				e.printStackTrace();
			}
		}

		return json;

	}

	/**
	 * 根据key从json中获取相应的Boolean 类型values
	 * 
	 * @param data
	 *            json数据�?
	 * @param key
	 *            要获取的key关键�?
	 * 
	 * @return Boolean
	 */
	static public boolean getBoolean(String data, String key) {

		// 将字符串解析成JSONObject
		JSONObject jsonObject = getJsonFromString(data);
		return getBoolean(jsonObject,key);

	}
	static public boolean getBoolean(JSONObject jsonObject, String key) {
		
		boolean json = false;
		// 将字符串解析成JSONObject
		
		if (jsonObject != null) { // 判断jsonObject是否为空
			try {
				json = jsonObject.getBoolean(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("该key值解析成bool有错误�??");
				e.printStackTrace();
			}
		}

		return json;
		
	}

	/**
	 * 根据key从json中获取相应的JsonArray
	 * 
	 * @param data
	 *            json数据�?
	 * @param key
	 *            要获取的key关键�?
	 * 
	 * @return JsonArray
	 */
	static public JSONArray getJsonArray(String data, String key) {
		
		// 将字符串解析成JSONObject
		JSONObject jsonObject = getJsonFromString(data);
		return getJsonArray(jsonObject , key);

	}
	static public JSONArray getJsonArray(JSONObject jsonObject, String key){

		JSONArray json = null;
		
		if (jsonObject != null) { // 判断jsonObject是否为空
			try {
				json = jsonObject.getJSONArray(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("该key值解析成array有错误??");
				e.printStackTrace();
			}
		}

		return json;
		
	}


	/**
	 * 根据key从json中获取相应的Object 类型values
	 * 
	 * @param data
	 *            json数据�?
	 * @param key
	 *            要获取的key关键�?
	 * 
	 * @return Object
	 */
	static public JSONObject getJsonObject(String data, String key) {

		// 将字符串解析成JSONObject
		JSONObject jsonObject = getJsonFromString(data);
		return getJsonObject( jsonObject,  key);

	}
	static public JSONObject getJsonObject(JSONObject jsonObject, String key){

		JSONObject json = null;
		
		if (jsonObject != null) { // 判断jsonObject是否为空
			try {
				json = jsonObject.getJSONObject(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("该key值解析成object有错误�??");
				e.printStackTrace();
			}
		}

		return json;
		
	}
	
	
	static public JSONObject getJSONObject(JSONArray jsonArray, int index) {
		JSONObject object = null;
		try {
			object = jsonArray.getJSONObject(index);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return object;
	}
	
	static public String getString(JSONArray jsonArray, int index) {
		String object = null;
		try {
			object = jsonArray.getString(index);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return object;
	}

	/**
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	static public long getLong(String data, String key) {

		// 将字符串解析成JSONObject
		JSONObject jsonObject = getJsonFromString(data);
		return getInt(jsonObject,key);

	}
	static public long getLong(JSONObject jsonObject, String key) {

		long json = 0;
		// 将字符串解析成JSONObject
		
		if (jsonObject != null) { // 判断jsonObject是否为空
			try {
				json = jsonObject.getLong(key);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("该key值解析成int有错误�??");
				e.printStackTrace();
			}
		}

		return json;

	}

}
