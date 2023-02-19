package converter

import java.math.BigInteger
import java.math.BigDecimal
import kotlin.math.pow

// Do not delete this line

var convert_natural: MutableList<BigInteger> = mutableListOf()
var convert_decimal: MutableList<BigInteger> = mutableListOf()

fun divide(input: BigDecimal, base: BigInteger): BigDecimal {
    val result = input * base.toBigDecimal()
    val integer = result.toBigInteger()
    val decimal = result - integer.toBigDecimal()

    convert_decimal.add(integer)

    if (convert_decimal.size < 5) {
        divide(decimal, base)
    }

    return decimal
}

fun divide(input: BigInteger, base: BigInteger): BigInteger {
    val result = input / base
    val remain = input % base

    if (result < base) {
        if (convert_natural.size != 0 || result != BigInteger.ZERO)
            convert_natural.add(result)
    }
    else {
        divide(result, base)
    }

    convert_natural.add(remain)

    return remain
}

fun convertCharNum(input: BigInteger): Char {
    return (
        if (input < BigInteger.TEN) input.toString()[0]
        else 'a' + (input.toInt() - 10)
    )
}

fun convertChar(input: Char): Int {
    return if (input.isDigit())
        (Character.getNumericValue(input))
    else {
        if (input >= 'a') {
            (10 + (input - 'a'))
        } else {
            (10 + (input - 'A'))
        }
    }
}

fun decimalToDecimal(source: String, base: Int): BigDecimal {
    var result: BigDecimal = BigDecimal.ZERO

    for (index in source.indices) {
//        val temp = convertChar(source[index]).toBigDecimal() / base.toBigDecimal().pow(index + 1)
//        result += temp
        val temp = convertChar(source[index]) * 1.0 / base.toDouble().pow(index.toDouble() + 1)
        result += temp.toBigDecimal()
    }

    return (result)
}

fun naturalToDecimal(source: String, base: Int): BigInteger {
    var result: BigInteger = BigInteger.ZERO

    for(index in source.indices) {
        val temp = convertChar(source[index]).toBigInteger() * base.toBigInteger().pow(index)
        result += temp
    }

    return (result)
}

fun main() {
    var base: Int
    var target: Int

    while (true) {
        print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")

        val input = readln()
        if (input == "/exit") {
            break
        }
        else {
            val temp = input.split(" ").map { it.toInt() }
            base = temp[0]
            target = temp[1]
        }

        while (true) {
            print("Enter number in base $base to convert to base $target (To go back type /back) ")

            val inputData = readln()
            if (inputData == "/back") {
                break
            }
            else {
                val inputList: List<String> = inputData.split(".")
                var result = ""
                
                // Convert natural
                if ((inputList[0].length != 1) || (inputList[0] != "0")) {
                    result = if (base == 10) {
                        divide(inputList[0].toBigInteger(), target.toBigInteger())
                        convert_natural.joinToString("")
                    } else {
                        divide(naturalToDecimal(inputList[0].reversed(), base), target.toBigInteger())
                        convert_natural.map { convertCharNum(it) }.joinToString("").replaceFirst("^0+(?!$)", "")
                    }
                }

                // Convert decimal
                if (inputList.size >= 2) {
                    result += "."

                    result += if (base == 10) {
                        divide(inputList[1].toBigDecimal(), target.toBigInteger())
                        convert_decimal.joinToString("")
                    } else {
                        divide(decimalToDecimal(inputList[1], base), target.toBigInteger())
                        convert_decimal.map { convertCharNum(it) }.joinToString("").replaceFirst("^0+(?!$)", "")
                    }
                }
                
                println("Conversion result: ${result}\n")
                convert_natural.clear()
                convert_decimal.clear()

                println(
                    if (inputList.size >= 2)
                        "${inputList[0]}, ${inputList[1]}"
                    else
                        "${inputList[0]}"
                )
            }
        }
    }
}