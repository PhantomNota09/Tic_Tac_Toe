package com.example.tictactoe.game

import com.example.tictactoe.models.Board

class GameManager {
    private val board = Board()

    // Function to handle moves, and determine if there's a win or draw after the move
    fun makeMove(row: Int, col: Int, player: Char): Boolean {
        return if (board.makeMove(row, col, player)) {
            true
        } else {
            false
        }
    }

    // Function to check if the game is won
    fun checkWin(): Char? {
        return board.checkWin()
    }

    // Function to check if the game is a draw
    fun isDraw(): Boolean {
        return board.isDraw()
    }

    // Function to reset the board
    fun resetGame() {
        board.resetBoard()
    }

    // Get current board state (useful for AI logic)
    fun getBoardState(): Array<CharArray> {
        return board.getBoardState()
    }
}
