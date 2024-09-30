package com.example.tictactoe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tictactoe.viewmodel.GameViewModel

@Composable
fun GameUI(viewModel: GameViewModel, onNavigateToSettings: () -> Unit) {

    val difficulty by viewModel.difficulty.collectAsState() // Collect the current difficulty as state

    var showGame by remember { mutableStateOf(false) }
    var gameBoard = remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var currentPlayer = remember { mutableStateOf("X") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        IconButton(
            onClick = onNavigateToSettings,  // Using the navigation function passed as a parameter
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = Color.White
            )
        }

        // Displaying current difficulty
        Text(
            "Current Difficulty: $difficulty",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            color = Color.White,
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!showGame) {
                Button(onClick = { showGame = true }) {
                    Text("Start Game")
                }
            } else {
                gameBoard.value.forEachIndexed { rowIndex, row ->
                    Row {
                        row.forEachIndexed { colIndex, value ->
                            BoxCell(value, rowIndex, colIndex) {
                                if (gameBoard.value[rowIndex][colIndex].isEmpty()) {
                                    gameBoard.value[rowIndex][colIndex] = currentPlayer.value
                                    currentPlayer.value =
                                        if (currentPlayer.value == "X") "O" else "X"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BoxCell(value: String, row: Int, col: Int, onClick: () -> Unit) {
    val borderWidth = 1.dp
    val borderColor = Color.Black // Set border color

    // Conditional padding to ensure borders are internal only
    val borderModifier = when {
        row > 0 && col > 0 -> Modifier.padding(top = borderWidth, start = borderWidth)
        row > 0 -> Modifier.padding(top = borderWidth)
        col > 0 -> Modifier.padding(start = borderWidth)
        else -> Modifier
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(100.dp)
            .background(Color.White)  // Set cell background to white
            .then(borderModifier)
            .border(borderWidth, borderColor) // Apply border on top of the padding
            .clickable(onClick = onClick)
    ) {
        Text(
            text = value,
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            color = Color.Black // Ensure text color contrasts with white background
        )
    }
}

