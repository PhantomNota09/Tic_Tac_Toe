package com.example.tictactoe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameUI() {
    var showGame by remember { mutableStateOf(false) }
    var gameBoard = remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var currentPlayer = remember { mutableStateOf("X") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TicTacToe") },
                actions = {
                    IconButton(onClick = {
                        // Handle settings action
                    }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                },
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            color = Color.Black
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!showGame) {
                    Button(onClick = { showGame = true }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.White)) {
                        Text("Start Game")
                    }
                } else {
                    gameBoard.value.forEachIndexed { rowIndex, row ->
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            row.forEachIndexed { colIndex, value ->
                                BoxCell(value, rowIndex, colIndex) {
                                    if (gameBoard.value[rowIndex][colIndex].isEmpty()) {
                                        gameBoard.value[rowIndex][colIndex] = currentPlayer.value
                                        currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
                                    }
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
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(100.dp)
            .padding(2.dp)
            .border(
                width = if (row == 0 || col == 0) 0.dp else 1.dp,
                color = Color.Gray,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(0.dp)
            )
            .background(Color.Black)
            .clickable(onClick = onClick)
    ) {
        Text(text = value, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium, color = if (value.isNotEmpty()) Color.White else Color.Transparent)
    }
}
