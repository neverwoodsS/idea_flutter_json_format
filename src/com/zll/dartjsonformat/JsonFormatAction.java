package com.zll.dartjsonformat;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import javax.swing.*;

/**
 * Created by zhangll on 2018/7/31.
 */
public class JsonFormatAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("json format");
        // Setting the width and height of frame
        frame.setSize(700, 470);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.add(new UiBuilder().build());
        frame.setVisible(true);
    }
}
