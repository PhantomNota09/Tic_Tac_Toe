package com.example.tictactoe

import android.database.Cursor
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tictactoe.data.GamedbHelper

class history_act : AppCompatActivity() {

    private lateinit var dbHelper: GamedbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        // Set window insets to adjust for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize dbHelper
        dbHelper = GamedbHelper(this)

        // Display the results from the database
        displayResults()
    }

    private fun displayResults() {
        // Query the database for all results
        val cursor: Cursor = dbHelper.getAllResults()
        val resultsList = mutableListOf<String>()

        // Iterate through the cursor and collect results
        if (cursor.moveToFirst()) {
            do {
                val date = cursor.getString(cursor.getColumnIndexOrThrow(GamedbHelper.COLUMN_DATE))
                val winner = cursor.getString(cursor.getColumnIndexOrThrow(GamedbHelper.COLUMN_WINNER))
                val difficulty = cursor.getString(cursor.getColumnIndexOrThrow(GamedbHelper.COLUMN_DIFFICULTY))
                resultsList.add("Date: $date, Winner: $winner, Difficulty: $difficulty")
            } while (cursor.moveToNext())
        }

        cursor.close()

        // Find the ListView and populate it with data
        val listViewResults = findViewById<ListView>(R.id.listViewResults)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, resultsList)
        listViewResults.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()  // Handle toolbar's back navigation
        return true
    }
}
