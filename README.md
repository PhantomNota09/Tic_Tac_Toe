UI Integration Variables for Tic-Tac-Toe Game

This file outlines the variables and methods needed to integrate the game logic with the UI for the Tic-Tac-Toe game.

1. Player Markers

- AI_MARKER: Board.PLAYER_O
    - The AI uses the marker 'O' in the game.

- PLAYER_MARKER: Board.PLAYER_X
    - The human player uses the marker 'X' in the game.

2. Board Representation

- boardState: Array<CharArray>
    - This 2D array represents the current state of the Tic-Tac-Toe board.
    - Each element can be:
        - 'X': Represents the human player's move.
        - 'O': Represents the AI's move.
        - Board.EMPTY: Represents an empty cell (initial state or after reset).

UI Components

- boardButtons: Array<Array<Button>>
    - This array stores the references to the buttons on the Tic-Tac-Toe board.
    - Each button is linked to a grid cell that reflects the current state of the board.
    - You can update the text on each button using:
        boardButtons[row][col].text = boardState[row][col].toString()

Example:
  boardButtons = arrayOf(
      arrayOf(findViewById(R.id.btn00), findViewById(R.id.btn01), findViewById(R.id.btn02)),
      arrayOf(findViewById(R.id.btn10), findViewById(R.id.btn11), findViewById(R.id.btn12)),
      arrayOf(findViewById(R.id.btn20), findViewById(R.id.btn21), findViewById(R.id.btn22))
  )

3. Game Status Display

- statusText: TextView
    - This TextView displays the current status of the game, such as whose turn it is, or if there's a winner or a draw.
    - Update the game status like this:
        statusText.text = "Player X's turn"

Example Scenarios:
- "Player X's turn"
- "Player O wins!"
- "It's a draw!"

4. Player Move Handling

- onHumanMove(row: Int, col: Int)
    - Called when the human player makes a move by clicking a button.
    - The move updates the board state and checks if the game has ended.
    - Example usage:
      onHumanMove(0, 1)  // Human plays at position (0,1)

- aiMove()
    - This method handles the AI's move. It fetches the AI's chosen move from the logic and updates the board.
    - Example usage:
      aiMove()  // AI makes its move

5. Game State Methods

- gameManager.getBoardState(): Array<CharArray>
    - Returns the current state of the game board. This is used to update the UI after every move.
    - Example usage:
      val boardState = gameManager.getBoardState()

- gameManager.checkWin(): Char?
    - Returns the marker (`'X'` or `'O'`) of the winner if there's one, or `null` if the game is still ongoing.
    - Example usage:
      val winner = gameManager.checkWin()
      if (winner != null) {
          statusText.text = "$winner wins!"
      }

- gameManager.isDraw(): Boolean
    - Returns true if the board is full and there is no winner, meaning the game ends in a draw.
    - Example usage:
      if (gameManager.isDraw()) {
          statusText.text = "It's a draw!"
      }

6. Difficulty Levels

- DifficultyLevel: Enum
    - The difficulty levels for the AI are:
        - EASY
        - MEDIUM
        - HARD
    - Example usage:
      aiPlayer.getMove(boardState, DifficultyLevel.HARD)

7. Resetting the Game

- gameManager.resetBoard()
    - Resets the board to the initial empty state. This should also reset the UI buttons and status text.
    - Example usage:
      gameManager.resetBoard()
      updateBoardUI()  // Reflect the reset in the UI
      statusText.text = "Player X's turn"

UI Updates Based on Game Logic

- When the human player makes a move, call onHumanMove(row, col) to process the move.
- After every move (human or AI), the UI should be updated by calling updateBoardUI() to reflect the new board state.
- After each move, the game status should be checked by calling checkGameStatus() to see if there's a winner or a draw.



