package com.example.tictactoe.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PastGamesActivity(games: List<Game>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Past Games",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.inversePrimary
        )
        LazyColumn {
            items(games) { game ->
                GameItem(game)
            }
        }
    }
}

@Composable
fun GameItem(game: Game) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Text(
                text = game.date,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = game.winner,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = game.difficulty,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = getDifficultyColor(game.difficulty)
            )

        }
    }
}

@Composable
private fun getDifficultyColor(difficulty: String): Color {
    return when (difficulty) {
        "Easy" -> Color(0xFF4CAF50)
        "Medium" -> MaterialTheme.colorScheme.secondary
        "Hard" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }
}