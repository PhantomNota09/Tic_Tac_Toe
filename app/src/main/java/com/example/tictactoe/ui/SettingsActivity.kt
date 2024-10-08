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
import androidx.navigation.NavController
import com.example.tictactoe.viewmodel.GameViewModel

@Composable
fun SettingsPage(
    viewModel: GameViewModel,
    navController: NavController,
    returnDestination: String,
    onDifficultySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Easy
        DifficultyButton("Easy", onClick = {
            viewModel.setDifficulty("Easy")
            onDifficultySelected("Easy")
            navController.navigate(returnDestination) {
                popUpTo(returnDestination) { inclusive = true }
            }
        })
        Spacer(modifier = Modifier.height(16.dp))

        // Medium
        DifficultyButton("Medium", onClick = {
            viewModel.setDifficulty("Medium")
            onDifficultySelected("Medium")
            navController.navigate(returnDestination) {
                popUpTo(returnDestination) { inclusive = true }
            }
        })
        Spacer(modifier = Modifier.height(16.dp))

        // Hard
        DifficultyButton("Hard", onClick = {
            viewModel.setDifficulty("Hard")
            onDifficultySelected("Hard")
            navController.navigate(returnDestination) {
                popUpTo(returnDestination) { inclusive = true }
            }
        })
    }
}

@Composable
fun DifficultyButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors()
    ) {
        Text(text = text)
    }
}
