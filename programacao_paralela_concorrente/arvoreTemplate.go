// exercicios com arvore
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

// 1a

func soma(r *Nodo) int {
	if r == nil {
		return 0
	}
	return r.v + soma(r.d) + soma(r.e)
}

// 1b

func somaConcorrente(r *Nodo) int {
  c1 := make(chan int, 100)

  sum := r.v
  go somac(r.e, c1);
  go somac(r.d, c1);

  for i := 0; i < 100; i++ {

    sum += <-c1
  }
  return sum;
}

func somac(r *Nodo, c1 chan int) {
	if r != nil {
  c1 <- r.v
  somac(r.e, c1)
  somac(r.d, c1)
	}
}

//2a

func busca(r *Nodo, v int) bool {
	if r != nil {
		if v == r.v {
			return true
		} else if v < r.v {
			return busca(r.e, v)
		} else {
			return busca(r.d, v)
		}
	}
	return false
}

// 2b

func buscaConcorrente(r *Nodo, v int) bool{
  c1 := make(chan bool, 100)

  if r.v == v { return true }
  
  go buscac(r.e, c1, v)
  go buscac(r.d, c1, v)

  result := <-c1
  return result;
}

func buscac(r *Nodo, c1 chan bool, v int) {
	if r != nil {
		if v == r.v {
			c1 <- true
		} else if v < r.v {
			buscac(r.e, c1, v)
		} else {
			buscac(r.d, c1, v)
		}
	}
	c1 <- false
}

// 3a

func retornaParImpar(r *Nodo, saidaP chan int, saidaI chan int, fin chan struct{}) {
	if r != nil {
		if r.v % 2 == 0 {
		  fmt.Print(r.v, "par, ")
      saidaP <- r.v
		} else {
		  fmt.Print(r.v, "impar, ")
      saidaI <- r.v
    }
		retornaParImpar(r.e, saidaP, saidaI, fin)
		retornaParImpar(r.d, saidaP, saidaI, fin)
	}
}

// 3b


func retornaParImparConcorrente(r *Nodo) {
  saidaP := make(chan int, 30)

  saidaI := make(chan int, 30)

  chfin := make(chan struct{}, 1)

  retornaParImparc(r.e, saidaP, saidaI, chfin)

  <- chfin

  for i := 0; i < 100; i++ {
    fmt.Println(<-saidaP)
    fmt.Println(<-saidaI)
  }
}

func retornaParImparc(r *Nodo, saidaP chan int, saidaI chan int, fin chan struct{}) {
	if r != nil {
		if r.v % 2 == 0 {
      saidaP <- r.v
		} else {
      saidaI <- r.v
    }
    if r.e != nil {
      retornaParImparc(r.e, saidaP, saidaI, fin)
    }
    if r.e != nil {
      retornaParImparc(r.d, saidaP, saidaI, fin)
    }
	}
  fin <- struct{}{}
}

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

  // saidaP := make(chan int, 30)
retornaParImparConcorrente(root);
  // saidaI := make(chan int, 30)

  // chfin := make(chan struct{}, 1)

  // retornaParImpar2(root, saidaP, saidaI, chfin);

  // result := buscaConcorrente(root, -1)

  // fmt.Println(result)

  // for i:=0; i < len(saidaP); i++ {
  //   fmt.Println(<-fin)
  // }
}
