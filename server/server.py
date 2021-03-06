#!/usr/bin/env python
# -*- coding:utf-8 -*-
import json
import os
import sys
import platform
import time
import base64
import struct
if sys.version_info.major is 3:
	import socketserver
	from json.decoder import JSONDecodeError
	import _thread as thread
else:
	import SocketServer as socketserver
	import thread
from libs.helper import *
config = None

def trigger_thread(name, event, invoke, outpath):
	print("start new thread", event, invoke, outpath)
	while True:
		event_stream = os.popen( event )	# start event
		ret = event_stream.read()		# may be block
		invoke_stream = os.popen( invoke )	# return saved file path
		file_path = invoke_stream.read()
		localtime = time.strftime("%y_%m_%d_%H_%M_%S")
		if not os.path.exists(outpath):
			os.makedirs(outpath)
		with open( outpath + localtime ,'w') as f:
			f.write(file_path)
		if not config.getboolean(name,"loop"):
			break

class MyServer(socketserver.BaseRequestHandler):

	def handle(self):
		print(self.request,self.client_address,self.server)
		conn = self.request
		if sys.platform == 'win32':
			os.system('tools\ini2json.exe config.ini')
		elif platform.uname()[4] == 'x86_64':
			os.system('./tools/ini2json_x86 config.ini')
		else:
			os.system('./tools/ini2json_ARM config.ini')
		with open('config.json','r') as f:
			respons = json.loads(f.read())
		respons = json.dumps(respons)
		conn.sendall(respons.encode())
		print(respons,'sended')
		flag = True
		while flag:
			respons = {}
			raw_data = conn.recv(1024).decode()
			try:
				data = json.loads(raw_data)
			except ValueError:
				print('Json Decode error')
				break
			print(data,'receive')
			component = {}
			for tupe in config.items(data['name']):
				component[tupe[0]] = tupe[1]
			if component['type'] == 'action':
				os.system(component['cmd'])
				respons = {'content':'success'}

			elif component['type'] == 'status':
				respons = {'content':os.popen(component["cmd"]).read()}

			elif component['type'] == 'setter':
				os.system(component['cmd']+" " + data['value'])
				respons = {'content':'success'}

			elif component['type'] == 'trigger':
				if not 'path' in component:
					component['path'] = './default_logs/'
					config.set(data['name'],'path','./default_logs/') #update new config file
				if not 'loop' in component:
					component['loop'] = 'False'
					config.set(data['name'],'loop','False')
				if(data['value'] == 'start'):
					thread.start_new_thread(trigger_thread,(data['name'],component['event'], component['invoke'], component['path']))
					respons = {'content':'success'}
				elif data['value'] == 'fetch':
					num = 0
					if os.path.exists(component['path']):
						for log in os.listdir(component['path']):
							with open(component['path'] + log, 'r') as f:
								file = f.read()
								if not os.path.exists(file):
									continue
								with open(file,'rb') as f2:
									respons['extra'+ str(num)] = base64.b64encode(f2.read())
								respons['name'+ str(num)] = file.split('/')[-1] ## get file name
								num = num + 1
							os.remove(component['path'] + log)
					respons['content'] = num
				elif data['value'] == 'loop':
					before = config.getboolean(data['name'],'loop')
					print('before',before)
					config.set(data['name'],'loop',str(not before))
				size = len(json.dumps(respons))
				print("fetch size is", size)
				conn.sendall(struct.pack('>I',size))
			respons = json.dumps(respons)
			conn.sendall(respons.encode())
			print(respons,'sended')
			with open('config.ini', 'w') as configfile:
				config.write(configfile)

if __name__ == '__main__':
	if os.fork() > 0:
		print("主进程退出")
		os._exit(0)
	else:
		print("启动守护进程")
		config = get_config()
		server = socketserver.ThreadingTCPServer(('',6666),MyServer)
		server.serve_forever()
