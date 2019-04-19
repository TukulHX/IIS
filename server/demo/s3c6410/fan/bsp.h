/*
 *=============================================================================
  bsp.h 
 *
 	2011/02/14
 *
 *=============================================================================
 */


#ifndef _BSP_H
#define _BSP_H


typedef volatile unsigned char AT91_P8;


void mmdelay(unsigned int ms);
void uudelay(unsigned int us);


/* define bsp return value */

#define BSP_OK	0

#define BSP_ERR_HARDWARE_VERSION	0x01
#define BSP_ERR_PARAMETER_INVALID	0x02
#define BSP_ERR_DEV_NOT_PRESENT	0x03
#define BSP_ERR_DEBUG_MODE		0x04

#define BSP_ERR_MEM_MALLOC		0x10
#define BSP_ERR_MEM_CALLOC	 	0x11
#define BSP_ERR_MEM_DMA_MALLOC	0x12
#define BSP_ERR_SEMBCREATE			0x13

#define BSP_ERR_OPEN_FILE			0x20
#define BSP_ERR_READ_FILE			0x21
#define BSP_ERR_WRITE_FILE			0x22

#define BSP_HDLC_NOT_INIT			0x30
#define BSP_HDLC_NO_BD				0x31
#define BSP_HDLC_MAX_LENGTH 		0x32
#define BSP_HDLC_MIN_LENGTH 		0x33

#define BSP_ERR_FPGA_INIT_HIGH	0x36
#define BSP_ERR_FPGA_INIT_LOW		0x37
#define BSP_ERR_FPGA_DONE_LOW	0x38

#define BSP_ERR_FLASH_ERASE		0x40
#define BSP_ERR_FLASH_WRITE		0x41
#define BSP_ERR_FLASH_FORMAT		0x42

#define BSP_ERR_CLK_NOT_LOCK		0x45
#define BSP_ERR_INFLATE_FAIL			0x46

// *****************************************************************************
//               GPIO
// *****************************************************************************

extern unsigned int *ADC_BaseVirt;
      
extern unsigned int *PIOA0_BaseVirt;  //  	0x7F008000
extern unsigned int *PIOB0_BaseVirt;  //  	0x7F008020
extern unsigned int *PIOC0_BaseVirt;  //  	0x7F008040
extern unsigned int *PIOD0_BaseVirt;  // 	  0x7F008060
extern unsigned int *PIOE0_BaseVirt;  // 	  0x7F008080
extern unsigned int *PIOF0_BaseVirt;  // 	  0x7F0080A0
extern unsigned int *PIOG0_BaseVirt;  //  	0x7F0080C0
extern unsigned int *PIOH0_BaseVirt;  //  	0x7F0080E0		     
extern unsigned int *PIOH1_BaseVirt;  //  	0x7F0080E4
extern unsigned int *PIOI0_BaseVirt;  // 	  0x7F008100
extern unsigned int *PIOJ0_BaseVirt;  // 	  0x7F008120
extern unsigned int *PIOK0_BaseVirt;  //  	0x7F008800		     
extern unsigned int *PIOK1_BaseVirt;  //  	0x7F008804
extern unsigned int *PIOL0_BaseVirt;  //	  0x7F008810	      
extern unsigned int *PIOL1_BaseVirt;  //	  0x7F008814
extern unsigned int *PIOM0_BaseVirt;  // 	  0x7F008820		
extern unsigned int *PION0_BaseVirt;  // 	  0x7F008830
extern unsigned int *PIOO0_BaseVirt;  //  	0x7F008100
extern unsigned int *PIOP0_BaseVirt;  //  	0x7F008160
extern unsigned int *PIOQ0_BaseVirt;  //  	0x7F008180

