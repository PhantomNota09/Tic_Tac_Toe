package com.example.tictactoe.models

class Board {
    companion object {
        const val PLAYER_X = 'X'
        const val PLAYER_O = 'O'
        const val EMPTY = ' '

        const val BOARD_SIZE = 3
    }

    private val board: Array<CharArray> = Array(BOARD_SIZE) { CharArray(BOARD_SIZE) { EMPTY } }

    fun getBoardState(): Array<CharArray> {
        return board
    }

    fun makeMove(row: Int, col: Int, player: Char): Boolean {
        return if (isValidMove(row, col)) {
            board[row][col] = player
            true
        } else {
            false
        }
    }

    private fun isValidMove(row: Int, col: Int): Boolean {
        return board[row][col] == EMPTY
    }

    fun checkWin(): Char? {
        // Check rows and columns
        for (i in 0 until BOARD_SIZE) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != EMPTY) {
                return board[i][0]
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != EMPTY) {
                return board[0][i]
            }
        }

        // Check diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != EMPTY) {
            return board[0][0]
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != EMPTY) {
            return board[0][2]
        }

        // No winner yet
        return null
    }

    fun isDraw(): Boolean {
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                if (board[i][j] == EMPTY) return false
            }
        }
        return true
    }

    fun resetBoard() {
        for (i in 0 until BOARD_SIZE) {
            for (j in 0 until BOARD_SIZE) {
                board[i][j] = EMPTY
            }
        }
    }
}
