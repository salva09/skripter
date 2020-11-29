package me.salva

import me.salva.syntax.setFrameLookAndFeel
import me.salva.syntax.setLightScrollPane
import me.salva.syntax.setLightTextArea
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.*

private const val FRAME_HEIGHT = 600
private const val FRAME_WIDTH = 550
private const val MIN_PANE_HEIGHT = 100
private const val MIN_PANE_WIDTH = 500

object Home : JFrame() {
    private lateinit var mainPane: JPanel
    private lateinit var editorPane: RSyntaxTextArea
    private lateinit var outputPane: JTextArea
    private lateinit var runButton: JButton
    private lateinit var running: JLabel

    init {
        setFrameLookAndFeel(this)
        buildUi()
        setFrameConfigurations()
        redirectOutput(outputPane)
    }

    private fun buildUi() {
        mainPane = JPanel()
        mainPane.layout = GridBagLayout()

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

        createMenuBar()
    }

    private fun setFrameConfigurations() {
        size = Dimension(FRAME_WIDTH, FRAME_HEIGHT)
        isResizable = true
        title = "Skripter"
        setLocationRelativeTo(null)
        contentPane = mainPane
        HomeManager.init(this, editorPane, outputPane)

        defaultCloseOperation = DO_NOTHING_ON_CLOSE

        this.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(event: WindowEvent) {
                HomeManager.closeFile()
            }
        })
    }

    private fun createSplitPane(): JSplitPane {
        initSyntax()
        editorPane = RSyntaxTextArea()

        // Editor pane configurations
        editorPane.closeCurlyBraces = true
        editorPane.markOccurrences = true
        editorPane.autoscrolls = true
        editorPane.isCodeFoldingEnabled = true
        editorPane.paintTabLines = true
        editorPane.isAutoIndentEnabled = true
        editorPane.isBracketMatchingEnabled = true

        setLightTextArea(editorPane)

        val sp = RTextScrollPane(editorPane)
        sp.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        sp.preferredSize = Dimension(500, 500)
        sp.minimumSize = Dimension(MIN_PANE_WIDTH, MIN_PANE_HEIGHT)
        setLightScrollPane(sp)

        outputPane = JTextArea()
        outputPane.isEditable = false

        val outputScrollPane = JScrollPane(outputPane)
        outputScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        // The output pane should be smaller but it doesn't want
        outputScrollPane.preferredSize = Dimension(500, 100)
        outputScrollPane.minimumSize = Dimension(MIN_PANE_WIDTH, MIN_PANE_HEIGHT)

        // return JSplitPane(JSplitPane.VERTICAL_SPLIT, editorScrollPane, outputScrollPane)
        return JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, outputScrollPane)
    }

    private fun createMenuBar() {
        val menuBar = JMenuBar()
        val file = JMenu("File")

        // We need one menu item to give to the file manager
        val item = newMenuItem("New", { HomeManager.newFile() }, file)
        newMenuItem("Open", { HomeManager.openFile() }, file)
        newMenuItem("Save", { HomeManager.saveFile() }, file)
        newMenuItem("Save as", { HomeManager.saveFilesAs() }, file)
        newMenuItem("Exit", { HomeManager.closeFile() }, file)

        FileManager.menuItem = item
        menuBar.add(file)

        val edit = JMenu("Edit")

        newMenuItem("Undo", {}, edit, RTextArea.getAction(RTextArea.UNDO_ACTION))
        newMenuItem("Redo", {}, edit, RTextArea.getAction(RTextArea.REDO_ACTION))
        edit.addSeparator()
        newMenuItem("Cut", {}, edit, RTextArea.getAction(RTextArea.CUT_ACTION))
        newMenuItem("Copy", {}, edit, RTextArea.getAction(RTextArea.COPY_ACTION))
        newMenuItem("Paste", {}, edit, RTextArea.getAction(RTextArea.PASTE_ACTION))
        newMenuItem("Delete", {}, edit, RTextArea.getAction(RTextArea.DELETE_ACTION))
        edit.addSeparator()
        newMenuItem("Select all", {}, edit, RTextArea.getAction(RTextArea.SELECT_ALL_ACTION))

        menuBar.add(edit)

        val console = JMenu("Console")
        newMenuItem("Clear", { HomeManager.cleanConsole() }, console)
        menuBar.add(console)

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

    fun idleLabel() {
        running.text = "Idle"
        running.icon = null
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

    private fun newMenuItem(
        label: String,
        actionListener: () -> Unit,
        parent: JMenu,
        action: Action? = null,
    ): JMenuItem {
        val item: JMenuItem

        if (action != null) {
            item = JMenuItem(action)
        } else {
            item = JMenuItem(label)
            item.addActionListener {
                actionListener()
            }
        }
        item.toolTipText = null
        parent.add(item)

        return item
    }

    fun clearOutput() {
        outputPane.text = ""
    }
}
