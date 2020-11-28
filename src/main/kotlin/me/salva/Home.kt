package me.salva

import com.formdev.flatlaf.FlatDarculaLaf
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*
import kotlin.system.exitProcess

object Home : JFrame() {
    private lateinit var mainPane: JPanel
    private lateinit var editorPane: JEditorPane
    private lateinit var outputPane: JTextArea
    private lateinit var runButton: JButton
    private lateinit var running: JLabel

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
        mainPane.layout = GridBagLayout()
        
        createMenuBar()

        val c = GridBagConstraints()

        /*
        Expected layout
        ______________________________
        |File|Edit|Help|         |Run|
        |____________________________|
        |println("Hello")            |
        |                            |
        |                            |
        |                            |
        |                            |
        |                            |
        |                            |
        |____________________________|
        |Hello                       |
        |                            |
        |                            |
        |____________________________|
        Exit code: 0 *------* Progress
         */

        c.gridx = 0
        c.gridy = 0
        c.ipadx = 100
        c.ipady = 310
        c.gridwidth = 5
        c.gridheight = 5
        c.weightx = 100.0
        c.weighty = 100.0
        c.fill = GridBagConstraints.BOTH
        mainPane.add(createSplitPane(), c)

        c.gridx = 0
        c.gridy = 10
        c.ipadx = 1
        c.ipady = 1
        c.gridwidth = 1
        c.gridheight = 1
        c.weightx = 1.0
        c.weighty = 1.0
        c.fill = GridBagConstraints.BOTH
        mainPane.add(createRunningLabel(), c)
    }

    private fun setFrameConfigurations() {
        size = Dimension(600, 550)
        isResizable = true
        title = "Skripter"
        setLocationRelativeTo(null)
        contentPane = mainPane
        HomeManager.init(this, editorPane)

        defaultCloseOperation = DO_NOTHING_ON_CLOSE

        this.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(event: WindowEvent) {
                HomeManager.closeFile()
            }
        })
    }

    private fun createSplitPane(): JSplitPane {
        // TODO("Implement syntax highlight")
        editorPane = JEditorPane()
        editorPane.isEditable = true

        val editorScrollPane = JScrollPane(editorPane)
        editorScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        editorScrollPane.preferredSize = Dimension(500, 500)
        editorScrollPane.minimumSize = Dimension(500, 100)

        outputPane = JTextArea()
        outputPane.isEditable = false

        val outputScrollPane = JScrollPane(outputPane)
        outputScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        // The output pane should be smaller but it doesn't want
        outputScrollPane.preferredSize = Dimension(500, 100)
        outputScrollPane.minimumSize = Dimension(500, 100)

        return JSplitPane(JSplitPane.VERTICAL_SPLIT, editorScrollPane, outputScrollPane)
    }

    private fun createMenuBar() {
        val menuBar = JMenuBar()
        val file = JMenu("File")

        // We need one menu item to give to the file manager
        val item = newMenuItem("New", { HomeManager.newFile() }, file)
        newMenuItem("Open file", { HomeManager.openFile() }, file)
        newMenuItem("Save file", { HomeManager.saveFile() }, file)
        newMenuItem("Exit", { HomeManager.closeFile() }, file)

        FileManager.menuItem = item
        menuBar.add(file)

        menuBar.add(Box.createHorizontalGlue())

        runButton = JButton("Run")
        runButton.addActionListener {
            HomeManager.runScript()
        }

        menuBar.add(runButton)
        jMenuBar = menuBar
    }

    private fun createRunningLabel(): JLabel {
        running = JLabel("Idle")
        running.maximumSize = Dimension(100, 10)

        return running
    }

    fun goodLabel() {
        running.text = "Exit code: 0"
        running.icon = ImageIcon(javaClass.getResource("/icons/good.png"))
    }

    fun runningLabel() {
        running.text = "Running"
        running.icon = ImageIcon(javaClass.getResource("/icons/loading.gif"))
    }

    fun badLabel(exitCode: Int) {
        running.text = "Non-zero exit code: $exitCode"
        running.icon = ImageIcon(javaClass.getResource("/icons/bad.png"))
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