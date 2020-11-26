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
    
    fun saveFile(): Boolean {
        return try {
            FileManager.saveFile(editorPane.text)
            savedText = editorPane.text
            true
        } catch (ex: Exception) {
            false
        }
    }

    fun closeFile() {
        ifIsSavedElseSave {
            exitProcess(0)
        }
    }

    fun runScript() {
        ifIsSavedElseSave {
            Runner.run(editorPane.text)
        }
    }

    private fun setEditorContent(newContent: String) {
        editorPane.text = newContent
        savedText = newContent
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
        if (editorPane.text == savedText) return true
        return false
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

        /*
        Results:
        0 -> File will be saved and callback is called (if file is saved successfully)
        1 -> The file won't be saved but callback will be called
        2 -> Interrupts the function and callback is not called
        */
        when (result) {
            2 -> { return }
            0 -> { if (!saveFile()) return }
        }
        callback()
    }
}