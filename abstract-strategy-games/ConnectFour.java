// Jessica Luo
// 2/4/2025
// CSE 123
// Creative Project 1: Abstract Strategy Games
// TA: Benoit Lek
// This class represents a connect four game. It includes instructions and stores a
// board that can be displayed to the players via its toString(). It contains methods
// so players can make their move and figure out which player will make the next move,
// as well as whether the game is over and who won.
// This class extends AbstractStrategyGame.
import java.util.*;

public class ConnectFour extends AbstractStrategyGame {
   private int[][] board;
   private boolean is1stTurn;

   // Behavior: 
   //   - Returns a String of instructions of how to play and win the game.
   // Returns:
   //   - String: the instructions
   public String instructions() {
      String result = "";
      result += "Connect Four is a two-player board game where players take turns dropping\n";
      result += "their tokens into a grid consisting of six rows and seven columns with the\n";
      result += "columns separated by lines. Each spot in the grid is marked by a number, 0\n";
      result += "means the spot is empty, 1 means it’s player 1’s token, and 2 means it’s\n";
      result += "player 2’s token. Player 1 goes first and the players alternate making moves\n";
      result += "On each turn, a player can either add or remove a token. If the player\n";
      result += "chooses to add a token, they choose one column to drop their token into.\n";
      result += "The token will fall to the lowest available space in that column, if a column\n";
      result += "is full, the player must choose a different one. If the player chooses to\n";
      result += "remove a token, they are able to remove one of their own tokens from the\n";
      result += "bottom row of any column, shifting all other tokens in that column down.\n";
      result += "A player wins by achieving four of their own tokens in a row,\n";
      result += "either horizontally or vertically, the game can also end in a tie\n";
      result += "if the board is completely filled without any winner.\n";    
      return result;
   }

   // Behavior: 
   //   - Creates a new Connect Four game with an empty board
   public ConnectFour() {
      board = new int[6][7];
      is1stTurn = true;
   }

   // Behavior: 
   //   - Returns a String representation of the current game's state. 
   // Returns:
   //   - String: current game state
   public String toString() {
      String result = "";
      for (int row = 0; row < board.length; row++) {
         result += "|";
         for (int col = 0; col < board[0].length; col++) {
            result += " " + board[row][col] + " |";
         }
         result += "\n";
      }
      return result;
   }

   // Behavior: 
   //   - Returns the index of the player who has won the game,
   //     0 if there is a tie, -1 if game is not over
   // Returns:
   //   - int: 1 if player 1, 2 if player 2, 0 if there is a tie, -1 if game is over
   public int getWinner() {
      for (int row = 0; row < board.length; row++) {
         int rowWinner = rowWin(row);
         if (rowWinner != -1) {
            return rowWinner;
         }
         for (int col = 0; col < board[0].length; col++) {
            int colWinner = colWin(col);
            if (colWinner != -1) {
               return colWinner;
            }
         }
      }
      return checkTie();
   }

   // Behavior: 
   //   - Returns the index of the player who will take the next turn, -1 if the game is over
   // Returns:
   //   - int: 1 if player 1, 2 if player 2, -1 if game is over
   public int getNextPlayer() {
      if (super.isGameOver()) {
         return -1;
      }
        
      return player(is1stTurn);
   }

   // Behavior: 
   //   - Asks whether the player would like to add or remove a token(cans-insensitive),
   //     then asks which column they would like to play in, 
   //     and changes board accordingly
   // Parameter:
   //   - input: Scanner to read player's move
   // Exceptions:
   //   - Throws an IllegalArgumentException if the provided scanner is null.
   //   - Throws IllegalArgumentException if a player chooses an option
   //     other then add or remove.
   //   - Throws an IllegalArgumentException if the player chose to add a token
   //     and the column they chose doesn't exist or is full.
   //   - Throws an IllegalArgumentException if the player chose to remove a token
   //     and the column they chose doesn't exist or if the bottom token is not theirs 
   public void makeMove(Scanner input) {
      if (input == null) {
         throw new IllegalArgumentException();
      }
      System.out.print("Would you like to add(add) or remove(remove) a token? ");
      String choice = input.next();
      if (choice.equalsIgnoreCase("add")) {
         System.out.print("Which column(1-7) would you like to place your token in? ");
         int col = input.nextInt();
         addToken(col - 1, player(is1stTurn));
      } 
      else if (choice.equalsIgnoreCase("remove")) {
         System.out.print("Which column(1-7) would you like to remove your token from? ");
         int col = input.nextInt();
         removeToken(col - 1, player(is1stTurn));
      } 
      else {
         throw new IllegalArgumentException("Invalid input");
      }
      is1stTurn = !is1stTurn;
   }

