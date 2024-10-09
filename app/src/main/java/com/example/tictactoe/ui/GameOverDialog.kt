package com.example.tictactoe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun GameOverDialog(gameOverInfo: Pair<Boolean, Char>, onDismiss: () -> Unit, onRestartGame: () -> Unit, onBackToMenu: () -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        // Background dim effect
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { /* absorb clicks */ }
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .align(Alignment.Center)
                    .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (gameOverInfo.second == 'X') "YOU WON!" else "YOU LOST",
                    fontSize = 24.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {
                    onRestartGame()
                    onDismiss()
                }) {
                    Text("Restart Game")
                }
                Button(onClick = {
                    onBackToMenu()
                    onDismiss()
                }) {
                    Text("Back to Menu")
                }
            }
        }
    }
}
