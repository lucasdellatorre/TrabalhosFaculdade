#include<stdio.h>
#include<pthread.h>
#include<semaphore.h>
#include<unistd.h>

sem_t customer;
sem_t barber;
sem_t acc;
sem_t cutting;
int customer1=0;

void *barbers();

void *customers();

int true=0;

int main()
{
    pthread_t th1,th2;
    sem_init(&customer,0,1);
    sem_init(&barber,0,1);
    pthread_create(&th1,NULL,customers,NULL);
    pthread_create(&th2,NULL,barbers,NULL);
    pthread_join(th1,NULL);
    pthread_join(th2,NULL);
    printf("%d\n",customer1);
}
void *barbers()
{
    while(true)
    {
        sem_wait(&customer);
        sem_wait(&acc);
        customer1=customer1-1;
        printf("%d\n",customer);
        sem_post(&barber);
        printf("%d\n",customer);
        sem_post(&acc);
        sem_wait(&cutting);
    }
}
void *customers()
{
    sem_wait(&access);
    customer1=customer1+1;
    sem_post(&customer);
    printf("%d\n",customer);
    sem_post(&acc);
    sem_wait(&barber);
    sem_post(&cutting);
    printf("%d\n",customer);
}