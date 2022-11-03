public class Main2 {

    /*
         N-Queens Problem (Versão dos Três porquinhos do JB)
         Em uma matriz NxN, coloque os porquinhos e as galinhas de forma que nenhum porquinho veja galinhas, e nenhuma galinha veja os porquinhos.
         Input - tamanho da matriz quadrada, e quantidade de porquinhos e galinhas
         ex: java porquinhos 5 3 5 // tabuleiro 5x5, 3 porquinhos, 5 galinhas
         Output - A saída é um inteiro que representa o número de soluções válidas para o problema.
         O problema é equivalente ao problema das N-Rainhas, mas com as seguintes restrições:
            - As galinhas podem ver as galinhas, mas não podem ver os porquinhos.
            - Os porquinhos podem ver os porquinhos, mas não podem ver as galinhas.
            - Todos os porquinhos e galinhas devem fazer parte do jogo
            - Porquinhos e galinhas só veem bichos que estão na mesma linha, coluna ou diagonal.
        O algoritmo deve ser baseado em backtraking que ler a configuração do jogo via linha de comando.
     */

    public static void main(String[] args) {
        System.out.println(new Main2().run(args));
    }

    private long run(String[] args) {
        char[][] tabuleiro = novoTabuleiro(Integer.parseInt(args[0]));

        int p = Integer.parseInt(args[1]);
        int g = Integer.parseInt(args[2]);

        printTabuleiro(tabuleiro);

        return posicionarPorquinhos(p, g, tabuleiro, 0, 0, 0);
    }

    /**
     * Aloca os porquinhos no tabuleiro de forma recursiva, tentando todas as possibilidades.
     * Toda vez que conseguir posicionar todos os porquinhos, tenta posicionar as galinhas.
     * Se conseguir posicionar todas as galinhas, incrementa o número de soluções.
     *
     * @param p              número de porquinhos a serem posicionados
     * @param g              número de galinhas a serem posicionadas
     * @param tabuleiro      tabuleiro onde os porquinhos e galinhas serão posicionados
     * @param numeroSolucoes número de soluções válidas encontradas até o momento
     */
    private long posicionarPorquinhos(int p, int g, char[][] tabuleiro, long numeroSolucoes, int linha, int coluna) {
        if (p == 0) {
            return posicionarGalinhas(g, tabuleiro, numeroSolucoes, 0, 0);
        }

        for (int i = linha; i < tabuleiro.length; i++) {
            for (int j = coluna; j < tabuleiro.length; j++) {
                if (tabuleiro[i][j] == '.') {
                    tabuleiro[i][j] = 'P';
                    numeroSolucoes = posicionarPorquinhos(p - 1, g, tabuleiro, numeroSolucoes, i, j);
                    tabuleiro[i][j] = '.';
                }
            }
            coluna = 0;
        }
        return numeroSolucoes;
    }

    private boolean podeColocarGalinha(char[][] tabuleiro, int linha, int coluna) {
        if (tabuleiro[linha][coluna] == 'P' || tabuleiro[linha][coluna] == 'G') return false;

        // confere na linha e coluna
        for (int i = 0; i < tabuleiro.length; i++) {
            if (tabuleiro[i][coluna] == 'P') return false;
            if (tabuleiro[linha][i] == 'P') return false;
        }

        // diagonal superior esquerda
        for (int i = linha, j = coluna; i >= 0 && j >= 0; i--, j--) {
            if (tabuleiro[i][j] == 'P') return false;
        }

        // diagonal inferior esquerda
        for (int i = linha, j = coluna; j >= 0 && i < tabuleiro.length; i++, j--) {
            if (tabuleiro[i][j] == 'P') return false;
        }

        // diagonal superior direita
        for (int i = linha, j = coluna; i >= 0 && j < tabuleiro.length; i--, j++) {
            if (tabuleiro[i][j] == 'P') return false;
        }

        // diagonal inferior direita
        for (int i = linha, j = coluna; i < tabuleiro.length && j < tabuleiro.length; i++, j++) {
            if (tabuleiro[i][j] == 'P') return false;
        }

        return true;
    }

    private long posicionarGalinhas(int g, char[][] tabuleiro, long numeroSolucoes, int linha, int coluna) {
        if (g == 0) {
            numeroSolucoes++;
            return numeroSolucoes;
        }

        for (int i = linha; i < tabuleiro.length; i++) {
            for (int j = coluna; j < tabuleiro.length; j++) {
                if (tabuleiro[i][j] == '.' && podeColocarGalinha(tabuleiro, i, j)) {
                    tabuleiro[i][j] = 'G';
                    numeroSolucoes = posicionarGalinhas(g - 1, tabuleiro, numeroSolucoes, i, j);
                    tabuleiro[i][j] = '.';
                }
            }
            coluna = 0;
        }
        return numeroSolucoes;
    }

    private char[][] novoTabuleiro(int n) {
        char[][] tabuleiro = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tabuleiro[i][j] = '.';
            }
        }
        return tabuleiro;
    }

    private void printTabuleiro(char[][] tabuleiro) {
        for (char[] chars : tabuleiro) {
            for (int j = 0; j < tabuleiro.length; j++) {
                System.out.print(chars[j] + " ");
            }
            System.out.println();
        }
    }
}