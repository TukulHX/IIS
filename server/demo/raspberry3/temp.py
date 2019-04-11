#!/usr/bin/python

import Adafruit_DHT

sensor = Adafruit_DHT.DHT22

pin = 4  #GPIO4

humidity, temperature = Adafruit_DHT.read_retry(sensor, pin)

if temperature is not None:
    print('{0:0.1f}'.format(temperature))
