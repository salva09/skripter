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
            Home.clearOutput()
            val p = Runtime.getRuntime().exec("kotlinc -script $script")
            inheritIO(p.inputStream, System.out)
            inheritIO(p.errorStream, System.err)
        }
        thread.start()
    }

    private fun inheritIO(src: InputStream, dest: PrintStream) {
        Thread {
            val sc = Scanner(src)
            while (sc.hasNextLine()) {
                dest.println(sc.nextLine())
            }
        }.start()
    }

}