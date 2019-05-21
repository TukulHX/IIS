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
//irDA_OUT
#define irDA_OUT XEINT14

//telecontrol
#define wirelessSW1 XEINT15
#define wirelessSW2 XEINT13
#define wirelessSW3 EINT4
#define wirelessSW4 EINT9

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
    unsigned int tmp=0,i=0,flag=0,a[20],b[20],j=0,k=0,max;
	float c;
    *(volatile unsigned int *)(S3C6410_BASE_ADC)  = *(volatile unsigned int *)(S3C6410_BASE_ADC)  & (0);

	int count=0;
	while(1){
        *(volatile unsigned int *)(S3C6410_BASE_ADC+0x4)  = *(volatile unsigned int *)(S3C6410_BASE_ADC+0x4) & 0;
        *(volatile unsigned int *)(S3C6410_BASE_ADC)  = *(volatile unsigned int *)(S3C6410_BASE_ADC) | (0x5049/*0x5041*/);  //0x5049 --AIN1---,,,,0x5041 --AIN0;
		if((*(volatile unsigned int *)(S3C6410_BASE_ADC)) & 0x8000)
		{
           //printf("*************read ADC***************\r\n");
		   *(volatile unsigned int *)(S3C6410_BASE_ADC) |= (1<<1);
           tmp= *(volatile unsigned int *)(S3C6410_BASE_ADC+0xC) & (0x3FF);
		   *(volatile unsigned int *)(S3C6410_BASE_ADC)  &= ~(1<<1);
		   //printf("tmp = %x\r\n",tmp);
           //printf("*************end read***************\r\n");
		   usleep(10000);
		   //flag+=tmp;
		   a[count++]=tmp;
		   //if(i<1)i++;
		   //if((a[i]>=(a[i-1]-10))&&(a[i]<=(a[i-1]+10)))
		   //{
               //flag+=tmp;
			   
		  // }
   	   //printf("tmp+ = %04d\r\n",flag);
		}
		if(count>=20)
		{
			count=0;max=0;
			for(i=0;i<20;i++)
			{
				k=0;
				for(j=0;j<20;j++)
				{
					if(a[i]==a[j])b[i]=++k;
				}
			}
			j=0;
			for(i=0;i<20;i++)
			{
				if(max<b[i])
				{
                    max=b[i];
                    j=i;
				}
			}
			
            flag=a[j];
			if(flag==0x3FF)
			{
				c=(float)0.07+(((float)(flag))*(float)(3.3))*0.979/(float)(1024.0);
                  //c=(float)0.07+(((float)(flag))*(float)(3.3))*0.503/(float)(1024.0);
			}
			else
			{
				c=(((float)(flag))*(float)(3.3))*0.979/(float)(1024.0);
                  //c=(((float)(flag))*(float)(3.3))*0.503/(float)(1024.0);
			}
			printf("%.2f\r\n",c*11);
			break;
		
            flag=0;
			//j=0;
			sleep(1);
		}
		//printf("converter is running\r\n");
	}
	return 0;
}


/* end of file */
