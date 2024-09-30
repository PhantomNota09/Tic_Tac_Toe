package com.example.tictactoe.ui

import android.R
import android.widget.ArrayAdapter
import com.example.tictactoe.utils.GameAdapter

class PastGamesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GameAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sortSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_past_games)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.gamesRecyclerView)
        sortSpinner = findViewById(R.id.sortSpinner)

        setupRecyclerView()
        setupSortSpinner()
        loadGames()
    }

    private fun setupRecyclerView() {
        adapter = GameAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setupSortSpinner() {
        val sortOptions = arrayOf("Date", "Winner", "Difficulty")
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, sortOptions)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sortSpinner.adapter = spinnerAdapter

        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when (position) {
                    0 -> loadGames(sortBy = "date")
                    1 -> loadGames(sortBy = "winner")
                    2 -> loadGames(sortBy = "difficulty")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadGames(sortBy: String = "date") {
        val games = dbHelper.getAllGames()
        val sortedGames = when (sortBy) {
            "date" -> games.sortedByDescending { it.date }
            "winner" -> games.sortedBy { it.winner }
            "difficulty" -> games.sortedBy { it.difficulty }
            else -> games
        }
        adapter.updateGames(sortedGames)
    }
}