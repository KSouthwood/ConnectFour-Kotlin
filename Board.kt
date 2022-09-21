package connectfour

import java.lang.IllegalArgumentException

const val BOX_DBL_UP_RT = "\u255a"
const val BOX_DBL_UP_LF = "\u255d"
const val BOX_DBL_HORZ = "\u2550"
const val BOX_DBL_VERT = "\u2551"
const val BOX_DBL_UP_HORZ = "\u2569"

class Board(private val rows: Int, private val cols: Int) {
    private val board = MutableList(rows) { MutableList(cols) { " " } }
    private val boxTop = buildString {
        repeat(cols) {
            append(" ${it + 1}")
        }
    }
    private val boxBtm = BOX_DBL_UP_RT + BOX_DBL_HORZ +
            "$BOX_DBL_UP_HORZ$BOX_DBL_HORZ".repeat(cols - 1) +
            BOX_DBL_UP_LF

    fun drawBoard() {
        println(boxTop)
        for (row in board) {
            println(row.joinToString(BOX_DBL_VERT, BOX_DBL_VERT, BOX_DBL_VERT))
        }
        println(boxBtm)
    }

    fun clearBoard() {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                board[row][col] = " "
            }
        }
    }

    /**
     * Plays in the specified column. Returns false if the column is full.
     */
    fun playColumn(column: Int, marker: String): Boolean {
        if (column >= board[0].size) {
            throw IllegalArgumentException("Column index out of range!")
        }

        for (index in board.indices.reversed()) {
            if (board[index][column] == " ") {
                board[index][column] = marker
                return true
            }
        }

        // no empty spaces in the column
        println("Column ${column + 1} is full")
        return false
    }

    /**
     * If the top row has a space, there are still moves left to make and indexOf()
     * returns an integer >= 0. Otherwise we get -1 indicating no spaces found
     * therefore no spaces are left on the board and the game is a draw. (This
     * is only called after we've checked for a winner.)
     */
    fun checkForDraw(): Boolean {
        return (board[0].indexOf(" ") == -1)
    }


    fun checkForWinner(marker: String): Boolean {
        val winString = ".*[$marker]{4}.*".toRegex()
        return checkRow(winString) || checkColumn(winString) || checkDiagonal(winString)
    }

    private fun checkRow(winString: Regex): Boolean {
        for (row in board) {
            if (row.joinToString("").matches(winString)) {
                return true
            }
        }
        return false
    }

    private fun checkColumn(winString: Regex): Boolean {
        for (col in 0 until cols) {
            val column = buildString {
                for (row in 0 until rows) {
                    append(board[row][col])
                }
            }
            if (column.matches(winString)) {
                return true
            }
        }
        return false
    }

    private fun checkDiagonal(winString: Regex): Boolean {
        val toRight = Pair(-1, 1)
        val toLeft  = Pair(-1, -1)
        val diagonal = { _row: Int, _col: Int, adjust: Pair<Int, Int> ->
            var row = _row
            var col = _col
            buildString {
                while ((row in 0 until rows) && (col in 0 until cols)) {
                    append(board[row][col])
                    row += adjust.first
                    col += adjust.second
                }
            }
        }

        for (startRow in rows - 1 downTo rows - 3) {
            for (startCol in 0..cols - 4) {
                if (diagonal(startRow, startCol, toRight).matches(winString)) {
                    return true
                }
            }

            for (startCol in 3 until cols) {
                if (diagonal(startRow, startCol, toLeft).matches(winString)) {
                    return true
                }
            }
        }

        return false
    }
}
