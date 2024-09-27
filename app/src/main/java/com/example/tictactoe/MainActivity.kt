package com.example.tictactoe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.ai.AiPlayer
import com.example.tictactoe.ai.DifficultyLevel
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

        boardButtons = arrayOf(
            arrayOf(findViewById(R.id.btn00), findViewById(R.id.btn01), findViewById(R.id.btn02)),
            arrayOf(findViewById(R.id.btn10), findViewById(R.id.btn11), findViewById(R.id.btn12)),
            arrayOf(findViewById(R.id.btn20), findViewById(R.id.btn21), findViewById(R.id.btn22))
        )

        statusText = findViewById(R.id.statusText)

        for (i in boardButtons.indices) {
            for (j in boardButtons[i].indices) {
                boardButtons[i][j].setOnClickListener {
                    onHumanMove(i, j)
                }
            }
        }
    }

    private fun onHumanMove(row: Int, col: Int) {
        if (currentPlayer != Board.PLAYER_X || !gameManager.makeMove(row, col, Board.PLAYER_X)) {
            return
        }

        updateBoardUI()
        checkGameStatus()

        currentPlayer = Board.PLAYER_O
        aiMove()
    }

    private fun aiMove() {
        val aiMove = aiPlayer.getMove(gameManager.getBoardState(), DifficultyLevel.HARD)
        if (aiMove != null) {
            gameManager.makeMove(aiMove.first, aiMove.second, Board.PLAYER_O)
            updateBoardUI()
            checkGameStatus()
            currentPlayer = Board.PLAYER_X
        }
    }

    private fun updateBoardUI() {
        val boardState = gameManager.getBoardState()
        for (i in boardButtons.indices) {
            for (j in boardButtons[i].indices) {
                boardButtons[i][j].text = boardState[i][j].toString()
            }
        }
    }

    private fun checkGameStatus() {
        when {
            gameManager.checkWin() != null -> showGameOver("${currentPlayer} wins!")
            gameManager.isDraw() -> showGameOver("It's a draw!")
        }
    }

    private fun showGameOver(message: String) {
        statusText.text = message
        // Optionally reset the game after a delay
        Handler(Looper.getMainLooper()).postDelayed({
            gameManager.resetBoard()
            updateBoardUI()
            statusText.text = "Player X's turn"
        }, 2000)
    }
}
