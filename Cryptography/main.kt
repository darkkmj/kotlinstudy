package cryptography

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import kotlin.experimental.xor

fun hideMessage(bufferedImage: BufferedImage, message: ByteArray, outputFileName: String) {
    var byteArray = message
    byteArray += 0x0.toByte()
    byteArray += 0x0.toByte()
    byteArray += 0x3.toByte()
    if (byteArray.size * 8 > bufferedImage.width * bufferedImage.height) {
        println("The input image is not large enough to hold this message.")
    }
    else {
        for (i in byteArray.indices) {
            for (j in 0 until 8) {
                val height = (i * 8 + j) / bufferedImage.width
                val width = (i * 8 + j) % bufferedImage.width

                val color = Color(bufferedImage.getRGB(width, height))
                //var bitValue = (byteArray[i].toInt() shr j) and 0x1
                var bitValue = byteArray[i].toInt().shr(7 - j).and(0x1)
                bitValue = if (bitValue == 0) color.blue and (0x1.inv()) else color.blue or 0x1
                val newColor = Color(
                        color.red,
                        color.green,
                        bitValue
                )

                bufferedImage.setRGB(width, height, newColor.rgb)
            }
        }

        // Write output file
        ImageIO.write(bufferedImage, "PNG", File(outputFileName))
        println("Message saved in $outputFileName image.")
    }
}

fun showMessage(bufferedImage: BufferedImage, password: String) {
    var message = byteArrayOf()
    var count = 0
    var temp: Byte = 0

    checkLoop@
    for (i in 0 until bufferedImage.height)  {
        for (j in 0 until bufferedImage.width) {
            val color = Color(bufferedImage.getRGB(j, i))

            // Restore message
            temp = temp.toInt().or(color.blue.and(0x1).shl(7 - count)).toByte()

            // Complete to restore one character
            if (count == 7) {
                message += temp
                if ((temp.toInt() == 0x3) && (message.size >= 3)) {
                    if ((message[message.lastIndex - 2].toInt() == 0) && (message[message.lastIndex - 1].toInt() == 0)) {
                        break@checkLoop
                    }
                }

                temp = 0
                count = 0
            }
            else
                count++
        }
    }

    // Decrypt message
    val passwordArray = password.encodeToByteArray()
    var decryptedArray = byteArrayOf()

    for (i in 0 until message.size - 3) {
        var index = i
        if (i > passwordArray.lastIndex) {
            index = i % passwordArray.size
        }

        decryptedArray += message[i] xor passwordArray[index]
    }

    // Print message
    println("message:");
    println("${decryptedArray.toString(Charsets.UTF_8)}")
}

fun encryptMessage(message: String, password: String): ByteArray {
    val byteArray = message.encodeToByteArray()
    val passwordArray = password.encodeToByteArray()
    var encryptedArray = byteArrayOf()

    for (i in byteArray.indices) {
        var index = i
        if (i > passwordArray.lastIndex) {
            index = i % passwordArray.size
        }

        encryptedArray += byteArray[i] xor passwordArray[index]
    }

    return (encryptedArray)
}

fun main() {
    while (true) {
        println("Task (hide, show, exit):")
        when(readln()) {
            "hide" -> {
                // Get file names
                println("Input image file:")
                val inputImageFile = readln()
                println("Output image file:")
                val outputImageFile = readln()

                try {
                    // Open input file
                    val bufferedImage = ImageIO.read(File(inputImageFile))
                    println("Message to hide:")
                    val message = readln()
                    println("Password:")
                    val password = readln()

                    // Convert message using password
                    val encryptedMessage = encryptMessage(message, password)

                    // Hide message into the image
                    hideMessage(bufferedImage, encryptedMessage, outputImageFile)
                } catch (e: Exception) {
                    println("Can't read input file!")
                }
            }

            "show" -> {
                // Get file names
                println("Input image file:")
                val inputImageFile = readln()

                try {
                    // Open input file
                    val bufferedImage = ImageIO.read(File(inputImageFile))
                    println("Password:")
                    val password = readln()
                    showMessage(bufferedImage, password)
                } catch (e: Exception) {
                    println("Can't read input file!")
                }
            }

            "exit" -> {
                println("Bye!")
                break
            }
        }
    }
}
