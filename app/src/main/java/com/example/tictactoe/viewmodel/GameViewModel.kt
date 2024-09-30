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
}

