import com.formdev.flatlaf.FlatLightLaf
import view.Home
import javax.swing.SwingUtilities

fun main() {
    FlatLightLaf.install()
    SwingUtilities.invokeLater {
        Home().isVisible = true
    }
}