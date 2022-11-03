import java.io.*;
import java.util.Scanner;

public class RomariaDaVovo {
	private final int TAM = 2009;
	private String[][] populacao;

	public static void main(String args[]) {
		new RomariaDaVovo().run();
	}

	private void run() {
		readFile();

		
	}

	private void printPopulacao() {
		for (int i = 0; i < TAM; i++) 
			for(int j = 0; j < 3; j++)
				System.out.println(populacao[i][j]);
	}

	private void readFile() {
	    File myObj = new File("data.txt");
	    try {
	      Scanner myReader = new Scanner(myObj);
	      myReader.nextLine(); // nao preciso do tamanho da populacao, ja coloquei la em cima
	      populacao = new String[TAM][3];

	      for (int i = 0; i < TAM; i++) {
			String[] line = myReader.nextLine().split(" ");
		for (int j = 0; j < 3; j++) {
		  populacao[i][j] = line[j];;
		}
	      }
	      myReader.close();
	    } catch (Exception e) {
		    System.out.println("ops, erro ao ler o arquivo");
	    }
	}

	private double distanciaEuclidiana(double x, double y) {
		return Math.abs(x - y);
	}
}
