package view

import javax.swing.JOptionPane
import FileManager
import javax.swing.JEditorPane
import javax.swing.JFrame

class EditorManager(private val frame: JFrame, private val editorPane: JEditorPane) {
    private var savedText = ""

    fun newFile() {
        setEditorContent("")
    }
    
    fun openFile() {
        if (!isPreviousTextSaved()) {
            when (promptFileWarning()) {
                0 -> { saveFile() }
                1 -> {}
                else -> { return }
            }
        }

        FileManager.openFile()
        setEditorContent(FileManager.getFileContent())
    }
    
    fun saveFile() {
        // TODO("Fix double dialog when accepting to save but cancel the save dialog")
        try {
            FileManager.saveFile(editorPane.text)
            savedText = editorPane.text
        } catch (ex: Exception) {}
    }

    private fun setEditorContent(newContent: String) {
        if (isPreviousTextSaved())
            editorPane.text = newContent
        else {
            when (promptFileWarning()) {
                0 -> { saveFile() }
                1 -> { editorPane.text = newContent }
                else -> {}
            }
        }
    }

    private fun promptFileWarning(): Int {
        val options = arrayOf(
            "Yes, please",
            "No, thanks",
            "Abort mission!"
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

        return result
    }

    private fun saveEditorContent() {
        try {
            FileManager.saveFile(editorPane.text)
            savedText = editorPane.text
        } catch (ex: Exception) {}
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