   // Behavior: 
   //   - Adds the players token to the bottom most empty row in the specified column
   // Parameter:
   //   - column: the column we are adding a token to
   //   - player: index of the player making the move
   // Exceptions:
   //   - Throws an IllegalArgumentException if the player chose to add a token
   //     and the column they chose doesn't exist or is full.
   private void addToken(int col, int player) {
      if (col >= board[0].length || col < 0) {
         throw new IllegalArgumentException("Column doesn't exist");
      }
      boolean placed = false;
      for(int row = board.length - 1; row >= 0 && !placed; row--) {
         if (board[row][col] == 0) {
            board[row][col] = player;
            placed = true;
         }
      }
      if (!placed) {
         throw new IllegalArgumentException("Column is full");
      }
   }

     // Behavior: 
   //   - removes the player's token from the bottom row of the given
   //     column, shifting all other tokens in that column (if any) down by one row.
   // Parameter:
   //   - column: the column we are removing a token from
   //   - player: index of the player making the move
   // Exceptions:
   //   - Throws an IllegalArgumentException if the player chose to remove a token
   //     and the column they chose doesn't exist or if the bottom token is not theirs.
   private void removeToken(int col, int player) {
      if (col >= board[0].length || col < 0) {
         throw new IllegalArgumentException("Column doesn't exist");
      }
        
      if (board[board.length - 1][col] != player) {
         throw new IllegalArgumentException("Not your token");
      }
      for(int row = board.length - 1; row > 0; row--) {
         board[row][col] = board[row - 1][col];
      }
      board[0][col] = 0;
   }

   // Behavior: 
   //   - returns the index of the player
   // Parameters:
   //   - is1stTurn: whether it is player 1's turn
   // Returns:
   //   - int: 1 if player 1, 2 if player 2
   private int player(boolean b) {
      if(b) {
         return 1;
      }
      return 2;
   }

   // Behavior: 
   //   - returns the index of the player who has won the 
   //     row(gotten four of their own tokens in a row), -1 if no winner
   // Returns:
   //   - int: 1 if player 1, 2 if player 2, -1 if no winner
   private int rowWin(int row) {
      boolean playerOne = true;
      int counter = 0;
      for(int col = 0; col < board[row].length && counter < 4; col++) {
         if(board[row][col] == 0) {
            counter = 0;
         } 
         else {
            if (player(playerOne) == board[row][col]) {
               counter++;
            }
            else {
               playerOne = !playerOne;
               counter = 1;
            }
         }
      }
      if (counter >= 4) {
         return player(playerOne);
      }
      return -1;
   }

   // Behavior: 
   //   - returns the index of the player who has won the 
   //     column(gotten four of their own tokens in a row), -1 if no winner
   // Returns:
   //   - int: 1 if player 1, 2 if player 2, -1 if no winner
   private int colWin(int col) {
      boolean playerOne = true;
      int counter = 0;
      for(int row = board.length - 1; row >= 0 && counter < 4; row--) {
         if(board[row][col] == 0) {
            counter = 0;
         } 
         else {
            if (player(playerOne) == board[row][col]) {
               counter++;
            }
            else {
               playerOne = !playerOne;
               counter = 1;
            }
         }
      }
      if (counter >= 4) {
         return player(playerOne);
      }
      return -1;
   }

   // Behavior: 
   //   - Checks for a tie, returns 0 if there's a tie 
   //     (no space left on the board) and -1 otherwise.
   // Returns:
   //   - int: 0 if there's a tie, -1 if not
   private int checkTie() {
      for (int row = 0; row < board.length; row++) {
         for (int col = 0; col < board[row].length; col++) {
            if (board[row][col] == 0) {
               return -1;
            }
         }
      }
      return 0;
   }
}
