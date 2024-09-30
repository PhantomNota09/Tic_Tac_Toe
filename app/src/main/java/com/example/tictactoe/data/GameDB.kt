package com.example.tictactoe.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GamedbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "Game.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "game_results"
        const val COLUMN_ID = "id"
        const val COLUMN_DATE = "date"
        const val COLUMN_WINNER = "winner"
        const val COLUMN_DIFFICULTY = "difficulty"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_DATE DATE, " +
                "$COLUMN_WINNER TEXT, " +
                "$COLUMN_DIFFICULTY TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertGameResult(date: String, winner: String, difficulty: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_DATE, date)
            put(COLUMN_WINNER, winner)
            put(COLUMN_DIFFICULTY, difficulty)
        }
        return db.insert(TABLE_NAME, null, contentValues)
    }

    fun getAllResults(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT COLUMN_DATE, COLUMN_WINNER, COLUMN_DIFFICULTY FROM $TABLE_NAME", null)
    }
}
