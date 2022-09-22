// por Fernando Dotti - PUCRS
// dado abaixo um exemplo de estrutura em arvore, uma arvore inicializada
// e uma operação de caminhamento, pede-se fazer:
//   1.a) a operação que soma todos elementos da arvore.
//        func soma(r *Nodo) int {...}
//   1.b) uma operação concorrente que soma todos elementos da arvore
//   2.a) a operação de busca de um elemento v, dizendo true se encontrou v na árvore, ou falso
//        func busca(r* Nodo, v int) bool {}...}
//   2.b) a operação de busca concorrente de um elemento, que informa imediatamente
//        por um canal se encontrou o elemento (sem acabar a busca), ou informa
//        que nao encontrou ao final da busca
//   3.a) a operação que escreve todos pares em um canal de saidaPares e
//        todos impares em um canal saidaImpares, e ao final avisa que acabou em um canal fin
//        func retornaParImpar(r *Nodo, saidaP chan int, saidaI chan int, fin chan struct{}){...}
//   3.b) a versao concorrente da operação acima, ou seja, os varios nodos sao testados
//        concorrentemente se pares ou impares, escrevendo o valor no canal adequado
//
//  RESPOSTA A QUESTOES 3
//
// OBSDERVACAO: veja as variacoes das funcoes de caminhamento na arvore, sequencial e concorrente.

package main

import (
	"fmt"
)

type Nodo struct {
	v int
	e *Nodo
	d *Nodo
}

func caminhaERD(r *Nodo) {
	if r != nil {
		caminhaERD(r.e)
		fmt.Print(r.v, ", ")
		caminhaERD(r.d)
	}
}

// ---- Separa Pares e Impares -----------------------------------------
// esta rotina dispara a chamada aa retornaParImpar
// fica lendo os resultados e escrevendo.   note o nao determinismo
func separaParesEImpares(r *Nodo) {
	pares := make(chan int)
	impares := make(chan int)
	fin := make(chan struct{})
	// aqui fica lendo os resultados.
	// note que o trecho em    go func(){ ... }()   eh concorrente
	// entao esta linha de execucao continua depois dele concorrentemente
	go func() {
		fmt.Println("Pares    e    Ímpares")
		for {
			select {
			case p := <-pares:
				fmt.Println("p: ", p)
			case i := <-impares:
				fmt.Println("               i: ", i)
			case <-fin:
				return
			}
		}
	}()
	// AQUI DISPARA A SEPARAÇÃO - note as duas possibilidades
	//
	// esta faz caminhamento concorrente
	retornaParImpar(r, pares, impares, fin)

	// esta faz caminhamento sequencial - comente a linha acima e
	// descomente as duas linhas abaixo
	// retornaParImpar1(r, pares, impares)
	// fin <- struct{}{}
}

// caminha sequencialmente na arvore,
// vai escrevendo em canais os pares e os impares
// outra rotina fica lendo concorrentemente
func retornaParImpar1(r *Nodo, saidaP chan int, saidaI chan int) {
	if r != nil {
		if (r.v % 2) == 0 {
			saidaP <- r.v
		} else {
			saidaI <- r.v
		}
		retornaParImpar1(r.e, saidaP, saidaI)
		retornaParImpar1(r.d, saidaP, saidaI)
	}
}

// caminha concorrentemente pela arvore
func retornaParImpar(r *Nodo, saidaP chan int, saidaI chan int, fin chan struct{}) {
	fe := make(chan struct{})
	fd := make(chan struct{})
	if r != nil {
		go retornaParImpar(r.e, saidaP, saidaI, fe)
		go retornaParImpar(r.d, saidaP, saidaI, fd)
		if (r.v % 2) == 0 {
			saidaP <- r.v
		} else {
			saidaI <- r.v
		}
		<-fe
		<-fd
		fin <- struct{}{}
	} else {
		fin <- struct{}{}
	}
}

// ---------   agora vamos criar a arvore e usar as funcoes acima

func main() {
	root := &Nodo{v: 10,
		e: &Nodo{v: 5,
			e: &Nodo{v: 3,
				e: &Nodo{v: 1, e: nil, d: nil},
				d: &Nodo{v: 4, e: nil, d: nil}},
			d: &Nodo{v: 7,
				e: &Nodo{v: 6, e: nil, d: nil},
				d: &Nodo{v: 8, e: nil, d: nil}}},
		d: &Nodo{v: 15,
			e: &Nodo{v: 13,
				e: &Nodo{v: 12, e: nil, d: nil},
				d: &Nodo{v: 14, e: nil, d: nil}},
			d: &Nodo{v: 18,
				e: &Nodo{v: 17, e: nil, d: nil},
				d: &Nodo{v: 19, e: nil, d: nil}}}}

	fmt.Println()
	fmt.Print("Valores na árvore: ")
	caminhaERD(root)
	fmt.Println()
	fmt.Println()

	separaParesEImpares(root)

}
