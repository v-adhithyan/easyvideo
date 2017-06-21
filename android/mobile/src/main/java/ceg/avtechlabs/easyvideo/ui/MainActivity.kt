package ceg.avtechlabs.easyvideo.ui

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import ceg.avtechlabs.easyvideo.R
import android.widget.Toast
import android.app.ProgressDialog
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.HashMap
import android.widget.AdapterView.OnItemClickListener
import ceg.avtechlabs.easyvideo.util.Globals


class MainActivity : AppCompatActivity() {
    val REQUEST_ENABLE_BT = 1
    var progressBar: ProgressDialog? = null
    val deviceMap: HashMap<String, BluetoothDevice> = HashMap<String, BluetoothDevice>()

    companion object {
        var bluetoothAdapter: BluetoothAdapter? = null
        val NAME_SECURE = "easyvideo"
        val UUID_SECURE = UUID.fromString("367946a6-5502-11e7-b114-b2f933d5fe66")

        var DEVICE: BluetoothDevice? = null
        val DEVICE_NAME = "device_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = ProgressDialog(this)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (!bluetoothAdapter?.isEnabled!!) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(intent, REQUEST_ENABLE_BT)
        } else {
            setupChat()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

            REQUEST_ENABLE_BT -> if (resultCode === Activity.RESULT_OK) {
                setupChat()
            } else {
                Toast.makeText(this, R.string.bt_not_enabled_leaving,
                        Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    fun setupChat() {
        //PROGRESS_BAR?.show()
        progressBar?.setTitle("easyvideo")
        progressBar?.setMessage("Obtaining paired devices ..")
        progressBar?.show()
        Log.d("ev", "setup chat")
        val pairedDevices = bluetoothAdapter?.bondedDevices!!

        val deviceNames = LinkedList<String>()

        for (device in pairedDevices) {
            deviceNames.add(device.name)
            deviceMap.put(device.name, device)
        }

        listview_devices.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceNames)
        listview_devices.setOnItemClickListener(OnItemClickListener {
            arg0, arg1, position, arg3 ->
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra(MainActivity.DEVICE_NAME, deviceNames[position])
            Globals.device = deviceMap[deviceNames[position]]
            startActivity(intent)
        })

        progressBar?.dismiss()
    }

}
