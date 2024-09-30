package com.example.tictactoe.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tictactoe.viewmodel.GameViewModel

@Composable
fun SettingsPage(viewModel: GameViewModel, onDifficultySelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Easy Button
        DifficultyButton("Easy", onClick = {
            viewModel.setDifficulty("Easy")
            onDifficultySelected("Easy")
        })
        Spacer(modifier = Modifier.height(16.dp)) // Spacing between buttons

        // Medium Button
        DifficultyButton("Medium", onClick = {
            viewModel.setDifficulty("Medium")
            onDifficultySelected("Medium")
        })
        Spacer(modifier = Modifier.height(16.dp)) // Spacing between buttons

        // Hard Button
        DifficultyButton("Hard", onClick = {
            viewModel.setDifficulty("Hard")
            onDifficultySelected("Hard")
        })
    }
}

@Composable
fun DifficultyButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors() // Customize colors if needed
    ) {
        Text(text = text)
    }
}
