package ceg.avtechlabs.easyvideo.threads

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import ceg.avtechlabs.easyvideo.ui.MainActivity
import ceg.avtechlabs.easyvideo.util.Globals
import java.io.IOException

/**
 * Created by Adhithyan V on 20-06-2017.
 */

class SendThread(message: String, handler: Handler, list: Boolean): Thread() {
    var socket: BluetoothSocket? = null
    var send: ByteArray? = null
    val mHandler = handler
    val mList = list

    init {

        var temp: BluetoothSocket? = null

        try {
            temp = Globals.device?.createRfcommSocketToServiceRecord(MainActivity.UUID_SECURE)
        } catch (ex: Exception) {
            Log.d("ev", ex.message)
        }

        socket = temp
        send = message.toByteArray()
    }

    override fun run() {
        socket?.connect()
        if(socket != null) {
            val outputStream = socket?.outputStream
            outputStream?.write(send)
        }

        while (true) {
            val buffer = ByteArray(10240)


            try {
                val inputStream = socket?.inputStream
                val size = inputStream?.read(buffer)!!

                if (size > 0) {
                    val content = String(buffer.copyOfRange(0, size!!))
                    val message = mHandler.obtainMessage(Globals.EV_RESPONSE)
                    val bundle = Bundle()
                    if (mList) bundle.putString(Globals.EV_LIST, content)
                    message.data = bundle
                    mHandler.sendMessage(message)
                    break
                }

            } catch (ex: IOException) {
                mHandler.sendEmptyMessage(Globals.EV_EMPTY)
            }

        }
    }
}