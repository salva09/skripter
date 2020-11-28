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

            process = Runtime.getRuntime().exec(getCommand(script))

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

    private fun getCommand(script: String): String {
        return when (FileManager.getFileExtension()) {
            "kts" -> {
                "kotlinc -script $script"
            }
            "swift" -> {
                "/usr/bin/env swift $script"
            }
            "py" -> {
                "/usr/bin/env python $script"
            }
            else -> {
                throw Exception("File not supported as script")
            }
        }
    }

    private fun inheritIO(src: InputStream, dest: PrintStream) {
        val sc = Scanner(src)
        while (sc.hasNextLine()) {
            dest.println(sc.nextLine())
        }
    }
}