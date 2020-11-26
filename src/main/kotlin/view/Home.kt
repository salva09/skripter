package view

import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.UIManager
import kotlin.system.exitProcess

class Home : JFrame() {
    var mainPane: JPanel = JPanel()

    init {
        setFrameLookAndFeel()
        //buildUi()
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
                JOptionPane.ERROR_MESSAGE)
            exitProcess(0)
        }
    }

    private fun buildUi() {
        TODO("Not yet implemented")
    }

    private fun setFrameConfigurations() {
        title = "Skripter"
        contentPane = mainPane
        defaultCloseOperation = EXIT_ON_CLOSE
        setLocationRelativeTo(null)
    }
}