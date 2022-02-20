#! /usr/bin/env python3
import pygame
import rospy
from std_msgs.msg import String
from geometry_msgs.msg import Twist
from time import sleep 
pygame.init()

window = pygame.display.set_mode((500, 500))
pygame.display.set_caption("controller")

up = 119
down = 115
left = 97
right = 100

directions = [up, down, left, right]
dir_states = [False, False, False, False]
outputs = ["up", "down", "left", "right"]

rospy.init_node("drone_key_publisher")

def controller():
    drone_key_pub = rospy.Publisher("/drone_key", String, queue_size=10)
    while not rospy.is_shutdown():
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
                drone_key_pub.publish(outputs[dir_states.index(dir)])
        sleep(0.1)
            
if __name__ == "__main__":
    controller()