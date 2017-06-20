package ceg.avtechlabs.easyvideo.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast

import ceg.avtechlabs.easyvideo.R
import ceg.avtechlabs.easyvideo.threads.SendThread
import ceg.avtechlabs.easyvideo.util.Globals
import kotlinx.android.synthetic.main.activity_controller.*

class ControllerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        title = "Connected to ${intent.getStringExtra(MainActivity.DEVICE_NAME)}"

    }

    fun list(v: View) {
        Runnable {  SendThread(message = "list", handler = mHandler, list = true).run() }.run()
    }

    fun sendToList(message: String) {
        Toast.makeText(this, "chosen: $message", Toast.LENGTH_LONG).show()
        Runnable {  SendThread(message = message, handler = mHandler, list = true).run() }.run()
    }

    private fun setList(data: List<String>) {
        listview_list.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data)
        listview_list.setOnItemClickListener { parent, view, position, id ->
            sendToList("list${Globals.DELIMITER}${parent.adapter.getItem(position).toString()}")
        }
    }

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Globals.EV_RESPONSE -> {
                    val data = msg.data.getString(Globals.EV_LIST).split(Globals.DELIMITER)
                    setList(data)
                } Globals.EV_EMPTY -> {
                    setList(arrayListOf("Empty folder"))
                } else -> {
                // Todo dummy
                }
            }
        }
    }

    fun playback(v: View){
        Runnable {  SendThread(message = "play", handler = mHandler, list = true).run() }.run()
    }
}
