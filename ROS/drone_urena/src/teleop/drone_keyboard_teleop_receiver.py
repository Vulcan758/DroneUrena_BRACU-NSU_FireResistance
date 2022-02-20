#! /usr/bin/env python3

import rospy
from std_msgs.msg import String
from geometry_msgs.msg import Twist
from time import sleep 


rospy.init_node("we_the_receiver")

def callback(msg, publisher):
    print(msg.data)
    drone_move(msg.data, publisher)

def drone_move(dir, publisher):
    
    direction = Twist()
    stop = Twist()
    if dir == "up":
        direction.linear.x = 1
    if dir == "down":
        direction.linear.x = -1
    if dir == "right":
        direction.linear.y = 1
    if dir == "left":
        direction.linear.y = -1
    publisher.publish(direction)
    sleep(0.1)
    publisher.publish(stop)
    
drone_mov = rospy.Publisher("/cmd_vel", Twist, queue_size=10)
drone_key_sub = rospy.Subscriber("/drone_key", String, callback, (drone_mov))
rospy.spin()