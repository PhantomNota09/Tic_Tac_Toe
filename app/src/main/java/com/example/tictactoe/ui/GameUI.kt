package com.example.tictactoe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.tictactoe.viewmodel.GameViewModel

@Composable
fun GameUI(viewModel: GameViewModel, navController: NavController) {
    val gameBoard by viewModel.gameBoard.collectAsState()  // gameBoard state
    val currentPlayer by viewModel.currentPlayer.collectAsState()  // currentPlayer state
    val difficulty by viewModel.difficulty.collectAsState()  // difficulty state
    val gameOverInfo by viewModel.gameOver.collectAsState() // gameOver state


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(32.dp),
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        gameOverInfo?.let {
            if (it.first) { // Game over
                Dialog(onDismissRequest = {  }) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        val gameResultText = when (it.second) { // Check how the game ended
                            'X' -> "YOU WON!"
                            'O' -> "YOU LOST"
                            'D' -> "DRAW!"
                            else -> "GAME OVER"
                        }
                        Text(
                            text = gameResultText,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = { viewModel.resetGame() }) {
                            Text("Restart Game")
                        }
                        Button(onClick = { navController.popBackStack() }) {
                            Text("Back to Menu")
                        }
                    }
                }
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { navController.navigate("homeUI") }, modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Filled.Home, contentDescription = "Home", tint = Color.White
                )
            }

            IconButton(
                onClick = { navController.navigate("settings/gameUI") }, modifier = Modifier
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { },
                elevation = null,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(60.dp)
            ) {
                Text(
                    text = "Difficulty: $difficulty",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        // Game board
        gameBoard.forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { colIndex, value ->
                    BoxCell(value, rowIndex, colIndex) {
                        if (value == ' ') {
                            viewModel.makeMove(rowIndex, colIndex)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun BoxCell(value: Char, row: Int, col: Int, onClick: () -> Unit) {
    val borderWidth = 2.dp
    val borderColor = Color.Black

    val cellModifier = Modifier
        .size(100.dp)
        .background(MaterialTheme.colorScheme.surface)
        .border(borderWidth, borderColor)
        .clickable(
            onClick = { if (value == ' ') onClick() },
            indication = ripple(),
            interactionSource = remember { MutableInteractionSource() }
        )

    Box(
        contentAlignment = Alignment.Center, modifier = cellModifier
    ) {
        Text(
            text = value.toString(),
            fontSize = 48.sp,
            style = MaterialTheme.typography.bodyLarge,
            color = when (value) {
                'X' -> Color.Blue
                'O' -> Color.Red
                else -> Color.Transparent
            }
        )
    }
}
