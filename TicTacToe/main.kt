package tictactoe

var winner = 0
var winner_team = ' '
var team_of_curr_turn = 'X'

fun CheckWinner(data: Char) {
    if (data == 'X' || data == 'O') {
        winner++ 
        winner_team = data
    }
}

fun CheckGame(data: MutableList<Char>): Boolean {
    val cnt_O = data.count{ it == 'O' }
    val cnt_X = data.count{ it == 'X' }
    val cnt_E = data.count{ it == ' ' }
    var result = false
    
    if ((data[0] == data[1]) && (data[1] == data[2])) CheckWinner(data[0])
    if ((data[3] == data[4]) && (data[4] == data[5])) CheckWinner(data[3])
    if ((data[6] == data[7]) && (data[7] == data[8])) CheckWinner(data[6])
    if ((data[0] == data[3]) && (data[3] == data[6])) CheckWinner(data[0])
    if ((data[1] == data[4]) && (data[4] == data[7])) CheckWinner(data[1])
    if ((data[2] == data[5]) && (data[5] == data[8])) CheckWinner(data[2])
    if ((data[0] == data[4]) && (data[4] == data[8])) CheckWinner(data[0])
    if ((data[2] == data[4]) && (data[4] == data[6])) CheckWinner(data[2])

    // Check impossible
    if ((cnt_O - cnt_X >= 2) || (cnt_X - cnt_O >= 2)) {
        println("Impossible")
        result = true
    }
    // Check draw
    else if ((winner == 0) && (cnt_E == 0)) {
        println("Draw")
        result = true
    }
    // Check game not finished
    else if ((winner == 0) && (cnt_E > 0)) {
        //println("Game not finished")
        result = false
    }
    else {
        println("$winner_team wins")
        result = true
    }

    return (result)
}

fun DispGame(data: MutableList<Char>) {
    println("---------")
    println(data.subList(0, 3).joinToString(" ", "| ", " |"))
    println(data.subList(3, 6).joinToString(" ", "| ", " |"))
    println(data.subList(6, 9).joinToString(" ", "| ", " |"))
    println("---------")
}

fun GetInput(data: MutableList<Char>) {
    while (true) {
        try {
            val (row, column) = readln().split(' ').map { it.toInt() }
            if ((row > 3) || (column > 3)) {
                println("Coordinates should be from 1 to 3!")
            }
            else if (data[(row - 1) * 3 + (column - 1)] != ' ') {
                println("This cell is occupied! Choose another one!")
            }
            else {
                data[(row - 1) * 3 + (column - 1)] = team_of_curr_turn
                if (team_of_curr_turn == 'X') team_of_curr_turn = 'O'
                else team_of_curr_turn = 'X'
                DispGame(data)
                break;
            }
        }
        catch(e: Exception) {
            println("You should enter numbers!")
        }
    }
}

fun main() {
    // write your code here
    val input = MutableList<Char>(9) { ' ' }
    DispGame(input)

    while(true) {
        GetInput(input)
        if (CheckGame(input))
            break
    }
}