/*****************************************************************/

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

/*****************************************************************/


#define GPA0CON_OFFSET 0x0000
#define GPA1CON_OFFSET 0x0020
#define GPBCON_OFFSET  0x0040
#define GPC0CON_OFFSET 0x0060
#define GPC1CON_OFFSET 0x0080
#define GPD0CON_OFFSET 0x00A0
#define GPD1CON_OFFSET 0x00C0
#define GPE0CON_OFFSET 0x00E0
#define GPE1CON_OFFSET 0x0100
#define GPF0CON_OFFSET 0x0120
#define GPF1CON_OFFSET 0x0140
#define GPF2CON_OFFSET 0x0160
#define GPF3CON_OFFSET 0x0180
#define GPG0CON_OFFSET 0x01A0
#define GPG1CON_OFFSET 0x01C0
#define GPG2CON_OFFSET 0x01E0
#define GPG3CON_OFFSET 0x0200
#define GPICON_OFFSET  0x0220
#define GPJ0CON_OFFSET 0x0240
#define GPJ1CON_OFFSET 0x0260
#define GPJ2CON_OFFSET 0x0280
#define GPJ3CON_OFFSET 0x02A0
#define GPJ4CON_OFFSET 0x02C0

#define GPH2CON_OFFSET 0x0C40
#define GPH3CON_OFFSET 0x0C60

/*****************************************************************/
#define CORTEX_A8_GPA0CON		gpioports+GPA0CON_OFFSET
#define CORTEX_A8_GPA1CON		gpioports+GPA1CON_OFFSET
#define CORTEX_A8_GPBCON		gpioports+GPBCON_OFFSET
#define CORTEX_A8_GPC0CON		gpioports+GPC0CON_OFFSET
#define CORTEX_A8_GPC1CON		gpioports+GPC1CON_OFFSET
#define CORTEX_A8_GPD0CON		gpioports+GPD0CON_OFFSET
#define CORTEX_A8_GPD1CON		gpioports+GPD1CON_OFFSET
#define CORTEX_A8_GPE0CON		gpioports+GPE0CON_OFFSET
#define CORTEX_A8_GPE1CON		gpioports+GPE1CON_OFFSET
#define CORTEX_A8_GPF0CON		gpioports+GPF0CON_OFFSET
#define CORTEX_A8_GPF1CON		gpioports+GPF1CON_OFFSET
#define CORTEX_A8_GPF2CON		gpioports+GPF2CON_OFFSET
#define CORTEX_A8_GPF3CON		gpioports+GPF3CON_OFFSET
#define CORTEX_A8_GPG0CON		gpioports+GPG0CON_OFFSET
#define CORTEX_A8_GPG1CON		gpioports+GPG1CON_OFFSET
#define CORTEX_A8_GPG2CON		gpioports+GPG2CON_OFFSET
#define CORTEX_A8_GPG3CON		gpioports+GPG3CON_OFFSET
#define CORTEX_A8_GPICON		gpioports+GPICON_OFFSET
#define CORTEX_A8_GPJ0CON		gpioports+GPJ0CON_OFFSET
#define CORTEX_A8_GPJ1CON		gpioports+GPJ1CON_OFFSET
#define CORTEX_A8_GPJ2CON		gpioports+GPJ2CON_OFFSET
#define CORTEX_A8_GPJ3CON		gpioports+GPJ3CON_OFFSET

//#define CORTEX_A8_GPH0CON		gpioports+GPH0CON_OFFSET
//#define CORTEX_A8_GPH1CON		gpioports+GPH1CON_OFFSET
#define CORTEX_A8_GPH2CON		gpioports+GPH2CON_OFFSET
#define CORTEX_A8_GPH3CON		gpioports+GPH3CON_OFFSET

