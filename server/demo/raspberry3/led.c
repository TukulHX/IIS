#include <wiringPi.h>
char LED = 0;
int main(void) 
{
       	if(wiringPiSetup() < 0) return -1;
 	pinMode  (LED,OUTPUT);
        digitalWrite(LED,0); //设置为高电平
}

