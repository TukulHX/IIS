CC=arm-linux-gcc
fan: fan.c bsp.o
	$(CC) -std=c99 fan.c bsp.o -o fan
bsp.o: bsp.c bsp.h
	$(CC) -c bsp.c
clean:
	rm *.o && rm fan