/* K L M */
#define S3C6410_BASE_PIOACON0      ((unsigned int)PIOA0_BaseVirt) //0x7F008000
#define S3C6410_BASE_PIOBCON0      ((unsigned int)PIOB0_BaseVirt) //0x7F008020
#define S3C6410_BASE_PIOCCON0      ((unsigned int)PIOC0_BaseVirt) //0x7F008040
#define S3C6410_BASE_PIODCON0      ((unsigned int)PIOD0_BaseVirt) //0x7F008060
#define S3C6410_BASE_PIOECON0      ((unsigned int)PIOE0_BaseVirt) //0x7F008080
#define S3C6410_BASE_PIOFCON0      ((unsigned int)PIOF0_BaseVirt) //0x7F0080A0
#define S3C6410_BASE_PIOGCON0      ((unsigned int)PIOG0_BaseVirt) //0x7F0080C0
#define S3C6410_BASE_PIOHCON0      ((unsigned int)PIOH0_BaseVirt) //0x7F0080E0 
#define S3C6410_BASE_PIOHCON1      ((unsigned int)PIOH1_BaseVirt) //0x7F0080E4
#define S3C6410_BASE_PIOICON0      ((unsigned int)PIOI0_BaseVirt) //0x7F008100
#define S3C6410_BASE_PIOJCON0      ((unsigned int)PIOJ0_BaseVirt) //0x7F008120
#define S3C6410_BASE_PIOKCON0      ((unsigned int)PIOK0_BaseVirt) //0x7F008800 
#define S3C6410_BASE_PIOKCON1      ((unsigned int)PIOK1_BaseVirt) //0x7F008804
#define S3C6410_BASE_PIOLCON0      ((unsigned int)PIOL0_BaseVirt) //0x7F008810
#define S3C6410_BASE_PIOLCON1      ((unsigned int)PIOL1_BaseVirt) //0x7F008814
#define S3C6410_BASE_PIOMCON0      ((unsigned int)PIOM0_BaseVirt) //0x7F008820
#define S3C6410_BASE_PIONCON0      ((unsigned int)PION0_BaseVirt) //0x7F008830
#define S3C6410_BASE_PIOOCON0      ((unsigned int)PIOO0_BaseVirt) //0x7F008100
#define S3C6410_BASE_PIOPCON0      ((unsigned int)PIOP0_BaseVirt) //0x7F008160
#define S3C6410_BASE_PIOQCON0      ((unsigned int)PIOQ0_BaseVirt) //0x7F008180

#define S3C6410_BASE_ADC      ((unsigned int)ADC_BaseVirt)

/* 端口控制寄存器相对于基地址的偏移地址 */
#define PIO_PER_OFFSET  0x00    // PER相对基地址的偏移
#define PIO_OER_OFFSET  0x10
#define PIO_ODR_OFFSET  0x14
#define PIO_CODR_OFFSET 0x34
#define PIO_SODR_OFFSET 0x30
#define PIO_PDSR_OFFSET 0x3C
#define PIO_PUER_OFFSET	0x64

#define PIO_PDR_OFFSET		0x04	/* Disable Register */
#define PIO_ASR_OFFSET		0x70	/* Peripheral A Select Register */
#define PIO_BSR_OFFSET		0x74	/* Peripheral B Select Register */


#define AT91_US_CSR		0x14				  // Channel Status Register 
#define AT91_US_MR		0x04				  // Mode Register           


#define S3C6410_GPXCON0_OFFSET 0x0
#define S3C6410_GPXDAT_OFFSET 0x4
#define S3C6410_GPXPUD_OFFSET 0x8


// *****************************************************************************
//               PIO DEFINITIONS FOR S3C6410
// *****************************************************************************
#define S3C6410_PIO_PA0        S3C6410_BASE_PIOACON0,(0),0,0,0  // Pin Controlled by PORTA
#define S3C6410_PIO_PA1        S3C6410_BASE_PIOACON0,(1),0,0,0  // Pin Controlled by PORTA
#define S3C6410_PIO_PA2        S3C6410_BASE_PIOACON0,(2),0,0,0  // Pin Controlled by PORTA
#define S3C6410_PIO_PA3        S3C6410_BASE_PIOACON0,(3),0,0,0  // Pin Controlled by PORTA
#define S3C6410_PIO_PA4        S3C6410_BASE_PIOACON0,(4),0,0,0  // Pin Controlled by PORTA
#define S3C6410_PIO_PA5        S3C6410_BASE_PIOACON0,(5),0,0,0  // Pin Controlled by PORTA
#define S3C6410_PIO_PA6        S3C6410_BASE_PIOACON0,(6),0,0,0  // Pin Controlled by PORTA
#define S3C6410_PIO_PA7        S3C6410_BASE_PIOACON0,(7),0,0,0  // Pin Controlled by PORTA


