package syntax

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea
import org.fife.ui.rtextarea.RTextScrollPane
import java.awt.Color
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager

class GtkTheme : Theme {
    override val background: Color = Color(255, 255, 255)
    override val foreground: Color = Color(255, 255, 255)

    override fun setFrameLookAndFeel(frame: JFrame) {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")
        SwingUtilities.updateComponentTreeUI(frame)
    }

    override fun setTextAreaTheme(textArea: RSyntaxTextArea) {
        // TODO("Not yet implemented")
    }

    override fun setScrollPaneTheme(scrollPane: RTextScrollPane) {
        // TODO("Not yet implemented")
    }
}