package com.example.tictactoe.data

import android.content.Context
import android.widget.Toast

class DBUpdate(val context: Context) {
    val dbHelper = GamedbHelper(context)

    fun storeGameResult(date: String, winner: String, difficulty: String) {
        val result = dbHelper.insertGameResult(date, winner, difficulty)
        if (result != -1L) {
            Toast.makeText(context, "Game result saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Error saving game result", Toast.LENGTH_SHORT).show()
        }
    }
}