#define S3C6410_PIO_PB0        S3C6410_BASE_PIOBCON0,(0),0,0,0  // Pin Controlled by PORTB
#define S3C6410_PIO_PB1        S3C6410_BASE_PIOBCON0,(1),0,0,0  // Pin Controlled by PORTB
#define S3C6410_PIO_PB2        S3C6410_BASE_PIOBCON0,(2),0,0,0  // Pin Controlled by PORTB
#define S3C6410_PIO_PB3        S3C6410_BASE_PIOBCON0,(3),0,0,0  // Pin Controlled by PORTB
#define S3C6410_PIO_PB4        S3C6410_BASE_PIOBCON0,(4),0,0,0  // Pin Controlled by PORTB
#define S3C6410_PIO_PB5        S3C6410_BASE_PIOBCON0,(5),0,0,0  // Pin Controlled by PORTB
#define S3C6410_PIO_PB6        S3C6410_BASE_PIOBCON0,(6),0,0,0  // Pin Controlled by PORTB


#define S3C6410_PIO_PC0        S3C6410_BASE_PIOCCON0,(0),0,0,0  // Pin Controlled by PORTC
#define S3C6410_PIO_PC1        S3C6410_BASE_PIOCCON0,(1),0,0,0  // Pin Controlled by PORTC
#define S3C6410_PIO_PC2        S3C6410_BASE_PIOCCON0,(2),0,0,0  // Pin Controlled by PORTC
#define S3C6410_PIO_PC3        S3C6410_BASE_PIOCCON0,(3),0,0,0  // Pin Controlled by PORTC
#define S3C6410_PIO_PC4        S3C6410_BASE_PIOCCON0,(4),0,0,0  // Pin Controlled by PORTC
#define S3C6410_PIO_PC5        S3C6410_BASE_PIOCCON0,(5),0,0,0  // Pin Controlled by PORTC
#define S3C6410_PIO_PC6        S3C6410_BASE_PIOCCON0,(6),0,0,0  // Pin Controlled by PORTC
#define S3C6410_PIO_PC7        S3C6410_BASE_PIOCCON0,(7),0,0,0  // Pin Controlled by PORTC


#define S3C6410_PIO_PD0        S3C6410_BASE_PIODCON0,(0),0,0,0  // Pin Controlled by PORTD
#define S3C6410_PIO_PD1        S3C6410_BASE_PIODCON0,(1),0,0,0  // Pin Controlled by PORTD
#define S3C6410_PIO_PD2        S3C6410_BASE_PIODCON0,(2),0,0,0  // Pin Controlled by PORTD
#define S3C6410_PIO_PD3        S3C6410_BASE_PIODCON0,(3),0,0,0  // Pin Controlled by PORTD
#define S3C6410_PIO_PD4        S3C6410_BASE_PIODCON0,(4),0,0,0  // Pin Controlled by PORTD


#define S3C6410_PIO_PE0        S3C6410_BASE_PIOECON0,(0),0,0,0  // Pin Controlled by PORTE
#define S3C6410_PIO_PE1        S3C6410_BASE_PIOECON0,(1),0,0,0  // Pin Controlled by PORTE
#define S3C6410_PIO_PE2        S3C6410_BASE_PIOECON0,(2),0,0,0  // Pin Controlled by PORTE
#define S3C6410_PIO_PE3        S3C6410_BASE_PIOECON0,(3),0,0,0  // Pin Controlled by PORTE
#define S3C6410_PIO_PE4        S3C6410_BASE_PIOECON0,(4),0,0,0  // Pin Controlled by PORTE

#define S3C6410_PIO_PF0        S3C6410_BASE_PIOFCON0,(0),0,1,0  // Pin ControFFed by PORTF
#define S3C6410_PIO_PF1        S3C6410_BASE_PIOFCON0,(1),0,1,0  // Pin ControFFed by PORTF
#define S3C6410_PIO_PF2        S3C6410_BASE_PIOFCON0,(2),0,1,0  // Pin ControFFed by PORTF
#define S3C6410_PIO_PF3        S3C6410_BASE_PIOFCON0,(3),0,1,0  // Pin ControFFed by PORTF
#define S3C6410_PIO_PF4        S3C6410_BASE_PIOFCON0,(4),0,1,0  // Pin ControFFed by PORTF
#define S3C6410_PIO_PF5        S3C6410_BASE_PIOFCON0,(5),0,1,0  // Pin ControFFed by PORTF
#define S3C6410_PIO_PF6        S3C6410_BASE_PIOFCON0,(6),0,1,0  // Pin ControFFed by PORTF
#define S3C6410_PIO_PF7        S3C6410_BASE_PIOFCON0,(7),0,1,0  // Pin ControFFed by PORTF

