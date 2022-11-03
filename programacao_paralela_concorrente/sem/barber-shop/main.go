package main

import (
	"fmt"
	"sync"
)

var (
	wg          sync.WaitGroup
	n           int
	customers   int
	mutex       *Semaphore
	customerSem *Semaphore
	barberSem   *Semaphore
)

type Semaphore struct { // este semáforo implementa quaquer numero de creditos em "v"
	v    int           // valor do semaforo: negativo significa proc bloqueado
	fila chan struct{} // canal para bloquear os processos se v < 0
	sc   chan struct{} // canal para atomicidade das operacoes wait e signal
}

func NewSemaphore(init int) *Semaphore {
	s := &Semaphore{
		v:    init,                   // valor inicial de creditos
		fila: make(chan struct{}),    // canal sincrono para bloquear processos
		sc:   make(chan struct{}, 1), // usaremos este como semaforo para SC, somente 0 ou 1
	}
	return s
}

func (s *Semaphore) Wait() {
	s.sc <- struct{}{} // SC do semaforo feita com canal
	s.v--              // decrementa valor
	if s.v < 0 {       // se negativo era 0 ou menor, tem que bloquear
		<-s.sc               // antes de bloq, libera acesso
		s.fila <- struct{}{} // bloqueia proc
	} else {
		<-s.sc // libera acesso
	}
}

func (s *Semaphore) Signal() {
	s.sc <- struct{}{} // entra sc
	s.v++
	if s.v <= 0 { // tem processo bloqueado ?
		<-s.fila // desbloqueia
	}
	<-s.sc // libera SC para outra op
}

func main() {
	customers = 0
	n = 10
	mutex := NewSemaphore(1)
	customerSem := NewSemaphore(0)
	barberSem := NewSemaphore(0)

	go customer(customerSem, barberSem, mutex)

	go barber(customerSem, barberSem)
	var blq chan struct{} = make(chan struct{})
	<-blq
}

func customer(customerSem *Semaphore, barberSem *Semaphore, mutex *Semaphore) {
	mutex.Wait()
	if customers == n+1 {
		mutex.Signal()
		fmt.Println("balk")
	}
	customers++
	mutex.Signal()

	customerSem.Signal()
	barberSem.Wait()
	fmt.Println("get hair cut")

	mutex.Wait()
	customers--
	mutex.Signal()
}

func barber(customerSem *Semaphore, barberSem *Semaphore) {
	for {
		customerSem.Wait()
		barberSem.Signal()
		fmt.Println("cut hair")
	}
}
