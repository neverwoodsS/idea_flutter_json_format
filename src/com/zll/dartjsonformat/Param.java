package com.zll.dartjsonformat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zhangll on 2018/8/3.
 */
public class Param {
    String key;
    String value;
    List<Param> clazz;

    /**
     *
     * @param key 变量名
     * @param object 变量内容
     * @return
     */
    public static Param makeParam(String key, Object object) {
        if (object instanceof JsonObject) {
            JsonObject jsonObject = (JsonObject) object;
            return new Param("object", key, json2Params(jsonObject));
        } else if (object instanceof JsonArray) {
            JsonArray jsonArray = (JsonArray) object;
            if (jsonArray.size() != 0) {
                Object obj = jsonArray.get(0);
                if (obj instanceof JsonObject) {
                    return new Param("list", key, json2Params(jsonArray.get(0).getAsJsonObject()));
                } else {
                    Param temp = makeParam("placeholder", obj);
                    return new Param("List<" + temp.key + ">", key, null);
                }
            } else {
                return new Param("list", key, null);
            }
        } else if (tryParseBoolean(object)) {
            return new Param("bool", key, null);
        } else if (tryParseInt(object)) {
            return new Param("int", key, null);
        } else if (tryParseLong(object)) {
            return new Param("int", key, null);
        } else if (tryParseDouble(object)) {
            return new Param("double", key, null);
        } else if (tryParseFloat(object)) {
            return new Param("double", key, null);
        } else {
            return new Param("String", key, null);
        }
    }

    public static List<Param> json2Params(JsonObject jsonObject) {
        List<Param> list = new ArrayList<>();
        for (Object o : jsonObject.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            list.add(Param.makeParam(entry.getKey().toString(), entry.getValue()));
        }

        return list;
    }

    private static boolean tryParseInt(Object object) {
        try {
            int i = Integer.parseInt(object.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean tryParseLong(Object object) {
        try {
            long i = Long.parseLong(object.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean tryParseDouble(Object object) {
        try {
            double d = Double.parseDouble(object.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean tryParseFloat(Object object) {
        try {
            float f = Float.parseFloat(object.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean tryParseBoolean(Object object) {
        String b = object.toString();
        return Objects.equals(b, "true") || Objects.equals(b, "false");
    }

    public Param(String key, String value, List<Param> clazz) {
        this.key = key;
        this.value = value;
        this.clazz = clazz;
    }

    @Override
    public String toString() {
        return "Param{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", classes=" + clazz +
                '}';
    }
}
