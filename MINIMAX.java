import java.util.*;

public class MinimaxAlgorithm {
    private static final char EMPTY = '\u0000'; // Represents an empty cell on the board
    private static final int MAX = 1;  // Maximizing player
    private static final int MIN = -1; // Minimizing player
    private char[][] board = new char[3][3]; // The game board
    private int currentPlayer = MAX; // Starts with MAX player
    private Map<String, Integer> memo = new HashMap<>(); // Memoization cache

    public static void main(String[] args) {
        MinimaxAlgorithm game = new MinimaxAlgorithm();
        game.runGame();
    }

    // Runs the game loop
    private void runGame() {
        initializeBoard();
        printBoard();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (currentPlayer == MAX) {
                bestMove();
            } else {
                System.out.println("Player MIN's turn.");
                System.out.println("Enter row and column to place, and the letter (e.g., 1 1 C):");
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                char letter = scanner.next().charAt(0);

                if (canPlace(row, col, letter)) {
                    board[row][col] = letter;
                } else {
                    System.out.println("Invalid move, try again.");
                    continue;
                }
            }

            printBoard();
            if (checkWin()) {
                System.out.println("Player " + (currentPlayer == MAX ? "MAX" : "MIN") + " wins!");
                break;
            }
            if (isBoardFull()) {
                System.out.println("It's a tie!");
                break;
            }
            currentPlayer = -currentPlayer;
        }
        scanner.close();
    }

    // Determines the best move for MAX using Minimax
    private void bestMove() {
        int bestValue = Integer.MIN_VALUE;
        int bestRow = -1, bestCol = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = 'S'; // Example of setting a move for MAX
                    int moveValue = minimax(0, false);
                    board[i][j] = EMPTY;

                    if (moveValue > bestValue) {
                        bestValue = moveValue;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }
        board[bestRow][bestCol] = 'S'; // MAX makes the best move
        System.out.println("Player MAX placed 'S' at (" + bestRow + ", " + bestCol + ")");
    }

    // Minimax algorithm implementation
    private int minimax(int depth, boolean isMaximizing) {
        if (checkWin()) {
            return isMaximizing ? -1 : 1; // +1 for MAX win, -1 for MIN win
        }
        if (isBoardFull()) {
            return 0; // Tie
        }

        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = 'S';
                        best = Math.max(best, minimax(depth + 1, false));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = 'C'; // MIN player placing 'C'
                        best = Math.min(best, minimax(depth + 1, true));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return best;
        }
    }

    // Initializes the board
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    // Checks if a move is valid
    private boolean canPlace(int row, int col, char letter) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == EMPTY;
    }

    // Checks for winning patterns
    private boolean checkWin() {
        String[] patterns = {"CSE", "ESC"};
        return checkLines(patterns);
    }

    // Helper method to check rows, columns, and diagonals for win patterns
    private boolean checkLines(String[] patterns) {
        String rowString, colString, diag1 = "", diag2 = "";
        for (int i = 0; i < 3; i++) {
            rowString = "";
            colString = "";
            for (int j = 0; j < 3; j++) {
                rowString += board[i][j];
                colString += board[j][i];
            }

            for (String pattern : patterns) {
                if (rowString.contains(pattern) || colString.contains(pattern))
                    return true;
            }
            diag1 += board[i][i];
            diag2 += board[i][2 - i];
        }
        for (String pattern : patterns) {
            if (diag1.contains(pattern) || diag2.contains(pattern))
                return true;
        }
        return false;
    }

    // Checks if the board is full
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY)
                    return false;
            }
        }
        return true;
    }

    // Prints the current board state
    private void printBoard() {
        System.out.println("~board~");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print((board[i][j] == EMPTY ? '_' : board[i][j]) + " ");
            }
            System.out.println();
        }
    }
}
