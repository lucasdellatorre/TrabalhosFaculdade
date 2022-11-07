import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import javax.naming.LimitExceededException;

public class RomariaDaVovo {
	private int TAM;
	private int TAM_CROMOSSOMOS = 30; //quantidade de cromossomos por geracao
	private ArrayList<Cidade> mapaCidades = new ArrayList<>(TAM);
	private ArrayList<Double> resultadosHeuristica = new ArrayList<>(TAM);
	private ArrayList<ArrayList<Integer>> populacao = new ArrayList<>(TAM);

	public static void main(String args[]) {
		new RomariaDaVovo().run();
	}

	public void preProcessamento() {
	    File myObj = new File("data1.txt");
	    try {
	      Scanner myReader = new Scanner(myObj);
	      TAM = Integer.parseInt(myReader.nextLine()); // nao preciso do tamanho da populacao, ja coloquei la em cima
	      for (int i = 0; i < TAM; i++) {
			String[] line = myReader.nextLine().split(" ");
			mapaCidades.add(new Cidade(Double.parseDouble(line[0]), Double.parseDouble(line[1]), line[2]));
	      }
	      myReader.close();
	    } catch (Exception e) {
		    System.out.println("ops, erro ao ler o arquivo");
	    }

		Collections.sort(mapaCidades);

		// for (int i = 0; i < mapaCidades.size(); i++) {
		// 	Cidade sentinela = mapaCidades.get(i);
		// 	HashMap<Integer, Double> mapaDistancias = new HashMap<>();
		// 	for ( Cidade c : mapaCidades ) {
		// 		double distancia = distanciaEuclidiana(
		// 			sentinela.getLatitude(), 
		// 			sentinela.getLongitude(), 
		// 			c.getLatitude(), 
		// 			c.getLongitude()
		// 		);
		// 		mapaDistancias.put(c.getId(), distancia);
		// 	}
		// 	sentinela.setMapaDistancia(mapaDistancias);
		// }
	}

	private void run() {
		preProcessamento();
		init();
		printPopulacao();
	}

	void calculaHeuristica() {
		resultadosHeuristica = new ArrayList<>();
		for(int i = 0; i < TAM_CROMOSSOMOS; i++){
			resultadosHeuristica.add(heuristica(i));
		  }
	}

	double heuristica(int individuo) {
		return -1.0;
	}

	public void init() {
		Random rand = new Random();
		populacao = new ArrayList<>();

		for (int i=0; i < TAM_CROMOSSOMOS; i++) {
			ArrayList<Integer> rota = new ArrayList<>();
			for (int j=0; j < mapaCidades.size(); j++) {
				int cidadeId = 1 + rand.nextInt(TAM);
				while (rota.contains(cidadeId)) cidadeId = 1 + rand.nextInt(TAM);
				rota.add(cidadeId);
			}
			populacao.add(rota);
		}

		System.out.println(populacao.get(0).toString());
	}

	private void printPopulacao() {
		populacao.forEach(System.out::println);
	}

	private double distanciaEuclidiana(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	void heuristica() {}

	

	// elitismo
	void pegaMelhor() {} 

	void torneio() {}

	void crossover() {}

	void mutacao() {}
}
