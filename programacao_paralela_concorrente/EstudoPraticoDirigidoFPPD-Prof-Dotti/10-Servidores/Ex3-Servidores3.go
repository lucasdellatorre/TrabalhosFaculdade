// por Fernando Dotti - fldotti.github.io - PUCRS - Escola Politécnica
// servidor com criacao dinamica de thread de servico
// Problema:
//   considere um servidor que recebe pedidos por um canal (representando uma conexao)
//   ao receber o pedido, sabe-se através de qual canal (conexao) responder ao cliente.
//
// EXERCICIO:
//   agora suponha que o seu servidor pode estar tratando no maximo 10 clientes concorrentemente.
//   como voce faria ?
// RESPOSTA:
//   Veja a resposta abaixo ... veja o pool de servidores ...
// ATENCAO:
//   note que todos processos tomam requisicoes do mesmo canal

package main

import (
	"fmt"
	"math/rand"
)

const (
	NCL  = 100
	Pool = 10
)

type Request struct {
	v      int
	ch_ret chan int
}

// ------------------------------------
// cliente
func cliente(i int, req chan Request) {
	var v, r int
	my_ch := make(chan int)
	for {
		v = rand.Intn(1000)
		req <- Request{v, my_ch}
		r = <-my_ch
		fmt.Println("cli: ", i, " req: ", v, "  resp:", r)
	}
}

// ------------------------------------
// servidor sequencial
func servidorSeq(id int, in chan Request) {
	for {
		req := <-in
		fmt.Println("   Serv: ", id, "            trataReq ", req)
		req.ch_ret <- req.v * 2 // responde  ao cliente
	}
}

// servidor concorrente é um pool de sequenciais lendo do mesmo canal
func servidorConc(in chan Request) {
	for i := 1; i < Pool; i++ {
		go servidorSeq(i, in)
	}
}

// ------------------------------------
// main
func main() {
	fmt.Println("------ Servidores - criacao dinamica -------")
	serv_chan := make(chan Request)
	go servidorConc(serv_chan)
	for i := 0; i < NCL; i++ {
		go cliente(i, serv_chan)
	}
	<-make(chan int)
}
