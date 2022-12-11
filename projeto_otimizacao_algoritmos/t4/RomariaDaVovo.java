import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class RomariaDaVovo {
	private int N_Cidades;
	private int TAM_CROMOSSOMOS = 7; //quantidade de cromossomos por geracao
	private HashMap<Integer, Cidade> mapaCidades = new HashMap<>(N_Cidades);

	public static void main(String args[]) {
		new RomariaDaVovo().run();
	}

	public void preProcessamento() {
	    File myObj = new File("data1.txt");
	    try {
	      Scanner myReader = new Scanner(myObj);
	      N_Cidades = Integer.parseInt(myReader.nextLine());
	      for (int i = 0; i < N_Cidades; i++) {
			String[] line = myReader.nextLine().split(" ");
			Cidade c = new Cidade(Double.parseDouble(line[0]), Double.parseDouble(line[1]), line[2]);
			mapaCidades.put(c.getId(), c);
	      }
	      myReader.close();
	    } catch (Exception e) {
		    System.out.println("ops, erro ao ler o arquivo");
	    }
	}

	private void run() {
		preProcessamento();
		Random rand = new Random();
		ArrayList<ArrayList<Integer>> populacao = init();
		ArrayList<Integer> melhorRota = new ArrayList<>(N_Cidades);
		// printPopulacao(populacao);

		for (int g = 0; true; g++) {
			ArrayList<Double> resultadosHeuristica = calculaHeuristica(populacao);
			// printCalculaHeuristica(resultadosHeuristica);

			melhorRota = getMelhorRota(populacao, resultadosHeuristica);
			// System.out.println(melhorRota.toString());
			ArrayList<ArrayList<Integer>> populacaoIntermediaria = new ArrayList<>(TAM_CROMOSSOMOS);
			populacaoIntermediaria.add(melhorRota); // Adiciona o melhor cromossomo na populacao intermediaria
			// printPopulacaoIntermediaria(populacaoIntermediaria);
			System.out.println("==============");

			populacaoIntermediaria = crossover(populacao, populacaoIntermediaria, resultadosHeuristica);
			// printPopulacaoIntermediaria(populacaoIntermediaria);

			// populacao.clear();
			// populacao = populacaoIntermediaria;
			// populacao.addAll(populacaoIntermediaria);
			populacao = populacaoIntermediaria;
			if(rand.nextInt(1)==0) {    //aplica mutação (chance)
				System.out.println("Mutação");
				mutacao(populacao);
			}	
			// printStatus(g, populacao, populacaoIntermediaria,  resultadosHeuristica, melhorRota);
		}
	}

	void printStatus(
		int g, 
		ArrayList<ArrayList<Integer>> populacao, 
		ArrayList<ArrayList<Integer>> populacaoIntermediaria, 
		ArrayList<Double> resultadosHeuristica, 
		ArrayList<Integer> melhorRota
		) {
		System.out.println("Geração" + g);
		System.out.println("========Populacao========================");
		printPopulacao(populacao);
		System.out.println("========Heuristica=======================");
		printCalculaHeuristica(resultadosHeuristica);
		System.out.println("========Melhor da Geracao================");
		System.out.println(melhorRota.toString());
		System.out.println("========População Intermediária==========");
		printPopulacaoIntermediaria(populacaoIntermediaria);
	}

	ArrayList<ArrayList<Integer>> init() {
		Random rand = new Random();
		ArrayList<ArrayList<Integer>> populacao = new ArrayList<>(TAM_CROMOSSOMOS);

		for (int i=0; i < TAM_CROMOSSOMOS; i++) {
			ArrayList<Integer> rota = new ArrayList<>();
			for (int j=0; j < mapaCidades.size(); j++) {
				int cidadeId = 1 + rand.nextInt(N_Cidades);
				while (rota.contains(cidadeId)) cidadeId = 1 + rand.nextInt(N_Cidades);
				rota.add(cidadeId);
			}
			populacao.add(rota);
		}
		return populacao;
	}

  //Mutação
	void mutacao(ArrayList<ArrayList<Integer>> populacao){
	Random rand = new Random();
	int quant = rand.nextInt(150)+1;
	
	for(int i = 0; i<quant; i++){
		int individuo = 1 + rand.nextInt(TAM_CROMOSSOMOS - 1);
		int posicao1 = rand.nextInt(N_Cidades);
		int posicao2 = rand.nextInt(N_Cidades);

		int cidade1 = populacao.get(individuo).get(posicao1);
		int cidade2 = populacao.get(individuo).get(posicao2);

		populacao.get(individuo).set(posicao1, cidade2);
		populacao.get(individuo).set(posicao2, cidade1);
	}
   
}

	ArrayList<Double> calculaHeuristica(ArrayList<ArrayList<Integer>> populacao) {
		ArrayList<Double> resultadosHeuristica = new ArrayList<>(TAM_CROMOSSOMOS);
		for(int i = 0; i < TAM_CROMOSSOMOS; i++){
			resultadosHeuristica.add(heuristica(populacao.get(i)));
		}
		return resultadosHeuristica;
	}

	void printCalculaHeuristica(ArrayList<Double> resultadosHeuristica) {
		resultadosHeuristica.forEach(System.out::println);
	}
	
	void printPopulacaoIntermediaria(ArrayList<ArrayList<Integer>> populacaoIntermediaria) {
		populacaoIntermediaria.forEach(System.out::println);
	}

	ArrayList<Integer> getMelhorRota(
		ArrayList<ArrayList<Integer>> populacao,
		ArrayList<Double> resultadosHeuristica
		) {
		double melhorRota = Double.MAX_VALUE;
		int indexMelhorRota = -1;
		for (int i = 0; i < resultadosHeuristica.size(); i++) {
			if (resultadosHeuristica.get(i) < melhorRota) {
				melhorRota = resultadosHeuristica.get(i);
				indexMelhorRota = i;
			}
		}
		System.out.println("heuristica melhor cromossomo: " + melhorRota);
		ArrayList<Integer> best = populacao.get(indexMelhorRota);
		return best;
	}

	double heuristica(ArrayList<Integer> cromossomo) {
		double somaDistancias = 0;
		for(int j = 0; j < N_Cidades - 1; j++) {
			int idCidadeAtual = cromossomo.get(j);
			int idProxCidade = cromossomo.get(j + 1);
		   	Cidade cidadeAtual = mapaCidades.get(idCidadeAtual);
			Cidade proxCidade = mapaCidades.get(idProxCidade);
			somaDistancias += distanciaEuclidiana(
				cidadeAtual.getLatitude(), 
				cidadeAtual.getLongitude(), 
				proxCidade.getLatitude(), 
				proxCidade.getLongitude()
			);
		}
		// caminho de volta
		int idCidadeOriginal = cromossomo.get(0);
		int idCidadeFinal = cromossomo.get(N_Cidades - 1);
		Cidade cidadeAtual = mapaCidades.get(idCidadeFinal);
		Cidade proxCidade = mapaCidades.get(idCidadeOriginal);
			somaDistancias += distanciaEuclidiana(
				cidadeAtual.getLatitude(), 
				cidadeAtual.getLongitude(), 
				proxCidade.getLatitude(), 
				proxCidade.getLongitude()
			);
		return somaDistancias;
	}



	public ArrayList<Integer> torneio(ArrayList<ArrayList<Integer>> populacao, ArrayList<Double> resultadosHeuristica) {
		Random rand = new Random();
		int individuo1, individuo2;

		ArrayList<Integer> ind = new ArrayList<>(N_Cidades);
	
		individuo1 = rand.nextInt(TAM_CROMOSSOMOS);
		individuo2 = rand.nextInt(TAM_CROMOSSOMOS);

		if(resultadosHeuristica.get(individuo1) < resultadosHeuristica.get(individuo2)) {
			ind.addAll(populacao.get(individuo1));
			return ind;

		}
		else {
			ind.addAll(populacao.get(individuo2));
			return ind;
		}
	}

	public void printPopulacao(ArrayList<ArrayList<Integer>> populacao) {
		populacao.forEach(System.out::println);
		System.out.println("Cromossomos da população: " + populacao.stream().count());
	}

	private double distanciaEuclidiana(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	ArrayList<ArrayList<Integer>> crossover(
		ArrayList<ArrayList<Integer>> populacao, 
		ArrayList<ArrayList<Integer>> populacaoIntermediaria,
		ArrayList<Double> resultadosHeuristica
		) {
		//comeca no 1 pq ja colocamos o melhor cromossomo antes
		for (int j=1; j < TAM_CROMOSSOMOS; j=j+2) {
			ArrayList<Integer> tour1 = torneio(populacao, resultadosHeuristica);
			ArrayList<Integer> tour2 = torneio(populacao, resultadosHeuristica);

			int size = tour1.size();

			// choose two random numbers for the start and end indices of the slice
			// (one can be at index "size")
			Random rand = new Random();
		
			int number1 = rand.nextInt(size - 1);
			int number2 = rand.nextInt(size);
		
			// make the smaller the start and the larger the end
			int start = Math.min(number1, number2);
			int end = Math.max(number1, number2);
		
			// instantiate two child tours
			List<Integer> child1 = new Vector<Integer>();
			List<Integer> child2 = new Vector<Integer>();
		
			// add the sublist in between the start and end points to the children
			child1.addAll(tour1.subList(start, end));
			child2.addAll(tour2.subList(start, end));
		
			// iterate over each city in the parent tours
			int currentCityIndex = 0;
			int currentCityInTour1 = 0;
			int currentCityInTour2 = 0;
			for (int i = 0; i < size; i++) {
		
				// get the index of the current city
				currentCityIndex = (end + i) % size;
		
				// get the city at the current index in each of the two parent tours
				currentCityInTour1 = tour1.get(currentCityIndex);
				currentCityInTour2 = tour2.get(currentCityIndex);
		
				// if child 1 does not already contain the current city in tour 2, add it
				if (!child1.contains(currentCityInTour2)) {
				child1.add(currentCityInTour2);
				}
		
				// if child 2 does not already contain the current city in tour 1, add it
				if (!child2.contains(currentCityInTour1)) {
				child2.add(currentCityInTour1);
				}
			}
		
			// rotate the lists so the original slice is in the same place as in the
			// parent tours
			Collections.rotate(child1, start);
			Collections.rotate(child2, start);
		
			// copy the tours from the children back into the parents, because crossover
			// functions are in-place!

			Collections.copy(tour1, child2);
			Collections.copy(tour2, child1);

			populacaoIntermediaria.add(tour1);
			populacaoIntermediaria.add(tour2);
		}
		return populacaoIntermediaria;
	}
}
