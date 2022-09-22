// Fernando Dotti - fldotti.github.io - PUCRS - Escola Politécnica
// (Esta  solução foi apresentada em aula pela colega Micaela Lechmann)
//
// ------- Voce deve ter feito o Ex1 desta serie para entender e continuar aqui ------
// Aqui está a resposta do Ex1.  Esta versão elimina duplicatas de mensagens
//
// ATENÇÃO:
//    1) A partir do Ex1,  eliminamos repasses duplicados - veja abaixo.
//       Mas a topologia ainda é um **grafo aciclico dirigido.**
//       Ou seja, nem todo nodo consegue mandar para qualquer outro.
//
// EXERCÍCIO:
//    1) Veja  como foi feita a parte de eliminação de duplicatas.
//       Compare com sua solucao. Rode, teste.
//    2) Agora faça com que as arestas sejam bi-direcionais.   ou seja, se [i,j]==1 entao [j,i]==1 tambem.
//       Começe com apenas uma ou duas arestas bi-direcionais.
//    3) tente rodar o sistema.
//       o que ocorre?  como voce pode resolver ?

package main

import (
	"fmt"
	"math/rand"
)

// nro de nodos
const N = 10

// Topologia é uma matriz NxN onde 1 em [i][j] indica presenca da aresta do nodo i para o j
type Topology [N][N]int

// O que é enviado entre nodos, pode adicionar campos nesta estrutura ...
type Message struct {
	id       int // identificador da mensagem - um nro sequencial ...
	source   int
	receiver int
}

// um canal de entrada para cada nodo i
type inputChan [N]chan Message

type nodeStruct struct {
	id               int
	topo             Topology
	inCh             inputChan
	received         map[int]Message // repassadas
	receivedMessages []Message       // destino
}

// tamanho do buffer de cada canal de entrada
const channelBufferSize = 1

// difusão ou broadcast - um nodo manda para TODOS seus vizinhos do grafo
// nodo origin, conforme topology, usando canais do vetor inCh, manda message para todos eles
func (n *nodeStruct) broadcast(m Message) { // broadcast(origin int, topo Topology, inCh inputChan, m Message) {
	for j := 0; j < N; j++ { // para todo vizinho j em N
		if n.topo[n.id][j] == 1 { //  a matriz em [origin][j] diz se origin conectado com j
			n.inCh[j] <- m // escreve m no canal de j
		}
	}
}

// cada nodo recebe toda matriz de conectividade e os canais de entrada de todos processos
// cada nodo le o seu canal de entrada e escreve a mensagem em todos canais de saida
// (dele para outros nodos usando a funcao send)
func (n *nodeStruct) nodo() { //(id int, topo Topology, inCh inputChan) {
	fmt.Println(n.id, " ativo! ")
	for {
		m := <-n.inCh[n.id]     // espera entrada entrada, reage
		if m.receiver == n.id { // ee para mim
			n.receivedMessages = append(n.receivedMessages, m)
			fmt.Println("                                 ", n.id, " recebe de ", m.source, "msg ", m.id)
		} else { // nao ee para mim ... tenho q repassar se for a primeira vez
			_, achou := n.received[m.id] // procura no map, responde se achou
			if !achou {                  // nao achou = nao recebi a msg antes
				fmt.Println(n.id, " tratando msg ", m.id, " para ", m.receiver)
				n.broadcast(m)       // repassa a primeira vez                                                 // repassa m em todas arestas de saida
				n.received[m.id] = m // guarda para saber no futuro
			}
		}
	}
}

// ------------------------------------------------------------------------------------------------
// no main: montagem da topologia, criacao de canais, inicializacao de nodos e geracao de mensagens
// ------------------------------------------------------------------------------------------------

func main() {
	var topo Topology
	//  se [i,j]==1, entao o nodo i pode enviar para o nodo j pelo canal j.
	//  para alterar a topologia basta adicionar 1s.  cada 1 é uma aresta direcional.
	//  para modelar comunicacao em ambas direcoes entre i e j, entao [i,j] e [j,i] devem ser 1
	topo = [N][N]int{
		// conforme algoritmo na funco "nodo"
		//  0  1  2  3  4  5  6  7  8  9       aresta de    para
		{0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, // 0           0 -> 1
		{0, 0, 1, 0, 0, 0, 0, 0, 0, 0}, // 1           1 -> 2
		{0, 0, 0, 1, 0, 0, 0, 0, 0, 0}, // 2           2 -> 3
		{0, 0, 0, 0, 1, 0, 0, 0, 1, 0}, // 3           3 -> 4 e  3 -> 7
		{0, 0, 0, 0, 0, 1, 0, 0, 0, 1}, // 4           4 -> 5 e  4 -> 9
		{0, 0, 0, 0, 0, 0, 1, 0, 0, 0}, // 5           5 -> 6
		{0, 0, 0, 0, 0, 0, 0, 1, 0, 0}, // 6           6 -> 7
		{0, 0, 0, 0, 0, 0, 0, 0, 1, 0}, // 7           7 -> 8
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, // 8           8 -> 9
		{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}} // 9

	var inCh inputChan // cada nodo i tem um canal de entrada, chamado inCh[i]
	for i := 0; i < N; i++ {
		inCh[i] = make(chan Message, channelBufferSize) // criando cada um dos canais
	}

	// lanca todos os nodos
	for id := 0; id < N; id++ {
		n := nodeStruct{id, topo, inCh, make(map[int]Message), []Message{}}
		go n.nodo() // por simplicidade todos nodos tem acesso aa mesma topologia (que nao muda)
		// assim como todo nodo tem acesso ao canal de entrada de todos outros mas vai usar somente os que pode enviar
	}

	// carga de mensagens para que sejam "inundadas" na rede
	for i := 1; i < 500; i++ { // gera mensagem de teste a cada segundo
		inCh[0] <- Message{i, 0, rand.Intn(9)}
		// time.Sleep(time.Second)
	}
	<-make(chan struct{}) // bloqueia senao nodos acabam
}
