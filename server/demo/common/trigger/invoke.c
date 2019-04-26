#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <limits.h>
#include <string.h>
int main()
{
	int fd = open("/tmp/logs.txt",O_CREAT | O_APPEND |O_RDWR);
	write(fd, "hello world\n", 12);
	printf("/tmp/logs.txt");	
}

