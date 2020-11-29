package me.salva

data class Language(
    val name: String = "",
    val description: String = "",
    val executionCommand: String = "",
    val versionCommand: String = "",
    val fileExtension: String = "",
    val homeUri: String = "",
    val downloadUri: String = "",
)

private val languages = listOf(
    Language(
        "Kotlin",
        "A modern programming language that makes developers happier.",
        "kotlinc -script",
        "kotlin -version",
        "kts",
        "https://kotlinlang.org/",
        "https://kotlinlang.org/docs/tutorials/command-line.html"
    ),
    Language(
        "Swift",
        "Swift is a general-purpose programming language built using a modern " +
            "approach to safety, performance, and software design patterns.",
        "/usr/bin/env swift",
        "swift -version",
        "swift",
        "https://swift.org/",
        "https://swift.org/getting-started/#installing-swift"
    ),
    Language(
        "Python",
        "Python is a programming language that lets you work quickly and integrate " +
            "systems more effectively.",
        "/usr/bin/env python",
        "python -V",
        "py",
        "https://www.python.org/",
        "https://wiki.python.org/moin/BeginnersGuide/Download"
    )
)

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
    val index = languages.binarySearch {
        if (it.fileExtension == extension) {
            return@binarySearch 0
        }
        return@binarySearch -1
    }
    return languages[index]
}
