package cinema

fun showCinema(seat_data: Array<Array<Char>>, row: Int, seat: Int, row_number: Int = (-1), column_number: Int = (-1)) {
    println()
    println("Cinema:")
    for (i in 0..row) {
        if (i == 0) print(" ")
        else print(i)
        for (j in 0..seat - 1) {
            if (i == 0) print(" ${j + 1}")
            else print(" ${seat_data[i - 1][j]}")
        }

        println()
    }
    println()
}

fun getTicketPrice(row: Int, seat: Int, row_number: Int, column_number: Int): Int {
    if (row * seat < 60) return 10
    else {
        if (row / 2 >= row_number) return 10
        else return 8
    }
}

fun getTotalTicketPrice(row: Int, seat: Int): Int {
    val total = (
        if (row * seat < 60) row * seat * 10
        else {
            ((row / 2) * seat * 10) + ((row / 2) * seat * 8) + ((row % 2) * seat * 8)
        }
    )

    return total
}

fun main() {
    // write your code here
    println("Enter the number of rows:")
    val row = readln().toInt()
    println("Enter the number of seats in each row:")
    val seat = readln().toInt()
    var seat_data = Array(row, {Array(seat, {'S'})})
    var number_of_sales_ticket = 0
    var total_income = 0
    val max_income = getTotalTicketPrice(row, seat)

    while(true) {
        println("1. Show the seats")
        println("2. Buy a ticket")
        println("3. Statistics")
        println("0. Exit")
        println()
        val input = readln().toInt()
        when (input) {
            1 -> showCinema(seat_data, row, seat); 
            2 -> {
                while (true) {
                    println("\nEnter a row number:")
                    val row_number = readln().toInt()
                    println("Enter a seat number in that row:")
                    val seat_number = readln().toInt()
                    println()

                    // Check position
                    if ((row_number > row) || (seat_number > seat)) {
                        println("Wrong input!\n")
                    }
                    else if (seat_data[row_number - 1][seat_number - 1] == 'B') {
                        println("That ticket has already been purchased\n")
                    }
                    else {
                        println("Ticket price: \$${getTicketPrice(row, seat, row_number, seat_number)}\n")
                        seat_data[row_number - 1][seat_number - 1] = 'B'
                        number_of_sales_ticket++
                        total_income += getTicketPrice(row, seat, row_number, seat_number)
                        break
                    }
                }
            }
            3 -> {
                val percentage = number_of_sales_ticket.toDouble() / (row * seat) * 100.0
                val formatPercentage = "%.2f".format(percentage)
                
                println()
                println("Number of purchased tickets: $number_of_sales_ticket")
                println("Percentage: $formatPercentage%")
                println("Current income: \$$total_income")
                println("Total income: \$$max_income")
                println()
            }
            0 -> break
            else -> println("Wrong input!\n")
        }
    }
}
