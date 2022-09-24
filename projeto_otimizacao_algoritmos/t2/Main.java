import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Main {
  public static int negInf = -1000;

  public static void main(String[] args) {
    Main main;
    Map m;
    String[][] map;
    final int SIZE, POS_INIT_ROW, POS_INIT_COL;

    main = new Main();
    m = new Map(args[0]);
    map = m.getMap();

    SIZE = map.length;
    POS_INIT_ROW = SIZE - 1;
    POS_INIT_COL = 0;
    int golds = main.qGold(map, POS_INIT_ROW, POS_INIT_COL, SIZE);
    m.printMap();
    System.out.println(golds);
  }

  int qGold(String[][] map, int row, int col, int size) {
    if (row < 0 || row >= size) {
      return 0;
    }
    if (col < 0 || col >= size) {
      return 0;
    }
    if (map[row][col].equals("x")) {
      return 0;
    }

    int res = qGold(map, row - 1, col, size);
    int res1 = qGold(map, row, col + 1, size);
    int res2 = qGold(map, row - 1, col + 1, size);

    return (getMax(res, res1, res2) + Integer.parseInt(map[row][col]));
  }

  int getMax(int x, int y, int z) {
    return Math.max(Math.max(x, y), z);
  }

}
