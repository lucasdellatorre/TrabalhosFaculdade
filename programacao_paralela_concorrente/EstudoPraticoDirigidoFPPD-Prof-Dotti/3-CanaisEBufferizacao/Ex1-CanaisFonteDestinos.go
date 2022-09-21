// por Fernando Dotti - fldotti.github.io - PUCRS - Escola Politécnica
//
// ASSUNTO - Compreensão de concorrência e canais com buffer
//
// EXERCÍCIO:
//     1) Avalie o comportamento do programa para tamBuff
//        0 e 10.   Voce consegue explicar a diferença ?
//     2) Qual versao tem maior nivel de concorrencia ?
//     3) Faça uma versão que tem vários processos destino
//        que podem consumir os dados de forma não determinística.
//        Ou seja, processos diferentes podem consumir quantidades
//        diferentes de itens,  conforme sua velocidade.
//        Como você coordenaria o término dos processos depois do
//        consumo dos N valores ?

package main

import "sync"

const N = 100
const tamBuff = 10

func fonteDeDados(saida chan int, wg *sync.WaitGroup) {
	for i := 1; i < N; i++ {
		println(i, " -> ")
		saida <- i
	}
	wg.Done()
}

func destinoDosDados(entrada chan int, wg *sync.WaitGroup) {
	for {
		select {
		case v := <-entrada:
			println("                  -> ", v)
		default:
			wg.Done()
			break
		}
	}
}

func main() {
	c := make(chan int, tamBuff)
	var waitgroup sync.WaitGroup
	waitgroup.Add(10)
	for i := 0; i < 10; i++ {
		go fonteDeDados(c, &waitgroup)
	}
	waitgroup.Add(1)
	go destinoDosDados(c, &waitgroup)
	waitgroup.Wait()
}
