/*
 *=============================================================================
2011 02 14
 *=============================================================================
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <ctype.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <netinet/ip.h>
#include <net/if.h>
#include <sys/types.h>
#include <sys/ioctl.h>
#include <sys/io.h>
#include <linux/rtc.h>
#include <time.h>

#include <errno.h>
#include <termios.h>
#include <string.h>
#include <pthread.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <math.h>

#include "bsp.h"

void mmdelay(unsigned int ms)
{
	unsigned int i, j;
	for (i = 0; i < ms; i++)
		for (j = 0; j < 30000; j++);
}

void uudelay(unsigned int us)
{
	unsigned int i, j;
	for (i = 0; i < us; i++)
		for (j = 0; j < 170; j++);
}

/*
 *	bsp 
 */
int fdMem = -1 ;               //内存设备句柄，获得端口硬件地址的虚拟地址时用到 
unsigned char* gpioports;      // 系统外设(System Peripherals)虚拟基地址         
unsigned int *PIOA0_BaseVirt;   // 	0x7F008000
unsigned int *PIOB0_BaseVirt;   // 	0x7F008020
unsigned int *PIOC0_BaseVirt;   // 	0x7F008040
unsigned int *PIOD0_BaseVirt;   // 	0x7F008060
unsigned int *PIOE0_BaseVirt;   // 	0x7F008080
unsigned int *PIOF0_BaseVirt;   // 	0x7F0080A0
unsigned int *PIOG0_BaseVirt;   // 	0x7F0080C0
unsigned int *PIOH0_BaseVirt;   // 	0x7F0080E0
unsigned int *PIOH1_BaseVirt;   // 	0x7F0080E4
unsigned int *PIOI0_BaseVirt;   // 	0x7F008100
unsigned int *PIOJ0_BaseVirt;   // 	0x7F008120
unsigned int *PIOK0_BaseVirt;  //  	0x7F008800		     
unsigned int *PIOK1_BaseVirt;  //  	0x7F008804
unsigned int *PIOL0_BaseVirt;   //	0x7F008810	      
unsigned int *PIOL1_BaseVirt;   //	0x7F008814
unsigned int *PIOM0_BaseVirt;   // 	0x7F008820	
unsigned int *PION0_BaseVirt;   // 	0x7F008830
unsigned int *PIOO0_BaseVirt;   // 	0x7F008100
unsigned int *PIOP0_BaseVirt;   // 	0x7F008160
unsigned int *PIOQ0_BaseVirt;   // 	0x7F008180

unsigned int *ADC_BaseVirt;    //端口ADC控制器的虚拟基地址		0x7E00B000			  


int port_read(  unsigned int pio_port_base_addr,
			  unsigned int port_bit,
			  unsigned int con_state,
			  unsigned int con_bit,            //CON的多少位对应一个脚 0为4位,1为2位
			  unsigned int con_num)           //有几个控制寄存器，0为1个，1为2个          
{ 
	unsigned int val;
	
	// 对应的那两位配置为0000，INPUT
	if(con_bit)
	{
		(*(volatile unsigned int *)(pio_port_base_addr)) = ((*(volatile unsigned int *)(pio_port_base_addr)) & ~(0x0f<<(port_bit*2)))|(0x00<<(port_bit*2));
	}
	else
	{
		(*(volatile unsigned int *)(pio_port_base_addr)) = ((*(volatile unsigned int *)(pio_port_base_addr)) & ~(0x000f<<((port_bit-con_state*8)*4)))|(0x0000<<((port_bit-con_state*8)*4));
	}
	
	//00 = pull-up/down disabled   01 = pull-down enabled  10 = pull-up enabled  11 = Reserved.
	(*(volatile unsigned int *)(pio_port_base_addr+S3C6410_GPXPUD_OFFSET+(con_num-con_state)*4))  = ((*(volatile unsigned int *)(pio_port_base_addr+S3C6410_GPXPUD_OFFSET+(con_num-con_state)*4))  & ~(0x0f<< (port_bit*2)));
	val = (*(volatile unsigned int *)(pio_port_base_addr+S3C6410_GPXDAT_OFFSET+(con_num-con_state)*4)) & (0x0001<<port_bit);
	
	//	printf("CON:0x%x ,DAT:0x%x,port:%d\r\n",(*(volatile unsigned int *)(pio_port_base_addr)),(*(volatile unsigned int *)(pio_port_base_addr+S3C6410_GPXDAT_OFFSET)),port_bit);
	
    return (val ? 1 : 0);
}