#define S3C6410_PIO_PF8        S3C6410_BASE_PIOFCON0,(8),0,1,0  // Pin ControFFed by PORTF
#define S3C6410_PIO_PF9        S3C6410_BASE_PIOFCON0,(9),0,1,0  // Pin ControFFed by PORTF
#define S3C6410_PIO_PF10       S3C6410_BASE_PIOFCON0,(10),0,1,0 // Pin ControFFed by PORTF
#define S3C6410_PIO_PF11       S3C6410_BASE_PIOFCON0,(11),0,1,0 // Pin ControFFed by PORTF
#define S3C6410_PIO_PF12       S3C6410_BASE_PIOFCON0,(12),0,1,0 // Pin ControFFed by PORTF
#define S3C6410_PIO_PF13       S3C6410_BASE_PIOFCON0,(13),0,1,0 // Pin ControFFed by PORTF
#define S3C6410_PIO_PF14       S3C6410_BASE_PIOFCON0,(14),0,1,0 // Pin ControFFed by PORTF
#define S3C6410_PIO_PF15       S3C6410_BASE_PIOFCON0,(15),0,1,0 // Pin ControFFed by PORTF

#define S3C6410_PIO_PG0        S3C6410_BASE_PIOGCON0,(0),0,0,0  // Pin Controlled by PORTG
#define S3C6410_PIO_PG1        S3C6410_BASE_PIOGCON0,(1),0,0,0  // Pin Controlled by PORTG
#define S3C6410_PIO_PG2        S3C6410_BASE_PIOGCON0,(2),0,0,0  // Pin Controlled by PORTG
#define S3C6410_PIO_PG3        S3C6410_BASE_PIOGCON0,(3),0,0,0  // Pin Controlled by PORTG
#define S3C6410_PIO_PG4        S3C6410_BASE_PIOGCON0,(4),0,0,0  // Pin Controlled by PORTG
#define S3C6410_PIO_PG5        S3C6410_BASE_PIOGCON0,(5),0,0,0  // Pin Controlled by PORTG
#define S3C6410_PIO_PG6        S3C6410_BASE_PIOGCON0,(6),0,0,0  // Pin Controlled by PORTG

#define S3C6410_PIO_PH0        S3C6410_BASE_PIOHCON0,(0),0,0,1  // Pin Controlled by PORTH
#define S3C6410_PIO_PH1        S3C6410_BASE_PIOHCON0,(1),0,0,1  // Pin Controlled by PORTH
#define S3C6410_PIO_PH2        S3C6410_BASE_PIOHCON0,(2),0,0,1  // Pin Controlled by PORTH
#define S3C6410_PIO_PH3        S3C6410_BASE_PIOHCON0,(3),0,0,1  // Pin Controlled by PORTH
#define S3C6410_PIO_PH4        S3C6410_BASE_PIOHCON0,(4),0,0,1  // Pin Controlled by PORTH
#define S3C6410_PIO_PH5        S3C6410_BASE_PIOHCON0,(5),0,0,1  // Pin Controlled by PORTH
#define S3C6410_PIO_PH6        S3C6410_BASE_PIOHCON0,(6),0,0,1  // Pin Controlled by PORTH
#define S3C6410_PIO_PH7        S3C6410_BASE_PIOHCON0,(7),0,0,1  // Pin Controlled by PORTH

#define S3C6410_PIO_PH8        S3C6410_BASE_PIOHCON1,(8),1,0,1  // Pin Controlled by PORTH
#define S3C6410_PIO_PH9        S3C6410_BASE_PIOHCON1,(9),1,0,1  // Pin Controlled by PORTH


#define S3C6410_PIO_PI0        S3C6410_BASE_PIOICON0,(0),0,1,0  // Pin ControIIed by PORTI
#define S3C6410_PIO_PI1        S3C6410_BASE_PIOICON0,(1),0,1,0  // Pin ControIIed by PORTI
#define S3C6410_PIO_PI2        S3C6410_BASE_PIOICON0,(2),0,1,0  // Pin ControIIed by PORTI
#define S3C6410_PIO_PI3        S3C6410_BASE_PIOICON0,(3),0,1,0  // Pin ControIIed by PORTI
#define S3C6410_PIO_PI4        S3C6410_BASE_PIOICON0,(4),0,1,0  // Pin ControIIed by PORTI
#define S3C6410_PIO_PI5        S3C6410_BASE_PIOICON0,(5),0,1,0  // Pin ControIIed by PORTI
#define S3C6410_PIO_PI6        S3C6410_BASE_PIOICON0,(6),0,1,0  // Pin ControIIed by PORTI
#define S3C6410_PIO_PI7        S3C6410_BASE_PIOICON0,(7),0,1,0  // Pin ControIIed by PORTI

