package com.zll.format

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBScrollPane
import java.awt.Insets
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

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

        val commentCb = JCheckBox("generate comments", settings.generateComments).apply {
            setBounds(10, 460, 200, 30)
        }

        val jsonText = JTextArea()
        val okBtn = JButton("ok")
        val icon = IconLoader.getIcon("/images/flutter-40.png")

        okBtn.apply {
            setBounds(600, 460, 80, 30)
            isVisible = true
            isEnabled = false
            addActionListener {

                // 保存配置
                settings.generateComments = commentCb.isSelected
                settings.save()

                // 开始生成代码
                val classesString = ClazzGenerator(settings).generate(className, jsonText.text)
                if (classesString.startsWith("error:")) {
                    JOptionPane.showMessageDialog(panel, classesString.substring(6), "error", JOptionPane.WARNING_MESSAGE, icon)
                } else {
                    Util.writeToFile(project, virtualFile, classesString)
                    frame?.dispose()
                }
            }
        }

        jsonText.apply {
            setBounds(10,50,680,400)
            margin = Insets(5, 5, 5, 5)

            val checkInput = { okBtn.isEnabled = ClazzGenerator.isJson(jsonText.text) }

            document.addDocumentListener(object : DocumentListener {
                override fun changedUpdate(e: DocumentEvent?) = Unit
                override fun removeUpdate(e: DocumentEvent?) = checkInput()
                override fun insertUpdate(e: DocumentEvent?) = checkInput()
            })
        }

        add(JBScrollPane(jsonText).apply {
            verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            setBounds(10, 50, 680, 400)
        })

        add(okBtn)
        add(commentCb)
        add(tipLabel)
    }
}