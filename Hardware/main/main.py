import time
import network
import usocket
from motor import move, stop, shoot
from machine import Pin
from time import sleep

forward = 0
backward = 1
left = 2
right = 3
stop = 4

IN1 = Pin(19, Pin.OUT)
IN2 = Pin(21, Pin.OUT)
IN3 = Pin(22, Pin.OUT)
IN4 = Pin(23, Pin.OUT)

pins = [IN1, IN2, IN3, IN4]

wlan = network.WLAN(network.AP_IF)
wlan.active(True)
wlan.config(essid="OgniBot")
hostname = wlan.ifconfig()[0]

mysocket = usocket.socket(usocket.AF_INET, usocket.SOCK_STREAM)
mysocket.setsockopt(usocket.SOL_SOCKET, usocket.SO_REUSEADDR, 1)

mysocket.bind((hostname, 80))
mysocket.listen(10)

connected = False
disconnect_msg = "disc"

while True:
    conn, addr = mysocket.accept()
    print("Connected from: %s " % str(addr))
    connected = True
    while connected:
        message = conn.recv(1024).decode("utf-8")
        if message == disconnect_msg:
            connected = False
        print("message: %s" % str(message))
        if message == "forward":
            move(forward)
        if message == "backward":
            move(backward)
        if message == "right":
            move(right)
        if message == "left":
            move(left)
        if message == "shoot":
            shoot()
        conn.send(message)
    conn.close()
    stop()
    sleep(1)
