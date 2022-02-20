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

def stop():
    IN1.value(0)
    IN2.value(0)
    IN3.value(0)
    IN4.value(0)

    


def move(dir):
    states = [[1, 0, 1, 0], [0, 1, 0, 1], [1, 0, 0, 1], [0, 1, 1, 0], [0, 0, 0, 0]]
    logic_list = states[dir]
    for i in range(len(logic_list)):
        pins[i].value(logic_list[i])
    sleep(1)
    stop()
    sleep(1)

if __name__ == "__main__":
    while True:
        move(forward)
        #forward()
        print("going forward")
        move(backward)
        #backward()
        print("going backward")
        move(left)
        #left()
        print("going left")
        move(right)
        #right()
        print("going right")
        
        
