package me.salva.syntax

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

// Light colors
private val GREEN = Color(123, 160, 91)
private val GRAY = Color(128, 128, 128)
private val RED = Color(220, 20, 60)
private val BLUE = Color(176, 196, 222)
private val BACKGROUND = Color(255, 255, 255)
private val FOREGROUND = Color(0, 0, 0)

fun setFrameLookAndFeel(frame: JFrame) {
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

fun setLightTextArea(textArea: RSyntaxTextArea) {
    // Color schemes
    val scheme = textArea.syntaxScheme

    scheme.getStyle(Token.RESERVED_WORD).foreground = GRAY
    scheme.getStyle(Token.RESERVED_WORD_2).foreground = RED
    scheme.getStyle(Token.FUNCTION).foreground = GREEN
    scheme.getStyle(Token.LITERAL_NUMBER_DECIMAL_INT).foreground = BLUE
    textArea.background = BACKGROUND
    textArea.foreground = FOREGROUND
    textArea.currentLineHighlightColor = BACKGROUND
    textArea.isMarginLineEnabled = true
    textArea.marginLineColor = Color.DARK_GRAY

    textArea.revalidate()
}

fun setLightScrollPane(scrollPane: RTextScrollPane) {
    scrollPane.gutter.borderColor = Color.GRAY
    scrollPane.gutter.background = BACKGROUND

    scrollPane.revalidate()
}
