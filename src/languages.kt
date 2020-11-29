import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory
import org.fife.ui.rsyntaxtextarea.SyntaxConstants
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory
import org.fife.ui.rsyntaxtextarea.folding.CurlyFoldParser
import org.fife.ui.rsyntaxtextarea.folding.FoldParserManager

data class Language(
    val name: String = "",
    val description: String = "",
    val icon: String = "",
    val executionCommand: String = "",
    val versionCommand: String = "",
    val syntaxKey: String = "",
    val syntaxClass: String = "",
    val fileExtension: String = "",
    val homeUri: String = "",
    val downloadUri: String = "",
)

private val languages = listOf(
    Language(
        "Kotlin",
        "A modern programming language that makes developers happier.",
        "icons/kotlin.png",
        "kotlinc -script",
        "kotlin -version",
        "text/kotlin",
        "syntax.KotlinSyntax",
        "kts",
        "https://kotlinlang.org/",
        "https://kotlinlang.org/docs/tutorials/command-line.html"
    ),
    Language(
        "Swift",
        "Swift is a general-purpose programming language built using a modern " +
            "approach to safety, performance, and software design patterns.",
        "icons/swift.png",
        "/usr/bin/env swift",
        "/usr/bin/env swift -version",
        "text/swift",
        "syntax.SwiftSyntax",
        "swift",
        "https://swift.org/",
        "https://swift.org/getting-started/#installing-swift"
    ),
    Language(
        "Python",
        "Python is a programming language that lets you work quickly and integrate " +
            "systems more effectively.",
        "icons/python.png",
        "/usr/bin/env python",
        "python -V",
        SyntaxConstants.SYNTAX_STYLE_PYTHON,
        "",
        "py",
        "https://www.python.org/",
        "https://wiki.python.org/moin/BeginnersGuide/Download"
    )
)

fun initSyntax() {
    val atmf = TokenMakerFactory.getDefaultInstance() as AbstractTokenMakerFactory

    for (language in languages) {
        if (language.syntaxClass != "") {
            atmf.putMapping(language.syntaxKey, language.syntaxClass)
            FoldParserManager.get().addFoldParserMapping(language.syntaxKey, CurlyFoldParser())
        }
    }
}

fun getListOfLanguages(): List<Language> {
    return languages
}

fun getLanguageByName(name: String): Language {
    val index = languages.binarySearch {
        if (it.name == name) {
            return@binarySearch 0
        }
        return@binarySearch -1
    }
    return languages[index]
}

fun getLanguageByExtension(extension: String): Language {
    for (language in languages) {
        if (language.fileExtension == extension)
            return language
    }
    throw ClassNotFoundException("Language not supported")
}
