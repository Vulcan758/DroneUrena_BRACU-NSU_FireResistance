#!/usr/bin/env python3

import rospy
from geometry_msgs.msg import Twist
from std_msgs.msg import String, Empty
from time import sleep 
from sensor_msgs.msg import Range

rospy.init_node("obstacle_avoidance")
rotate = Twist()
direction = Twist()

rotate.angular.z = 1
direction.linear.x = .2




def callback(msg, pub):
    dist = msg.range
    if dist < 1:
      pub.publish(rotate)
      print("rotating")
    else:
      pub.publish(direction)
      print("forward")

pub = rospy.Publisher('/cmd_vel', Twist, queue_size=10)
sub = rospy.Subscriber('/drone/sonar', Range, callback, (pub)) 

move = Twist()

rospy.spin()