import socket
import pygame
from time import sleep 
pygame.init()

HEADER = 64
PORT = 80
FORMAT = 'utf-8'
DISCONNECT_MESSAGE = "disc"
SERVER = "192.168.4.1"
ADDR = (SERVER, PORT)

window = pygame.display.set_mode((500, 500))
pygame.display.set_caption("controller")

up = 119
down = 115
left = 97
right = 100
shoot = pygame.K_f

directions = [up, down, left, right, shoot]
dir_states = [False, False, False, False, False]
outputs = ["forward", "backward", "left", "right", "shoot"]

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(ADDR)

def send(msg):
    message = msg.encode(FORMAT)
    client.send(message)
    print(client.recv(2048).decode(FORMAT))

def controller():
    while True:
        for event in pygame.event.get():
            if event.type == pygame.KEYDOWN:
                pressed = directions.index(event.key)
                dir_states[pressed] = True
            elif event.type == pygame.KEYUP:
                released = directions.index(event.key)
                dir_states[released] = False
        for dir in dir_states:
            if dir == True:
                print(outputs[dir_states.index(dir)])
                send(outputs[dir_states.index(dir)])
        sleep(0.1)
            
if __name__ == "__main__":
    controller()


send(DISCONNECT_MESSAGE)
