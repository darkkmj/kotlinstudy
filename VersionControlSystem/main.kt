package svcs

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.security.MessageDigest
import kotlin.io.path.exists


class SVCS() {
    private val commandMap = mapOf<String, String> (
            "config" to "Get and set a username.",
            "add" to "Add a file to the index.",
            "log" to "Show commit logs.",
            "commit" to "Save changes.",
            "checkout" to "Restore a file."
    )

    private val digits = "0123456789ABCDEF"
    private var name: String = ""
    private var fileList: MutableList<String> = arrayListOf()
    private var md: MessageDigest = MessageDigest.getInstance("SHA-256")
    private var currentHash = ""

    init {
        if (!Paths.get("./vcs").exists())
            Files.createDirectories(Paths.get("./vcs"))
        if (!Paths.get("./vcs/commits").exists())
            Files.createDirectories(Paths.get("./vcs/commits"))
        val configFile = File("./vcs/config.txt")
        if (configFile.exists()) {
            name = configFile.readText()
        }
        val indexFile = File("./vcs/index.txt")
        if (indexFile.exists()) {
            fileList = indexFile.readLines().toMutableList()
        }

        val logFile = File("./vcs/log.txt")
        if (logFile.exists()) {
            val temp = logFile.readLines().toMutableList()
            currentHash = temp[0].substringAfter(delimiter = "commit ")
        }
    }

    private fun showHelp(key: String) {
        if (commandMap.keys.contains(key)) {
            println("${commandMap[key]}")
        }
        else {
            println("'${key}' is not a SVCS command.")
        }
    }

    private fun showTrackedFileList() {
        println("Tracked files:")
        println(fileList.joinToString(separator = "\n"))
    }

    private fun cmdConfig(name_: String) {
        if (name_ != "") {
            name = name_
            println("The username is $name.")
            val file = File("./vcs/config.txt")
            file.writeText(name, Charsets.UTF_8)
        }
        else {
            println(
                if (name.isEmpty()) "Please, tell me who you are."
                else "The username is $name."
            )
        }
    }

    private fun cmdAdd(file_name: String) {
        if (file_name != "") {
            if (File(file_name).exists()) {
                fileList.add(file_name)
                val file = File("./vcs/index.txt")
                file.writeText(fileList.joinToString(separator = "\n"), Charsets.UTF_8)

                println("The file '${file_name}' is tracked.")
            }
            else {
                println("Can't find '${file_name}'.")
            }
        }
        else {
            if (fileList.isEmpty()) showHelp("add")
            else showTrackedFileList()
        }
    }

    private fun cmdLog() {
        val logFile = File("./vcs/log.txt")
        if (logFile.length() == 0L) {
            println("No commits yet.")
        }
        else {
            println(logFile.readText(charset = Charsets.UTF_8))
        }
    }

    private fun cmdCommit(content: String) {
        when (content) {
            "" -> println("Message was not passed.")
            else -> {
                // update hash
                val hashCode = getHash()

                if (currentHash == hashCode) {
                    println("Nothing to commit.")
                }
                else {
                    // update current hash
//                    currentHash = hashCode

                    // copy indexed files
                    try {
                        Files.createDirectories(Paths.get("./vcs/commits/${hashCode}"))
                        for (it in fileList) {
                            Files.copy(File(it).toPath(), File("./vcs/commits/${hashCode}/${it}").toPath())
                        }
                    } catch(e: Exception) {
                        println(" ${e.message} -> ${currentHash}:${hashCode}")
                        println("              -> ${currentHash == hashCode}")
                    }

                    currentHash = hashCode

                    // Update log file
                    val logFile = File("./vcs/log.txt")
                    var logContent = ""
                    if (logFile.exists()) {
                        logContent = logFile.readText(charset = Charsets.UTF_8)
                    }
                    logFile.bufferedWriter().use { out ->
                        out.write("commit $hashCode\n")
                        out.write("Author: $name\n")
                        out.write("$content\n")
                        out.write(logContent)
                    }

                    println("Changes are committed.")
                }
            }
        }
    }

    private fun cmdCheckout(content: String) {
        when (content) {
            "" -> println("Commit id was not passed.")
            else -> {
                val logFile = File("./vcs/log.txt")
                if (logFile.exists()) {
                    var hashList = mutableListOf<String>()
                    logFile.forEachLine {
                        if (it.startsWith("commit")) {
                            hashList.add(it.substringAfter(delimiter = "commit "))
                        }
                    }

                    for (hash in hashList) {
                        if (hash == content) {
                            // Restore tracked files
                            val commitFolder = File("./vcs/commits/${hash}")
                            commitFolder.copyRecursively(target = File("./"), overwrite = true)
                            println("Switched to commit ${content}.")
                            return
                        }
                    }

                    println("Commit does not exist.")
                }
                else {
                    println("Commit does not exist.")
                }
            }
        }
    }

    private fun getHash(): String {
        // open files and calculate hash code
        var fileContents: String = ""

        for (file in fileList) {
            val indexedFile = File(file)
            fileContents += indexedFile.readText()
        }

        md.update(fileContents.toByteArray())
        return bytesToHex(md.digest())
    }

    private fun bytesToHex(byteArray: ByteArray): String {
        val hexChars = CharArray(byteArray.size * 2)
        for (i in byteArray.indices) {
            val v = byteArray[i].toInt() and 0xff
            hexChars[i * 2] = digits[v shr 4]
            hexChars[i * 2 + 1] = digits[v and 0xf]
        }
        return String(hexChars)
    }

    fun showHelp() {
        println("These are SVCS commands:")
        commandMap.forEach {
            print(it.key)
            repeat(11 - it.key.length) {
                print(" ")
            }
            println(it.value)
        }
    }

    fun command(args: Array<String>) {
        if (args.size == 1) {
            when(args[0]) {
                "config" -> cmdConfig("")
                "add" -> cmdAdd("")
                "log" -> cmdLog()
                "commit" -> cmdCommit("")
                "checkout" -> cmdCheckout("")
                else -> showHelp(args[0])
            }
        }
        else {
            when(args[0]) {
                "config" -> cmdConfig(args[1])
                "add" -> cmdAdd(args[1])
                "commit" -> cmdCommit(args[1])
                "checkout" -> cmdCheckout((args[1]))
            }
        }
    }
}


fun main(args: Array<String>) {
    val svcs = SVCS()

    if (args.isNotEmpty()) {
        if (args[0] == "--help") {
            svcs.showHelp()
        }
        else {
            svcs.command(args)
        }
    }
    else {
        svcs.showHelp()
    }
}
