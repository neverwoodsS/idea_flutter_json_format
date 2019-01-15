package com.zll.format;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBScrollPane;
import javax.swing.*;

/**
 * Created by zhangll on 2018/7/31.
 */
public class UiBuilder {
    private VirtualFile file;

    private JLabel label;
    private JTextArea jsonText;

    public UiBuilder(VirtualFile file) {
        this.file = file;
    }

    public JComponent build() {
        JPanel panel = new JPanel();
        placeComponents(panel);
        return panel;
    }

    private void placeComponents(JPanel panel) {
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
        okButton.addActionListener(arg0 -> {
            String classesString = new ClassMaker().make(jsonText.getText());
            if (classesString.startsWith("error:")) {
                label.setText(classesString);
            } else {
                Util.setSysClipboardText(classesString);
                Util.writeToFile(file, classesString);
            }
        });

        panel.add(okButton);
        panel.add(jbScrollPane);
        panel.add(label);
    }
}