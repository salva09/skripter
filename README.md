# Skripter
A GUI tool that allows users to enter a script, execute it, and see its output side-by-side

## Required functionality:
- [x] Should have an editor pane and an output pane.
- [x] Write the script to a file and run it using `/usr/bin/env swift foo.swift`, or `kotlinc -script foo.kts` respectively.
- [x] Assume the script might run for a long time.
- [x] Show live output of the script as it executes.
- [x] Show errors from the execution/if the script couldn’t be interpreted.
- [ ] Show an indication whether the script is currently running.
- [x] Show an indication whether the exit code of the last run was non-zero.

## Implement at least one of the following:
- [ ] Highlight language keywords in a color different from the rest of the code. You may assume that keywords are not valid in other contexts, e.g. as enum member names. You may restrict yourself to 10 keywords, if more could be added easily.
- [ ] Make location descriptions of errors (e.g. “script:2:1: error: cannot find 'foo' in scope”) clickable, so users can navigate to the exact cursor positions in code.
- [ ] Add a mechanism to run the script multiple times. Show a progress bar as well as an estimate of the time remaining. The estimate should be based on the time elapsed for already finished runs of the batch. More recent runs should be weighted more than older ones.

## Built with
* [Kotlin](https://kotlinlang.org/) - Programming language
* [OpenJDK 11](https://openjdk.java.net/) - Runtime environment and development kit
* [Gradle](https://gradle.org/) - Build system

## Author
* **Salvador** - [salva09](https://github.com/salva09)

## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE) file for details.
