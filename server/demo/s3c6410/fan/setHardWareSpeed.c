#include "bsp.h"
#include<pthread.h>
#include<stdio.h>
#include<unistd.h>
#include<fcntl.h>
//dcmotor
#define MOTERK1 S3C6410_PIO_PK2
#define MOTERK2 S3C6410_PIO_PD3
#define MOTERK3 S3C6410_PIO_PD4
#define MOTERK4 S3C6410_PIO_PF13
int bsp_init(void)
{
	open_port_device();
	return 0;
}

void * threadLoop(void *arg)
{
	bsp_init();
	port_write(MOTERK2,0);
        port_write(MOTERK1,1);
	int speed = *(int*)arg;
	if(speed > 100) speed = 100;
	if( speed < 0) speed = 0;
	while(1){
		for(int i = 0; i < speed; i++)
			port_write(MOTERK3,1);
		for(int i = speed; i < 100; i++)
			port_write(MOTERK3,0);
	}
}

void setHardWareSpeed(int speed, pthread_t * thread)
{
	pthread_cancel(*thread);
	pthread_create(thread, NULL, threadLoop, &speed);
}


