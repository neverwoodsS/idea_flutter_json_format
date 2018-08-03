package com.zll.dartjsonformat;

import com.google.gson.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBScrollPane;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by zhangll on 2018/7/31.
 */
public class UiBuilder {
    private VirtualFile file;

    private JLabel label;
    private JTextArea jsonText;

    private Map<String, List<CustomField>> classes = new HashMap<>();

    public UiBuilder(VirtualFile file) {
        this.file = file;
    }

    public JComponent build() {
        JPanel panel = new JPanel();
        placeComponents(panel);
//        panel.setBounds(0, 0, 700, 450);
        return panel;
    }

    private void placeComponents(JPanel panel) {
        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);

        label = new JLabel("input your json and click ok, \ndart class contents will be copied to Clipboard");
        label.setBounds(10, 0, 500, 40);

        /*
         * 创建文本域用于用户输入
         */
        jsonText = new JTextArea();
        jsonText.setBounds(0,50,700,400);

        JBScrollPane jbScrollPane = new JBScrollPane(jsonText);
        jbScrollPane.setVerticalScrollBarPolicy(JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jbScrollPane.setHorizontalScrollBarPolicy(JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jbScrollPane.setBounds(0, 50, 700, 400);

        // 创建按钮
        JButton okButton;
        okButton = new JButton("ok");
        okButton.setBounds(600, 10, 80, 30);
        okButton.setVisible(true);
        okButton.addActionListener(arg0 -> string2Json(jsonText.getText()));

        panel.add(okButton);
//        panel.add(jsonText);
        panel.add(jbScrollPane);
        panel.add(label);
    }

    private void string2Json(String text) {
        try {
            if (label != null) {
                label.setText("input your json and click ok, \ndart classes will be copied to Clipboard");
            }

            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(text).getAsJsonObject();
            List<CustomField> fields = json2Fields(jsonObj);

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("class ").append("Temp {\n")
                    .append(printCustomFields(fields, 2, "Temp"))
                    .append("\n}\n")
                    .append(buildClasses());

            setSysClipboardText(stringBuilder.toString());
            writeToFile(stringBuilder.toString());

            if (label != null) {
                label.setText("dart classes have been copied to Clipboard automatically");
            }
//            if (jsonText != null) {
//                jsonText.setText(stringBuilder.toString());
//            }
        } catch (JsonParseException jsonParseException) {
            jsonParseException.printStackTrace();
            if (label != null) {
                label.setText("not supported json");
            }
        } catch (IllegalStateException illegalStateException) {
            illegalStateException.printStackTrace();
            if (illegalStateException.getMessage().startsWith("Not a JSON Object")) {
                if (label != null) {
                    label.setText("not supported json");
                }
            }
        }
    }

    private String buildClasses() {
        StringBuilder sb = new StringBuilder();
        // 开始定义类
        for (Map.Entry<String, List<CustomField>> entry : classes.entrySet()) {
            sb.append("\n")
                    .append("class").append(" ").append(entry.getKey()).append(" ").append("{").append("\n")
                    .append(printCustomFields(entry.getValue(), 2, entry.getKey()))
                    .append("}").append("\n");
        }

        return sb.toString();
    }

    private String printCustomFields(List<CustomField> fields, int space, String className) {
        StringBuilder sb = new StringBuilder();
        List<String> orderedList = new ArrayList<>();
        List<NameValuePair> listList = new ArrayList<>();
        List<NameValuePair> objectList = new ArrayList<>();
        List<NameValuePair> listBaseList = new ArrayList<>();
        Map<String, List<CustomField>> tempClasses = new HashMap<>();

        String spaceStr = "";
        for (int i = 0; i < space; i++) {
            spaceStr += " ";
        }

        for (CustomField field : fields) {
            if ("String".equals(field.key)) {
                sb.append(spaceStr).append("String").append(" ").append(field.value).append(";").append("\n");
                orderedList.add(field.value);
            }
        }

        for (CustomField field : fields) {
            if ("int".equals(field.key)) {
                sb.append(spaceStr).append("int").append(" ").append(field.value).append(";").append("\n");
                orderedList.add(field.value);
            }
        }

        for (CustomField field : fields) {
            if ("double".equals(field.key)) {
                sb.append(spaceStr).append("double").append(" ").append(field.value).append(";").append("\n");
                orderedList.add(field.value);
            }
        }

        for (CustomField field : fields) {
            if ("bool".equals(field.key)) {
                sb.append(spaceStr).append("bool").append(" ").append(field.value).append(";").append("\n");
                orderedList.add(field.value);
            }
        }

        for (CustomField field : fields) {
            if ("object".equals(field.key)) {
                String clazzName = toUpperCaseFirstOne(field.value + "Bean");
                classes.put(clazzName, field.clazz);
                tempClasses.put(clazzName, field.clazz);
                sb.append(spaceStr).append(clazzName).append(" ").append(field.value).append(";").append("\n");
//                orderedList.add(field.value);
                objectList.add(new NameValuePair(clazzName, field.value));
            }
        }

        for (CustomField field : fields) {
            if (field.key.startsWith("List<")) {
                sb.append(spaceStr).append(field.key).append(" ").append(field.value).append(";").append("\n");
                listBaseList.add(new NameValuePair(field.key, field.value));
            }
        }

        for (CustomField field : fields) {
            if ("list".equals(field.key)) {
                String clazzName = toUpperCaseFirstOne(field.value + "ListBean");
                classes.put(clazzName, field.clazz);
                tempClasses.put(clazzName, field.clazz);
                sb.append(spaceStr).append("List<").append(clazzName).append(">").append(" ").append(field.value).append(";").append("\n");
//                orderedList.add(field.value);
                listList.add(new NameValuePair(clazzName, field.value));
            }
        }

        String tempSpaceStr = spaceStr + "  ";
        int childSize = orderedList.size() + objectList.size() + listList.size();

        if (childSize != 0) {
            // 构造器
            sb.append("\n")
                    .append(spaceStr).append(className).append("({");

            for (String value : orderedList) {
                sb.append("\n").append(tempSpaceStr).append("this.").append(value).append(",");
            }
            for (NameValuePair pair : objectList) {
                sb.append("\n").append(tempSpaceStr).append("this.").append(pair.value).append(",");
            }
            for (NameValuePair pair : listList) {
                sb.append("\n").append(tempSpaceStr).append("this.").append(pair.value).append(",");
            }
            for (NameValuePair pair : listBaseList) {
                sb.append("\n").append(tempSpaceStr).append("this.").append(pair.value).append(",");
            }

            sb.append("\n").append(spaceStr).append("});\n");
        }

        // map 转换
        String fieldName = toLowerCaseFirstOne(className);
        sb.append("\n").append(spaceStr)
                .append("static ").append(className).append(" fromMap").append("(Map<String, dynamic> map) {")
                .append("\n").append(tempSpaceStr)
                .append(className).append(" ").append(fieldName).append(" = new ").append(className).append("();")
//                .append("return new ").append(className).append("()");
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

        sb.append("\n").append(tempSpaceStr).append("return ").append(fieldName).append(";\n");
        sb.append(spaceStr).append("}\n");

        // list 转换
        sb.append("\n").append(spaceStr)
                .append("static ").append("List<").append(className).append(">").append(" fromMapList").append("(dynamic mapList) {")
                .append("\n").append(tempSpaceStr).append("List<").append(className).append("> list = new List(mapList.length);")
                .append("\n").append(tempSpaceStr).append("for (int i = 0; i < mapList.length; i++) {")
                .append("\n").append(tempSpaceStr).append("  ").append("list[i] = fromMap(mapList[i]);")
                .append("\n").append(tempSpaceStr).append("}")
                .append("\n").append(tempSpaceStr).append("return list;")
                .append("\n").append(spaceStr).append("}")
                .append("\n");

        // 遍历类中类（主要目的是添加进 classes，而不 append）
        for (Map.Entry<String, List<CustomField>> entry : tempClasses.entrySet()) {
            printCustomFields(entry.getValue(), space + 2, entry.getKey());
        }

        return sb.toString();
    }

    private List<CustomField> json2Fields(JsonObject jsonObject) {
        List<CustomField> list = new ArrayList<>();
        for (Object o : jsonObject.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            list.add(keyValue(entry.getKey().toString(), entry.getValue()));
        }

        return list;
    }

    private CustomField keyValue(String key, Object object) {
        if (object instanceof JsonObject) {
            JsonObject jsonObject = (JsonObject) object;
            return new CustomField("object", key, json2Fields(jsonObject));
        } else if (object instanceof JsonArray) {
            JsonArray jsonArray = (JsonArray) object;
            if (jsonArray.size() != 0) {
                Object obj = jsonArray.get(0);
                if (obj instanceof JsonObject) {
                    return new CustomField("list", key, json2Fields(jsonArray.get(0).getAsJsonObject()));
                } else {
                    CustomField temp = keyValue("placeholder", obj);
                    return new CustomField("List<" + temp.key + ">", key, null);
                }
            } else {
                return new CustomField("list", key, null);
            }
        } else if (tryParseBoolean(object)) {
            return new CustomField("bool", key, null);
        } else if (tryParseInt(object)) {
            return new CustomField("int", key, null);
        } else if (tryParseLong(object)) {
            return new CustomField("int", key, null);
        } else if (tryParseDouble(object)) {
            return new CustomField("double", key, null);
        } else if (tryParseFloat(object)) {
            return new CustomField("double", key, null);
        } else {
            return new CustomField("String", key, null);
        }
    }

    private boolean tryParseInt(Object object) {
        try {
            int i = Integer.parseInt(object.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean tryParseLong(Object object) {
        try {
            long i = Long.parseLong(object.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean tryParseDouble(Object object) {
        try {
            double d = Double.parseDouble(object.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean tryParseFloat(Object object) {
        try {
            float f = Float.parseFloat(object.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean tryParseBoolean(Object object) {
        String b = object.toString();
        return Objects.equals(b, "true") || Objects.equals(b, "false");
    }

    private class CustomField {
        String key;
        String value;
        List<CustomField> clazz;

        public CustomField(String key, String value, List<CustomField> clazz) {
            this.key = key;
            this.value = value;
            this.clazz = clazz;
        }

        @Override
        public String toString() {
            return "CustomField{" +
                    "key='" + key + '\'' +
                    ", value='" + value + '\'' +
                    ", classes=" + clazz +
                    '}';
        }
    }

    private class NameValuePair {
        String name;
        String value;

        public NameValuePair(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    /**
     * 将字符串复制到剪切板。
     */
    private void setSysClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

    // 首字母转大写
    private String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    // 首字母转小写
    private String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    private void writeToFile(String content) {
        try {
            file.setBinaryContent(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}