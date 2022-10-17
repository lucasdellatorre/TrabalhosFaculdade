package main

import (
	"fmt"
	"sync"
)

var (
	wg          sync.WaitGroup
	n           int
	customers   int
	mutex       sync.Mutex
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
	n = 3
	customerSem := NewSemaphore(0)
	barberSem := NewSemaphore(0)

	wg.Add(2)
	go customer(customerSem, barberSem)
	go barber(customerSem, barberSem)
	wg.Wait()
}

func customer(customerSem *Semaphore, barberSem *Semaphore) {
	mutex.Lock()
	if customers == n+1 {
		mutex.Unlock()
		fmt.Println("balk")
	}
	customers++
	mutex.Unlock()

	customerSem.Signal()
	barberSem.Wait()
	fmt.Println("get hair cut")

	mutex.Lock()
	customers--
	mutex.Unlock()
  wg.Done()
}

func barber(customerSem *Semaphore, barberSem *Semaphore) {
	customerSem.Wait()
	barberSem.Signal()
	fmt.Println("cut hair")
  wg.Done()
}