/*****************************************************************/
//
//			H3----COL-----KEYA_D
//			H2----ROW-----KEYA_R
//
#define KEYA_R_1 /*S3C6410_PIO_PC7*/			CORTEX_A8_GPH2CON,(0),0,0,0/*S3C6410_BASE_PIOCCON0,(7),0,0,0*/
#define KEYA_R_2 /*S3C6410_PIO_PK3*/			CORTEX_A8_GPH2CON,(1),0,0,0/*S3C6410_BASE_PIOKCON0,(3),0,0,1*/
#define KEYA_R_3 /*S3C6410_PIO_PC6*/			CORTEX_A8_GPH2CON,(2),0,0,0/*S3C6410_BASE_PIOCCON0,(6),0,0,0*/
#define KEYA_R_4 /*S3C6410_PIO_PK0*/			CORTEX_A8_GPH2CON,(3),0,0,0/*S3C6410_BASE_PIOKCON0,(0),0,0,1*/
#define KEYA_D_1 /*S3C6410_PIO_PL13*/			CORTEX_A8_GPH3CON,(0),0,0,0/*S3C6410_BASE_PIOLCON1,(13),1,0,1*/
#define KEYA_D_2 /*S3C6410_PIO_PK5*/			CORTEX_A8_GPH3CON,(1),0,0,0/*S3C6410_BASE_PIOKCON0,(5),0,0,1*/
#define KEYA_D_3 /*S3C6410_PIO_PA7*/			CORTEX_A8_GPH3CON,(2),0,0,0/*S3C6410_BASE_PIOACON0,(7),0,0,0*/
#define KEYA_D_4 /*S3C6410_PIO_PK1*/			CORTEX_A8_GPH3CON,(3),0,0,0/*S3C6410_BASE_PIOKCON0,(1),0,0,1*/


/*****************************************************************/

#define CORTEX_A8_GPXDAT_OFFSET 0x4
#define CORTEX_A8_GPXPUD_OFFSET 0x8

#define CORTEX_A8_BASE_ADC      ((unsigned int)ADC_BaseVirt)
unsigned int *ADC_BaseVirt;    //¶Ë¿ÚADC¿ØÖÆÆ÷µÄÐéÄâ»ùµØÖ·

/*****************************************************************/
#define BELLCTRL		CORTEX_A8_GPD0CON,(0),0,0,0
/*****************************************************************/

#define CAN_SPICLK		CORTEX_A8_GPH0CON,(0),0,0,0//S3C6410_PIO_PC1
#define CAN_SPI_MISO	CORTEX_A8_GPH0CON,(1),0,0,0//S3C6410_PIO_PC0
#define CAN_SPI_MOSI	CORTEX_A8_GPH0CON,(2),0,0,0//S3C6410_PIO_PC2
#define CAN_SPI_CS		CORTEX_A8_GPH0CON,(5),0,0,0//S3C6410_PIO_PC3
#define CAN_INT			CORTEX_A8_GPH0CON,(6),0,0,0//S3C2440_PIO_PF0

/*****************************************************************/

#define I2C_SCL CORTEX_A8_GPD1CON,(1),0,0,0// /*S3C2440_PIO_PE13*/S3C6410_PIO_PA6	//RTCÊ±ÖÓÏß
#define I2C_SDA CORTEX_A8_GPD1CON,(0),0,0,0// /*S3C2440_PIO_PJ6*/S3C6410_PIO_PC4	//RTCÊýŸÝÏß

/*****************************************************************/

unsigned int  fdMem;
unsigned char *gpioports; 

void Delay(unsigned int time)
{
 unsigned int i,j;
 for(i=0;i<time;i++)
  {
    for( j=0;j<100;j++)
      {
	 ;
     }
  }
}


void mmdelay(unsigned int ms)
{
	unsigned int i, j;
	for (i = 0; i < ms; i++)
		for (j = 0; j < 3000; j++);
}

void uudelay(unsigned int us)
{
	unsigned int i, j;
	for (i = 0; i < us; i++)
		for (j = 0; j < 170; j++);
}


