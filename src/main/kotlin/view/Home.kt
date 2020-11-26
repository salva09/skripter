package view

import com.formdev.flatlaf.FlatDarculaLaf
import java.awt.Dimension
import javax.swing.*
import kotlin.system.exitProcess


class Home : JFrame() {
    private lateinit var mainPane: JPanel

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
        // TODO("Implement syntax highlight")
        mainPane = JPanel()
        createMenuBar()
        createPanes()
    }

    private fun createPanes() {
        val editorPane = JEditorPane()
        val outputPane = JPanel()

        editorPane.isEditable = true

        val editorScrollPane = JScrollPane(editorPane)
        editorScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        editorScrollPane.preferredSize = Dimension(500, 450)
        editorScrollPane.minimumSize = Dimension(500, 100)

        outputPane.minimumSize = Dimension(500, 100)

        val splitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT, editorScrollPane, outputPane)
        mainPane.add(splitPane)
    }

    private fun createMenuBar() {
        val menuBar = JMenuBar()

        menuBar.add(JMenu("Test"))

        jMenuBar = menuBar
    }

    private fun setFrameConfigurations() {
        size = Dimension(510, 540)
        isResizable = false
        title = "Skripter"
        contentPane = mainPane
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)
    }
}