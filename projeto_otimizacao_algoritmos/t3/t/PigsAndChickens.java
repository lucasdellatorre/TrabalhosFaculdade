public class PigsAndChickens {
    private int matrixLength;
    private int pigsCounter;
    private int chickensCounter;

    PigsAndChickens(int pigsCounter, int chickensCounter, int matrixLength) {
        this.pigsCounter = pigsCounter;
        this.chickensCounter = chickensCounter;
        this.matrixLength = matrixLength;
    }

    public int countPlays() {
        return countPlays(new int[matrixLength][matrixLength], pigsCounter, chickensCounter, 0, 0, 0);
    }
    
    private int countPlays(int[][] board, int pig, int chicken, int count, int row, int col) {

        /* Quando colocar todos os porcos, pode colocar as galinhas no tabuleiro 
         * as galinhas precisam comecar a olhar do inicio para conferir todas as possibilidades
        */

        if (pig == 0) 
            return chickens(board, chicken, count, 0, 0); 

        for ( int i = row; i < matrixLength; i++ ) {
            for ( int j = col; j < matrixLength; j++ ) {
                if ( board[i][j] != Animals.EMPTY.getValue() ) continue;
                board[i][j] = Animals.PIG.getValue();
                count = countPlays( board, pig - 1, chicken, count, i, j );
                board[i][j] = 0; // limpa possibilidade
            }
            col = 0;
        }
        return count;
    }

    private int chickens(int[][] board, int chicken, int count, int row, int col) {

        /* se conseguir colocar todas as galinhas, incrementa o contador */  

        if ( chicken == Animals.EMPTY.getValue() ) return ++count; 

        for ( int i = row; i < matrixLength; i++ ) {
            for ( int j = col; j < matrixLength; j++ ) {
                if ( !possible(board, i, j) ) continue;
                board[i][j] = Animals.CHICKEN.getValue();
                count = chickens( board, chicken - 1, count, i, j );
                board[i][j] = 0; // limpa possibilidade
            }
            col = 0;
        }
        return count;
    }

    private boolean possible(int[][] board, int row, int col) {
        if ( board[row][col] != Animals.EMPTY.getValue() ) return false; // se nao eh uma posicao vazia

        // se tem um porco na linha 
        for (int i = 0; i < matrixLength; i++) 
            if (board[row][i] == Animals.PIG.getValue()) return false;

        // se tem um porco na coluna 
        for (int i = 0; i < matrixLength; i++) 
            if (board[i][col] == Animals.PIG.getValue()) return false;

        // diagonal superior esquerda
        for (int i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == Animals.PIG.getValue()) return false;
        }

        // diagonal inferior esquerda
        for (int i = row, j = col; j >= 0 && i < matrixLength; i++, j--) {
            if (board[i][j] == Animals.PIG.getValue()) return false;
        }

        // diagonal superior direita
        for (int i = row, j = col; i >= 0 && j < matrixLength; i--, j++) {
            if (board[i][j] == Animals.PIG.getValue()) return false;
        }

        // diagonal inferior direita
        for (int i = row, j = col; i < matrixLength && j < matrixLength; i++, j++) {
            if (board[i][j] == Animals.PIG.getValue()) return false;
        }
        return true;
    }

    public void printBoard(int[][] board) {
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

    public int[][] cloneBoard(int[][] board) {
        int[][] aux = new int[matrixLength][matrixLength];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                aux[i][j] = board[i][j];
            }
        }
        return aux;
    }
}