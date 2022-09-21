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
//  RESPOSTA A QUESTOES 2
//

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

// -------- BUSCA ----------
// sequencial
func busca(r *Nodo, v int) bool { // retorna true se v esta na arvore, senao falso
	if r != nil {
		if r.v == v {
			return true
		}
		return (busca(r.e, v) || busca(r.d, v))
	}
	return false
}

// concorrente
// novamente esta função eh um wrapper para a de baixo,
// que usa canal para o retorno
func buscaConc(r *Nodo, v int) bool {
	result := make(chan bool)
	go buscaConcCh(r, v, result)
	return <-result
}

func buscaConcCh(r *Nodo, v int, result chan bool) { // retorna true se v esta na arvore, senao falso
	if r != nil {
		if r.v == v {
			result <- true
		}
		resultNxt := make(chan bool)
		go buscaConcCh(r.e, v, resultNxt)
		go buscaConcCh(r.d, v, resultNxt)
		result <- (<-resultNxt || <-resultNxt)
	}
	result <- false
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

	fmt.Println("Busca 19: ", busca(root, 19))
	fmt.Println("Busca Conc 19: ", buscaConc(root, 19))
	fmt.Println("Busca Conc 20: ", buscaConc(root, 20))
	fmt.Println()
}
