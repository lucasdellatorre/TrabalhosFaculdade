/*
 * PUCRS
 * Disciplina: Projeto e Análise de Algoritmos
 * Prof Joao Batista  
 * Os três porquinhos e seus amigos
 *
 * @author Lucas Dellatorre de Freitas
 *
 */

public class Main {
  public static void main(String args[]) {
    int matrixLength = Integer.parseInt(args[0]);
    int pigs = Integer.parseInt(args[1]);
    int chicken  = Integer.parseInt(args[2]);
    PigsAndChickens pigsAndChickens = new PigsAndChickens(pigs, chicken, matrixLength);
    pigsAndChickens.countPlays();
    pigsAndChickens.printBoard();
  }
}
