import time
import network
import usocket as socket

wlan = network.WLAN(network.AP_IF)
wlan.active(True)

wlan.config(essid="OgniBot")
hostname = wlan.ifconfig[0]