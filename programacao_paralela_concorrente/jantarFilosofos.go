// MCC - Fernando Dotti
/*
Implemente as seguintes variações de jantar dos filósofos sem deadlock
      a) evitando formação de ciclo com quebra de simetria dos processos (filosofo canhoto)
      b) evitando hold and wait fazendo com que o filosofo largue o primeiro garfo se o segundo estiver ocupado
      c) evitando hold and wait sendo a ação de pegar os dois garfos (ou nenhum) atômica
      d) adicionando garfos - coloque em comentários como voce adicionou os garfos
*/

package main

import (
	"fmt"
	"strconv"
)

const (
	PHILOSOPHERS = 5
	FORKS        = 5
)

func philosopherDL(id int, first_fork chan struct{}, second_fork chan struct{}) {
	for {
		fmt.Println(strconv.Itoa(id) + " senta")
		<-first_fork // pega
		fmt.Println(strconv.Itoa(id) + " pegou direita")
		<-second_fork
		fmt.Println(strconv.Itoa(id) + " come")
		first_fork <- struct{}{} // devolve
		second_fork <- struct{}{}
		fmt.Println(strconv.Itoa(id) + " levanta e pensa")
	}
}

func philosopher1(id int, first_fork chan struct{}, second_fork chan struct{}) {
	for {
		if id == 4 {
			fmt.Println(strconv.Itoa(id) + " senta")
			<-second_fork // pega
			fmt.Println(strconv.Itoa(id) + " pegou direita")
			<-first_fork
			fmt.Println(strconv.Itoa(id) + " come")
			first_fork <- struct{}{} // devolve
			second_fork <- struct{}{}
			fmt.Println(strconv.Itoa(id) + " levanta e pensa")
		} else {
			fmt.Println(strconv.Itoa(id) + " senta")
			<-first_fork // pega
			fmt.Println(strconv.Itoa(id) + " pegou direita")
			<-second_fork
			fmt.Println(strconv.Itoa(id) + " come")
			first_fork <- struct{}{} // devolve
			second_fork <- struct{}{}
			fmt.Println(strconv.Itoa(id) + " levanta e pensa")
		}
	}
}

func philosopher2(id int, first_fork chan struct{}, second_fork chan struct{}) {
	for {
		fmt.Println(strconv.Itoa(id) + " senta")
		<-first_fork // pega
		fmt.Println(strconv.Itoa(id) + " pegou direita")
		if len(second_fork) == 0 {
			first_fork <- struct{}{} // devolve
		} else {
			<-second_fork
			second_fork <- struct{}{}
			fmt.Println(strconv.Itoa(id) + " come")
		}
		fmt.Println(strconv.Itoa(id) + " levanta e pensa")
	}
}

func philosopher3(id int, first_fork chan struct{}, second_fork chan struct{}) {
	for {
		if len(first_fork) != 0 && len(second_fork) != 0 {
			fmt.Println(strconv.Itoa(id) + " senta")
			<-first_fork // pega
			fmt.Println(strconv.Itoa(id) + " pegou direita")
			<-second_fork
			fmt.Println(strconv.Itoa(id) + " come")
			first_fork <- struct{}{} // devolve
			second_fork <- struct{}{}
			fmt.Println(strconv.Itoa(id) + " levanta e pensa")
		}
	}
}

func main() {
	var fork_channels [FORKS]chan struct{}
	for i := 0; i < FORKS; i++ {
		fork_channels[i] = make(chan struct{}, 1)
		fork_channels[i] <- struct{}{} // no inicio garfo esta livre
	}
	for i := 0; i < (PHILOSOPHERS); i++ {
		fmt.Println("Filosofo " + strconv.Itoa(i))
		go philosopher3(i, fork_channels[i], fork_channels[(i+1)%PHILOSOPHERS])
	}
	var blq chan struct{} = make(chan struct{})
	<-blq
}
