package com.example.tictactoe.ai

import com.example.tictactoe.models.Board

class AiPlayer {

    fun getMove(boardState: Array<CharArray>, difficulty: DifficultyLevel): Pair<Int, Int>? {
        return when (difficulty) {
            DifficultyLevel.EASY -> getRandomMove(boardState)
            DifficultyLevel.MEDIUM -> {
                if (Math.random() > 0.5) {
                    getRandomMove(boardState)
                } else {
                    getOptimalMove(boardState)
                }
            }
            DifficultyLevel.HARD -> getOptimalMove(boardState)
        }
    }

    // Easy mode (random move)
    private fun getRandomMove(boardState: Array<CharArray>): Pair<Int, Int>? {
        val emptySpaces = mutableListOf<Pair<Int, Int>>()
        for (i in boardState.indices) {
            for (j in boardState[i].indices) {
                if (boardState[i][j] == Board.EMPTY) {
                    emptySpaces.add(Pair(i, j))
                }
            }
        }
        return if (emptySpaces.isNotEmpty()) {
            emptySpaces.random()
        } else {
            null
        }
    }

    // Hard mode (optimal move using Alpha-Beta Pruning)
    private fun getOptimalMove(boardState: Array<CharArray>): Pair<Int, Int>? {
        var bestMove: Pair<Int, Int>? = null
        var bestScore = Int.MIN_VALUE

        for (i in boardState.indices) {
            for (j in boardState[i].indices) {
                if (boardState[i][j] == Board.EMPTY) {
                    boardState[i][j] = Board.PLAYER_O
                    val score = alphaBeta(boardState, 0, Int.MIN_VALUE, Int.MAX_VALUE, false)
                    boardState[i][j] = Board.EMPTY

                    if (score > bestScore) {
                        bestScore = score
                        bestMove = Pair(i, j)
                    }
                }
            }
        }
        return bestMove
    }

    // Alpha-Beta Pruning algorithm (fail-hard version)
    private fun alphaBeta(boardState: Array<CharArray>, depth: Int, alpha: Int, beta: Int, isMaximizing: Boolean): Int {
        var alphaVar = alpha
        var betaVar = beta
        val winner = checkWinner(boardState)

        // Terminal condition: return the heuristic value of the board
        if (winner == Board.PLAYER_X) return -10 + depth
        if (winner == Board.PLAYER_O) return 10 - depth
        if (isBoardFull(boardState)) return 0

        return if (isMaximizing) {
            var bestScore = Int.MIN_VALUE

            // Maximizing player (AI with 'O')
            for (i in boardState.indices) {
                for (j in boardState[i].indices) {
                    if (boardState[i][j] == Board.EMPTY) {
                        boardState[i][j] = Board.PLAYER_O
                        val score = alphaBeta(boardState, depth + 1, alphaVar, betaVar, false)
                        boardState[i][j] = Board.EMPTY
                        bestScore = maxOf(bestScore, score)
                        alphaVar = maxOf(alphaVar, bestScore)

                        if (betaVar <= alphaVar) {
                            break // Beta cutoff
                        }
                    }
                }
            }
            bestScore
        } else {
            var bestScore = Int.MAX_VALUE

            // Minimizing player (Human with 'X')
            for (i in boardState.indices) {
                for (j in boardState[i].indices) {
                    if (boardState[i][j] == Board.EMPTY) {
                        boardState[i][j] = Board.PLAYER_X
                        val score = alphaBeta(boardState, depth + 1, alphaVar, betaVar, true)
                        boardState[i][j] = Board.EMPTY
                        bestScore = minOf(bestScore, score)
                        betaVar = minOf(betaVar, bestScore)

                        if (betaVar <= alphaVar) {
                            break // Alpha cutoff
                        }
                    }
                }
            }
            bestScore
        }
    }

    // Check if the board is full
    private fun isBoardFull(boardState: Array<CharArray>): Boolean {
        for (row in boardState) {
            for (cell in row) {
                if (cell == Board.EMPTY) {
                    return false
                }
            }
        }
        return true
    }

    // Check the winner on the current board
    private fun checkWinner(boardState: Array<CharArray>): Char? {
        val board = Board()
        return board.checkWin()
    }
}
