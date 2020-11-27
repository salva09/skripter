import java.util.concurrent.TimeUnit

object Runner {
    fun run(script: String) {
        // Runs the command in other thread so we can edit while is running
        val thread = Thread {
            "kotlinc -script $script".runCommand()
        }
        thread.start()
    }

    private fun String.runCommand() {
        ProcessBuilder(*split(" ").toTypedArray())
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor(60, TimeUnit.MINUTES)
    }
}