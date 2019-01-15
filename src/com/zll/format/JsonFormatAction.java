package com.zll.format;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import javax.swing.*;

/**
 * Created by zhangll on 2018/7/31.
 */
public class JsonFormatAction extends AnAction {

//    public JsonFormatAction() {
//        super((CodeInsightActionHandler) null);
//    }
//
//    public JsonFormatAction(CodeInsightActionHandler handler) {
//        super(handler);
//    }
//
//    @Override
//    protected boolean isValidForClass(PsiClass targetClass) {
//        return super.isValidForClass(targetClass);
//    }
//
//    @Override
//    protected boolean isValidForFile(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
//        return super.isValidForFile(project, editor, file);
//    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = (Project)event.getData(PlatformDataKeys.PROJECT);
        Editor editor = (Editor)event.getData(PlatformDataKeys.EDITOR);
        PsiFile mFile = PsiUtilBase.getPsiFileInEditor(editor, project);
//        PsiClass psiClass = this.getTargetClass(editor, mFile);

        VirtualFile virtualFile = mFile.getVirtualFile();

        // 创建 JFrame 实例
        JFrame frame = new JFrame("json format");
        // Setting the width and height of frame
        frame.setSize(700, 470);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.add(new UiBuilder(virtualFile).build());
        frame.setVisible(true);

    }

}
