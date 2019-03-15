package com.zll.format.ui

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBScrollPane
import com.zll.format.ClassGenerator
import com.zll.format.Util
import javax.swing.*

class UiBuilder(private val project: Project, private val virtualFile: VirtualFile) {

    companion object {
        const val KEY_COMMENT = "dart_json_format_comment"
        const val KEY_IGNORE = "dart_json_format_ignore"
    }

    var frame: JFrame? = null

    fun build(): JComponent {
        return JPanel().apply {
            placeComponents(this)
        }
    }

    private fun placeComponents(panel: JPanel) = panel.apply {
        val className = Util.toUpperCaseFirstOne(virtualFile.nameWithoutExtension)
        val (needComment, needIgnore) = readSettings()

        layout = null

        val tipLabel = JLabel("class name: $className").apply {
            setBounds(10, 0, 500, 40)
        }

        val jsonText = JTextArea().apply {
            setBounds(10,50,680,400)
        }

        val commentCb = JCheckBox("generate comments", needComment).apply {
            setBounds(10, 460, 200, 30)
        }

        val ignoreCb = JCheckBox("ignore null or empty array", needIgnore).apply {
            setBounds(240, 460, 200, 30)
        }

        add(JButton("ok").apply {
            setBounds(600, 460, 80, 30)
            isVisible = true
            addActionListener {
                saveSettings(commentCb.isSelected, ignoreCb.isSelected)
                val classesString = ClassGenerator(commentCb.isSelected, ignoreCb.isSelected).generate(className, jsonText.text)
                if (classesString.startsWith("error:")) {
                    tipLabel.text = classesString
                } else {
//                    Util.setSysClipboardText(classesString) // 复制到粘贴板
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
        add(ignoreCb)
        add(tipLabel)
    }

    private fun saveSettings(comment: Boolean, ignoreEmptyOrNull: Boolean) {
        PropertiesComponent.getInstance().apply {
            setValue(KEY_COMMENT, comment.toString())
            setValue(KEY_IGNORE, ignoreEmptyOrNull.toString())
        }
    }

    private fun readSettings() = PropertiesComponent.getInstance().let {
        it.getBoolean(KEY_COMMENT, true) to it.getBoolean(KEY_IGNORE, true)
    }
}