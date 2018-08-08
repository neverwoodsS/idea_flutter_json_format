package com.zll.dartjsonformat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangll on 2018/8/3.
 */
public class ClassMaker {

    private Map<String, List<Param>> classes = new HashMap<>();

    public String make(String text) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(text).getAsJsonObject();
            List<Param> fields = Param.json2Params(jsonObj);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("class ").append("Temp {\n")
                    .append(printClassWithParams(fields, 2, "Temp"))
                    .append("\n}\n")
                    .append(buildClasses());

            return stringBuilder.toString();
        } catch (JsonParseException jsonParseException) {
            jsonParseException.printStackTrace();

            return "error: not supported json";
        } catch (IllegalStateException illegalStateException) {
            illegalStateException.printStackTrace();

            if (illegalStateException.getMessage().startsWith("Not a JSON Object")) {
                return "error: not supported json";
            } else {
                return "error: unknown";
            }
        }
    }

    /**
     * dart （似乎）不支持内部类，所以需要在最外部生成各个 class
     * @return
     */
    private String buildClasses() {
        StringBuilder sb = new StringBuilder();
        // 开始定义类
        for (Map.Entry<String, List<Param>> entry : classes.entrySet()) {
            sb.append("\n")
                    .append("class").append(" ").append(entry.getKey()).append(" ").append("{").append("\n")
                    .append(printClassWithParams(entry.getValue(), 2, entry.getKey()))
                    .append("}").append("\n");
        }

        return sb.toString();
    }

    /**
     * 根据参数生成对应类的代码
     * @param params 类中的参数
     * @param space 缩进格数
     * @param className 要生成的 class 的名字
     * @return
     */
    private String printClassWithParams(List<Param> params, int space, String className) {
        StringBuilder sb = new StringBuilder();

        List<String> orderedList = new ArrayList<>(); // 基本类型
        List<NameValuePair> listList = new ArrayList<>(); // list<对象>
        List<NameValuePair> objectList = new ArrayList<>(); // 对象类型
        List<NameValuePair> listBaseList = new ArrayList<>(); // list<基本类型>
        Map<String, List<Param>> tempClasses = new HashMap<>(); // 统计子类

        String spaceStr = "";
        for (int i = 0; i < space; i++) {
            spaceStr += " ";
        }

        /* 基本类型参数声明与统计 start **/
        for (Param param : params) {
            if ("String".equals(param.key)) {
                sb.append(spaceStr).append("String").append(" ").append(param.value).append(";").append("\n");
                orderedList.add(param.value);
            }
        }

        for (Param param : params) {
            if ("int".equals(param.key)) {
                sb.append(spaceStr).append("int").append(" ").append(param.value).append(";").append("\n");
                orderedList.add(param.value);
            }
        }

        for (Param param : params) {
            if ("double".equals(param.key)) {
                sb.append(spaceStr).append("double").append(" ").append(param.value).append(";").append("\n");
                orderedList.add(param.value);
            }
        }

        for (Param param : params) {
            if ("bool".equals(param.key)) {
                sb.append(spaceStr).append("bool").append(" ").append(param.value).append(";").append("\n");
                orderedList.add(param.value);
            }
        }
        /* 基本类型参数声明与统计 end **/

        /* 对象类型参数声明与统计 start **/
        for (Param param : params) {
            if ("object".equals(param.key)) {
                String clazzName = Util.toUpperCaseFirstOne(param.value + "Bean");
                classes.put(clazzName, param.clazz);
                tempClasses.put(clazzName, param.clazz);
                sb.append(spaceStr).append(clazzName).append(" ").append(param.value).append(";").append("\n");
                objectList.add(new NameValuePair(clazzName, param.value));
            }
        }
        /* 对象类型参数声明与统计 end **/

        /* 基本类型 list 参数声明与统计 start **/
        for (Param param : params) {
            if (param.key.startsWith("List<")) {
                sb.append(spaceStr).append(param.key).append(" ").append(param.value).append(";").append("\n");
                listBaseList.add(new NameValuePair(param.key, param.value));
            }
        }
        /* 基本类型 list 参数声明与统计 end **/

        /* 对象类型 list 参数声明与统计 start **/
        for (Param param : params) {
            if ("list".equals(param.key)) {
                String clazzName = Util.toUpperCaseFirstOne(param.value + "ListBean");
                classes.put(clazzName, param.clazz);
                tempClasses.put(clazzName, param.clazz);
                sb.append(spaceStr).append("List<").append(clazzName).append(">").append(" ").append(param.value).append(";").append("\n");
                listList.add(new NameValuePair(clazzName, param.value));
            }
        }
        /* 对象类型 list 参数声明与统计 end **/

        String tempSpaceStr = spaceStr + "  ";
        int childSize = orderedList.size() + objectList.size() + listList.size() + listBaseList.size();

        if (childSize != 0) {
            // 构造器
//            sb.append("\n")
//                    .append(spaceStr).append(className).append("({");
//
//            for (String value : orderedList) {
//                sb.append("\n").append(tempSpaceStr).append("this.").append(value).append(",");
//            }
//            for (NameValuePair pair : objectList) {
//                sb.append("\n").append(tempSpaceStr).append("this.").append(pair.value).append(",");
//            }
//            for (NameValuePair pair : listList) {
//                sb.append("\n").append(tempSpaceStr).append("this.").append(pair.value).append(",");
//            }
//            for (NameValuePair pair : listBaseList) {
//                sb.append("\n").append(tempSpaceStr).append("this.").append(pair.value).append(",");
//            }
//
//            sb.append("\n").append(spaceStr).append("});\n");
        }

        /* map.value 转换为对象的静态函数 start **/
        String fieldName = Util.toLowerCaseFirstOne(className);
        sb.append("\n").append(spaceStr)
                .append("static ").append(className).append(" fromMap").append("(Map<String, dynamic> map) {")
                .append("\n").append(tempSpaceStr)
                .append(className).append(" ").append(fieldName).append(" = new ").append(className).append("();")
        ;

        for (String value : orderedList) {
            sb.append("\n").append(tempSpaceStr).append(fieldName).append(".").append(value).append(" = ").append("map['").append(value).append("'];");
        }
        for (NameValuePair pair : objectList) {
            sb.append("\n").append(tempSpaceStr).append(fieldName).append(".").append(pair.value).append(" = ").append(pair.name).append(".fromMap(map['").append(pair.value).append("']);");
        }
        for (NameValuePair pair : listList) {
            sb.append("\n").append(tempSpaceStr).append(fieldName).append(".").append(pair.value).append(" = ").append(pair.name).append(".fromMapList(map['").append(pair.value).append("']);");
        }

        /* map.value 转换为基础类型 list start **/
        int count = 0;
        for (NameValuePair pair : listBaseList) {
            sb.append("\n");
            sb.append("\n").append(tempSpaceStr).append("List<dynamic> dynamicList").append(count).append(" = map['").append(pair.value).append("'];");
            sb.append("\n").append(tempSpaceStr).append(fieldName).append(".").append(pair.value).append(" = new List();");

            String function = "o.toString()";
            switch (pair.name) {
                case "List<int>":
                    function = "int.parse(o.toString())";
                    break;
                case "List<double>":
                    function = "double.parse(o.toString())";
                    break;
                case "List<bool>":
                    function = "o.toString() == 'true'";
                    break;
            }

            sb.append("\n").append(tempSpaceStr).append(fieldName).append(".").append(pair.value).append(".addAll(dynamicList").append(count).append(".map((o) => ").append(function).append("));");
            sb.append("\n");
            count++;
        }
        /* map.value 转换为基础类型 list end **/

        sb.append("\n").append(tempSpaceStr).append("return ").append(fieldName).append(";\n");
        sb.append(spaceStr).append("}\n");
        /* map.value 转换为对象的静态函数 end **/

        /* map.value 转换为 list 的静态函数 start **/
        sb.append("\n").append(spaceStr)
                .append("static ").append("List<").append(className).append(">").append(" fromMapList").append("(dynamic mapList) {")
                .append("\n").append(tempSpaceStr).append("List<").append(className).append("> list = new List(mapList.length);")
                .append("\n").append(tempSpaceStr).append("for (int i = 0; i < mapList.length; i++) {")
                .append("\n").append(tempSpaceStr).append("  ").append("list[i] = fromMap(mapList[i]);")
                .append("\n").append(tempSpaceStr).append("}")
                .append("\n").append(tempSpaceStr).append("return list;")
                .append("\n").append(spaceStr).append("}")
                .append("\n");
        /* map.value 转换为 list 的静态函数 end **/

        // 遍历类中类（主要目的是添加进 classes 统计，而不 append）
        // 还是由于 dart 不支持内部类导致的
        for (Map.Entry<String, List<Param>> entry : tempClasses.entrySet()) {
            printClassWithParams(entry.getValue(), space + 2, entry.getKey());
        }

        return sb.toString();
    }
}