package com.example.tictactoe

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.tictactoe.ui.GameUI
import com.example.tictactoe.ui.HomeUI
import com.example.tictactoe.ui.SettingsPage
import com.example.tictactoe.viewmodel.GameViewModel
import com.example.tictactoe.ui.PastGamesActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the ViewModel
        val viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        setContent {
            MainApp(viewModel, this)
        }
    }
}

@Composable
fun MainApp(gameViewModel: GameViewModel, context: Context) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "homeUI") {
        composable("homeUI") {
            HomeUI(
                navController = navController,
                viewModel = gameViewModel
            )
        }
        composable("gameUI") {
            GameUI(gameViewModel, navController, context)
        }
        composable("pastGames") {
            PastGamesActivity(context)
        }
        composable(
            "settings/{returnDestination}",
            arguments = listOf(navArgument("returnDestination") { type = NavType.StringType })
        ) { backStackEntry ->
            val returnDestination =
                backStackEntry.arguments?.getString("returnDestination") ?: "homeUI"
            SettingsPage(
                viewModel = gameViewModel,
                navController = navController,
                returnDestination = returnDestination,
                onDifficultySelected = { difficulty ->
                    gameViewModel.setDifficulty(difficulty)
                    navController.navigate(returnDestination) {
                        popUpTo(returnDestination) { inclusive = true }
                    }
                }
            )
        }
    }
}


