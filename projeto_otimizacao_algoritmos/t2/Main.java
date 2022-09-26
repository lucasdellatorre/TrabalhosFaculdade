import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class Main {
  static int negInf = -1000000000;
  static int[][] M;
  static LinkedList<String> P;
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
    P = new LinkedList<>();
    POS_INIT_ROW = SIZE - 1;
    POS_INIT_COL = 0;
    m.printMap();
    int golds = main.qGoldMem(map, POS_INIT_ROW, POS_INIT_COL, SIZE);
    // int golds = main.qGoldMem(map, SIZE);
    System.out.println(golds);
    System.out.println();
    for (int row = 0; row < M.length; row++) {
      for (int col = 0; col < M[0].length; col++) {
        System.out.print(M[row][col] + ",");
      }
      System.out.println();
    }
    main.extractGoldR(POS_INIT_ROW, POS_INIT_COL, SIZE);
    P.forEach(System.out::println);
  }

  void extractGoldR(int row, int col, int size) {
    System.out.println(row);
    if (row < 0 || row >= size) {
      return;
    }
    if (col < 0 || col >= size) {
      return;
    }

    int up = M[row - 1][col];
    int right = M[row][col + 1];
    int diagonal = M[row - 1][col + 1];

    int max = getMax(up, right, diagonal);

    if (max == up) {
      extractGoldR(row - 1, col, size);
      P.add(DIRECTION_N);
    } else if (max == right) {
      extractGoldR(row, col + 1, size);
      P.add(DIRECTION_E);
    } else {
      extractGoldR(row - 1, col + 1, size);
      P.add(DIRECTION_NE);
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

  void getPath(int up, int right, int diagonal) {
    String ne = "NE";
    String n = "N";
    String e = "E";

    int max = getMax(up, right, diagonal);

    if (max == up)
      P.add(n);
    else if (max == right)
      P.add(e);
    else
      P.add(ne);
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

    int res = qGoldMem(map, row - 1, col, size);
    int res1 = qGoldMem(map, row, col + 1, size);
    int res2 = qGoldMem(map, row - 1, col + 1, size);

    getPath(res, res1, res2);

    return M[row][col] = getMax(res, res1, res2) + Integer.parseInt(map[row][col]);
  }

  // int qGoldNotRecursive(String[][] map, int size) {
  // M[0][size - 1] = Integer.parseInt(map[0][size - 1]);

  // botton row

  // for (int i = 1; i < size; i++) {
  // if (map[size - 1][i].equals("x"))
  // M[size - 1][i] = negInf;
  // else
  // M[size - 1][i] = M[size - 1][i - 1] + Integer.parseInt(map[size - 1][i]);
  // }

  // // left column

  // for (int i = 1; i < size; i++) {
  // if (map[i][0].equals("x"))
  // M[i][0] = negInf;
  // else
  // M[i][0] = M[i - 1][0] + Integer.parseInt(map[i][0]);
  // }

  // for (int row = 0; row < size; row++) {
  // for (int col = 0; col < size; col++) {
  // M[row][col] = map[row][col];
  // if (row > 0 && col > 0 ) {
  // M[row][col] += getMax(M[i - 1][col], M[i][j - 1], )
  // }
  // if (!map[row][col].equals("x"))
  // M[row][col] = getMax(M[row - 1][col], M[row][col + 1], M[row - 1][col + 1]) +
  // Integer.parseInt(map[row][col]);
  // }
  // }
  // return M[size - 1][0];
  // }

  int getMax(int x, int y, int z) {
    return Math.max(Math.max(x, y), z);
  }
}
