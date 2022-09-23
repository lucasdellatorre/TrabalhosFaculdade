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
  private ArrayList<ArrayList<String>> map;

  public Map(String path) {
    this.path = path;
    loadMaze();
  }

  private void loadMaze() {
    Path path = Paths.get(this.path);
    try (BufferedReader br = Files.newBufferedReader(path, Charset.defaultCharset())) {
      int size = Integer.parseInt(br.readLine().trim());
      String line;
      map = new ArrayList<>(size);
      while ((line = br.readLine()) != null) {
        String[] splittedLine = line.split(" ");
        ArrayList<String> aux = new ArrayList<>(size);
        String element;
        for (int i = 0; i < splittedLine.length; i++) {
          element = splittedLine[i];
          if (!element.isBlank()) {
            aux.add(splittedLine[i]);
          }
        }
        map.add(aux);
      }
    } catch (IOException io) {
      System.err.format("Erro de E/S: %s%n", io);
    } catch (Exception e) {
      System.out.println("Erro: " + e);
      System.out.print("Erro - trace da falha: ");
      e.printStackTrace();
    }
  }

  public void printMap() {
    int sum = 0;
    for (int row = 0; row < map.size(); row++) {
      for (int col = 0; col < map.get(0).size(); col++) {
        if (map.get(col).get(row).trim() != "x") {
          sum += Integer.parseInt(map.get(row).get(col));
        }
        System.out.print(map.get(row).get(col) + ",");
      }
      System.out.println();
    }
    System.out.println(sum);
  }

  public ArrayList<ArrayList<String>> getMap() {
    return map;
  }
}