#define S3C6410_PIO_PI8        S3C6410_BASE_PIOICON0,(8),0,1,0  // Pin ControIIed by PORTI
#define S3C6410_PIO_PI9        S3C6410_BASE_PIOICON0,(9),0,1,0  // Pin ControIIed by PORTI
#define S3C6410_PIO_PI10       S3C6410_BASE_PIOICON0,(10),0,1,0 // Pin ControIIed by PORTI
#define S3C6410_PIO_PI11       S3C6410_BASE_PIOICON0,(11),0,1,0 // Pin ControIIed by PORTI
#define S3C6410_PIO_PI12       S3C6410_BASE_PIOICON0,(12),0,1,0 // Pin ControIIed by PORTI
#define S3C6410_PIO_PI13       S3C6410_BASE_PIOICON0,(13),0,1,0 // Pin ControIIed by PORTI
#define S3C6410_PIO_PI14       S3C6410_BASE_PIOICON0,(14),0,1,0 // Pin ControIIed by PORTI
#define S3C6410_PIO_PI15       S3C6410_BASE_PIOICON0,(15),0,1,0 // Pin ControIIed by PORTI

#define S3C6410_PIO_PJ0        S3C6410_BASE_PIOJCON0,(0),0,1,0  // Pin ControJJed by PORTJ
#define S3C6410_PIO_PJ1        S3C6410_BASE_PIOJCON0,(1),0,1,0  // Pin ControJJed by PORTJ
#define S3C6410_PIO_PJ2        S3C6410_BASE_PIOJCON0,(2),0,1,0  // Pin ControJJed by PORTJ
#define S3C6410_PIO_PJ3        S3C6410_BASE_PIOJCON0,(3),0,1,0  // Pin ControJJed by PORTJ
#define S3C6410_PIO_PJ4        S3C6410_BASE_PIOJCON0,(4),0,1,0  // Pin ControJJed by PORTJ
#define S3C6410_PIO_PJ5        S3C6410_BASE_PIOJCON0,(5),0,1,0  // Pin ControJJed by PORTJ
#define S3C6410_PIO_PJ6        S3C6410_BASE_PIOJCON0,(6),0,1,0  // Pin ControJJed by PORTJ
#define S3C6410_PIO_PJ7        S3C6410_BASE_PIOJCON0,(7),0,1,0  // Pin ControJJed by PORTJ

#define S3C6410_PIO_PJ8        S3C6410_BASE_PIOJCON0,(8),0,1,0  // Pin ControJJed by PORTJ
#define S3C6410_PIO_PJ9        S3C6410_BASE_PIOJCON0,(9),0,1,0  // Pin ControJJed by PORTJ
#define S3C6410_PIO_PJ10       S3C6410_BASE_PIOJCON0,(10),0,1,0 // Pin ControJJed by PORTJ
#define S3C6410_PIO_PJ11       S3C6410_BASE_PIOJCON0,(11),0,1,0 // Pin ControJJed by PORTJ

#define S3C6410_PIO_PK0        S3C6410_BASE_PIOKCON0,(0),0,0,1  // Pin Controlled by PORTK
#define S3C6410_PIO_PK1        S3C6410_BASE_PIOKCON0,(1),0,0,1  // Pin Controlled by PORTK
#define S3C6410_PIO_PK2        S3C6410_BASE_PIOKCON0,(2),0,0,1  // Pin Controlled by PORTK
#define S3C6410_PIO_PK3        S3C6410_BASE_PIOKCON0,(3),0,0,1  // Pin Controlled by PORTK
#define S3C6410_PIO_PK4        S3C6410_BASE_PIOKCON0,(4),0,0,1  // Pin Controlled by PORTK
#define S3C6410_PIO_PK5        S3C6410_BASE_PIOKCON0,(5),0,0,1  // Pin Controlled by PORTK
#define S3C6410_PIO_PK6        S3C6410_BASE_PIOKCON0,(6),0,0,1  // Pin Controlled by PORTK
#define S3C6410_PIO_PK7        S3C6410_BASE_PIOKCON0,(7),0,0,1  // Pin Controlled by PORTK

