package ceg.avtechlabs.easyvideo.ui

import android.app.ProgressDialog
import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {
    var progressBar: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        title = "Connected to ${intent.getStringExtra(MainActivity.DEVICE_NAME)}"

    }

    fun list(v: View) {
        showProgressBar()
        SendThread(message = "list", handler = mHandler, list = true).start()
    }

    fun sendToList(message: String) {
        showProgressBar()
        Toast.makeText(this, "chosen: $message", Toast.LENGTH_LONG).show()
        SendThread(message = message, handler = mHandler, list = true).start()
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
                } Globals.EV_PLAY -> {
                    startActivity(Intent(this@ListActivity, RemoteControlActivity::class.java))
                } else -> {
                // Todo dummy
                }
            }
            progressBar?.dismiss()
        }
    }

    fun showProgressBar() {
        progressBar = ProgressDialog(this)
        progressBar?.setTitle("easyvideo")
        progressBar?.setMessage("Please wait ..")
        progressBar?.show()
    }
}
