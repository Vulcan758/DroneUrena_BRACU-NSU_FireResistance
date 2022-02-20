import socket

HEADER = 64
PORT = 80
FORMAT = 'utf-8'
DISCONNECT_MESSAGE = "disc"
SERVER = "192.168.4.1"
ADDR = (SERVER, PORT)

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(ADDR)

def send(msg):
    message = msg.encode(FORMAT)
    client.send(message)
    print(client.recv(2048).decode(FORMAT))

while True:
    var = input("enter msg > ")
    send(var)

send(DISCONNECT_MESSAGE)
