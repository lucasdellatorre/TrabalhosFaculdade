public class PigsAndChickens {
    private int matrixLength;
    private int pigsCounter;
    private int chickensCounter;
    private int[][] board;

    PigsAndChickens(int pigsCounter, int chickensCounter, int matrixLength) {
        this.pigsCounter = pigsCounter;
        this.chickensCounter = chickensCounter;
        this.matrixLength = matrixLength;
        this.board = new int[matrixLength][matrixLength];
    }

    public void countPlays() {
        countPlays(pigsCounter, chickensCounter);
    }
    
    private void countPlays(int pig, int chicken) {
        int col;
        System.out.println("deus me livre");
        for ( col = 1; col <= matrixLength; col++ ) {
            System.out.println("oi");
            if ( !possible(pig, chicken, col) ) continue;
            board[pig][col] = Animals.PIG.getValue();
            if ( pig == matrixLength ) printBoard();
            else countPlays(pig + 1, chicken);
            board[pig][col] = 0;
        }
    }

    private boolean possible(int pig, int chicken, int col) {
        int l1, c1, c2;
        l1 = pig - 1;
        c1 = col - 1;
        c2 = col + 1;
        while ( l1 > 0 ) {
            if ( board[l1][col] == Animals.PIG.getValue()) return false;
            if (c1 > 0 && board[l1][c1] == Animals.PIG.getValue()) return false;
            if (c2 < matrixLength && board[l1][c2] == Animals.PIG.getValue()) return false;
            l1--; c1--; c2--;
        }
        return true;
    }

    public void printBoard() {
        String fence = "";
        for (int i = 0; i < matrixLength * 4 - 1; i++) {
            fence += "=";
        }
        System.out.println(fence);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print("[" + board[i][j] + "] ");
            }
            System.out.println();
        }
        System.out.println(fence);
    }

    public int getPigsCounter() {
        return pigsCounter;
    }

    public int getChickenCounter() {
        return chickensCounter;
    }

    public int[][] getBoard() {
        int[][] aux = new int[matrixLength][matrixLength];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                aux[i][j] = board[i][j];
            }
        }
        return aux;
    }
}