int port_read(  unsigned int pio_port_base_addr,
			  unsigned int port_bit,
			  unsigned int con_state,
			  unsigned int con_bit,            //CONµÄ¶àÉÙÎ»¶ÔÓŠÒ»žöœÅ 0Îª4Î»,1Îª2Î»
			  unsigned int con_num)           //ÓÐŒžžö¿ØÖÆŒÄŽæÆ÷£¬0Îª1žö£¬1Îª2žö          
{ 
	unsigned int val;
	
	// ¶ÔÓŠµÄÄÇÁœÎ»ÅäÖÃÎª0000£¬INPUT
	if(con_bit)
	{
		(*(volatile unsigned int *)(pio_port_base_addr)) = ((*(volatile unsigned int *)(pio_port_base_addr)) & ~(0x0f<<(port_bit*2)))|(0x00<<(port_bit*2));
	}
	else
	{
		(*(volatile unsigned int *)(pio_port_base_addr)) = ((*(volatile unsigned int *)(pio_port_base_addr)) & ~(0x000f<<((port_bit-con_state*8)*4)))|(0x0000<<((port_bit-con_state*8)*4));
	}
	
	//00 = pull-up/down disabled   01 = pull-down enabled  10 = pull-up enabled  11 = Reserved.
	(*(volatile unsigned int *)(pio_port_base_addr+CORTEX_A8_GPXPUD_OFFSET+(con_num-con_state)*4))  = ((*(volatile unsigned int *)(pio_port_base_addr+CORTEX_A8_GPXPUD_OFFSET+(con_num-con_state)*4))  & ~(0x0f<< (port_bit*2)));
	val = (*(volatile unsigned int *)(pio_port_base_addr+CORTEX_A8_GPXDAT_OFFSET+(con_num-con_state)*4)) & (0x0001<<port_bit);
	
	//	printf("CON:0x%x ,DAT:0x%x,port:%d\r\n",(*(volatile unsigned int *)(pio_port_base_addr)),(*(volatile unsigned int *)(pio_port_base_addr+S3C6410_GPXDAT_OFFSET)),port_bit);
	
    return (val ? 1 : 0);
}


int port_write(unsigned int pio_port_base_addr,// PIO»ùµØÖ·			
			   unsigned int port_bit,          // Ä³Î»
			   unsigned int con_state,
			   unsigned int con_bit,            //CONµÄ¶àÉÙÎ»¶ÔÓŠÒ»žöœÅ 0Îª4Î»,1Îª2Î»
               unsigned int con_num,            //ÓÐŒžžö¿ØÖÆŒÄŽæÆ÷£¬0Îª1žö£¬1Îª2žö
			   int value)                      // Öµ:0,1
{
	// 0001£¬OUTPUT»ò01 OUTPUT
/* CON */
	if(con_bit)
		{
			(*(volatile unsigned int *)(pio_port_base_addr)) = ((*(volatile unsigned int *)(pio_port_base_addr)) & ~(0x0f<<(port_bit*2)))|(0x01<<(port_bit*2));
		}
	else
		{
			(*(volatile unsigned int *)(pio_port_base_addr)) = ((*(volatile unsigned int *)(pio_port_base_addr)) & ~(0x000f<<((port_bit-con_state*8)*4)))|(0x0001<<((port_bit-con_state*8)*4));
		}
	
	//00 = pull-up/down disabled   01 = pull-down enabled  10 = pull-up enabled  11 = Reserved.

/* PUD */
	(*(volatile unsigned int *)(pio_port_base_addr+CORTEX_A8_GPXPUD_OFFSET+(con_num-con_state)*4))  = ((*(volatile unsigned int *)(pio_port_base_addr+CORTEX_A8_GPXPUD_OFFSET+(con_num-con_state)*4))  & ~(0x0f<< (port_bit*2)));

/* DAT */
	if(value)
	{	/* Set Output */
		(*(volatile unsigned int *)(pio_port_base_addr+CORTEX_A8_GPXDAT_OFFSET+(con_num-con_state)*4)) |= (0x0001<<port_bit);
	}
	else
	{
		/* Clear Output */
		(*(volatile unsigned int *)(pio_port_base_addr+CORTEX_A8_GPXDAT_OFFSET+(con_num-con_state)*4)) &= ~(0x0001<<port_bit);
	}
	
//	printf("CON:0x%x ,DAT:0x%x,port:%d\r\n",(*(volatile unsigned int *)(pio_port_base_addr)),(*(volatile unsigned int *)(pio_port_base_addr+S3C6410_GPXDAT_OFFSET)),port_bit);
    return 0;
}

