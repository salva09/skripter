import view.Home
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        Home().isVisible = true
    }
}