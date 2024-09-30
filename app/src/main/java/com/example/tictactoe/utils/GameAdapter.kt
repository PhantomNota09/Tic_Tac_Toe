package com.example.tictactoe.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tictactoe.R

class GameAdapter(private var games: List<Game>) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val winnerTextView: TextView = view.findViewById(R.id.winnerTextView)
        val difficultyTextView: TextView = view.findViewById(R.id.difficultyTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_item, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.dateTextView.text = game.date
        holder.winnerTextView.text = game.winner
        holder.difficultyTextView.text = game.difficulty
    }

    override fun getItemCount() = games.size

    fun updateGames(newGames: List<Game>) {
        games = newGames
        notifyDataSetChanged()
    }
}