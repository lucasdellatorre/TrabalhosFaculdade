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
  public static int negInf = -1000000;

  public static void main(String[] args) {
    Main main;
    Map m;
    ArrayList<ArrayList<String>> map;
    final int SIZE, POS_INIT_X, POS_INIT_Y;

    main = new Main();
    m = new Map(args[0]);
    map = m.getMap();

    SIZE = map.size();
    POS_INIT_X = 0;
    POS_INIT_Y = SIZE - 1;
    int golds = main.giveMeGold(POS_INIT_X, POS_INIT_Y, map, SIZE, Main.negInf, 0, 0);
    System.out.println("golds" + golds);

  }

  int giveMeGold(int x, int y, ArrayList<ArrayList<String>> map, int size, int best, int bag, int res) {
    if (x < 0 || x > size)
      return negInf;
    if (y < 0 || y > size)
      return negInf;
    if (map.get(x).get(y) == "x")
      return negInf;

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
}
