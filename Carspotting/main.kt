package parking

import java.util.*

/**
 * CAR class
 */
data class Car(val registration_number: String, val color: String);

/**
 * ParkingLot class
 */
class ParkingLot {
    private var parkingLotInfo: MutableList<Car?> = arrayListOf()

    init {
        // Nothing to do
    }

    private fun checkParkingLot(): Boolean {
        return if (parkingLotInfo.isEmpty()) {
            println("Sorry, a parking lot has not been created.")
            (true)
        } else {
            (false)
        }
    }

    fun cmdPark(car_number: String, car_color: String) {
        // Park car
        if (checkParkingLot()) return

        for (i in parkingLotInfo.indices) {
            if (parkingLotInfo[i] == null) {
                parkingLotInfo[i] = Car(car_number, car_color)
                println("$car_color car parked in spot ${i + 1}.")
                return
            }
        }

        println("Sorry, the parking lot is full.")
    }

    fun cmdLeave(index: Int) {
        if (checkParkingLot()) return

        if (parkingLotInfo[index - 1] != null) {
            parkingLotInfo[index - 1] = null
            println("Spot $index is free.")
        }
        else {
            println("Spot $index has not car.")
        }
    }

    fun cmdCreate(number: Int) {
        // create parking lots
        parkingLotInfo = MutableList(number) { null }
        println("Created a parking lot with $number spots.")
    }

    fun cmdStatus() {
        // print status of parking lot
        if (checkParkingLot()) return

        var parkingLogEmpty = true
        for (i in parkingLotInfo.indices) {
            if (parkingLotInfo[i] != null) {
                parkingLogEmpty = false
                println("${i + 1} ${parkingLotInfo[i]?.registration_number} ${parkingLotInfo[i]?.color}")
            }
        }

        if (parkingLogEmpty)
            println("Parking lot is empty.")
    }

    fun cmdRegByColor(color: String) {
        if (checkParkingLot()) return

        var isCarExist = false
        parkingLotInfo.forEach {
            if (it?.color?.lowercase() == color.lowercase()) {
                print(
                    if (!isCarExist) it.registration_number
                    else ", ${it.registration_number}"
                )
                isCarExist = true
            }
        }

        if (isCarExist) println()
        else println("No cars with color ${color} were found.")
    }

    fun cmdSpotByColor(color: String) {
        if (checkParkingLot()) return

        var isCarExist = false
        for (i in parkingLotInfo.indices) {
            if (parkingLotInfo[i]?.color?.lowercase() == color.lowercase()) {
                print(
                    if (!isCarExist) i + 1
                    else ", ${i + 1}"
                )
                isCarExist = true
            }
        }

        if (isCarExist) println()
        else println("No cars with color ${color} were found.")
    }

    fun cmdSpotByReg(regNum: String) {
        if (checkParkingLot()) return

        var isCarExist = false
        for (i in parkingLotInfo.indices) {
            if (parkingLotInfo[i]?.registration_number == regNum) {
                print(
                    if (!isCarExist) i + 1
                    else ", ${i + 1}"
                )
                isCarExist = true
            }
        }

        if (isCarExist) println()
        else println("No cars with registration number ${regNum} were found.")
    }
}

fun main() {
    val parkingLot = ParkingLot()
    while (true) {
        val input = readln().split(' ')
        when (input[0]) {
            "park" -> parkingLot.cmdPark(input[1], input[2])
            "leave" -> parkingLot.cmdLeave(input[1].toInt())
            "create" -> parkingLot.cmdCreate(input[1].toInt())
            "status" -> parkingLot.cmdStatus()
            "reg_by_color" -> parkingLot.cmdRegByColor(input[1])
            "spot_by_color" -> parkingLot.cmdSpotByColor(input[1])
            "spot_by_reg" -> parkingLot.cmdSpotByReg(input[1])
            "exit" -> break
        }
    }
}
