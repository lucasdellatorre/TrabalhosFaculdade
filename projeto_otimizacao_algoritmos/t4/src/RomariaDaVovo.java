import java.io.*;
import java.util.Scanner;

public class RomariaDaVovo {
	private final int TAM = 2009;
	private String[][] populacao;

	public static void main(String args[]) {
		new RomariaDaVovo().run();
	}

	private void run() {
		leArquivo();

		int melhor;
		for (int g = 0; g < 300; g++) {
			calculaHeuristica();
			printPopulacao();
		}
	}

	private void printPopulacao() {
		String msg;
		for (int i = 0; i < TAM; i++) {
			msg = "";
			for(int j = 0; j < 3; j++)
				msg += populacao[i][j] + " ";
			System.out.println(msg);
		}
	}

	private void leArquivo() {
	    File myObj = new File("data.txt");
	    try {
	      Scanner myReader = new Scanner(myObj);
	      myReader.nextLine(); // nao preciso do tamanho da populacao, ja coloquei la em cima
	      populacao = new String[TAM][3];

	      for (int i = 0; i < TAM; i++) {
			String[] line = myReader.nextLine().split(" ");
			for (int j = 0; j < 3; j++) {
				populacao[i][j] = line[j];
			}
	      }
	      myReader.close();
	    } catch (Exception e) {
		    System.out.println("ops, erro ao ler o arquivo");
	    }
	}

	private double distanciaEuclidiana(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	void init() {}

	void heuristica() {}

	void calculaHeuristica() {}

	// elitismo
	void pegaMelhor() {} 

	void torneio() {}

	void crossover() {}

	void mutacao() {}
}
