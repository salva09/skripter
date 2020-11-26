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
        editorPane.isEditable = true

        val editorScrollPane = JScrollPane(editorPane)
        editorScrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        editorScrollPane.preferredSize = Dimension(250, 145)
        editorScrollPane.minimumSize = Dimension(10, 10)

        mainPane.add(editorScrollPane)
    }

    private fun setFrameConfigurations() {
        title = "Skripter"
        contentPane = mainPane
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)
    }
}