int open_port_device(void)//  
{
	
	fdMem = open("/dev/mem",O_RDWR);
	if (fdMem < 0)
	{
		printf("Can't open mem device entry.Error number:%d\n",fdMem);
		return -1;
	}
	
	gpioports = mmap(0,getpagesize(),PROT_READ|PROT_WRITE,MAP_SHARED,fdMem,
		0xE0200000);
	if (gpioports==MAP_FAILED) 
	{
		printf("\n Map failed!\n");
		return -1;
	}

/*

  PIOA0_BaseVirt = (unsigned int *) (gpioports + 0x000);   //  	0x7F008000
  PIOB0_BaseVirt = (unsigned int *) (gpioports + 0x020);   //  	0x7F008020
  PIOC0_BaseVirt = (unsigned int *) (gpioports + 0x040);   //  	0x7F008040
  PIOD0_BaseVirt = (unsigned int *) (gpioports + 0x060);   // 	0x7F008060
  PIOE0_BaseVirt = (unsigned int *) (gpioports + 0x080);   // 	0x7F008080
  PIOF0_BaseVirt = (unsigned int *) (gpioports + 0x0A0);   // 	0x7F0080A0
  PIOG0_BaseVirt = (unsigned int *) (gpioports + 0x0C0);   //  	0x7F0080C0
  PIOH0_BaseVirt = (unsigned int *) (gpioports + 0x0E0);   //  	0x7F0080E0		     
  PIOH1_BaseVirt = (unsigned int *) (gpioports + 0x0E4);   //  	0x7F0080E4
  PIOI0_BaseVirt = (unsigned int *) (gpioports + 0x100);   // 	0x7F008100
  PIOJ0_BaseVirt = (unsigned int *) (gpioports + 0x120);   // 	0x7F008120
  PIOK0_BaseVirt = (unsigned int *) (gpioports + 0x800);   //  	0x7F008800		     
  PIOK1_BaseVirt = (unsigned int *) (gpioports + 0x804);   //  	0x7F008804
  PIOL0_BaseVirt = (unsigned int *) (gpioports + 0x810);   //  	0x7F008810	      
  PIOL1_BaseVirt = (unsigned int *) (gpioports + 0x814);   //	0x7F008814
  PIOM0_BaseVirt = (unsigned int *) (gpioports + 0x820);   // 	0x7F008820
  PION0_BaseVirt = (unsigned int *) (gpioports + 0x830);   // 	0x7F008830
  PIOO0_BaseVirt = (unsigned int *) (gpioports + 0x100);   //  	0x7F008100
  PIOP0_BaseVirt = (unsigned int *) (gpioports + 0x160);   //  	0x7F008160
  PIOQ0_BaseVirt = (unsigned int *) (gpioports + 0x180);   //  	0x7F008180
  
*/
/*
	adccont = mmap(0xFFF,getpagesize(),PROT_READ|PROT_WRITE,MAP_SHARED,fdMem,
		0x7E00B000);
	if (adccont==MAP_FAILED) 
	{	
		printf("\n ADC Map failed!\n");
		return -1;
	}
	
	ADC_BaseVirt =(unsigned int *) (adccont + 0x000);
*/
	return 0;
}


int bsp_init(void)
{
	open_port_device();

	return 0;
}


int close_port_device(void)
{
	close(fdMem);
	
	return 0;
}

/* end of file */
