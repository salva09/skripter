package me.salva

import javax.swing.JEditorPane
import javax.swing.JFrame
import javax.swing.JOptionPane
import kotlin.system.exitProcess

object HomeManager {
    private lateinit var frame: JFrame
    private lateinit var editorPane: JEditorPane
    private var savedText = ""

    fun init(frame: JFrame, editorPane: JEditorPane) {
        this.frame = frame
        this.editorPane = editorPane
    }

    fun newFile() {
        ifIsSavedElseSave {
            frame.title = "Skripter"
            setEditorContent("")
        }
    }
    
    fun openFile() {
        ifIsSavedElseSave {
            try {
                FileManager.openFile()
                frame.title = "Skripter: ${FileManager.fileName}"
                setEditorContent(FileManager.getFileContent())
            } catch (ex: Exception) {}
        }
    }
    
    fun saveFile(): Boolean {
        return try {
            if (!FileManager.isAFileOpen()) FileManager.saveFileAs(editorPane.text)
            else FileManager.saveFile(editorPane.text)
            savedText = editorPane.text
            frame.title = "Skripter: ${FileManager.fileName}"
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
        if (!isPreviousTextSaved()) {
            promptFileWarning {}
        }
        if (isPreviousTextSaved()) {
            if (!Runner.isProcessAlive()) {
                Runner.run(FileManager.getFilePath())
            } else {
                val message = "The previous script is still running!"
                val options = arrayOf(
                    "Wait to finish",
                    "Kill it >:)"
                )
                val result = JOptionPane.showOptionDialog(
                    Home,
                    message,
                    "",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    null
                )
                if (result == 1) stopScript()
            }
        }
    }

    private fun stopScript() {
        Runner.stop()
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
        if (editorPane.text == savedText) return true
        return false
    }

    private fun promptFileWarning(callback: () -> Unit = {}) {
        val options = arrayOf(
            "Yes, please",
            "No, thanks",
            "Go back"
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
            2 -> {
                return
            }
            0 -> {
                if (!saveFile()) return
            }
        }
        callback()
    }

    fun changeLabelToRunning() {
        Home.runningLabel()
    }

    fun changeLabelToIdle(exitCode: Int) {
        if (exitCode == 0) Home.goodLabel()
        else Home.badLabel(exitCode)
    }
}