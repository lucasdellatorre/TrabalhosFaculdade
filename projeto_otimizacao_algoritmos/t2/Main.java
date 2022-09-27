import java.util.LinkedList;

class Main {
  static int negInf = -10000;
  static int[][] M; // cache for recursive solution
  static int[][] Table; 
  static LinkedList<String> P; // best player path
  final String DIRECTION_NE = "NE";
  final String DIRECTION_N = "N";
  final String DIRECTION_E = "E";

  public static void main(String[] args) {
    Main main;
    Map m;
    String[][] map;
    final int SIZE, POS_INIT_ROW, POS_INIT_COL;

    main = new Main();
    m = new Map(args[0]);
    map = m.getMap();

    SIZE = map.length;
    M = new int[SIZE][SIZE];
    Table = new int[SIZE][SIZE];
    P = new LinkedList<>();
    POS_INIT_ROW = SIZE - 1;
    POS_INIT_COL = 0;

    // m.printMap();

    // 1 - Solucao recursiva
    // int golds = main.qGold(map, POS_INIT_ROW, POS_INIT_COL, SIZE);

    // 2 - Solucao recursiva com cache
    int golds = main.qGoldMem(map, POS_INIT_ROW, POS_INIT_COL, SIZE);
    main.eRecursao(); // realiza a extracao do caminho

    // // 3 - Solucao nao recursiva
    // int golds = main.qGoldNotRecursive(map, SIZE);
    // main.eTable(); // realiza a extracao do caminho

    /* saida de dados */
    System.out.println("Quantidade de ouro: " + golds);
    System.out.println("caminho: " + P.toString());
  }

  void eTable() {
    int row, col;
    int down, left, diagonal;
    int max;

    row = 0;
    col = M.length - 1;
    while (row != M.length - 1 || col != 0) {
      down = Integer.MIN_VALUE;
      left = Integer.MIN_VALUE;
      diagonal = Integer.MIN_VALUE;
      max = 0;
      if (row + 1 > M.length - 1 && col - 1 >= 0) { //colado na parte de baixo
        left = M[row][col - 1];
        max = left;
      } else if (row + 1 < M.length - 1 && col - 1 < 0) { //colado na parede esquerda
        down = M[row + 1][col];
        max = down;
      } else {
        down = M[row + 1][col];
        left = M[row][col - 1];
        diagonal = M[row + 1][col - 1];
        max = getMax(down, left, diagonal);
      }
      if (max == down) {
        P.add(0, DIRECTION_N);
        row++;
      } else if (max == left) {
        P.add(0, DIRECTION_E);
        col--;
      } else {
        P.add(0, DIRECTION_NE);
        row++;
        col--;
      }
    }
  }

  void eRecursao() {
    int row, col;
    int up, right, diagonal;
    int max;

    row = M.length - 1;
    col = 0;
    while (row != 0 || col != M.length - 1) {
      up = Integer.MIN_VALUE;
      right = Integer.MIN_VALUE;
      diagonal = Integer.MIN_VALUE;
      max = 0;
      if (row - 1 < 0 && col + 1 < M.length) { // colado na parte de cima
        right = M[row][col + 1];
        max = right;
      } else if (row - 1 >= 0 && col + 1 > M.length - 1) { // colado na parede direita
        up = M[row - 1][col];
        max = up;
      } else { // outras celulas
        up = M[row - 1][col];
        right = M[row][col + 1];
        diagonal = M[row - 1][col + 1];

        max = getMax(up, right, diagonal);
      }
      if (max == up) {
        P.add(DIRECTION_N);
        row--;
      } else if (max == right) {
        P.add(DIRECTION_E);
        col++;
      } else {
        P.add(DIRECTION_NE);
        row--;
        col++;
      }
    }
  }

  // sem memorizaçao
  int qGold(String[][] map, int row, int col, int size) {
    // destination
    if (row == 0 && col == size - 1) {
      return Integer.parseInt(map[0][size - 1]);
    }
    if (row < 0 || row >= size) {
      return negInf;
    }
    if (col < 0 || col >= size) {
      return negInf;
    }
    if (map[row][col].equals("x")) {
      return negInf;
    }

    int res = qGold(map, row - 1, col, size);
    int res1 = qGold(map, row, col + 1, size);
    int res2 = qGold(map, row - 1, col + 1, size);

    return getMax(res, res1, res2) + Integer.parseInt(map[row][col]);
  }

  // com memorização
  int qGoldMem(String[][] map, int row, int col, int size) {
    if (row == 0 && col == size - 1) {
      return M[row][col] = Integer.parseInt(map[0][size - 1]);
    }
    if (row < 0 || row >= size) {
      return negInf;
    }
    if (col < 0 || col >= size) {
      return negInf;
    }
    if (map[row][col].equals("x")) {
      return negInf;
    }
    if (M[row][col] != 0) {
      return M[row][col];
    }

    int res = qGoldMem(map, row - 1, col, size); // up
    int res1 = qGoldMem(map, row, col + 1, size); // right
    int res2 = qGoldMem(map, row - 1, col + 1, size); // d

    return M[row][col] = getMax(res, res1, res2) + Integer.parseInt(map[row][col]);
  }

  // table
  int qGoldNotRecursive(String[][] map, int size) {
    // initial pos
    M[size - 1][0] = Integer.parseInt(map[size - 1][0]);

    // botton row

    for (int i = 1; i < size; i++) {
      if (map[size - 1][i].equals("x"))
        M[size - 1][i] = negInf;  
      else
        M[size - 1][i] = M[size - 1][i - 1] + Integer.parseInt(map[size - 1][i]);
    }

    // left column

    for (int i = size - 2; i >= 0; i--) {
      if (map[i][0].equals("x"))
        M[i][0] = negInf;
      else
        M[i][0] = M[i + 1][0] + Integer.parseInt(map[i][0]);
    }

    for (int row = size - 2; row >= 0; row--) {
      for (int col = 1; col < size; col++) {
        if (map[row][col].equals("x"))
          M[row][col] = negInf;
        else
          M[row][col] = getMax(M[row][col - 1], M[row + 1][col], M[row + 1][col - 1]) + Integer.parseInt(map[row][col]);
      }
    }

    return M[0][size - 1];
  }

  int getMax(int x, int y, int z) {
    return Math.max(Math.max(x, y), z);
  }
}
