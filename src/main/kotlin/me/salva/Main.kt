package me.salva

import com.formdev.flatlaf.FlatLightLaf
import javax.swing.SwingUtilities

fun main() {
    FlatLightLaf.install()
    SwingUtilities.invokeLater {
        Home.isVisible = true
    }
}