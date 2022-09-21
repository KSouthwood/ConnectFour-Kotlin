package connectfour

class Controller(
    private val players: Pair<String, String>,
    private val boardSize: Pair<Int, Int>
) {
    private val board = Board(boardSize.first, boardSize.second)
    private val minCol = 1
    private val maxCol = boardSize.second

    private var player = 0
    private var scores = mutableListOf(0, 0)

    private val marker = { player: Int ->
        if (player == 0) "o" else "*" }

    private val playerName = { player: Int ->
        if (player == 0) players.first else players.second
    }

    fun start() {
        val games = getNumOfGames()
        println("${players.first} VS ${players.second}")
        println("${boardSize.first} X ${boardSize.second} board")
        println(if (games == 1) "Single game" else "Total $games games")
        gameLoop(games)
    }

    private fun getNumOfGames(): Int {
        var input: String
        var games = 0
        do {
            println("Do you want to play single or multiple games?")
            println("For a single game, input 1 or press Enter")
            println("Input a number of games:")
            input = readln()
            when {
                input.isEmpty() -> games = 1
                input.matches(Regex("^[1-9]\\d*$")) -> games = input.toInt()
                else -> println("Invalid input")
            }
        } while (games == 0)
        return games
    }


    private fun gameLoop(games: Int) {
        val multipleGames = games > 1
        for (game in 1..games) {
            if (multipleGames) {
                println("Game #$game")
            }
            player = (game - 1) % 2
            var playGame: Boolean
            board.drawBoard()
            do {
                playGame = playerTurn()
            } while (playGame)
            if (multipleGames) {
                println("Score\n${players.first}: ${scores[0]} ${players.second}: ${scores[1]}")
            }
            board.clearBoard()
        }
        println("Game over!")
    }

    /**
     * Processes a turn for the players. True is returned if the play doesn't
     * result in a winner and there are still spaces left. False indicates the
     * game is over.
     */
    private fun playerTurn(): Boolean {
        var input: String
        do {
            input = getPlayerInput(player)
            if (input.lowercase() == "end") {
                return false
            }
        } while (!validateInput(input))
        board.drawBoard()
        if (board.checkForWinner(marker(player))) {
            println("Player ${playerName(player)} won")
            scores[player] += 2
            return false
        }
        if (board.checkForDraw()) {
            println("It is a draw")
            scores[0]++
            scores[1]++
            return false
        }
        player = (player + 1) % 2
        return true
    }

    private fun getPlayerInput(player: Int): String {
        var input: String
        while (true) {
            println("${playerName(player)}'s turn:")
            input = readln()
            // limit the input to only digits or "end"
            if (input.matches(Regex("^\\d+$|end"))) {
                break
            }
            println("Incorrect column number")
        }
        return input
    }

    /**
     * Check if the column number input is in range and attempts to play if it is.
     * Returns true if a play succeeded, false otherwise.
     */
    private fun validateInput(input: String): Boolean {
        val column = input.toInt()
        // make sure the column number is in the valid range
        if (column !in minCol..maxCol) {
            println("The column number is out of range (1 - $maxCol)")
            return false
        }
        return board.playColumn(column - 1, marker(player))
    }
}
