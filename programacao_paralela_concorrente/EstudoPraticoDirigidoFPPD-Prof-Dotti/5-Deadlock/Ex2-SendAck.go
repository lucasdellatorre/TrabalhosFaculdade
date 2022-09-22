// PUCRS - Fernando Dotti
//  Um sistema tem um gerador de dados que solicita a um processo Fonte enviar dados
//  para um processo Destino.
//  A cada dado recebido, o Destino manda uma confirmação para a Fonte.
//  Note que isto é uma modelagem de um sistema onde a confirmacao seria importante.
//  Com canais não existe necessidade de confirmacao de recepcao pois nenhum dado é perdido.
//
//  Exercício:
//  Rode este sistema e veja se algum problema ocorre.
//  Corrija o problema.

package main

import (
	"fmt"
	"sync"
)

const (
	N = 100
	T = 5
)

func Gerador(solicitaEnvio chan int, wg *sync.WaitGroup) {
	for i := 1; i < N; i++ {
		solicitaEnvio <- i
	}
	wg.Done()
}

func Fonte(solicitaEnvio chan int, envia chan int, confirma chan struct{}, wg *sync.WaitGroup) {
	contConf := 0
	for {
		select {
		case x := <-solicitaEnvio:
			envia <- x
		case <-confirma:
			contConf++
		}
	}
}

func Destino(envia chan int, confirma chan struct{}, wg *sync.WaitGroup) {
	for {
		rec := <-envia         // recebe valor
		confirma <- struct{}{} // confirma
		fmt.Print(rec, ", ")
	}
}

func destino(envia chan int, confirma chan struct{}) {
	for {
		select {
		case rec := <-envia:
			confirma <- struct{}{}
			fmt.Print(rec, ", ")
		default:
			break
		}
	}
}

func main() {
	var wg sync.WaitGroup
	solicitaEnvio := make(chan int, T)
	envia := make(chan int, T)
	confirma := make(chan struct{}, T)
	wg.Add(3)
	go Gerador(solicitaEnvio, &wg)
	go Fonte(solicitaEnvio, envia, confirma, &wg)
	fmt.Println()
	go Destino(envia, confirma, &wg)
	wg.Wait()
}
