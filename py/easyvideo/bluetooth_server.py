import bluetooth
import os
from easyvideo import UUID
from easyvideo import APP_NAME
from easyvideo import DELIMITER
from easyvideo import controller
from easyvideo import config

def run():
    server = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
    server.bind(("", bluetooth.PORT_ANY))
    server.listen(1)
    print "listenng on port %d" %server.getsockname()[1]
    uuid = UUID
    bluetooth.advertise_service( server, APP_NAME,
                   service_id = uuid,
                   service_classes = [ uuid, bluetooth.SERIAL_PORT_CLASS ],
                   profiles = [ bluetooth.SERIAL_PORT_PROFILE ],
                   )
    while True:
        client, address = server.accept()
        print "accepted connection from ",address

        data = client.recv(1024)
        print "received: %s" %(data)


        if "list" in data:
            current = data.split(DELIMITER)
            if len(current) > 1:
                current = current[1]
            else:
                config.remove_android_section()
                current = ""

            message, is_file = controller.list(current=current)
            if is_file:
                client.send("play")
                os.startfile(config.get_android_folder())
            else:
                client.send(DELIMITER.join(message))

        elif "play" in data:
            controller.playback()
            client.send("done")

        elif "volumeup" in data:
            controller.volume_up()
            client.send("done")

        elif "volumedown" in data:
            controller.volume_down()
            client.send("done")

        elif "mute" in data:
            controller.mute_unmute()
            client.send("done")

        elif data == "quit":
            break
        client.close()

    server.close()
