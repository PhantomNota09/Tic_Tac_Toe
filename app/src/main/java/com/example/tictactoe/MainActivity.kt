package com.example.tictactoe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.ai.AiPlayer
import com.example.tictactoe.ai.DifficultyLevel
import com.example.tictactoe.models.Board
import com.example.tictactoe.ui.GameUI
import com.example.tictactoe.ui.SettingsPage
import com.example.tictactoe.viewmodel.GameViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var boardButtons: Array<Array<Button>>
    private lateinit var statusText: TextView

    private val gameManager = GameManager()
    private val aiPlayer = AiPlayer()
    private var currentPlayer = Board.PLAYER_X  // Human is 'X', AI is 'O'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp()
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

@Composable
fun MainApp() {
    val navController =
        rememberNavController()  // Navigation controller to manage navigation events

    NavHost(navController = navController, startDestination = "gameUI") {
        composable("gameUI") {
            // Obtain ViewModel here or pass from a higher level if shared
            val gameViewModel: GameViewModel = viewModel()
            GameUI(
                viewModel = gameViewModel,
                onNavigateToSettings = { navController.navigate("settings") })
        }
        composable("settings") {
            // It's crucial to obtain the same ViewModel instance
            val gameViewModel: GameViewModel = viewModel()
            SettingsPage(viewModel = gameViewModel, onDifficultySelected = { difficulty ->
                gameViewModel.setDifficulty(difficulty)
                navController.navigateUp()  // Navigate back after setting difficulty
            })
        }
    }
}
