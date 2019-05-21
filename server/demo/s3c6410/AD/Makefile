KERNELDIR := /home/ARM11_6410_wifi_LInux/linux-2.6.38
CROSS_COMPILE := arm-linux-
#CROSS_COMPILE := 
#
# 定义编译器和汇编器
#
CC := $(CROSS_COMPILE)gcc
AS := $(CROSS_COMPILE)as
AR := $(CROSS_COMPILE)ar
MAKE := make

LDLIBFLAG += m
#LFLAGS +=  -I /usr/local/arm/3.4.3/lib/ -lm
#LFLAGS +=  -I /opt/EmbedSky/arm/4.3.3/lib/ -lm
CFLAGS += -Os -Wall -march=armv4t  
Q := @


#
# 定义总控Makefile所在的文件目录
#
top_dir = $(PWD)

#
# 追加编译头文件路径
#
#CFLAGS += -I./  -L/opt/EmbedSky/crosstools_3.4.5_softfloat


#
# 定义编译目标
#
2440_exec := ad


#
# 定义生成的目标
#
exes += $(2440_exec)

#
# 定义多目标
#
poly_exes := starttip
poly_exes += $(exes)
poly_exes += endtip

.PHONY : all
all :   $(poly_exes)

starttip :
	$(Q)echo
	$(Q)echo +++++++++++++++++ start of making ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	$(Q)echo
	
endtip :
	$(Q)echo
	$(Q)echo ++++++++++++++++++ end of making +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	$(Q)echo



#
# 定义当前模块的目标名
#

cur_exec := $(2440_exec)

#
# 定义当前模块相关的各个目标模块
#
module_objects := bsp.o main.o   
module_app_headers := main.h bsp.h 

######################################################
#
# 下面的脚本各应用程序模块完全相同
#


#
# 定义生成的目标模块位置和名称
#
cur_exec_ld := $(top_dir)/$(cur_exec)

#
# 连接生成可执行文件
#
$(2440_exec) : $(module_objects) 
	$(CC) -o $(cur_exec_ld) -static  $(module_objects) -lm -lpthread 

#
# 编译生成各中间目标文件
#
$(module_objects) : $(module_app_headers) 


clean:
	rm -f *.o
	rm -f $(cur_exec_ld)

#
# 
#
######################################################
