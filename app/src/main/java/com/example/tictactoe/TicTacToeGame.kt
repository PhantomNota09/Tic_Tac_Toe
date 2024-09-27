package com.example.tictactoe

import com.example.tictactoe.ai.AiPlayer
import com.example.tictactoe.ai.DifficultyLevel
import com.example.tictactoe.game.GameManager
import com.example.tictactoe.models.Board
import java.util.Scanner

fun main() {
    val gameManager = GameManager()
    val aiPlayer = AiPlayer()
    val scanner = Scanner(System.`in`)

    println("Welcome to Tic-Tac-Toe!")
    println("Choose difficulty level: (1) Easy, (2) Medium, (3) Hard")
    val difficultyChoice = scanner.nextInt()
    val difficulty = when (difficultyChoice) {
        1 -> DifficultyLevel.EASY
        2 -> DifficultyLevel.MEDIUM
        3 -> DifficultyLevel.HARD
        else -> DifficultyLevel.EASY
    }

    var currentPlayer = Board.PLAYER_X  // Player X is the human
    var gameOver = false

    // Game loop
    while (!gameOver) {
        printBoard(gameManager.getBoardState())

        if (currentPlayer == Board.PLAYER_X) {
            // Human player's turn
            println("Your move! Enter row and column (1-3 for both):")
            var row: Int
            var col: Int
            do {
                print("Row: ")
                row = scanner.nextInt() - 1
                print("Column: ")
                col = scanner.nextInt() - 1
            } while (!gameManager.makeMove(row, col, Board.PLAYER_X))
        } else {
            // AI's turn
            println("AI is thinking...")
            val aiMove = aiPlayer.getMove(gameManager.getBoardState(), difficulty)
            if (aiMove != null) {
                gameManager.makeMove(aiMove.first, aiMove.second, Board.PLAYER_O)
                println("AI played at row ${aiMove.first + 1}, column ${aiMove.second + 1}")
            }
        }

        // Check if the game is over (win or draw)
        val winner = gameManager.checkWin()
        if (winner != null) {
            printBoard(gameManager.getBoardState())
            println("Player $winner wins!")
            gameOver = true
        } else if (gameManager.isDraw()) {
            printBoard(gameManager.getBoardState())
            println("It's a draw!")
            gameOver = true
        } else {
            // Switch turns
            currentPlayer = if (currentPlayer == Board.PLAYER_X) Board.PLAYER_O else Board.PLAYER_X
        }
    }

    println("Game over!")
}

// Helper function to print the board in the terminal
fun printBoard(boardState: Array<CharArray>) {
    println("Current board:")
    for (row in boardState) {
        for (cell in row) {
            if (cell == Board.EMPTY) {
                print("_ ")
            } else {
                print("$cell ")
            }
        }
        println()
    }
}