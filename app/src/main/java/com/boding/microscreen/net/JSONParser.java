package com.boding.microscreen.net;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Json对象转换器
 * Created by Jerry on 2015/4/20.
 */
public class JSONParser {
    protected static Gson mGson = new Gson();

    public static String toString(Object obj){
        return mGson.toJson(obj);
    }

    /**
     * @param type 类型反射(Class<?>)或反射令牌(TypeToken)
     * @return Object
     * @throws
     * @Description: 将标准JSON字符串反序列化为对象
     */
    public static Object toObject(String jsonString, Object type) {
        jsonString = jsonString.replace("&nbsp", "");
        jsonString = jsonString.replace("﹠nbsp", "");
        jsonString = jsonString.replace("nbsp", "");
        jsonString = jsonString.replace("&amp;", "");
        jsonString = jsonString.replace("&amp", "");
        jsonString = jsonString.replace("amp", "");
        if (type instanceof Type) {
            try {
                return mGson.fromJson(jsonString, (Type) type);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        } else if (type instanceof Class<?>) {
            try {
                return mGson.fromJson(jsonString, (Class<?>) type);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new RuntimeException("只能是Class<?>或者通过TypeToken获取的Type类型");
        }
    }

    public static Object fromGson(String jsonStr,Object type){

        if (type instanceof Type) {
                return mGson.fromJson(jsonStr, (Type) type);

        } else if (type instanceof Class<?>) {

                return mGson.fromJson(jsonStr, (Class<?>) type);
        }
        return null;
    }

}