#define S3C6410_PIO_PK8        S3C6410_BASE_PIOKCON1,(8),1,0,1  // Pin Controlled by PORTK
#define S3C6410_PIO_PK9        S3C6410_BASE_PIOKCON1,(9),1,0,1  // Pin Controlled by PORTK
#define S3C6410_PIO_PK10       S3C6410_BASE_PIOKCON1,(10),1,0,1 // Pin Controlled by PORTK
#define S3C6410_PIO_PK11       S3C6410_BASE_PIOKCON1,(11),1,0,1 // Pin Controlled by PORTK
#define S3C6410_PIO_PK12       S3C6410_BASE_PIOKCON1,(12),1,0,1 // Pin Controlled by PORTK
#define S3C6410_PIO_PK13       S3C6410_BASE_PIOKCON1,(13),1,0,1 // Pin Controlled by PORTK
#define S3C6410_PIO_PK14       S3C6410_BASE_PIOKCON1,(14),1,0,1 // Pin Controlled by PORTK
#define S3C6410_PIO_PK15       S3C6410_BASE_PIOKCON1,(15),1,0,1 // Pin Controlled by PORTK

#define S3C6410_PIO_PL0        S3C6410_BASE_PIOLCON0,(0),0,0,1  // Pin Controlled by PORTL
#define S3C6410_PIO_PL1        S3C6410_BASE_PIOLCON0,(1),0,0,1  // Pin Controlled by PORTL
#define S3C6410_PIO_PL2        S3C6410_BASE_PIOLCON0,(2),0,0,1  // Pin Controlled by PORTL
#define S3C6410_PIO_PL3        S3C6410_BASE_PIOLCON0,(3),0,0,1  // Pin Controlled by PORTL
#define S3C6410_PIO_PL4        S3C6410_BASE_PIOLCON0,(4),0,0,1  // Pin Controlled by PORTL
#define S3C6410_PIO_PL5        S3C6410_BASE_PIOLCON0,(5),0,0,1  // Pin Controlled by PORTL
#define S3C6410_PIO_PL6        S3C6410_BASE_PIOLCON0,(6),0,0,1  // Pin Controlled by PORTL
#define S3C6410_PIO_PL7        S3C6410_BASE_PIOLCON0,(7),0,0,1  // Pin Controlled by PORTL

#define S3C6410_PIO_PL8        S3C6410_BASE_PIOLCON1,(8),1,0,1  // Pin Controlled by PORTL
#define S3C6410_PIO_PL9        S3C6410_BASE_PIOLCON1,(9),1,0,1  // Pin Controlled by PORTL
#define S3C6410_PIO_PL10       S3C6410_BASE_PIOLCON1,(10),1,0,1 // Pin Controlled by PORTL
#define S3C6410_PIO_PL11       S3C6410_BASE_PIOLCON1,(11),1,0,1 // Pin Controlled by PORTL
#define S3C6410_PIO_PL12       S3C6410_BASE_PIOLCON1,(12),1,0,1 // Pin Controlled by PORTL
#define S3C6410_PIO_PL13       S3C6410_BASE_PIOLCON1,(13),1,0,1 // Pin Controlled by PORTL
#define S3C6410_PIO_PL14       S3C6410_BASE_PIOLCON1,(14),1,0,1 // Pin Controlled by PORTL

#define S3C6410_PIO_PM0        S3C6410_BASE_PIOMCON0,(0),0,0,0  // Pin ControMMed by PORTM
#define S3C6410_PIO_PM1        S3C6410_BASE_PIOMCON0,(1),0,0,0  // Pin ControMMed by PORTM
#define S3C6410_PIO_PM2        S3C6410_BASE_PIOMCON0,(2),0,0,0  // Pin ControMMed by PORTM
#define S3C6410_PIO_PM3        S3C6410_BASE_PIOMCON0,(3),0,0,0  // Pin ControMMed by PORTM
#define S3C6410_PIO_PM4        S3C6410_BASE_PIOMCON0,(4),0,0,0  // Pin ControMMed by PORTM
#define S3C6410_PIO_PM5        S3C6410_BASE_PIOMCON0,(5),0,0,0  // Pin ControMMed by PORTM