int port_write(unsigned int pio_port_base_addr,// PIO基地址			
			   unsigned int port_bit,          // 某位
			   unsigned int con_state,
			   unsigned int con_bit,            //CON的多少位对应一个脚 0为4位,1为2位
               unsigned int con_num,            //有几个控制寄存器，0为1个，1为2个
			   int value)                      // 值:0,1
{
	// 0001，OUTPUT或01 OUTPUT
	if(con_bit)
		{
			(*(volatile unsigned int *)(pio_port_base_addr)) = ((*(volatile unsigned int *)(pio_port_base_addr)) & ~(0x0f<<(port_bit*2)))|(0x01<<(port_bit*2));
		}
	else
		{
			(*(volatile unsigned int *)(pio_port_base_addr)) = ((*(volatile unsigned int *)(pio_port_base_addr)) & ~(0x000f<<((port_bit-con_state*8)*4)))|(0x0001<<((port_bit-con_state*8)*4));
		}
	
	//00 = pull-up/down disabled   01 = pull-down enabled  10 = pull-up enabled  11 = Reserved.
	(*(volatile unsigned int *)(pio_port_base_addr+S3C6410_GPXPUD_OFFSET+(con_num-con_state)*4))  = ((*(volatile unsigned int *)(pio_port_base_addr+S3C6410_GPXPUD_OFFSET+(con_num-con_state)*4))  & ~(0x0f<< (port_bit*2)));

	if(value)
	{	
		(*(volatile unsigned int *)(pio_port_base_addr+S3C6410_GPXDAT_OFFSET+(con_num-con_state)*4)) |= (0x0001<<port_bit);
	}
	else
	{
		/* Clear Output */			
		(*(volatile unsigned int *)(pio_port_base_addr+S3C6410_GPXDAT_OFFSET+(con_num-con_state)*4)) &= ~(0x0001<<port_bit);
	}
	
//	printf("CON:0x%x ,DAT:0x%x,port:%d\r\n",(*(volatile unsigned int *)(pio_port_base_addr)),(*(volatile unsigned int *)(pio_port_base_addr+S3C6410_GPXDAT_OFFSET)),port_bit);
    return 0;
}

int open_port_device(void)//  
{
	unsigned char *gpioports; 
	unsigned char *adccont; 
	
	fdMem = open("/dev/mem",O_RDWR);
	if (fdMem < 0)
	{
		printf("Can't open mem device entry.Error number:%d\n",fdMem);
		return -1;
	}
	
	gpioports = mmap(0,getpagesize(),PROT_READ|PROT_WRITE,MAP_SHARED,fdMem,
		0x7F008000);
	if (gpioports==MAP_FAILED) 
	{
		printf("\n Map failed!\n");
		return -1;
	}
	
  PIOA0_BaseVirt = (unsigned int *) (gpioports + 0x000);   //  	0x7F008000
  PIOB0_BaseVirt = (unsigned int *) (gpioports + 0x020);   //  	0x7F008020
  PIOC0_BaseVirt = (unsigned int *) (gpioports + 0x040);   //  	0x7F008040
  PIOD0_BaseVirt = (unsigned int *) (gpioports + 0x060);   // 	0x7F008060
  PIOE0_BaseVirt = (unsigned int *) (gpioports + 0x080);   // 	0x7F008080
  PIOF0_BaseVirt = (unsigned int *) (gpioports + 0x0A0);   // 	0x7F008100
  PIOG0_BaseVirt = (unsigned int *) (gpioports + 0x0C0);   //  	0x7F0080C0
  PIOH0_BaseVirt = (unsigned int *) (gpioports + 0x0E0);   //  	0x7F0080E0		     
  PIOH1_BaseVirt = (unsigned int *) (gpioports + 0x0E4);   //  	0x7F0080E4
  PIOI0_BaseVirt = (unsigned int *) (gpioports + 0x100);   // 	0x7F008100
  PIOJ0_BaseVirt = (unsigned int *) (gpioports + 0x120);   // 	0x7F008120
  PIOK0_BaseVirt = (unsigned int *) (gpioports + 0x800);   //  	0x7F008800		     
  PIOK1_BaseVirt = (unsigned int *) (gpioports + 0x804);   //  	0x7F008804
  PIOL0_BaseVirt = (unsigned int *) (gpioports + 0x810);   //  	0x7F008810	      
  PIOL1_BaseVirt = (unsigned int *) (gpioports + 0x814);   //	  0x7F008814
  PIOM0_BaseVirt = (unsigned int *) (gpioports + 0x820);   // 	0x7F008820
  PION0_BaseVirt = (unsigned int *) (gpioports + 0x830);   // 	0x7F008830
  PIOO0_BaseVirt = (unsigned int *) (gpioports + 0x100);   //  	0x7F008100
  PIOP0_BaseVirt = (unsigned int *) (gpioports + 0x160);   //  	0x7F008160
  PIOQ0_BaseVirt = (unsigned int *) (gpioports + 0x180);   //  	0x7F008180
  
	adccont = mmap(0xFFF,getpagesize(),PROT_READ|PROT_WRITE,MAP_SHARED,fdMem,
		0x7E00B000);
	if (adccont==MAP_FAILED) 
	{	
		printf("\n ADC Map failed!\n");
		return -1;
	}
	
	ADC_BaseVirt =(unsigned int *) (adccont + 0x000);

	return 0;
}

int close_port_device(void)
{
	close(fdMem);
	
	return 0;
}

/* end of file */