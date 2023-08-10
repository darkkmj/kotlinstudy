package encryptdecrypt

import java.io.File

fun decryptMessage(key: Int, input: String, alg: String) : String {
    var result = CharArray(0);

    when(alg) {
        "shift" -> {
            input.forEach {
                result += when (it) {
                    in 'a'..'z' -> if ('a' <= it - key) it - key else 'z' - key + (it + 1 - 'a')
                    in 'A'..'Z' -> if ('A' <= it - key) it - key else 'Z' - key + (it + 1 - 'A')
                    else -> it
                }
            }
        }

        "unicode" -> {
            input.forEach {
                result += it - key
            }
        }
    }

    return String(result)
}

fun encryptMessage(key: Int, input: String, alg: String) : String {
    var result = CharArray(0);

    when(alg) {
        "shift" -> {
            input.forEach {
                result += when (it) {
                    in 'a'..'z' -> if ('z' - it >= key) it + key else 'a' + key - ('z' - it + 1)
                    in 'A'..'Z' -> if ('Z' - it >= key) it + key else 'A' + key - ('Z' - it + 1)
                    else -> it
                }
            }
        }

        "unicode" -> {
            input.forEach {
                result += it + key
            }
        }
    }

    return String(result)
}

fun main(args: Array<String>) {
    var mode = "enc"
    var data = ""
    var key = 0
    var inFile = ""
    var outFile = ""
    var dataFixed = false
    var result = ""
    var alg = "shift"

    for(i in args.indices) {
        try {
            when (args[i]) {
                "-mode" -> {
                    mode = args[i + 1]
                    if (mode != "enc" && mode != "dec") {
                        println("Error: invalid mode value")
                        return
                    }
                }

                "-alg" -> {
                    alg = args[i + 1]
                    if (alg != "shift" && alg != "unicode") {
                        println("Error: invalid algorithm value")
                        return
                    }
                }

                "-data" -> {
                    data = args[i + 1]
                    dataFixed = true
                }

                "-key" -> key = args[i + 1].toInt()
                "-in" -> inFile = args[i + 1]
                "-out" -> outFile = args[i + 1]
            }
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return
        }
    }

    // Update data if dataFixed == false && inFile != ""
    if (!dataFixed && (inFile != "")) {
        try {
            data = File(inFile).readText()
        } catch (e: Exception) {
            println("Error: ${e.message}")
            return
        }
    }

    // Execute command
    when(mode) {
        "enc" -> result = encryptMessage(key, data, alg)
        "dec" -> result = decryptMessage(key, data, alg)
    }

    // Save data or print it
    if (outFile != "") {
        File(outFile).bufferedWriter().use { out -> out.write(result) }
    }
    else {
        println(result)
    }
}
