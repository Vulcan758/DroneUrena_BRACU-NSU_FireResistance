#!/usr/bin/env python3

import rospy
from geometry_msgs.msg import Pose
from tf.transformations import euler_from_quaternion, quaternion_from_euler

def callback(data):
    orientation = data.orientation
    orient_list = [orientation.x, orientation.y, orientation.z, orientation.w]
    (roll, pitch, yaw) = euler_from_quaternion[orient_list]
    print(yaw)


rospy.init_node("position_stuff")

rate = rospy.Rate(5)

while not rospy.is_shutdown():
    pose = rospy.Subscriber("/drone/gt_pose", Pose, callback)
    rate.sleep()