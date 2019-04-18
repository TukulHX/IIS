#include<stdio.h>
#include<unistd.h>
#include<fcntl.h>
#include<sys/stat.h>
#include <sys/prctl.h>
int exist(char * process_name)
{
	int count = 0;
	FILE * fp = NULL;
	char command[150];
	char buf[100];
	sprintf(command, "ps -C %s|wc -l", process_name);
	fp = popen(command,"r");
	if((fgets(buf,100,fp)) != NULL){
		count = atoi(buf);
		if( (count - 1) == 0)
			return 0;
		else
			return 1;
	}
}

void setSpeed(char * buffer){
        int fd = 0;
        if((fd=open("/tmp/dameon.log",O_CREAT|O_WRONLY|O_APPEND,0600))<0){
                perror("open");
                exit(1);
        }
        int len = 0;
        while(buffer[len] != 0)  len++;
        write(fd,buffer, len);
        write(fd,"\n",1);
        close(fd);

}

void createDamonifNotExist()
{
	if(exist("fanD"))
		return;
	int pid;
        if( pid = fork() > 0 )
                return;
        else if(pid < 0)
                exit(1);
        setsid();
	prctl(PR_SET_NAME, "fanD");
        const char * fifo_name = "/tmp/fan_d";
        if( access(fifo_name,F_OK) == -1){
                mkfifo(fifo_name,0777);
        }
        char buffer[1024];
        memset(buffer,1024,0);
        while(1){
                int pipe_fd = open(fifo_name,O_RDONLY);
                read(pipe_fd,buffer,1024);
                if(strcmp(buffer,"0") == 0)
                        break;
                else
                        setSpeed(buffer);
                close(pipe_fd);
        }

}

int main(int argc, char ** argv)
{
	createDamonifNotExist();
	int pipe_fd;
	const char * fifo_name = "/tmp/fan_d";
	if(access(fifo_name,F_OK) == -1){
		mkfifo(fifo_name,0777);
	}
	pipe_fd = open(fifo_name,O_WRONLY);
	write(pipe_fd,argv[1],1024);
	close(pipe_fd);
}
