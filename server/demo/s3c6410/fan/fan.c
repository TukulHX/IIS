#include<stdio.h>
#include<string.h>
#include "bsp.h"

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


int main(int argc, char ** argv)
{
	bsp_init();
	int speed = atoi(argv[1]);
	int tmp;
	port_write(MOTERK2,0);
        port_write(MOTERK1,1);
	if( speed == 0){
		port_write(MOTERK3,0);
		return 0;
	}
	else
	    tmp = 100 / speed;
	while(1)
	{
		for(int i = 0; i < 100; i++)
		    if( i % tmp == 0)
			port_write(MOTERK3,1);
		    else
			port_write(MOTERK3,0);
	}
}