#define S3C6410_PIO_PN0        S3C6410_BASE_PIONCON0,(0),0,1,0  // Pin Controlled by PORTN
#define S3C6410_PIO_PN1        S3C6410_BASE_PIONCON0,(1),0,1,0  // Pin Controlled by PORTN
#define S3C6410_PIO_PN2        S3C6410_BASE_PIONCON0,(2),0,1,0  // Pin Controlled by PORTN
#define S3C6410_PIO_PN3        S3C6410_BASE_PIONCON0,(3),0,1,0  // Pin Controlled by PORTN
#define S3C6410_PIO_PN4        S3C6410_BASE_PIONCON0,(4),0,1,0  // Pin Controlled by PORTN
#define S3C6410_PIO_PN5        S3C6410_BASE_PIONCON0,(5),0,1,0  // Pin Controlled by PORTN
#define S3C6410_PIO_PN6        S3C6410_BASE_PIONCON0,(6),0,1,0  // Pin Controlled by PORTN
#define S3C6410_PIO_PN7        S3C6410_BASE_PIONCON0,(7),0,1,0  // Pin Controlled by PORTN

#define S3C6410_PIO_PN8        S3C6410_BASE_PIONCON0,(8),0,1,0  // Pin Controlled by PORTN
#define S3C6410_PIO_PN9        S3C6410_BASE_PIONCON0,(9),0,1,0  // Pin Controlled by PORTN
#define S3C6410_PIO_PN10       S3C6410_BASE_PIONCON0,(10),0,1,0 // Pin Controlled by PORTN
#define S3C6410_PIO_PN11       S3C6410_BASE_PIONCON0,(11),0,1,0 // Pin Controlled by PORTN
#define S3C6410_PIO_PN12       S3C6410_BASE_PIONCON0,(12),0,1,0 // Pin Controlled by PORTN
#define S3C6410_PIO_PN13       S3C6410_BASE_PIONCON0,(13),0,1,0 // Pin Controlled by PORTN
#define S3C6410_PIO_PN14       S3C6410_BASE_PIONCON0,(14),0,1,0 // Pin Controlled by PORTN
#define S3C6410_PIO_PN15       S3C6410_BASE_PIONCON0,(15),0,1,0 // Pin Controlled by PORTN

#define S3C6410_PIO_PO0        S3C6410_BASE_PIOOCON0,(0),0,1,0  // Pin Controlled by PORTO
#define S3C6410_PIO_PO1        S3C6410_BASE_PIOOCON0,(1),0,1,0  // Pin Controlled by PORTO
#define S3C6410_PIO_PO2        S3C6410_BASE_PIOOCON0,(2),0,1,0  // Pin Controlled by PORTO
#define S3C6410_PIO_PO3        S3C6410_BASE_PIOOCON0,(3),0,1,0  // Pin Controlled by PORTO
#define S3C6410_PIO_PO4        S3C6410_BASE_PIOOCON0,(4),0,1,0  // Pin Controlled by PORTO
#define S3C6410_PIO_PO5        S3C6410_BASE_PIOOCON0,(5),0,1,0  // Pin Controlled by PORTO
#define S3C6410_PIO_PO6        S3C6410_BASE_PIOOCON0,(6),0,1,0  // Pin Controlled by PORTO
#define S3C6410_PIO_PO7        S3C6410_BASE_PIOOCON0,(7),0,1,0  // Pin Controlled by PORTO

#define S3C6410_PIO_PO8        S3C6410_BASE_PIOOCON0,(8),0,1,0  // Pin Controlled by PORTO
#define S3C6410_PIO_PO9        S3C6410_BASE_PIOOCON0,(9),0,1,0  // Pin Controlled by PORTO
#define S3C6410_PIO_PO10       S3C6410_BASE_PIOOCON0,(10),0,1,0 // Pin Controlled by PORTO
#define S3C6410_PIO_PO11       S3C6410_BASE_PIOOCON0,(11),0,1,0 // Pin Controlled by PORTO
#define S3C6410_PIO_PO12       S3C6410_BASE_PIOOCON0,(12),0,1,0 // Pin Controlled by PORTO
#define S3C6410_PIO_PO13       S3C6410_BASE_PIOOCON0,(13),0,1,0 // Pin Controlled by PORTO
#define S3C6410_PIO_PO14       S3C6410_BASE_PIOOCON0,(14),0,1,0 // Pin Controlled by PORTO
#define S3C6410_PIO_PO15       S3C6410_BASE_PIOOCON0,(15),0,1,0 // Pin Controlled by PORTO

