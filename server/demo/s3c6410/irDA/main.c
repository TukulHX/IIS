/*
 *=============================================================================

 *=============================================================================
 */

//#include "/home/hx/9261/linux-2.6.20/include/asm-arm/termbits.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <errno.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
//
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <ctype.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <netinet/ip.h>
#include <fcntl.h>
#include <errno.h>
#include <net/if.h>
#include <string.h>
#include <sys/types.h>
#include <sys/ioctl.h>
#include <sys/io.h>
#include <linux/rtc.h>
#include <time.h>

#include "main.h"

#include "REGS2515.h"
#define GPM2 S3C6410_PIO_PM2
#define GPL1 S3C6410_PIO_PL1
#define GPK2 S3C6410_PIO_PK2
#define GPK8 S3C6410_PIO_PK8
#define GPM2 S3C6410_PIO_PM2
#define GPI0 B0
#define GPI8 G0
#define IY2 S3C6410_PIO_PF7
#define IY0 S3C6410_PIO_PF5

#define XDACOUT1 IY2
#define ADC1 IY0
#define irDA_OUT XEINT14

int bsp_init(void)
{
	open_port_device();

	return 0;
}

int main(int argc, char *argv[])
{
	/*
	 * Ó²¼þ³õÊ¼»¯
	 */
	bsp_init();
        port_write(XDACOUT1,0);
	int i;
	while (1)
	{ 
           i=port_read(irDA_OUT);
	   sleep(1);
	   if(i)
		break;
	}
	return 0;
}


/* end of file */
