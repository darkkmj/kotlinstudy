package chess

var chessBoard = mutableListOf(
    "    a   b   c   d   e   f   g   h  ".toCharArray(),
    "  +---+---+---+---+---+---+---+---+".toCharArray(),
    "1 |   |   |   |   |   |   |   |   |".toCharArray(),
    "  +---+---+---+---+---+---+---+---+".toCharArray(),
    "2 | W | W | W | W | W | W | W | W |".toCharArray(),
    "  +---+---+---+---+---+---+---+---+".toCharArray(),
    "3 |   |   |   |   |   |   |   |   |".toCharArray(),
    "  +---+---+---+---+---+---+---+---+".toCharArray(),
    "4 |   |   |   |   |   |   |   |   |".toCharArray(),
    "  +---+---+---+---+---+---+---+---+".toCharArray(),
    "5 |   |   |   |   |   |   |   |   |".toCharArray(),
    "  +---+---+---+---+---+---+---+---+".toCharArray(),
    "6 |   |   |   |   |   |   |   |   |".toCharArray(),
    "  +---+---+---+---+---+---+---+---+".toCharArray(),
    "7 | B | B | B | B | B | B | B | B |".toCharArray(),
    "  +---+---+---+---+---+---+---+---+".toCharArray(),
    "8 |   |   |   |   |   |   |   |   |".toCharArray(),
    "  +---+---+---+---+---+---+---+---+".toCharArray()
)
val rowIndex = mutableListOf(4, 8, 12, 16, 20, 24, 28, 32)
val columnIndex = mutableListOf(2, 4, 6, 8, 10, 12, 14, 16)
var black_passant = mutableListOf(0, 0)
var white_passant = mutableListOf(0, 0)
var total_black = 8
var total_white = 8
var whitePawnList = mutableListOf(
    "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"
)
var blackPawnList = mutableListOf(
    "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"
)

fun showChessBoard() {
    for (i in chessBoard.size - 1 downTo(0))
        println(chessBoard[i])
    println()
}

fun invalidInputReturn(): Int {
    // invalid input
    println("Invalid Input")
    return (0)
}

fun updatePosOfPawn(currCol_: Int, currRow_: Int, nextCol_: Int, nextRow_: Int, unit_: Char, passant_: Boolean = false) {
    chessBoard[currCol_][currRow_] = ' '
    chessBoard[nextCol_][nextRow_] = unit_
    if (passant_) {
        if (unit_ == 'B') {
            chessBoard[nextCol_ + 2][nextRow_] = ' '
        }
        else {
            chessBoard[nextCol_ - 2][nextRow_] = ' '
        }
    }
}

fun checkStalemate(black_: Boolean): Boolean {
    var result = true
    val targetPawnList: MutableList<String>
    val oppositePawnList: MutableList<String>
    val offset: Int

    if (black_) {
        targetPawnList = whitePawnList
        oppositePawnList = blackPawnList
        offset = 1
    }
    else {
        targetPawnList = blackPawnList
        oppositePawnList = whitePawnList
        offset = (-1)
    }

    if (targetPawnList.size == 0)
        result = false

    for (pawn in targetPawnList) {
        // check forward
        val currPos = pawn[1].digitToInt()
        val nextPosStr = (currPos + offset).toString()
        var keyword = pawn[0] + nextPosStr
        if (oppositePawnList.binarySearch(keyword) < 0) {
            result = false
            break
        }

        // check side
        if (pawn[0] == 'a') {
            keyword = "b$nextPosStr"
            if (oppositePawnList.binarySearch(keyword) >= 0) {
                result = false
                break
            }
        }
        else if (pawn[0] == 'h') {
            keyword = "g$nextPosStr"
            if (oppositePawnList.binarySearch(keyword) >= 0) {
                result = false
                break
            }
        }
        else {
            keyword = (pawn[0] - 1) + nextPosStr
            if (oppositePawnList.binarySearch(keyword) >= 0) {
                result = false
                break
            }

            keyword = (pawn[0] + 1) + nextPosStr
            if (oppositePawnList.binarySearch(keyword) >= 0) {
                result = false
                break
            }
        }
    }

    return (result)
}

fun updatePawnList(black_: Boolean, input_: String, enemy_: Boolean = false, passant_: Boolean = false) {
    val inputChunked = input_.chunked(2)
    if (black_) {
        val index = blackPawnList.binarySearch(inputChunked[0])
        blackPawnList[index] = inputChunked[1]

        if (enemy_) {
            whitePawnList.removeAt(whitePawnList.binarySearch(inputChunked[1]))
        }

        if (passant_) {
            val keyword = inputChunked[1][0] + (inputChunked[1][1].digitToInt() + 2).toString()
            whitePawnList.removeAt(whitePawnList.binarySearch(keyword))
        }
    }
    else {
        val index = whitePawnList.binarySearch(inputChunked[0])
        whitePawnList[index] = inputChunked[1]

        if (enemy_) {
            blackPawnList.removeAt(blackPawnList.binarySearch(inputChunked[1]))
        }

        if (passant_) {
            val keyword = inputChunked[1][0] + (inputChunked[1][1].digitToInt() - 2).toString()
            blackPawnList.removeAt(blackPawnList.binarySearch(keyword))
        }
    }

    // For binary search
    whitePawnList.sort()
    blackPawnList.sort()

//    println(blackPawnList)
//    println(whitePawnList)
}

