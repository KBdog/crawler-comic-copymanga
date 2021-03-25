package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/*
json工具
 */
public class JSONTool {
    /*
    格式化json
     */
    public static String formatJSON(JSON json){
        return JSON.toJSONString(json, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
    }
    /*
    @param String key,JSONObject object
    根据key值获得jsonObject
     */
    public static JSONObject getJSONObject(String key,JSONObject object){
        return object.getJSONObject(key);
    }
    /*
    @param String key,JSONObject object
    根据key值获得jsonArray
     */
    public static JSONArray getJSONArray(String key,JSONObject object){
        return object.getJSONArray(key);
    }
}
