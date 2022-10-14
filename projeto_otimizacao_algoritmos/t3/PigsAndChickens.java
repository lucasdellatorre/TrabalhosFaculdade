public class PigsAndChickens {
    private int matrixLength;
    private int pigsCounter;
    private int chickensCounter;
    private int[][] board;

    PigsAndChickens(int pigsCounter, int chickensCounter, int matrixLength) {
        this.pigsCounter = pigsCounter;
        this.chickensCounter = chickensCounter;
        this.board = new int[matrixLength][matrixLength];
    }
    
    public int countPlays() {
        System.out.println("cool algorithm here!");
        return -1;
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
