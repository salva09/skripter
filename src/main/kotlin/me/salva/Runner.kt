package me.salva

import java.io.InputStream
import java.io.PrintStream
import java.util.*
import kotlin.properties.Delegates

object Runner {
    // For swift scripting this can be changed
    private lateinit var scriptThread: Thread
    private var exitCode = 0
    private var isAlive = false
    private var process: Process? = null

    fun run(script: String) {
        // Runs the command in other thread so we can edit while is running
        // also kotlin scripts right now are kinda slow
        scriptThread = Thread {
            // We want to clear the previous output, don't we?
            Home.clearOutput()

            HomeManager.changeLabelToRunning()
            isAlive = true
            process = when (FileManager.getFileExtension()) {
                "kts" -> {
                    Runtime.getRuntime().exec("kotlinc -script $script")
                }
                "swift" -> {
                    Runtime.getRuntime().exec("/usr/bin/env swift $script")
                }
                "py" -> {
                    Runtime.getRuntime().exec("/usr/bin/env python $script")
                }
                else -> {
                    // This will terminate the program immediately
                    null
                }
            }

            inheritIO(process!!.inputStream, System.out)
            inheritIO(process!!.errorStream, System.err)

            exitCode = process!!.waitFor()
            isAlive = false
            HomeManager.changeLabelToIdle(exitCode)
        }
        scriptThread.start()
    }

    fun stop() {
        process!!.destroyForcibly()
        println("I was killed. x_x")
    }

    fun isProcessAlive(): Boolean {
        return isAlive
    }

    private fun inheritIO(src: InputStream, dest: PrintStream) {
        val sc = Scanner(src)
        while (sc.hasNextLine()) {
            dest.println(sc.nextLine())
        }
    }

}