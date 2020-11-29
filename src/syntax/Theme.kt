package syntax

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.Color
import javax.swing.JFrame

interface Theme {
    val background: Color
    val foreground: Color

    fun setFrameLookAndFeel(frame: JFrame)
    fun setTextAreaTheme(textArea: RSyntaxTextArea)
    fun setScrollPaneTheme(scrollPane: RTextScrollPane)
}
