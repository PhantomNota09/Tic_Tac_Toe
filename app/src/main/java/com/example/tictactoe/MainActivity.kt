package com.example.tictactoe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.ai.AiPlayer
import com.example.tictactoe.ai.DifficultyLevel
import com.example.tictactoe.game.GameManager
import com.example.tictactoe.models.Board

class MainActivity : AppCompatActivity() {

    private lateinit var boardButtons: Array<Array<Button>>
    private lateinit var statusText: TextView

    private val gameManager = GameManager()
    private val aiPlayer = AiPlayer()
    private var currentPlayer = Board.PLAYER_X  // Human is 'X', AI is 'O'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize board buttons and status text
        boardButtons = arrayOf(
            arrayOf(findViewById(R.id.btn00), findViewById(R.id.btn01), findViewById(R.id.btn02)),
            arrayOf(findViewById(R.id.btn10), findViewById(R.id.btn11), findViewById(R.id.btn12)),
            arrayOf(findViewById(R.id.btn20), findViewById(R.id.btn21), findViewById(R.id.btn22))
        )

        statusText = findViewById(R.id.statusText)

        // Set click listeners for each button
        for (i in boardButtons.indices) {
            for (j in boardButtons[i].indices) {
                boardButtons[i][j].setOnClickListener {
                    onHumanMove(i, j)
                }
            }
        }
    }

    // Handle human move
    private fun onHumanMove(row: Int, col: Int) {
        if (currentPlayer != Board.PLAYER_X || !gameManager.makeMove(row, col, Board.PLAYER_X)) {
            return
        }

        updateBoardUI()
        checkGameStatus()

        // AI Move
        currentPlayer = Board.PLAYER_O
        aiMove()
    }

    // AI Move
    private fun aiMove() {
        val aiMove = aiPlayer.getMove(gameManager.getBoardState(), DifficultyLevel.EASY)  // Change difficulty if needed
        if (aiMove != null) {
            gameManager.makeMove(aiMove.first, aiMove.second, Board.PLAYER_O)
        }

        updateBoardUI()
        checkGameStatus()

        // Switch back to human player
        currentPlayer = Board.PLAYER_X
    }

    // Update the UI board with the current state
    private fun updateBoardUI() {
        val boardState = gameManager.getBoardState()
        for (i in boardState.indices) {
            for (j in boardState[i].indices) {
                boardButtons[i][j].text = boardState[i][j].toString()
            }
        }
    }

    // Check if the game has ended
    private fun checkGameStatus() {
        val winner = gameManager.checkWin()
        when {
            winner != null -> {
                statusText.text = "Game Over! $winner wins!"
                disableBoard()
                restartGameAfterDelay()
            }
            gameManager.isDraw() -> {
                statusText.text = "Game Over! It's a draw!"
                disableBoard()
                restartGameAfterDelay()
            }
            else -> {
                // Update the status text to reflect whose turn it is
                statusText.text = if (currentPlayer == Board.PLAYER_X) "Your Turn" else "AI's Turn"
                // Switch turns
                currentPlayer = if (currentPlayer == Board.PLAYER_X) Board.PLAYER_O else Board.PLAYER_X
            }
        }
    }

    // Disable all buttons when the game ends
    private fun disableBoard() {
        for (i in boardButtons.indices) {
            for (j in boardButtons[i].indices) {
                boardButtons[i][j].isEnabled = false
            }
        }
    }

    // Reset the game after a short delay
    private fun restartGameAfterDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            resetGame()
        }, 2000)  // 2-second delay to show the game result
    }

    // Reset the game state and UI
    private fun resetGame() {
        gameManager.resetGame()
        currentPlayer = Board.PLAYER_X  // Start with the human player again
        updateBoardUI()
        enableBoard()
        statusText.text = "Your Turn"
    }

    // Enable all buttons for the new game
    private fun enableBoard() {
        for (i in boardButtons.indices) {
            for (j in boardButtons[i].indices) {
                boardButtons[i][j].isEnabled = true
                boardButtons[i][j].text = ""  // Clear the button text
            }
        }
    }
}
