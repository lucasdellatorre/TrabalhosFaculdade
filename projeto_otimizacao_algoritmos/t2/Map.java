import java.io.*;
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
      System.out.println("informe o arquivo de teste no formato 'testes/nome-do-arquivo.txt'");
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
