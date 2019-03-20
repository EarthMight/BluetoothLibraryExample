package com.example.bluetoothlibraryexample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import me.aflak.bluetooth.Bluetooth;

public class BrodcastBlueTooth extends BroadcastReceiver {

    public BrodcastBlueTooth() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bluetooth bluetooth;
        bluetooth = new Bluetooth(context);
        String DeviceName=null;
        String action = intent.getAction();
//        Log.d("BroadcastActions", "Action "+action+"received");
        int state;
        BluetoothDevice bluetoothDevice;
        ArrayList<String> pairedDevice;
        pairedDevice=new ArrayList<>();


        switch(action)
        {
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                if (state == BluetoothAdapter.STATE_OFF)
                {
//                    Toast.makeText(context, "Bluetooth is off", Toast.LENGTH_SHORT).show();
                    Log.d("BroadcastActions", "Bluetooth is off");
                }
                else if (state == BluetoothAdapter.STATE_TURNING_OFF)
                {
//                    Toast.makeText(context, "Bluetooth is turning off", Toast.LENGTH_SHORT).show();
                    Log.d("BroadcastActions", "Bluetooth is turning off");
                }
                else if(state == BluetoothAdapter.STATE_ON)
                {
//                    bluetooth.onStart();
//                    bluetooth.enable();
                    Toast.makeText(context, "Bluetooth is On", Toast.LENGTH_SHORT).show();
                    final BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
                    Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            pairedDevice.add(device.getName());
                        }
                        for(int i=0;i<pairedDevice.size();i++){
                            String name = pairedDevice.get(i);
                            if (name.contains("OBD")){
                            bluetooth.connectToName(name);
                            }else if (name.contains("obd")){
                                String Tname=name;
                                Log.d("Tname", Tname);
                                bluetooth.connectToName(name);
                            }else{
                                Toast.makeText(context,"No device which has obd name",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    Log.d("DevicePaired", String.valueOf(pairedDevice));

                }
                break;

            case BluetoothDevice.ACTION_ACL_CONNECTED:
                bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Toast.makeText(context, "Connected to "+bluetoothDevice.getName(), Toast.LENGTH_SHORT).show();
                Log.d("BroadcastActions", "Connected to "+bluetoothDevice.getName());

                DeviceName=bluetoothDevice.getName();
                if (DeviceName.contains("OBD")){
                    Intent newIntent = new Intent();
                    newIntent.setClassName("com.example.bluetoothlibraryexample", "com.example.bluetoothlibraryexample.MainActivity");
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(newIntent);

                }else if(DeviceName.contains("obd")){
                    Intent newIntent = new Intent();
                    newIntent.setClassName("com.example.bluetoothlibraryexample", "com.example.bluetoothlibraryexample.MainActivity");
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(newIntent);
                }

                break;

            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                Toast.makeText(context, "Disconnected from "+bluetoothDevice.getName(),
//                        Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void PairedDevice(){

    }
}
