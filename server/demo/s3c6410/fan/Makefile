CC=arm-linux-gcc
fan : fan.c setHardWareSpeed.o bsp.o
	$(CC) -std=c99 fan.c setHardWareSpeed.o bsp.o  -lpthread -o fan

setHardWareSpeed.o : bsp.c bsp.h setHardWareSpeed.c
	$(CC) -std=c99 -c setHardWareSpeed.c 

bsp.o : bsp.h bsp.c
	$(CC) -std=c99 -c bsp.c

clean:
	rm *.o && rm fan 
