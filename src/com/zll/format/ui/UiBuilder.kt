package com.zll.format.ui

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBScrollPane
import com.zll.format.ClassMaker
import com.zll.format.Util
import javax.swing.*

class UiBuilder(private val virtualFile: VirtualFile) {

    fun build(): JComponent {
        return JPanel().apply {
            placeComponents(this)
        }
    }

    private fun placeComponents(panel: JPanel) {
        panel.layout = null

        val tipLabel = JLabel("input your json and click ok, \ndart class contents will be copied to Clipboard").apply {
            setBounds(10, 0, 500, 40)
        }

        val jsonText = JTextArea().apply {
            setBounds(0,50,700,400)
        }

        panel.add(JButton("ok").apply {
            setBounds(600, 10, 80, 30)
            isVisible = true
            addActionListener {
                val classesString = ClassMaker().make(jsonText.text)
                if (classesString.startsWith("error:")) {
                    tipLabel.text = classesString
                } else {
//                    Util.setSysClipboardText(classesString) // 复制到粘贴板
                    Util.writeToFile(virtualFile, classesString)
                }
            }
        })

        panel.add(JBScrollPane(jsonText).apply {
            verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            setBounds(0, 50, 700, 400)
        })

        panel.add(tipLabel)
    }
}