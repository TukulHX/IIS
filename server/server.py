#!/usr/bin/env python
# -*- coding:utf-8 -*-
import json
import os
import sys
import platform
if sys.version_info.major is 3:
	import socketserver
	from json.decoder import JSONDecodeError
else:
	import SocketServer as socketserver
from libs.helper import *
config = None

class MyServer(socketserver.BaseRequestHandler):
        
	def handle(self):
		print(self.request,self.client_address,self.server)
		conn = self.request
		with open('config.ini', 'w') as configfile:
        		config.write(configfile)
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
			raw_data = conn.recv(1024).decode()
			try:
				data = json.loads(raw_data)
			except ValueError:
				print('Json Decode error')
				break
			print(data,'receive')
			if sys.version_info.major is 3:
				component = config[data['name']]
			else: # for python2 convert item to dict
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
						
			respons = json.dumps(respons)
			conn.sendall(respons.encode())
			print(respons,'sended')

if __name__ == '__main__':
	if os.fork() > 0:
		print("主进程退出")
		os._exit(0)
	else:
		print("启动守护进程")
		config = get_config()
		server = socketserver.ThreadingTCPServer(('',6666),MyServer)
		server.serve_forever()
