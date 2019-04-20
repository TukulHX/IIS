CC=gcc

fanC : fan.c 
	$(CC) -std=c99 fan.c -o fanC
fanD : fanD.c bsp.o setHardWareSpeed.o
	$(CC) -std=c99 fanD.c bsp.o  setHardWareSpeed.o -lpthread -o fanD

setHardWareSpeed.o : bsp.o setHardWareSpeed.c
	$(CC) -std=c99 -c setHardWareSpeed.c bsp.o

bsp.o : bsp.h bsp.c
	$(CC) -std=c99 -c bsp.c

clean:
	rm *.o 
