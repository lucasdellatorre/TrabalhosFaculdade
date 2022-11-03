package main

import (
	"fmt"
	"math/rand"
	"time"
)

var (
	elves       int
	reindeers   int
	santaSem    *Semaphore
	reindeerSem *Semaphore
	elfTex      *Semaphore
	mutex       *Semaphore
)

const NUM_ELVES = 10
const NUM_REINDEER = 9

type Semaphore struct { // este semáforo implementa quaquer numero de creditos em "v"
	v    int           // valor do semaforo: negativo significa proc bloqueado
	fila chan struct{} // canal para bloquear os processos se v < 0
	sc   chan struct{} // canal para atomicidade das operacoes wait e signal
}

func main() {
	elves = 0
	reindeers = 0
	santaSem := NewSemaphore(0)
	reindeerSem := NewSemaphore(0)
	elfTex := NewSemaphore(1)
	mutex := NewSemaphore(1)

	go santa(santaSem, reindeerSem, mutex)

	for i := 0; i < NUM_REINDEER; i++ {
		go reindeer(santaSem, reindeerSem, mutex)
	}

	for i := 0; i < NUM_ELVES; i++ {
		go elve(santaSem, elfTex, mutex)
	}

	var blq chan struct{} = make(chan struct{})
	<-blq
}

func santa(santaSem *Semaphore, reindeerSem *Semaphore, mutex *Semaphore) {
	for {
		santaSem.Wait()
		mutex.Wait()
		if reindeers == 9 {
			fmt.Println("prepare Sleigh")
			for i := 0; i < 9; i++ {
				reindeerSem.Signal()
			}
			reindeers = 0
		} else if elves == 3 {
			fmt.Println("help elves")
		}
		mutex.Signal()
	}
}

func reindeer(santaSem *Semaphore, reindeerSem *Semaphore, mutex *Semaphore) {
	for {
		mutex.Wait()
		reindeers++
		if reindeers == 9 {
			santaSem.Signal()
		}
		mutex.Signal()

		reindeerSem.Wait()
		fmt.Println("get hitched")
		time.Sleep(20)
	}

}

func elve(santaSem *Semaphore, elfTex *Semaphore, mutex *Semaphore) {
	for {
		needHelp := rand.Int31()%100 < 10
		if needHelp {
			elfTex.Wait()
			mutex.Wait()

			elves++
			if elves == 3 {
				santaSem.Signal()
			} else {
				elfTex.Signal()
			}
			mutex.Signal()

			fmt.Println("get help")

			mutex.Wait()

			elves--
			if elves == 0 {
				elfTex.Signal()
			}
			mutex.Signal()
		}
		time.Sleep(20)
	}
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
