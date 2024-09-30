package com.example.tictactoe.ui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.tictactoe.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var difficultyRadioGroup: RadioGroup
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        difficultyRadioGroup = findViewById(R.id.difficultyRadioGroup)
        saveButton = findViewById(R.id.saveButton)

//         TODO Check with the team on what they are naming the preference file
        val sharedPrefs = getSharedPreferences("TicTacToePrefs", Context.MODE_PRIVATE)
        val currentDifficulty = sharedPrefs.getInt("difficulty", 0)

        when (currentDifficulty) {
            0 -> difficultyRadioGroup.check(R.id.easyRadioButton)
            1 -> difficultyRadioGroup.check(R.id.mediumRadioButton)
            2 -> difficultyRadioGroup.check(R.id.hardRadioButton)
        }

        saveButton.setOnClickListener {
            saveDifficultySetting()
            finish()
        }
    }

    private fun saveDifficultySetting() {
        val difficulty = when (difficultyRadioGroup.checkedRadioButtonId) {
            R.id.easyRadioButton -> 0
            R.id.mediumRadioButton -> 1
            R.id.hardRadioButton -> 2
            else -> 0
        }

//        TODO make changes here as well if the team is going with different name
        val sharedPrefs = getSharedPreferences("TicTacToePrefs", Context.MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putInt("difficulty", difficulty)
            apply()
        }
    }
}