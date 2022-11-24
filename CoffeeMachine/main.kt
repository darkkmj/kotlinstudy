package machine

class Coffee(val need_water: Int, val need_milk: Int, val need_coffee: Int, val price: Int)

class CoffeeMachine {
    var ml_of_water: Int = 400
    var ml_of_milk: Int = 540
    var grams_of_coffee: Int = 120
    var disposable_cups: Int = 9
    var total_money: Int = 550

    val espresso = Coffee(250, 0, 16, 4)
    val latte = Coffee(350, 75, 20, 7)
    val cappuccino = Coffee(200, 100, 12, 6)

    fun display() {
        println("The coffee machine has:")
        println("$ml_of_water ml of water")
        println("$ml_of_milk ml of milk")
        println("$grams_of_coffee g of coffee beans")
        println("$disposable_cups disposable cups")
        println("\$$total_money of money")
        println()
    }

    fun fill() {
        println("Write how many ml of water you want to add:")
        ml_of_water += readln().toInt()
        println("Write how many ml of milk you want to add:")
        ml_of_milk += readln().toInt()
        println("Write how many grams of coffee beans you want to add:")
        grams_of_coffee += readln().toInt()
        println("Write how many disposable cups you want to add:")
        disposable_cups += readln().toInt()
        println()
    }

    fun check(coffee: Coffee): Boolean {
        var result: Boolean = false
        if (ml_of_water < coffee.need_water) {
            println("Sorry, not enough water!")
        }
        else if (ml_of_milk < coffee.need_milk) {
            println("Sorry, not enough milk!")
        }
        else if (grams_of_coffee < coffee.need_coffee) {
            println("Sorry, not enough coffee!")
        }
        else {
            println("I have enough resources, making you a coffee!")
            result = true
        }

        return (result)
    }

    fun update(coffee: Coffee) {
        ml_of_water -= coffee.need_water
        ml_of_milk -= coffee.need_milk
        grams_of_coffee -= coffee.need_coffee
        total_money += coffee.price
        disposable_cups--
    }

    fun buy() {
        println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:")
        var input = readln()
        when(input) {
            "1" -> {
                if (check(espresso)) update(espresso)
            }
            "2" -> {
                if (check(latte)) update(latte)
            }
            "3" -> {
                if (check(cappuccino)) update(cappuccino)
            }
            "back" -> return
        }
        println()
    }

    fun take() {
        println("I gave you \$$total_money\n")
        total_money = 0
    }
}

fun main() {
    val coffee_machine = CoffeeMachine()

    loop@ while (true) {
        println("Write action (buy, fill, take, remaining, exit):")
        val input = readln()
        when (input) {
            "buy" -> {
                coffee_machine.buy()
            }
            "fill" -> {
                coffee_machine.fill()
            }
            "take" -> {
                coffee_machine.take()
            }
            "remaining" -> coffee_machine.display()
            "exit" -> break@loop
        }
        println()
    }
/*    
    println("Write how many ml of water the coffee machine has:")
    val ml_of_water = readln().toInt()
    println("Write how many ml of milk the coffee machine has:")
    val ml_of_milk = readln().toInt()
    println("Write how many grams of coffee beans the coffee machine has:")
    val grams_of_coffee = readln().toInt()
    println("Write how many cups of coffee you will need:")
    val cups_of_coffee = readln().toInt()
    val coffee_machine_supply = arrayListOf(
        ml_of_water / 200,
        ml_of_milk / 50,
        grams_of_coffee / 15
    )
    val max_cups_of_coffee = coffee_machine_supply.minOf{ it }
    println(
        if (cups_of_coffee == max_cups_of_coffee) "Yes, I can make that amount of coffee"
        else if (cups_of_coffee < max_cups_of_coffee) "Yes, I can make that amount of coffee (and even ${max_cups_of_coffee - cups_of_coffee} more than that)"
        else "No, I can make only $max_cups_of_coffee cups of coffee"
    )
*/
}
