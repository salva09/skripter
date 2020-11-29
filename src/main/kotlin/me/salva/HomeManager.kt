package me.salva

import java.awt.Desktop
import java.net.URI
import javax.swing.JEditorPane
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JTextArea
import kotlin.system.exitProcess

object HomeManager {
    private lateinit var frame: JFrame
    private lateinit var editorPane: JEditorPane
    private lateinit var outputPane: JTextArea
    private var savedText = ""

    fun init(frame: JFrame, editorPane: JEditorPane, outputPane: JTextArea) {
        this.frame = frame
        this.editorPane = editorPane
        this.outputPane = outputPane
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

    fun saveFilesAs() {
        FileManager.saveFileAs(editorPane.text)
        savedText = editorPane.text
        frame.title = "Skripter: ${FileManager.fileName}"
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
            if (!hasInterpreterInstalled()) {
                showErrorWhenNoInterpreter()
                return
            }

            if (Runner.isProcessAlive()) {
                val message = "The previous script is still running!"
                val options = arrayOf(
                    "Wait to finish",
                    "Kill it"
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
                if (result == 0) return
                else stopScript()
            } else {
                Runner.run(FileManager.getFilePath())
            }
        }
    }

    fun changeLabelToRunning() {
        Home.runningLabel()
    }

    fun changeLabelToIdle(exitCode: Int) {
        if (exitCode == 0) Home.goodLabel()
        else Home.badLabel(exitCode)
    }

    fun cleanConsole() {
        outputPane.text = ""
        Home.idleLabel()
    }

    private fun showErrorWhenNoInterpreter() {
        val options = arrayOf(
            "Okay",
            "Online help"
        )
        var language: Language
        var message: String
        try {
            language = getLanguageByExtension(FileManager.getFileExtension())
            message = "To run ${language.name} scripts I need to be able to access the ${language.name} interpreter.\n" +
                "Please make sure that you have it installed \n" +
                "and \"${language.versionCommand}\" prints the interpreter version."
        } catch (ex: ArrayIndexOutOfBoundsException) {
            message = "I'm sorry, ${FileManager.getFileExtension()} scripts are not currently supported"
            language = Language(downloadUri = "https://www.google.com/search?q=${FileManager.getFileExtension()}")
        }

        val result = JOptionPane.showOptionDialog(
            Home,
            message,
            "Error",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null,
            options,
            null
        )
        if (result == 1) Desktop.getDesktop().browse(URI(language.downloadUri))
    }

    private fun hasInterpreterInstalled(): Boolean {
        // If kotlin is the selected script, this will take a moment, the kotlin interpreter takes a while
        // before start executing the scripts/commands
        val process = Runtime.getRuntime().exec(
            getLanguageByExtension(FileManager.getFileExtension())
                .versionCommand
        )
        if (process.waitFor() == 0) return true
        return false
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
}
