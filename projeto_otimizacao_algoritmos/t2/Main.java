import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Main main = new Main();
    Map m = new Map(args[0]);
    ArrayList<ArrayList<String>> map = m.getMap();
  }

  public void someRecursion() {
    System.out.println("some cool algorithm");
  }
}
