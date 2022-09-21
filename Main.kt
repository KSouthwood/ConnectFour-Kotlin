package connectfour

fun main() {
    println("Connect Four")
    val controller = Controller(getPlayerNames(), getBoardSize())
    controller.start()
}

private fun getInput(msg: String): String {
    println(msg)
    return readln()
}

private fun getPlayerNames(): Pair<String, String> {
    val player1 = getInput("First player's name:")
    val player2 = getInput("Second player's name:")
    return Pair(player1, player2)
}

private fun getBoardSize(): Pair<Int, Int> {
    val isInvalid = { x: Int -> x !in 5..9 }
    val errorMsg = { msg: String -> println("Board $msg should be from 5 to 9")}
    var inputs = mutableListOf(0, 0)
    do {
        println("Set the board dimensions (Rows x Columns)")
        val input = getInput("Press enter for default (6 x 7)").replace(Regex("\\s+"), "")
        if (input.isEmpty()) {  // an empty string means we use default settings
            inputs = mutableListOf(6, 7)
            continue
        }
        if (!input.matches(Regex("\\d+[xX]\\d+"))) {
            println("Invalid input")
            continue
        }
        inputs = input.split(Regex("[xX]"))
            .map { it.toInt() }
            .toMutableList()
        if (isInvalid(inputs.first())) {
            errorMsg("rows")
            continue
        }
        if (isInvalid(inputs.last())) {
            errorMsg("columns")
            continue
        }
    } while (isInvalid(inputs.first()) || isInvalid(inputs.last()))

    return Pair(inputs.first(), inputs.last())
}
