import bluetooth
import sys
import threading

"""devices = bluetooth.discover_devices(lookup_names=True)
print "found {} devices".format(len(devices))

for addr, name in devices:
    print "{} {}".format(addr, name)"""

addr = "BC:D1:1F:7A:49:2D"
port = 1

sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
sock.connect((addr, port))

sock.send("hello")
sock.close()
