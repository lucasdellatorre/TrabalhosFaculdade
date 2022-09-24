import java.util.ArrayList;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Map {
  private String path;
  private String[][] map;

  public Map(String path) {
    this.path = path;
    loadMaze();
  }

  private void loadMaze() {
    File myObj = new File(this.path);
    try {
      Scanner myReader = new Scanner(myObj);
      int n = Integer.parseInt(myReader.nextLine());
      map = new String[n][n];

      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          map[i][j] = String.valueOf(myReader.next());
        }
      }
      myReader.close();
    } catch (Exception e) {
      System.out.println("ixiiii");
    }
  }

  public void printMap() {
    for (int row = 0; row < map.length; row++) {
      for (int col = 0; col < map[0].length; col++) {
        System.out.print(map[row][col] + ",");
      }
      System.out.println();
    }
  }

  public String[][] getMap() {
    return map;
  }
}
