package ceg.avtechlabs.easyvideo.ui

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.Toast

import ceg.avtechlabs.easyvideo.R
import ceg.avtechlabs.easyvideo.threads.SendThread
import ceg.avtechlabs.easyvideo.util.Globals

class RemoteControlActivity : AppCompatActivity() {
    var progressBar: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_control)
    }

    fun playback(v: View){
        sendCommand("play")
    }

    fun volumeUp(v: View) {
        sendCommand("volumeup")
    }

    fun volumeDown(v: View) {
        sendCommand("volumedown")
    }

    fun mute(v: View) {
        sendCommand("mute")
    }

    fun goFullscreen(v: View) {
        sendCommand("fullscreen")
    }

    fun lock(v: View) {
        sendCommand("lock")
    }

    fun shutdown(v:View) {
        sendCommand("shutdown")
    }

    fun sendCommand(message: String) {
        showProgressBar()
        SendThread(message = message, handler = mHandler, list = true).start()
    }

    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Globals.EV_EMPTY -> Toast.makeText(this@RemoteControlActivity, "done", Toast.LENGTH_LONG).show()
                else -> {}
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
