package com.example.tictactoe.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.R
import com.example.tictactoe.data.TicTacToeDbHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO This is a test file to be deleted at last. This is just to test the dB
class TicTacToeActivity : AppCompatActivity() {

    private lateinit var dbHelper: TicTacToeDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        dbHelper = TicTacToeDbHelper(this)

        saveGameResult("Vyas", "Easy")
        Log.d("TicTacToeActivity", "Game result - Meet saved.")
//        saveGameResult("Abc", "Hard")
//        Log.d("TicTacToeActivity", "Game result - Abc saved.")

        Log.d("TicTacToeActivity", "Retrieving game results...")
        displayGameResults()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    // When a game ends, save the result
    private fun saveGameResult(winner: String, difficulty: String) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        dbHelper.insertGameResult(currentDate, winner, difficulty)
    }

    // To retrieve all game results
    private fun displayGameResults() {
        val gameResults = dbHelper.getAllGameResults()
        for (result in gameResults) {
            Log.d("TicTacToe", "Date: ${result.date}, Winner: ${result.winner}, Difficulty: ${result.difficulty}")
        }
    }
}