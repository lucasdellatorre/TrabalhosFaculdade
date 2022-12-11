import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlanoB {
	private int N_Cidades;
	private HashMap<Integer, Cidade> mapaCidades = new HashMap<>(N_Cidades);

	public static void main(String args[]) {
		new PlanoB().run();
	}

	public void preProcessamento() {
	    File myObj = new File("data.txt");
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
		ArrayList<Integer> populacao = init();
        double melhorHeuristica = heuristica(populacao);
        double resultadoCromo = Double.MAX_VALUE;
        ArrayList<Integer> cromossomoCandidato = new ArrayList<>(populacao.size());
		// printPopulacao(populacao);

        int i =0;
        while(true) {
            if (resultadoCromo < melhorHeuristica) {
                populacao.clear();
                populacao.addAll(cromossomoCandidato);
                melhorHeuristica = resultadoCromo;
                System.out.println("======================");
                System.out.println("\n\n\n\n\n\nmelhor rota: " + populacao);
                System.out.println("======================");
                System.out.println("melhor heuristica: " + melhorHeuristica);
                System.out.println("======================");

            }

            cromossomoCandidato = crossover(populacao);

            if (i == 5000) {
                System.out.println(cromossomoCandidato);
                mutacao(cromossomoCandidato);
                System.out.println(cromossomoCandidato);
                i = 0;
            }

            resultadoCromo = heuristica(cromossomoCandidato);
            i++;
		}
	}

	ArrayList<Integer> init() {
		Random rand = new Random();
		ArrayList<Integer> populacao = new ArrayList<>(N_Cidades);
			for (int j=0; j < mapaCidades.size(); j++) {
				int cidadeId = 1 + rand.nextInt(N_Cidades);
				while (populacao.contains(cidadeId)) cidadeId = 1 + rand.nextInt(N_Cidades);
				populacao.add(cidadeId);
			}
		return populacao;
	}

  //Mutação
	void mutacao(ArrayList<Integer> populacao){
        Random rand = new Random();
        int quant = rand.nextInt(100)+1;
        // System.out.println("antes da mutacao" + populacao.toString());
        
        for(int i = 0; i<1; i++){
            int posicao1 = rand.nextInt(N_Cidades);
            int posicao2 = rand.nextInt(N_Cidades);

            int cidade1 = populacao.get(posicao1);
            int cidade2 = populacao.get(posicao2);

            populacao.set(posicao1, cidade2);
            populacao.set(posicao2, cidade1);
        }
    }

	void printCalculaHeuristica(ArrayList<Double> resultadosHeuristica) {
		resultadosHeuristica.forEach(System.out::println);
	}
	
	void printPopulacaoIntermediaria(ArrayList<ArrayList<Integer>> populacaoIntermediaria) {
		populacaoIntermediaria.forEach(System.out::println);
	}

	double heuristica(ArrayList<Integer> cromossomo) {
		double somaDistancias = 0;
		for(int j = 0; j < N_Cidades - 1; j++) {
			int idCidadeAtual = cromossomo.get(j);
			int idProxCidade = cromossomo.get(j + 1);
		   	Cidade cidadeAtual = mapaCidades.get(idCidadeAtual);
			Cidade proxCidade = mapaCidades.get(idProxCidade);
			somaDistancias += distanciaEuclidiana(
                cidadeAtual.getLongitude(), 
				cidadeAtual.getLatitude(), 
				proxCidade.getLongitude(),
				proxCidade.getLatitude()
			);
		}
		// caminho de volta
		int idCidadeOriginal = cromossomo.get(0);
		int idCidadeFinal = cromossomo.get(N_Cidades - 1);
		Cidade cidadeAtual = mapaCidades.get(idCidadeFinal);
		Cidade proxCidade = mapaCidades.get(idCidadeOriginal);
			somaDistancias += distanciaEuclidiana(
                cidadeAtual.getLongitude(), 
				cidadeAtual.getLatitude(), 
				proxCidade.getLongitude(),
				proxCidade.getLatitude()
			);
		return somaDistancias;
	}

	public void printPopulacao(ArrayList<ArrayList<Integer>> populacao) {
		populacao.forEach(System.out::println);
		System.out.println("Cromossomos da população: " + populacao.stream().count());
	}

	private double distanciaEuclidiana(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}

	ArrayList<Integer> crossover(
		ArrayList<Integer> populacao1
		) {

        ArrayList<Integer> populacao = new ArrayList<>(populacao1.size());

        populacao.addAll(populacao1);

		//comeca no 1 pq ja colocamos o melhor cromossomo antes

        Random rand = new Random();

        int size = populacao.size();

        int number1 = rand.nextInt(size - 1);
        int number2 = rand.nextInt(size);

        // make the smaller the start and the larger the end
        int iStart = Math.min(number1, number2);
        int iEnd = Math.max(number1, number2);

        List<Integer> start = populacao.subList(0, iStart);

        List<Integer> middle = populacao.subList(iStart, iEnd);

        List<Integer> end = populacao.subList(iEnd, populacao.size());

        Collections.reverse(middle);

        List<Integer> list = Stream.of(start, middle, end).flatMap(Collection::stream).collect(Collectors.toList());
        
		return (ArrayList<Integer>) list;
	}
}
