package view

import com.formdev.flatlaf.FlatDarculaLaf
import java.awt.Color
import java.awt.Dimension
import java.awt.Insets
import java.awt.event.ActionEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*
import kotlin.system.exitProcess


class Home : JFrame() {
    private lateinit var mainPane: JPanel
    private lateinit var editorPane: JEditorPane
    private lateinit var outputPane: JEditorPane
    private lateinit var editorManager: EditorManager

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
        outputPane = JEditorPane()
        editorManager = EditorManager(this, editorPane, outputPane)

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

        val toggleAction: Action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                val button = e.source as AbstractButton
                if (button.isSelected) {
                    button.text = "Stop"
                    editorManager.runScript()
                } else {
                    button.text = "Run"
                    editorManager.stopScript()
                }
            }
        }

        val runButton = JToggleButton(toggleAction)
        runButton.text = "Run"
        //runButton.border = null  // looks really bad without borders

        // Spacer
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
}