#define S3C6410_PIO_PP0        S3C6410_BASE_PIOPCON0,(0),0,1,0  // Pin Controlled by PORTP
#define S3C6410_PIO_PP1        S3C6410_BASE_PIOPCON0,(1),0,1,0  // Pin Controlled by PORTP
#define S3C6410_PIO_PP2        S3C6410_BASE_PIOPCON0,(2),0,1,0  // Pin Controlled by PORTP
#define S3C6410_PIO_PP3        S3C6410_BASE_PIOPCON0,(3),0,1,0  // Pin Controlled by PORTP
#define S3C6410_PIO_PP4        S3C6410_BASE_PIOPCON0,(4),0,1,0  // Pin Controlled by PORTP
#define S3C6410_PIO_PP5        S3C6410_BASE_PIOPCON0,(5),0,1,0  // Pin Controlled by PORTP
#define S3C6410_PIO_PP6        S3C6410_BASE_PIOPCON0,(6),0,1,0  // Pin Controlled by PORTP
#define S3C6410_PIO_PP7        S3C6410_BASE_PIOPCON0,(7),0,1,0  // Pin Controlled by PORTP

#define S3C6410_PIO_PP8        S3C6410_BASE_PIOPCON0,(8),0,1,0  // Pin Controlled by PORTP
#define S3C6410_PIO_PP9        S3C6410_BASE_PIOPCON0,(9),0,1,0  // Pin Controlled by PORTP
#define S3C6410_PIO_PP10       S3C6410_BASE_PIOPCON0,(10),0,1,0 // Pin Controlled by PORTP
#define S3C6410_PIO_PP11       S3C6410_BASE_PIOPCON0,(11),0,1,0 // Pin Controlled by PORTP
#define S3C6410_PIO_PP12       S3C6410_BASE_PIOPCON0,(12),0,1,0 // Pin Controlled by PORTP
#define S3C6410_PIO_PP13       S3C6410_BASE_PIOPCON0,(13),0,1,0 // Pin Controlled by PORTP
#define S3C6410_PIO_PP14       S3C6410_BASE_PIOPCON0,(14),0,1,0 // Pin Controlled by PORTP

#define S3C6410_PIO_PQ0        S3C6410_BASE_PIOQCON0,(0),0,1,0  // Pin Controlled by PORTQ
#define S3C6410_PIO_PQ1        S3C6410_BASE_PIOQCON0,(1),0,1,0  // Pin Controlled by PORTQ
#define S3C6410_PIO_PQ2        S3C6410_BASE_PIOQCON0,(2),0,1,0  // Pin Controlled by PORTQ
#define S3C6410_PIO_PQ3        S3C6410_BASE_PIOQCON0,(3),0,1,0  // Pin Controlled by PORTQ
#define S3C6410_PIO_PQ4        S3C6410_BASE_PIOQCON0,(4),0,1,0  // Pin Controlled by PORTQ
#define S3C6410_PIO_PQ5        S3C6410_BASE_PIOQCON0,(5),0,1,0  // Pin Controlled by PORTQ
#define S3C6410_PIO_PQ6        S3C6410_BASE_PIOQCON0,(6),0,1,0  // Pin Controlled by PORTQ
#define S3C6410_PIO_PQ7        S3C6410_BASE_PIOQCON0,(7),0,1,0  // Pin Controlled by PORTQ

#define S3C6410_PIO_PQ8        S3C6410_BASE_PIOQCON0,(8),0,1,0  // Pin Controlled by PORTQ


extern int open_port_device(void);
extern int close_port_device(void);
extern int port_read ( unsigned int pio_port_base_addr,     // PIO基地址
										unsigned int port_bit,
								   unsigned int con_state);  // 某位
extern int port_write(unsigned int pio_port_base_addr,// PIO基地址
					unsigned int port_bit,
			   unsigned int con_state,          // 某位
			   unsigned int con_bit,            //CON的多少位对应一个脚 0为4位,1为2位
               unsigned int con_num,            //有几个控制寄存器，0为1个，1为2个
			   int value);                      // 值:0,1
	


#endif 
