#!/usr/bin/env python3

import math
import rospy
from geometry_msgs.msg import Pose, Twist
from tf.transformations import euler_from_quaternion, quaternion_from_euler
from std_msgs.msg import String, Empty
from time import sleep 
#import numpy as np

rospy.init_node("change_position")

orient_list = None
x, y, z = None, None, None


def callback(msg, publisher):
    print(msg.data)
    drone_move(msg.data, publisher)

def drone_move(dir, publisher):
    direction = Twist()
    stop = Twist()
    direction.linear.x = dir[0]
    direction.linear.y = dir[1]
    direction.linear.z = dir[2]
    publisher.publish(direction)
    sleep(0.1)
    publisher.publish(stop)
    

def get_length(x,y,z):
    #yaw = math.atan(y/x)
    length  = math.sqrt(x*x + y*y)
    return length

length_t = get_length(x, y, z)

while True:
    current_euler = euler_from_quaternion(orient_list)
    roll_c, pitch_c, yaw_c = euler_from_quaternion(orient_list)
    target_orient_euler = [roll_c, pitch_c, yaw_t]
    if (round(current_euler[2] - target_orient_euler[2]), 2 == 0) :
        break
    else :
        drone_mov = rospy.Publisher("/cmd_vel", Twist, queue_size=10)
        drone_key_sub = rospy.Subscriber("/position_get_test", String, callback, (drone_mov))
        rospy.spin()

