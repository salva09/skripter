package view

import FileManager
import com.formdev.flatlaf.FlatDarculaLaf
import java.awt.Dimension
import javax.swing.*
import kotlin.system.exitProcess


class Home : JFrame() {
    private lateinit var mainPane: JPanel
    private lateinit var editorPane: JEditorPane
    private lateinit var outputPane: JEditorPane

    init {
        setFrameLookAndFeel()
        buildUi()
        setFrameConfigurations()
    }

    private fun setFrameLookAndFeel() {
        try {
            UIManager.setLookAndFeel(FlatDarculaLaf())
        } catch (ex: Exception) {
            JOptionPane.showMessageDialog(
                this,
                "Oh no! There was an error :( \n" +
                        "Error: ${ex.message}",
                "Error",
                JOptionPane.ERROR_MESSAGE
            )
            exitProcess(0)
        }
    }

    private fun buildUi() {
        mainPane = JPanel()
        createPanes()
        createMenuBar()
    }

    private fun createPanes() {
        // TODO("Implement syntax highlight")
        editorPane = JEditorPane()
        outputPane = JEditorPane()

        editorPane.isEditable = true
        outputPane.isEditable = false

        val editorScrollPane = JScrollPane(editorPane)
        editorScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        editorScrollPane.preferredSize = Dimension(500, 450)
        editorScrollPane.minimumSize = Dimension(500, 100)

        val outputScrollPane = JScrollPane(outputPane)
        outputScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        outputScrollPane.minimumSize = Dimension(500, 100)

        val splitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT, editorScrollPane, outputScrollPane)
        mainPane.add(splitPane)
    }

    private fun createMenuBar() {
        // TODO("Add basic options and a run button")
        val menuBar = JMenuBar()

        val file = JMenu("File")
        val editorManager = EditorManager(this, editorPane)

        val openFile = JMenuItem("Open file")
        FileManager.menuItem = openFile
        openFile.addActionListener {
            editorManager.openFile()
        }

        val saveFile = JMenuItem("Save file")
        saveFile.addActionListener {
            editorManager.saveFile()
        }

        val newFile = JMenuItem("New")
        newFile.addActionListener {
            editorManager.newFile()
        }

        file.add(newFile)
        file.add(openFile)
        file.add(saveFile)
        menuBar.add(file)

        jMenuBar = menuBar
    }
    
    private fun setFrameConfigurations() {
        size = Dimension(510, 545)
        isResizable = false
        title = "Skripter"
        contentPane = mainPane
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)
    }
}