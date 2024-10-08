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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.viewmodel.GameViewModel

@Composable
fun HomeUI(viewModel: GameViewModel, onNavigateToSettings: () -> Unit) {

    val difficulty by viewModel.difficulty.collectAsState() // Collect the current difficulty as state

    var showGame by remember { mutableStateOf(false) }
    var gameBoard = remember {
        mutableStateListOf(
            mutableStateListOf("", "", ""),
            mutableStateListOf("", "", ""),
            mutableStateListOf("", "", "")
        )
    }
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
            style = MaterialTheme.typography.bodyLarge
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
                gameBoard.forEachIndexed { rowIndex, row ->
                    Row {
                        row.forEachIndexed { colIndex, value ->
                            BoxCell(value, rowIndex, colIndex) {
                                if (gameBoard[rowIndex][colIndex].isEmpty()) {
                                    gameBoard[rowIndex][colIndex] = currentPlayer.value
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
    val borderColor = Color.Black // Color for the border

    // Modifier for the cell
    val cellModifier = Modifier
        .size(100.dp)
        .background(MaterialTheme.colorScheme.surface) // Using Material3 theme surface color
        .then(
            if (row > 0 && col > 0) {
                Modifier.padding(top = borderWidth, start = borderWidth)
            } else {
                Modifier
            }
        )
        .border(borderWidth, borderColor) // Apply border around each cell
        .clickable(
            onClick = onClick,
            indication = ripple(), // Material3 bounded ripple effect
            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
        )

    Box(
        contentAlignment = Alignment.Center,
        modifier = cellModifier
    ) {
        Text(
            text = value,
            fontSize = 48.sp,
            style = MaterialTheme.typography.bodyLarge,
            color = when (value) {
                "X" -> Color.Blue // Color for "X"
                "O" -> Color.Red  // Color for "O"
                else -> Color.Transparent // No color when empty
            }
        )
    }
}
