package me.salva

import java.io.IOException
import java.io.OutputStream
import java.io.PrintStream
import javax.swing.JTextArea

fun redirectOutput(textArea: JTextArea) {
    val printStream = PrintStream(CustomOutputStream(textArea))
    System.setOut(printStream)
    System.setErr(printStream)
}

class CustomOutputStream(private val textArea: JTextArea) : OutputStream() {
    @Throws(IOException::class)
    override fun write(b: Int) {
        // Redirects data to the text area
        textArea.text = textArea.text + b.toChar()
        // Scrolls the text area to the end of data
        textArea.caretPosition = textArea.document.length
        // Keeps the textArea up to date
        textArea.update(textArea.graphics)
    }
}
