package view

import com.formdev.flatlaf.FlatDarculaLaf
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.IOException
import java.io.OutputStream
import java.io.PrintStream
import javax.swing.*
import kotlin.system.exitProcess


object Home : JFrame() {
    private lateinit var mainPane: JPanel
    private lateinit var editorPane: JEditorPane
    private lateinit var outputPane: JTextArea
    private lateinit var editorManager: EditorManager

    init {
        setFrameLookAndFeel()
        buildUi()
        setFrameConfigurations()
        redirectOutput()
    }

    private fun redirectOutput() {
        val printStream = PrintStream(CustomOutputStream(outputPane))
        System.setOut(printStream)
        System.setErr(printStream)
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
                editorManager.closeFile()
            }
        })
    }

    private fun createPanes() {
        // TODO("Implement syntax highlight")
        editorPane = JEditorPane()
        outputPane = JTextArea()
        editorManager = EditorManager(this, editorPane)

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

        newMenuItem("New", { editorManager.newFile() }, file)
        newMenuItem("Open file", { editorManager.openFile() }, file)
        newMenuItem("Save file", { editorManager.saveFile() }, file)
        newMenuItem("Exit", { editorManager.closeFile() }, file)

        menuBar.add(file)

        val runButton = JButton("Run")
        runButton.addActionListener {
            editorManager.runScript()
        }

        menuBar.add(Box.createHorizontalGlue())

        menuBar.add(runButton)
        jMenuBar = menuBar
    }

    private fun newMenuItem(label: String, action: () -> Unit, parent: JMenu) {
        val exit = JMenuItem(label)

        exit.addActionListener {
            action()
        }

        parent.add(exit)
    }

    fun clearOutput() {
        outputPane.text = ""
    }
}

class CustomOutputStream(private val textArea: JTextArea) : OutputStream() {
    @Throws(IOException::class)
    override fun write(b: Int) {
        // Redirects data to the text area
        textArea.text = textArea.text + b.toChar()
        // Scrolls the text area to the end of data
        textArea.caretPosition = textArea.document.length
        // Keeps the textArea up to date
        textArea.update(textArea.graphics)
    }
}