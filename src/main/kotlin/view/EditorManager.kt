package view

import javax.swing.JOptionPane
import FileManager
import javax.swing.JEditorPane
import javax.swing.JFrame

class EditorManager(val frame: JFrame, val editorPane: JEditorPane) {
    private var savedText = ""

    fun newFile() {
        setEditorContent("")
    }
    
    fun openFile() {
        if (FileManager.openFile()) {
            setEditorContent(FileManager.getFileContent())
        }
    }
    
    fun saveFile() {
        try {
            FileManager.saveFile(editorPane.text)
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
        if (savedText == "") return true
        if (savedText == editorPane.text) return true
        return false
    }
}