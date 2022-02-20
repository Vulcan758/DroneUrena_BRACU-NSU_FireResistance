#!/usr/bin/env python3

import math
import rospy
from geometry_msgs.msg import Pose, Twist
from tf.transformations import euler_from_quaternion, quaternion_from_euler
from std_msgs.msg import String, Empty
from time import sleep 
import sys

rospy.init_node("rotate_pitch")

x, y, z = -5, 5, 5

speed = 0.25

turn = Twist()
turn.angular.z = -0.05

straight = Twist()
straight.linear.x = speed

stop = Twist()

def callback(data, args):
    yaw_t_ = args[0]
    drone_ = args[1]
    speed_ = args[2]
    straight_length_ = args[3]
    orientation = data.orientation
    orient_list = [orientation.x, orientation.y, orientation.z, orientation.w]
    algo_turn(orient_list, yaw_t_, drone_, speed_, straight_length_)
    
def get_angle(x,y,z):
    yaw = math.atan(y/x)
    length  = math.sqrt(x*x + y*y)
    return yaw, length

def algo_straight(speed, straight_length, pub):
    time = round(float(straight_length / speed), 3)
    pub.publish(straight)
    sleep(time)
    pub.publish(stop)
    print("home baby")
    sleep(5)
    #rospy.signal_shutdown()
    sys.exit()


def algo_turn(orient_list, yaw_t, pub, speed, straight_length):
    roll_c, pitch_c, yaw_c = euler_from_quaternion(orient_list)
    current_euler = [roll_c, pitch_c, yaw_c]
    target_orient_euler = [roll_c, pitch_c, yaw_t]
    diff = current_euler[2] - target_orient_euler[2]
    if round(current_euler[2] - target_orient_euler[2], 2) == 0 :
        pub.publish(stop)
        sleep(2)
        print("reached")
        algo_straight(speed, straight_length, pub)

    else :
        pub.publish(turn)
        print("not there yet")
        print("__________",current_euler[2], target_orient_euler[2], diff)



yaw_t, straight_length = get_angle(x, y, z)

drone = rospy.Publisher("/cmd_vel", Twist, queue_size=10)
pose = rospy.Subscriber("/drone/gt_pose", Pose, callback, (yaw_t, drone, speed, straight_length))
rospy.spin()
drone.publish(stop)

