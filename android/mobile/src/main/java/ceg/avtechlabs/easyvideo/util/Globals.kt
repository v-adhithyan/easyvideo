package ceg.avtechlabs.easyvideo.util

import android.bluetooth.BluetoothDevice

/**
 * Created by Adhithyan V on 20-06-2017.
 */

class Globals {
    companion object {
        var device: BluetoothDevice? = null
        val EV_RESPONSE = 0
        val EV_EMPTY = -1
        val EV_PLAY = 1
        val EV_LIST = "ev_list"
        val EV_PLAY_RESPONSE = "ev_play"
        val DELIMITER = ",,,"
    }

}