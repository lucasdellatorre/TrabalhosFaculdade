// Fernando Dotti - fldotti.github.io - PUCRS - Escola Politécnica
// >>> Veja o Ex0 desta série
// ABRE E FECHA CONCORRENCIA
// Há várias formas de esperar o término de processos concorrentes.
// EXERCICIOS:
//   1)  isto seria uma solução para sincronizar o final do programa ?
//   2)  aumente para criar 10 prodessos concorrentes say(...).
//       como voce faz a espera de todos ?
// OBS:  tente um comando de repeticao.

package main

import (
	"fmt"
	"time"
)

func say(s string, c chan struct{}) {
	for i := 0; i < 5; i++ {
		fmt.Println(s)
	}
	c <- struct{}{}
}

func main() {
	fin := make(chan struct{})
	for i := 0; i < 10; i++ {
		go say("world", fin)
		go say("hello", fin)
	}
	for i := 0; i < 10; i++ {
		<-fin
		<-fin
	}
	for true {
		time.Sleep(100 * time.Millisecond)
	}
}
