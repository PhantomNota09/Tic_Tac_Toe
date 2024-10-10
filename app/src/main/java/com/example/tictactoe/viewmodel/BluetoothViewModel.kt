package com.example.tictactoe.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.domain.game.BluetoothController
import com.example.tictactoe.domain.game.BluetoothDeviceDomain
import com.example.tictactoe.domain.game.ConnectionResult
import com.example.tictactoe.domain.game.GameData
import com.example.tictactoe.domain.game.GameState
import com.example.tictactoe.domain.game.MiniGame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
) : ViewModel() {
    private var localDev = bluetoothController.localDeviceName ?: ""
    var shouldNav = false
    private var isFirst = false
    val _state = MutableStateFlow(BluetoothUiState())
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state  // This is assuming _state already includes gameState and metadata updated elsewhere
    ) { scannedDevices, pairedDevices, currentState ->
        currentState.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            messages = if (currentState.isConnected) currentState.messages else emptyList(),
            gameState = currentState.gameState,  // Assuming gameState is updated elsewhere in your state flow
            metadata = currentState.metadata  // Assuming metadata is updated elsewhere in your state flow
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)


    private var deviceConnectionJob: Job? = null

    init {
        bluetoothController.setOnConnectionUpdated { newMetadata ->
            _state.update { currentState ->
                currentState.copy(metadata = newMetadata)
            }
        }

        bluetoothController.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        bluetoothController.errors.onEach { error ->
            _state.update {
                it.copy(errorMessage = error)
            }
        }.launchIn(viewModelScope)
    }

    fun connectToDevice(device: BluetoothDeviceDomain) {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController.connectToDevice(device).listen()
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
//        _state.update {
//            it.copy(
//                isConnecting = false, isConnected = false
//            )
//        }
        shouldNav = false
        _state.update { currentState ->
            currentState.copy(
                isConnecting = false,
                isConnected = false,
                gameState = GameState(
                    board = listOf(
                        listOf(" ", " ", " "),
                        listOf(" ", " ", " "),
                        listOf(" ", " ", " ")
                    ),
                    turn = "",
                    winner = "",
                    draw = false,
                    connectionEstablished = false,
                    reset = false
                ),
                metadata = currentState.metadata.copy(
                    miniGame = MiniGame(player1Choice = "", player2Choice = "")
                )
            )
        }
    }

    fun resetBoard() {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    gameState = currentState.gameState.copy(
                        board = listOf(
                            listOf(" ", " ", " "),
                            listOf(" ", " ", " "),
                            listOf(" ", " ", " ")
                        ),
                        reset = true,  // Set the reset flag to true
                        winner = "",  // Clear the winner
                        draw = false  // Clear the draw state
                    )
                )
            }

            // Send the updated game state to the other player via Bluetooth
            val updatedState = _state.value
            sendMessage(GameData(updatedState.gameState, updatedState.metadata))
        }
    }

    fun waitForIncomingConnections() {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController.startBluetoothServer().listen()
    }

    fun sendMessage(gameData: GameData, localDev: String = "") {
        viewModelScope.launch {
            // Assuming bluetoothController.trySendMessage now requires a GameData object and handles the BluetoothMessage creation
            val bluetoothMessage = bluetoothController.trySendMessage(gameData, localDev)
            if (bluetoothMessage != null) {
                _state.update { currentState ->
                    currentState.copy(
                        gameState = gameData.gameState,
                        metadata = gameData.metadata,
                    )
                }
            }
        }
//        Log.d("BluetoothViewModel", "Sending message: $gameData")
    }


    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when (result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            isConnected = true, isConnecting = false, errorMessage = null
                        )
                    }
                }

                is ConnectionResult.TransferSucceeded -> {
                    val receivedGameData = result.gameData
                    _state.update {
                        it.copy(
                            gameState = receivedGameData.gameState,
                            metadata = receivedGameData.metadata
                        )
                    }
                }

                is ConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            isConnected = false, isConnecting = false, errorMessage = result.message
                        )
                    }
                }
            }
        }.catch { throwable ->
            bluetoothController.closeConnection()
            _state.update {
                it.copy(
                    isConnected = false,
                    isConnecting = false,
                )
            }
        }.launchIn(viewModelScope)
    }


    override fun onCleared() {
        super.onCleared()
        bluetoothController.release()
    }

    fun setPlayerChoice(choice: String) {
        viewModelScope.launch {
            // Get the current miniGame state and gameState
            val currentMetadata = _state.value.metadata
            val miniGame = currentMetadata.miniGame
            val currentGameState = _state.value.gameState

            // Update the miniGame with the logic you provided
//            Log.d("BluetoothViewModel", "localDev: $localDev")
            val updatedMiniGame = when {
                choice == "Me" -> {
                    isFirst = true // This device is connecting, so it is Player 2
                    // If player1Choice is "Me", update both player1Choice and player2Choice with localDev
                    miniGame.copy(
                        player1Choice = localDev,
                        player2Choice = localDev
                    )
                }

                else -> {
                    // If both player1Choice and player2Choice are filled, keep them as is
                    miniGame
                }
            }

            // Update the state with the new miniGame and set the turn to "0"
            _state.update { currentState ->
                currentState.copy(
                    metadata = currentMetadata.copy(
                        miniGame = updatedMiniGame
                    ),
                    gameState = currentGameState.copy(
                        turn = "0"  // Set the turn to "0"
                    )
                )
            }

            // Send updated state as GameData
            val updatedState = _state.value
            sendMessage(GameData(updatedState.gameState, updatedState.metadata))
        }
    }


    fun makeMove(row: Int, col: Int) {
        viewModelScope.launch {
            val gameState = _state.value.gameState
            // Check if the cell is empty, no winner yet, and the move is allowed based on turn and isFirst
            if (gameState.board[row][col] == " " && gameState.winner.isEmpty() && isMoveAllowed(
                    gameState.turn.toInt()
                )
            ) {
                val currentPlayer = if (gameState.turn.toInt() % 2 == 0) "X" else "O"
                val newBoard = gameState.board.mapIndexed { r, list ->
                    if (r == row) list.mapIndexed { c, value -> if (c == col) currentPlayer else value }
                    else list
                }

                // Update the board and increment the turn
                _state.update { currentState ->
                    currentState.copy(
                        gameState = currentState.gameState.copy(
                            board = newBoard,
                            turn = (currentState.gameState.turn.toInt() + 1).toString()  // Increment the turn
                        )
                    )
                }

                // Post move updates (check for winner or draw)
                postMoveUpdate(newBoard)
            } else {
                Log.d(
                    "BluetoothViewModel",
                    "Invalid move: $row, $col : ${gameState.turn} : $isFirst"
                )
            }
        }
    }

    private fun postMoveUpdate(board: List<List<String>>) {
        val winner = checkWinner(board)

        val updatedWinner = when {
            winner == "X" && isFirst -> {
                localDev
                    ?: "Player 1"  // Set winner to localDev if X wins and isFirst is true (Player 1)
            }

            winner == "X" && !isFirst -> {
                _state.value.metadata.choices.find { it.id == "player2" }?.name
                    ?: "Player 2" // Player 2 wins if X wins and isFirst is false
            }

            winner == "O" && !isFirst -> {
                localDev
                    ?: "Player 2"  // Set winner to localDev if O wins and isFirst is false (Player 2)
            }

            winner == "O" && isFirst -> {
                _state.value.metadata.choices.find { it.id == "player1" }?.name
                    ?: "Player 1" // Player 1 wins if O wins and isFirst is true
            }

            else -> {
                ""  // No winner, it may be a draw or game is still in progress
            }
        }

        // If there's a winner
        if (updatedWinner.isNotEmpty()) {
            _state.update { currentState ->
                currentState.copy(gameState = currentState.gameState.copy(winner = updatedWinner))
            }
        }
        // Check for draw
        else if (isDraw(board)) {
            _state.update { currentState ->
                currentState.copy(
                    gameState = currentState.gameState.copy(
                        draw = true,
                        winner = "Draw"
                    )
                )
            }
        }

        // Send updated game state over Bluetooth
        sendMessage(GameData(_state.value.gameState, _state.value.metadata))
    }


    private fun checkWinner(board: List<List<String>>): String {
        // Check rows for winner
        for (i in 0 until 3) {
            if (board[i][0] != " " && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0]
            }
        }

        // Check columns for winner
        for (i in 0 until 3) {
            if (board[0][i] != " " && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i]
            }
        }

        // Check diagonals for winner
        if (board[0][0] != " " && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0]
        }
        if (board[0][2] != " " && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2]
        }

        // Check for draw (all cells are filled)
        val isDraw = board.all { row -> row.all { cell -> cell != " " } }
        if (isDraw) {
            return "Draw"
        }

        return "" // No winner yet
    }

    private fun isDraw(board: List<List<String>>): Boolean {
        // Check if all cells are filled and there is no winner
        return board.all { row -> row.all { cell -> cell != " " } }
    }

    private fun togglePlayer() {
        _state.update { currentState ->
            val newTurn = if (currentState.gameState.turn == "X") "O" else "X"
            currentState.copy(gameState = currentState.gameState.copy(turn = newTurn))
        }
    }

//    fun updateChoicesAfterConnection() {
//        val localAddress = _localDev
//
//        _state.update { currentState ->
//            currentState.copy(
//                metadata = currentState.metadata.copy(
//                    choices = listOf(
//                        Choice(id = "player1", name = localAddress),
//                        Choice(id = "player2", name = remoteAddress)
//                    )
//                )
//            )
//        }
//    }

    // Check if it's allowed to make a move based on isFirst and current turn
    private fun isMoveAllowed(currentTurn: Int): Boolean {
        return if (isFirst) {
            // If isFirst is true (Player 1), allow moves only on even turns
            currentTurn % 2 == 0
        } else {
            // If isFirst is false (Player 2), allow moves only on odd turns
            currentTurn % 2 != 0
        }
    }

}