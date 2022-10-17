package main

import (
	"fmt"
	"sync"
)

var (
	n           int
	customers   int
	mutex       sync.Mutex
	customerSem sync.Mutex
	barberSem   sync.Mutex
)

func main() {
	customers = 0
	n = 10

	go customer()

	go barber()

	var blq chan struct{} = make(chan struct{})
	<-blq
}

func customer() {
	mutex.Lock()
	if customers == n+1 {
		mutex.Unlock()
		fmt.Println("balk")
	}
	customers++
	mutex.Unlock()

	customerSem.Unlock()
	barberSem.Lock()
	fmt.Println("get hair cut")

	mutex.Lock()
	customers--
	mutex.Unlock()
}

func barber() {
	customerSem.Lock()
	barberSem.Unlock()
	fmt.Println("cut hair")
}
