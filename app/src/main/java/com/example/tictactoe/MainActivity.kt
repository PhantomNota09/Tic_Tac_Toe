package com.example.tictactoe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.ai.AiPlayer
import com.example.tictactoe.ai.DifficultyLevel
import com.example.tictactoe.models.Board
import com.example.tictactoe.ui.HomeUI
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
        // Obtain the ViewModel
        val viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        setContent {
            MainApp(viewModel)
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
fun MainApp(gameViewModel: GameViewModel) {
    val navController =
        rememberNavController()  // Navigation controller to manage navigation events

    NavHost(navController = navController, startDestination = "gameUI") {
        composable("gameUI") {
            HomeUI(
                viewModel = gameViewModel,
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        composable("settings") {
            // Pass navController to SettingsPage
            SettingsPage(
                viewModel = gameViewModel,
                navController = navController, // Pass the NavController here
                onDifficultySelected = { difficulty ->
                    gameViewModel.setDifficulty(difficulty)
                    navController.navigateUp()  // Navigate back after setting difficulty
                }
            )
        }
    }
}
