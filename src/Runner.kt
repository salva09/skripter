
import java.io.*
import java.util.*

object Runner {
    private lateinit var scriptThread: Thread
    private var exitCode = 0
    private var process: Process? = null

    fun run(script: String) {
        // Runs the command in other thread so we can edit while is running
        scriptThread = Thread {
            // We want to clear the previous output, don't we?
            view.clearConsole()

            view.setRunningLabel()

            process = Runtime.getRuntime().exec(
                getLanguageByExtension(FileManager.getFileExtension())
                    .executionCommand + " " + script
            )

            inheritIO(process!!.errorStream)
            inheritIO(process!!.inputStream)

            exitCode = process!!.waitFor()
            if (exitCode == 0) view.setGoodLabel()
            else view.setBadLabel(exitCode)
        }
        scriptThread.start()
    }

    fun stop() {
        /* The process can not be stopped
        "Because some native platforms only provide limited buffer size for standard
        input and output streams, failure to promptly write the input stream or read
        the output stream of the subprocess may cause the subprocess to block, and
        even deadlock."
        */
        process?.destroy()
        println("I was killed. x_x")
    }

    fun isProcessAlive(): Boolean {
        return if (process != null) process!!.isAlive
        else false
    }

    private fun inheritIO(inputStream: InputStream) {
        try {
            val isr = InputStreamReader(inputStream)
            val br = BufferedReader(isr)
            var line: String?
            while (br.readLine().also { line = it } != null) println(line)
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        }
    }
}
