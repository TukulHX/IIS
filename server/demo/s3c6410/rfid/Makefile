#---------------------------------------------------------

CC		= arm-linux-gcc
INSTALL		= install

TARGET		= rfid

all : $(TARGET)

$(TARGET):rfid.c bsp.h
	$(CC) -static $< -o $@

clean :
	rm -rf *.o $(TARGET) *~


#---------------------------------------------------------
