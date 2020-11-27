import view.Home
import java.io.InputStream
import java.io.PrintStream
import java.util.*
import java.util.concurrent.TimeUnit

object Runner {
    fun run(script: String) {
        // Runs the command in other thread so we can edit while is running
        // also kotlin scripts right now are kinda slow, would be nice to try to improve them
        val thread = Thread {
            // We want to clear the previous output, don't we
            Home.clearOutput()
            val process = Runtime.getRuntime().exec("kotlinc -script $script")
            inheritIO(process.inputStream, System.out)
            inheritIO(process.errorStream, System.err)

            // When the process has finished, print the exit code
            println("Exit code: ${process.waitFor()}")
        }.start()
    }

    private fun inheritIO(src: InputStream, dest: PrintStream) {
        val sc = Scanner(src)
        while (sc.hasNextLine()) {
            dest.println(sc.nextLine())
        }
    }

}