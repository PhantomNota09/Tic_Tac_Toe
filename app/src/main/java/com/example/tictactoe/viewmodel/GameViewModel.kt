package com.example.tictactoe.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {
    private val _difficulty = MutableStateFlow("Easy") // Default difficulty
    val difficulty = _difficulty.asStateFlow()

    fun setDifficulty(newDifficulty: String) {
        _difficulty.value = newDifficulty
    }

    // State for gameBoard
    private val _gameBoard = MutableStateFlow(
        listOf(
            mutableListOf("", "", ""),
            mutableListOf("", "", ""),
            mutableListOf("", "", "")
        )
    )
    val gameBoard = _gameBoard.asStateFlow()

    // State for currentPlayer
    private val _currentPlayer = MutableStateFlow("X")
    val currentPlayer = _currentPlayer.asStateFlow()

    // Call to update the gameBoard and currentPlayer
    fun makeMove(row: Int, col: Int) {
        if (_gameBoard.value[row][col].isEmpty()) {
            val updatedBoard = _gameBoard.value.map { it.toMutableList() }
            updatedBoard[row][col] = _currentPlayer.value
            _gameBoard.value = updatedBoard
            togglePlayer()
        }
    }

    // Call to reset the gameBoard and currentPlayer
    fun resetGame() {
        _gameBoard.value = listOf(
            mutableListOf("", "", ""),
            mutableListOf("", "", ""),
            mutableListOf("", "", "")
        )
        _currentPlayer.value = "X"
    }

    // Call to toggle the currentPlayer
    private fun togglePlayer() {
        _currentPlayer.value = if (_currentPlayer.value == "X") "O" else "X"
    }
}

