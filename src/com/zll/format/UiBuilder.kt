package com.zll.format

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBScrollPane
import com.zll.format.ClazzGenerator
import com.zll.format.Settings
import com.zll.format.Util
import javax.swing.*

class UiBuilder(private val project: Project, private val virtualFile: VirtualFile) {

    var frame: JFrame? = null

    fun build(): JComponent {
        return JPanel().apply {
            placeComponents(this)
        }
    }

    private fun placeComponents(panel: JPanel) = panel.apply {
        val className = Util.toUpperCaseFirstOne(virtualFile.nameWithoutExtension).split("_").reduce { acc, s -> "$acc${Util.toUpperCaseFirstOne(s)}" }
        val settings = Settings()

        layout = null

        val tipLabel = JLabel("class name: $className").apply {
            setBounds(10, 0, 500, 40)
        }

        val jsonText = JTextArea().apply {
            setBounds(10,50,680,400)
        }

        val commentCb = JCheckBox("generate comments", settings.generateComments).apply {
            setBounds(10, 460, 200, 30)
        }

        add(JButton("ok").apply {
            setBounds(600, 460, 80, 30)
            isVisible = true
            addActionListener {
                // 保存配置
                settings.generateComments = commentCb.isSelected
                settings.save()

                // 开始生成代码
                val classesString = ClazzGenerator(settings).generate(className, jsonText.text)
                if (classesString.startsWith("error:")) {
                    tipLabel.text = classesString
                } else {
                    Util.writeToFile(project, virtualFile, classesString)
                    frame?.dispose()
                }
            }
        })

        add(JBScrollPane(jsonText).apply {
            verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            setBounds(10, 50, 680, 400)
        })

        add(commentCb)
        add(tipLabel)
    }
}