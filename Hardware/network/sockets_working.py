import time
import network
import usocket 

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
        print()
        print(message)
        conn.send(message)
    conn.close()
