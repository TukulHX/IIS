#include<stdio.h>
#include<unistd.h>
#include<fcntl.h>
#include<sys/stat.h>
int main(int argc, char ** argv)
{
	int pipe_fd;
	const char * fifo_name = "/tmp/fan_d";
	if(access(fifo_name,F_OK) == -1){
		mkfifo(fifo_name,0777);
	}
	pipe_fd = open(fifo_name,O_WRONLY);
	write(pipe_fd,argv[1],1024);
	close(pipe_fd);
}
