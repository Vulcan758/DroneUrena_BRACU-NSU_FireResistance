#! /usr/bin/env python3
import rospy
from std_msgs.msg import String, Empty
from geometry_msgs.msg import Twist

rospy.init_node("movement_test")


def takeoff():
    empty = Empty()
    takeoff = rospy.Publisher("/drone/takeoff", Empty, queue_size = 10)
    takeoff.publish(empty)

def land():
    empty = Empty()
    land = rospy.Publisher("/drone/land", Empty, queue_size = 10)
    land.publish(empty) 

def forward():
    forward = Twist()
    forward.linear.x = 1
    for_ward = rospy.Publisher("/cmd_vel", Twist, queue_size=10)
    for_ward.publish(forward)

def stop():
    stop = Twist()
    st_op = rospy.Publisher("/cmd_vel", Twist, queue_size=10)
    st_op.publish(stop)

def rotate_z(dir):
    rotate = Twist()
    if dir == "ccw":
        rotate.angular.z = 1
    else:
        rotate.angular.z = -1
    rot_ate = rospy.Publisher("/cmd_vel", Twist, queue_size=10)
    rot_ate.publish(rotate)




while not rospy.is_shutdown():
    inp = input("Enter input> ")
    if inp == "takeoff":
        takeoff()
    if inp == "land":
        land()
    if inp == "forward":
        forward()
    if inp == "stop":
        stop()
    if inp == "rotate ccw":
        rotate_z("ccw")
    if inp == "rotate cw":
        rotate_z("cw")
    