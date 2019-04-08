import json
import sys
import os
if sys.version_info.major is 2:
    import ConfigParser as configparser
else:
    import configparser
def check_exist():
    """检测所需文件是否存在"""
    if os.path.isfile('config.ini'):
        return True
    else:
        return False

def get_config():
    configInstance = configparser.ConfigParser()
    if not check_exist():
        print('run init first')
        exit()
    configInstance.read('config.ini')
    return configInstance
    pass


def init():
    configInstance = configparser.ConfigParser()
    with open('config.ini', 'w') as configfile:
        configInstance.write(configfile)
    print('done')
