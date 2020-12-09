import syntax.GtkTheme
import syntax.LightTheme

lateinit var view: Home

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            view = Home(LightTheme())
        }
    }
}
