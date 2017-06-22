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
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.preference.Preference
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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

    var btDeviceName: String? = null

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

        btDeviceName = getString(R.string.pref_bt_device_name)

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

        val name = getStoredDeviceName()
        Log.d("adhithyan", "name: $name")

        for (device in pairedDevices) {
            deviceNames.add(device.name)
            deviceMap.put(device.name, device)

            if(name != null && device.name.equals(name)) {
                progressBar?.dismiss()
                Globals.device = deviceMap[name]
                startListActivty(name)
                break
            }
        }

        if (name == null) {
            listview_devices.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceNames)
            listview_devices.setOnItemClickListener(OnItemClickListener {
                arg0, arg1, position, arg3 ->
                Globals.device = deviceMap[deviceNames[position]]
                showDeviceStoreAlert(getPreferences(Context.MODE_PRIVATE), deviceNames[position])
            })
        }

        progressBar?.dismiss()
    }

    fun getStoredDeviceName(): String? {
        return getPreferences(Context.MODE_PRIVATE).getString(btDeviceName, null)
    }

    fun showDeviceStoreAlert(preference: SharedPreferences, name: String) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("easyvideo")
        alert.setMessage("Is this $name the default device?")

        alert.setPositiveButton("Yes", object :DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val editor = preference.edit()
                //editor.putBoolean(firstLaunch, true)
                editor.putString(btDeviceName, name)
                editor.apply()
                startListActivty(name)
            }
        })

        alert.setNegativeButton("No", object :DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                startListActivty(name)
            }
        })

        alert.show()
    }

    fun startListActivty(name: String) {
        val intent = Intent(this@MainActivity, ListActivity::class.java)
        intent.putExtra(MainActivity.DEVICE_NAME, name)
        startActivity(intent)
        finish()
    }

}
