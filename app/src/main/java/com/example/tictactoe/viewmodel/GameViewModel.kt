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
    }

    fun makeMove(row: Int, col: Int) {
        if (_gameBoard.value[row][col] == ' ') {
            val updatedBoard = _gameBoard.value.map { it.copyOf() }.toTypedArray()
            updatedBoard[row][col] = _currentPlayer.value[0] // Convert the current player string to char
            _gameBoard.value = updatedBoard
            togglePlayer()
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
}