/**
 * @return 0: Invalid Input, 1: Normal, 2: Game end
 */
fun updateChessBoard(black: Boolean, input: String): Int {
    val (unit, enemy) = (
        if(black) 'B' to 'W'
        else 'W' to 'B'
    )

    // Check input
    val currPos = input[1].digitToInt()
    val nextPos = input[3].digitToInt()
    val currRow = rowIndex[input[0] - 'a']
    val currCol = columnIndex[currPos - 1]
    val nextRow = rowIndex[input[2] - 'a']
    val nextCol = columnIndex[nextPos - 1]

    if (chessBoard[currCol][currRow] == unit) {
        if ((currRow - nextRow > 4) || (nextRow - currRow > 4) || (chessBoard[nextCol][nextRow] == unit)) {
            return invalidInputReturn()
        }
        else {
            val posGoLen: Int
            val startPos: Int
            val endPos: Int

            if (black) {
                posGoLen = currPos - nextPos
                startPos = 7
                endPos = 1
            }
            else {
                posGoLen = nextPos - currPos
                startPos = 2
                endPos = 8
            }

            if (currRow == nextRow) {
                if (posGoLen == 2) {
                    if (currPos == startPos) {
                        updatePosOfPawn(currCol, currRow, nextCol, nextRow, unit)
                        updatePawnList(black, input)
                        if (black) {
                            black_passant[0] = nextCol
                            black_passant[1] = nextRow
                        }
                        else {
                            white_passant[0] = nextCol
                            white_passant[1] = nextRow
                        }
                    }
                    else {
                        return invalidInputReturn()
                    }
                }
                else if (posGoLen == 1) {
                    // Normal move
                    if (chessBoard[nextCol][nextRow] == ' ') {
                        updatePosOfPawn(currCol, currRow, nextCol, nextRow, unit)
                        updatePawnList(black, input)
                    }
                    else {
                        return invalidInputReturn()
                    }
                }
                else {
                    return invalidInputReturn()
                }
            }
            else if ((currRow - nextRow == 4) || (nextRow - currRow == 4)) {
                if (posGoLen == 2) {
                    return invalidInputReturn()
                }
                else if (posGoLen == 1) {
                    if (chessBoard[nextCol][nextRow] == enemy) {
                        updatePosOfPawn(currCol, currRow, nextCol, nextRow, unit)
                        updatePawnList(black, input, true)
                    }
                    else if (black && (white_passant[0] != 0) && (columnIndex[nextPos] == white_passant[0]) && (nextRow == white_passant[1])) {
                        updatePosOfPawn(currCol, currRow, nextCol, nextRow, unit, true)
                        updatePawnList(true, input)
                    }
                    else if (!black && (black_passant[0] != 0) && (columnIndex[nextPos - 2] == black_passant[0]) && (nextRow == black_passant[1])) {
                        updatePosOfPawn(currCol, currRow, nextCol, nextRow, unit, true)
                        updatePawnList(false, input)
                    }
                    else {
                        return invalidInputReturn()
                    }

                    if (black) total_white--
                    else total_black--
                }
                else {
                    return invalidInputReturn()
                }
            }
            else {
                return invalidInputReturn()
            }

            if (black) {
                white_passant[0] = 0
                white_passant[1] = 0
                if (checkStalemate(true))
                    return (3)
            }
            else {
                black_passant[0] = 0
                black_passant[1] = 0
                if (checkStalemate(false))
                    return (3)
            }

            // Check current pos
            if ((nextPos == endPos) || (total_black == 0) || (total_white == 0)) {
                return (2)
            }
        }
    }
    else {
        // invalid input
        println(
            if (black) "No black pawn at ${input[0]}${input[1]}"
            else "No white pawn at ${input[0]}${input[1]}"
        )
        return (0)
    }

    return (1)
}

fun main() {
    println("Pawns-Only Chess")
    println("First Player's name:")
    val firstPlayerName = readln()
    println("Second Player's name:")
    val secondPlayerName = readln()

    // Print chess board
    showChessBoard()

    var bRunTask = true
    val checkRule = Regex("[a-h][1-8][a-h][1-8]")

    while(bRunTask) {
        while(bRunTask) {
            println("${firstPlayerName}'s turn:")
            val input = readln()
            if (input == "exit") {
                bRunTask = false
                println("Bye!")
                break
            }

            if (checkRule.matches(input)) {
                val result = updateChessBoard(false, input)
                if (result != 0) {
                    showChessBoard()
                    if (result == 2) {
                        println("White Wins!")
                        println("Bye!")
                    }
                    else if (result == 3) {
                        println("Stalemate!")
                        println("Bye!")
                    }
                    break
                }
            }
            else {
                println("Invalid Input")
            }
        }

        while(bRunTask) {
            println("${secondPlayerName}'s turn:")
            val input = readln()
            if (input == "exit") {
                bRunTask = false
                println("Bye!")
                break
            }

            if (checkRule.matches(input)) {
                val result = updateChessBoard(true, input)
                if (result != 0) {
                    showChessBoard()
                    if (result == 2) {
                        println("Black Wins!")
                        println("Bye!")
                    }
                    else if (result == 3) {
                        println("Stalemate!")
                        println("Bye!")
                    }
                    break
                }
            }
            else {
                println("Invalid Input")
            }
        }
    }
}