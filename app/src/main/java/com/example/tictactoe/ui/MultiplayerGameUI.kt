package com.example.tictactoe.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tictactoe.viewmodel.BluetoothViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MultiplayerGameUI(viewModel: BluetoothViewModel, navController: NavController) {
    val state = viewModel.state.collectAsState().value.gameState

    // Observe the game state for changes and trigger dialogs
    if (state.winner.isNotEmpty() || state.draw) {
        val result = when {
            state.draw -> "DRAW!"
//            state.winner == "X" -> "${state.winner} WINS!"
//            state.winner == "O" -> "${state.winner} WINS!"
            else -> "${state.winner} WINS!"
        }

        // This dialog shows up when there is a winner or a draw
        AlertDialog(
            onDismissRequest = {
                viewModel.disconnectFromDevice()
                navController.navigate("homeUI")
            },
            title = { Text("Game Over") },
            text = { Text(result) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetBoard()
                    }
                ) {
                    Text("Play Again")
                }
            },
            dismissButton = {
                Button(onClick = {
                    viewModel.disconnectFromDevice()
                    navController.navigate("homeUI")
                }) {
                    Text("Exit")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Navigation icons at the top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    viewModel.disconnectFromDevice()
                    navController.navigate("homeUI")
                }
            ) {
                Icon(Icons.Filled.Home, "Home", tint = Color.White)
            }


            // Reset board button on the top right
            IconButton(
                onClick = {
                    viewModel.resetBoard()  // Call the resetBoard function in your ViewModel
                }
            ) {
                Icon(Icons.Filled.Refresh, "Reset", tint = Color.White)
            }
        }

        // Display the game board
        state.board.forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { colIndex, cell ->
                    BoxCellMulti(cell, rowIndex, colIndex) {
                        if (cell == " ") {
                            viewModel.makeMove(rowIndex, colIndex)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BoxCellMulti(value: String, row: Int, col: Int, onClick: () -> Unit) {
    val cellModifier = Modifier
        .size(100.dp)
        .background(MaterialTheme.colorScheme.surface)
        .border(2.dp, Color.Black)
        .clickable(onClick = onClick)

    Box(contentAlignment = Alignment.Center, modifier = cellModifier) {
        Text(
            text = value,
            fontSize = 48.sp,
            style = MaterialTheme.typography.bodyLarge,
            color = when (value) {
                "X" -> Color.Blue
                "O" -> Color.Red
                else -> Color.Transparent
            }
        )
    }
}

