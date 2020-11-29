package syntax

import com.formdev.flatlaf.FlatLightLaf
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rsyntaxtextarea.Token
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.Color
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
import javax.swing.UIManager
import kotlin.system.exitProcess

class LightTheme : Theme {
    override val background: Color = Color(255, 255, 255)
    override val foreground: Color = Color(0, 0, 0)
    private val green = Color(123, 160, 91)
    private val gray = Color(128, 128, 128)
    private val red = Color(220, 20, 60)
    private val blue = Color(176, 196, 222)

    override fun setFrameLookAndFeel(frame: JFrame) {
        try {
            UIManager.setLookAndFeel(FlatLightLaf())
            SwingUtilities.updateComponentTreeUI(frame)
        } catch (ex: Exception) {
            JOptionPane.showMessageDialog(
                frame,
                "Oh no! There was an error :( \n" +
                    "Error: ${ex.message}",
                "Error",
                JOptionPane.ERROR_MESSAGE
            )
            exitProcess(0)
        }
    }
    override fun setTextAreaTheme(textArea: RSyntaxTextArea) {
        // Color schemes
        val scheme = textArea.syntaxScheme

        scheme.getStyle(Token.RESERVED_WORD).foreground = gray
        scheme.getStyle(Token.RESERVED_WORD_2).foreground = red
        scheme.getStyle(Token.FUNCTION).foreground = green
        scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = blue
        textArea.background = background
        textArea.foreground = foreground
        textArea.currentLineHighlightColor = background
        textArea.isMarginLineEnabled = true
        textArea.marginLineColor = Color.DARK_GRAY

        textArea.revalidate()
    }
    override fun setScrollPaneTheme(scrollPane: RTextScrollPane) {
        scrollPane.gutter.borderColor = Color.GRAY
        scrollPane.gutter.background = background

        scrollPane.revalidate()
    }
}
