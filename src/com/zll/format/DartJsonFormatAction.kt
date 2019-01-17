package com.zll.format

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiUtilBase
import com.zll.format.ui.UiBuilder
import javax.swing.JFrame

class DartJsonFormatAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        PsiUtilBase.getPsiFileInEditor(event.getData(PlatformDataKeys.EDITOR) as Editor,
                event.getData(PlatformDataKeys.PROJECT) as Project)
                ?.let { it.virtualFile }
                ?.let { UiBuilder(it) }
                ?.let {
                    JFrame("dart json format").apply {
                        setSize(700, 470)
                        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
                        add(it.build())
                        isVisible = true
                    }.apply { it.frame = this }
                }
    }
}