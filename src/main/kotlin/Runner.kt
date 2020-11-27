import java.util.concurrent.TimeUnit

object Runner {
    fun run(script: String) {
        // Runs the command in other thread so we can edit while is running
        // also kotlin scripts right now are kinda slow, would be nice to try to improve them
        val thread = Thread {
            "kotlinc -script $script".runCommand()
        }
        thread.start()
    }

    private fun String.runCommand() {
        ProcessBuilder(*split(" ").toTypedArray())
            .inheritIO()
            .start()
            .waitFor(60, TimeUnit.MINUTES)
    }
}