import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

class Main {
  public static int negInf = -1000;

  public static void main(String[] args) {
    Main main;
    Map m;
    ArrayList<ArrayList<String>> map;
    final int SIZE, POS_INIT_ROW, POS_INIT_COL;

    main = new Main();
    m = new Map(args[0]);
    map = m.getMap();

    SIZE = map.size();
    POS_INIT_ROW = SIZE - 1;
    POS_INIT_COL = 0;
    System.out.println(map.get(POS_INIT_ROW).get(POS_INIT_COL));
    int golds = main.giveMeGold2(POS_INIT_ROW, POS_INIT_COL, map, SIZE, Main.negInf, 0, 0);
    System.out.println("golds" + golds);
    m.printMap();

  }

  int giveMeGold(int x, int y, ArrayList<ArrayList<String>> map, int size, int best, int bag, int res) {
    if (x < 0 || x > size)
      return 0;
    if (y < 0 || y > size)
      return 0;
    if (map.get(x).get(y) == "x")
      return 0;

    int gold = Integer.parseInt(map.get(x).get(y));

    System.out.println(gold);
    res = giveMeGold(x, y - 1, map, size, best, bag, res);
    if (res < best) {
      best = res;
      bag += best;
    } else {
      best = giveMeGold(x + 1, y, map, size, best, bag, res);
      bag += best;
    }
    return bag;
  }

    int giveMeGold2(int x, int y, ArrayList<ArrayList<String>> map, int size, int best, int bag, int res) {
    if (x == 0 && y == size - 1)
    {
      System.out.println("oi");
      return Integer.parseInt(map.get(x).get(y));
    }
      
    if (x < 0 || x >= size)
      return 0;
    if (y < 0 || y >= size)
      return 0;
    if (map.get(x).get(y) == "x")
      return 0;

    System.out.println("[" + x + "][" + y + "]");
    return giveMeGold2(x - 1, y, map, size, best, bag, res) + giveMeGold2(x, y + 1, map, size, best, bag, res);
  }
}
