import java.io.File
import java.io.IOException
import javax.swing.JFileChooser
import javax.swing.JMenuItem
import javax.swing.JOptionPane

object FileManager {
    private lateinit var fileChooser: JFileChooser
    private var file: File? = null
    var menuItem: JMenuItem? = null
    var fileName = ""
        private set

    fun openFile(): Boolean {
        fileChooser = JFileChooser()
        val result = fileChooser.showOpenDialog(menuItem)

        if (result != 0) throw IOException("No file open")

        return if (!fileChooser.selectedFile.name.endsWith(".kts")) {
            JOptionPane.showMessageDialog(null, "File type not supported", "Error", JOptionPane.ERROR_MESSAGE)
            false
        } else {
            file = fileChooser.selectedFile
            fileName = file!!.name
            true
        }
    }

    fun getFileContent(): String {
        return File(file!!.absolutePath).readText()
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
        checkMenuItem()
        fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Save file"
        val result = fileChooser.showSaveDialog(menuItem)

        if (result == JFileChooser.APPROVE_OPTION) {
            file = File(fileChooser.selectedFile.absolutePath)
            file!!.writeText(modifiedText)
        }
        fileName = file!!.name
    }

    fun getFilePath(): String {
        return if (file != null) {
            file!!.absolutePath
        } else {
            ""
        }
    }

    private fun checkMenuItem() {
        if (menuItem == null) {
            throw Exception("Menu item is not initialized")
        }
    }
}