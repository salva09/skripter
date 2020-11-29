import java.io.File
import java.io.IOException
import javax.swing.JFileChooser
import javax.swing.JMenuItem

object FileManager {
    private lateinit var fileChooser: JFileChooser
    private var file: File? = null
    var menuItem: JMenuItem? = null
    var fileName = ""
        private set

    fun openFile() {
        fileChooser = JFileChooser()
        val result = fileChooser.showOpenDialog(menuItem)

        if (result != 0) throw IOException("No file open")

        file = fileChooser.selectedFile
        fileName = file!!.name
    }

    fun saveFile(modifiedText: String) {
        if (file != null) {
            File(file!!.absolutePath).writeText(modifiedText)
            fileName = file!!.name
        } else {
            saveFileAs(modifiedText)
        }
    }

    fun saveFileAs(modifiedText: String) {
        fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Save file"
        val result = fileChooser.showSaveDialog(menuItem)

        if (result == JFileChooser.APPROVE_OPTION) {
            file = File(fileChooser.selectedFile.absolutePath)
            file!!.writeText(modifiedText)
            fileName = file!!.name
        }
    }

    fun getFileContent(): String {
        return if (file != null) File(file!!.absolutePath).readText()
        else ""
    }

    fun getFilePath(): String {
        return if (file != null) file!!.absolutePath
        else ""
    }

    fun getFileExtension(): String {
        return if (file != null) file!!.extension
        else ""
    }

    fun isAFileOpen(): Boolean {
        return file != null
    }
}
