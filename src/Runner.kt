import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Error
import java.lang.Exception

object Runner {
    private lateinit var scriptThread: Thread
    var isProcessAlive = false
        private set
    private var isInterrupted = false
    private var exitCode = 0
    private var process: Process? = null

    fun run(script: String) {
        // Runs the command in other thread so we can edit while is running
        scriptThread = Thread {
            // We want to clear the previous output, don't we?
            view.clearConsole()

            view.setRunningLabel()
            isProcessAlive = true
            process = Runtime.getRuntime().exec(
                getLanguageByExtension(FileManager.getFileExtension())
                    .executionCommand + " " + script
            )

            inheritIO(process!!.errorStream)
            inheritIO(process!!.inputStream)

            isProcessAlive = false
            exitCode = process!!.waitFor()
            if (isInterrupted) {
                view.setInterruptedLabel()
                isInterrupted = false
            } else {
                if (exitCode == 0) view.setGoodLabel()
                else view.setBadLabel(exitCode)
            }
        }
        scriptThread.start()
    }

    fun stop() {
        if (!isProcessAlive) return
        process?.destroyForcibly()
        // Is this really safe and kills the process? I don't think so
        // but I will let it here unless I think in something better
        scriptThread.interrupt()
        isInterrupted = true
    }

    private fun inheritIO(inputStream: InputStream) {
        try {
            val isr = InputStreamReader(inputStream)
            val br = BufferedReader(isr)
            var line: String?
            while (br.readLine().also { line = it } != null) println(line)
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (err: Error) {
            // print(err.message)
        }
    }
}
