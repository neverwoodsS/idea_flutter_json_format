package com.zll.format

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiUtilBase
import javax.swing.JFrame

class DartJsonFormatAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        PsiUtilBase.getPsiFileInEditor(event.getData(PlatformDataKeys.EDITOR) as Editor,
                event.getData(PlatformDataKeys.PROJECT) as Project)
                ?.let { UiBuilder(it.project, it.virtualFile) }
                ?.let {
                    it.frame = JFrame("flutter json format").apply {
                        // size and location
                        setSize(700, 520)
                        setLocation(-350, -260)
                        setLocationRelativeTo(null)

                        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
                        add(it.build())
                        isVisible = true
                    }
                }
    }
}