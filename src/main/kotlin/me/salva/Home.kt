package me.salva

import com.formdev.flatlaf.FlatDarculaLaf
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*
import kotlin.system.exitProcess

object Home : JFrame() {
    private lateinit var mainPane: JPanel
    private lateinit var editorPane: JEditorPane
    private lateinit var outputPane: JTextArea
    private lateinit var homeManager: HomeManager
    private lateinit var runButton: JButton

    init {
        setFrameLookAndFeel()
        buildUi()
        setFrameConfigurations()
        redirectOutput(outputPane)
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

    private fun setFrameConfigurations() {
        size = Dimension(510, 545)
        isResizable = false
        title = "Skripter"
        contentPane = mainPane
        setLocationRelativeTo(null)

        defaultCloseOperation = DO_NOTHING_ON_CLOSE

        this.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(event: WindowEvent) {
                homeManager.closeFile()
            }
        })
    }

    private fun createPanes() {
        // TODO("Implement syntax highlight")
        editorPane = JEditorPane()
        outputPane = JTextArea()
        homeManager = HomeManager(this, editorPane)

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
        val menuBar = JMenuBar()
        val file = JMenu("File")

        // We need one menu item to give to the file manager
        val item = newMenuItem("New", { homeManager.newFile() }, file)
        newMenuItem("Open file", { homeManager.openFile() }, file)
        newMenuItem("Save file", { homeManager.saveFile() }, file)
        newMenuItem("Exit", { homeManager.closeFile() }, file)

        FileManager.menuItem = item
        menuBar.add(file)

        menuBar.add(Box.createHorizontalGlue())

        runButton = JButton("Run")
        runButton.addActionListener {
            homeManager.runScript()
        }

        menuBar.add(runButton)
        jMenuBar = menuBar
    }

    private fun newMenuItem(label: String, action: () -> Unit, parent: JMenu): JMenuItem {
        val item = JMenuItem(label)

        item.addActionListener {
            action()
        }

        parent.add(item)
        return item
    }

    fun clearOutput() {
        outputPane.text = ""
    }
}