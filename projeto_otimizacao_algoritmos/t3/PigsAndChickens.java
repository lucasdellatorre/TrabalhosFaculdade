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

    public int countPlays() {
        int aux = pigsCounter;
        int aux2 = 0;
        int pos = 0;
        while (aux != 0) {
            if (aux2 == matrixLength) { pos++; aux2 = 0; } 
            board[pos][aux2] = Animals.PIG.getValue();
            aux2++;
            aux--;
        }
        return countPlays(pigsCounter, chickensCounter, 0);
    }
    
    private int countPlays(int pig, int chicken, int count) {
        int col;
        for ( col = 0; col <= matrixLength; col++ )
            
            if ( !possibleChicken(chicken, col) ) continue;
            board[pig][col] = Animals.CHICKEN.getValue();
            if ( chicken == matrixLength ) {
                printBoard();
                count++;
            }
            else countPlays(pig, chicken + 1, count);
            board[chicken][col] = 0;
        }
        return count;
    }

    private boolean possibleChicken(int chicken, int col) {
        if ( board[chicken][col] == Animals.PIG.getValue()) return false;
        if ( board[chicken][col] == Animals.CHICKEN.getValue()) return false;
        int l1, c1, c2;
        l1 = chicken - 1;
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