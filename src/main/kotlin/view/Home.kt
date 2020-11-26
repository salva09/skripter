package view

import java.awt.Dimension
import java.io.IOException
import java.net.URL
import javax.swing.*
import kotlin.system.exitProcess


class Home : JFrame() {
    var mainPane: JPanel = JPanel()

    init {
        setFrameLookAndFeel()
        buildUi()
        setFrameConfigurations()
    }

    private fun setFrameLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
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
        val editorPane = JEditorPane()
        val outputPane = JPanel()

        editorPane.isEditable = true

        val editorScrollPane = JScrollPane(editorPane)
        editorScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        editorScrollPane.preferredSize = Dimension(500, 500)
        editorScrollPane.minimumSize = Dimension(10, 10)

        val splitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT, editorScrollPane, outputPane)
        mainPane.add(splitPane)
    }

    private fun setFrameConfigurations() {
        size = Dimension(510, 540)
        title = "Skripter"
        contentPane = mainPane
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)
    }
}