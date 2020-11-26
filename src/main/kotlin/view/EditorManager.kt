package view

import javax.swing.JOptionPane
import FileManager
import javax.swing.JEditorPane
import javax.swing.JFrame
import kotlin.system.exitProcess

class EditorManager(private val frame: JFrame, private val editorPane: JEditorPane) {
    private var savedText = ""

    fun newFile() {
        ifIsSavedElseSave {
            setEditorContent("")
        }
    }
    
    fun openFile() {
        ifIsSavedElseSave {
            FileManager.openFile()
            setEditorContent(FileManager.getFileContent())
        }
    }
    
    fun saveFile() {
        try {
            FileManager.saveFile(editorPane.text)
            savedText = editorPane.text
        } catch (ex: Exception) {}
    }

    fun closeFile() {
        ifIsSavedElseSave {
            exitProcess(0)
        }
    }

    private fun setEditorContent(newContent: String) {
        editorPane.text = newContent
        savedText = newContent
    }

    private fun promptFileWarning(callback: () -> Unit = {}) {
        val options = arrayOf(
            "Yes",
            "No",
            "Cancel"
        )

        val result = JOptionPane.showOptionDialog(
            frame,
            "It looks like your actual code is no saved.\n " +
                    "Would you like to save it?",
            "Warning: File is not saved",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[2]
        )

        when (result) {
            2 -> { return }
            0 -> { saveFile() }
        }
        callback()
    }

    private fun ifIsSavedElseSave(callback: () -> Unit) {
        if (isPreviousTextSaved()) {
            callback()
        } else {
            promptFileWarning {
                callback()
            }
        }
    }

    private fun isPreviousTextSaved(): Boolean {
        // TODO("Better implementation to validate if a text was saved")
        // this implementation of comparing string may be bad at large code sources
        // but this is for scripts so I hope it's okay
        if (editorPane.text == "") return true
        if (editorPane.text == savedText) return true
        return false
    }
}