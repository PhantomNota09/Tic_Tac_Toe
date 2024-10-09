package com.example.tictactoe.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tictactoe.ai.AiPlayer
import com.example.tictactoe.ai.DifficultyLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {
    private val _difficulty = MutableStateFlow(DifficultyLevel.EASY) // Set default difficulty as enum
    val difficulty = _difficulty.asStateFlow()

    private val aiPlayer = AiPlayer()
    private val _currentPlayer = MutableStateFlow("X")
    val currentPlayer = _currentPlayer.asStateFlow()

    private val _gameOver = MutableStateFlow<Pair<Boolean, Char>?>(null)
    val gameOver = _gameOver.asStateFlow()

    // Initialize _gameBoard as an Array of CharArray
    private val _gameBoard = MutableStateFlow(
        arrayOf(
            charArrayOf(' ', ' ', ' '),
            charArrayOf(' ', ' ', ' '),
            charArrayOf(' ', ' ', ' ')
        )
    )
    val gameBoard = _gameBoard.asStateFlow()

    fun setDifficulty(newDifficulty: DifficultyLevel) {
        _difficulty.value = newDifficulty
    }

    fun resetGame() {
        _gameBoard.value = arrayOf(
            charArrayOf(' ', ' ', ' '),
            charArrayOf(' ', ' ', ' '),
            charArrayOf(' ', ' ', ' ')
        )
        _currentPlayer.value = "X"
        _gameOver.value = null
    }

    fun makeMove(row: Int, col: Int) {
        if (_gameBoard.value[row][col] == ' ' && _gameOver.value == null) {
            val updatedBoard = _gameBoard.value.map { it.copyOf() }.toTypedArray()
            updatedBoard[row][col] = _currentPlayer.value[0]
            _gameBoard.value = updatedBoard
            if (checkWin()) {
                _gameOver.value = Pair(true, _currentPlayer.value[0])
            } else {
                togglePlayer()
            }
        }
    }

    private fun togglePlayer() {
        _currentPlayer.value = if (_currentPlayer.value == "X") "O" else "X"
        if (_currentPlayer.value == "O") {
            performAiMove()
        }
    }

    private fun performAiMove() {
        val aiMove = aiPlayer.getMove(_gameBoard.value, _difficulty.value)
        if (aiMove != null) {
            makeMove(aiMove.first, aiMove.second)
        }
    }

    private fun checkWin(): Boolean {
        val board = _gameBoard.value

        // Check rows and columns for a win
        for (i in 0 until 3) {
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true
            }
            if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return true
            }
        }

        // Check diagonals for a win
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true
        }

        return false
    }

}
