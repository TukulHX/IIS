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
	const char * fifo_name ="/tmp/fifo";
  	if (access(fifo_name, F_OK) == -1)
    	{
        	mkfifo(fifo_name, 0777);
    	}
	int pipe_fd = open(fifo_name,O_WRONLY);
	write(pipe_fd, "hello world",20);
}
