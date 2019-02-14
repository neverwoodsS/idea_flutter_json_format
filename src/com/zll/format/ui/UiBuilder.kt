package com.zll.format.ui

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBScrollPane
import com.zll.format.ClassGenerator
import com.zll.format.Util
import javax.swing.*

class UiBuilder(private val virtualFile: VirtualFile) {

    var frame: JFrame? = null

    fun build(): JComponent {
        return JPanel().apply {
            placeComponents(this)
        }
    }

    private fun placeComponents(panel: JPanel) = panel.apply {
        val className = Util.toUpperCaseFirstOne(virtualFile.nameWithoutExtension)

        layout = null

        val tipLabel = JLabel("class name: $className").apply {
            setBounds(10, 0, 500, 40)
        }

        val jsonText = JTextArea().apply {
            setBounds(10,50,680,400)
        }

        val checkBox = JCheckBox("Generate Comments", true).apply {
            setBounds(10, 460, 200, 30)

        }

        add(JButton("ok").apply {
            setBounds(600, 460, 80, 30)
            isVisible = true
            addActionListener {
//                val classesString = ClassMaker().make(jsonText.text)
                val classesString = ClassGenerator(checkBox.isSelected).generate(className, jsonText.text)
                if (classesString.startsWith("error:")) {
                    tipLabel.text = classesString
                } else {
//                    Util.setSysClipboardText(classesString) // 复制到粘贴板
                    Util.writeToFile(virtualFile, classesString)
                    frame?.dispose()
                }
            }
        })

        add(JBScrollPane(jsonText).apply {
            verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            setBounds(10, 50, 680, 400)
        })

        add(checkBox)
        add(tipLabel)
    }
}