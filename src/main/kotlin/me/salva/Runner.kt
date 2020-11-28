package me.salva

import java.io.InputStream
import java.io.PrintStream
import java.util.*
import kotlin.properties.Delegates

object Runner {
    private lateinit var scriptThread: Thread
    private var exitCode = 0
    private var isRunning: Boolean by Delegates.observable(false) { _, _, newValue ->
        run {
            if (newValue) {
                HomeManager.changeLabelToRunning()
            } else {
                HomeManager.changeLabelToIdle(exitCode)
            }
        }
    }

    fun run(script: String) {
        // Runs the command in other thread so we can edit while is running
        // also kotlin scripts right now are kinda slow
        scriptThread = Thread {
            // We want to clear the previous output, don't we?
            Home.clearOutput()

            isRunning = true
            val process = Runtime.getRuntime().exec("kotlinc -script $script")
            inheritIO(process.inputStream, System.out)
            inheritIO(process.errorStream, System.err)

            exitCode = process.waitFor()
            isRunning = false
        }
        scriptThread.start()
    }

    fun stop() {
        scriptThread.interrupt()
        println("Interrupted. x_x")
    }

    private fun inheritIO(src: InputStream, dest: PrintStream) {
        val sc = Scanner(src)
        while (sc.hasNextLine()) {
            dest.println(sc.nextLine())
        }
    }

}