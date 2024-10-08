package com.example.tictactoe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe.viewmodel.GameViewModel

@Composable
fun HomeUI(navController: NavController, viewModel: GameViewModel) {
    val difficulty by viewModel.difficulty.collectAsState() // difficulty state

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Button(
                onClick = {
                    viewModel.resetGame() // reset gameBoard and currentPlayer
                    navController.navigate("gameUI")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(60.dp)
            ) {
                Text("Start Game")
            }

            // Settings button
            Button(
                onClick = {
                    navController.navigate("settings/homeUI")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(60.dp)
            ) {
                Text("Difficulty: $difficulty")
            }

            Button(
                onClick = {
                    // navController.navigate("pastGames") if you have a past games screen setup
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(60.dp)
            ) {
                Text("Past Games")
            }
        }
    }
}
