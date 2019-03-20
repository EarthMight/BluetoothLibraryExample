package com.example.bluetoothlibraryexample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.BluetoothCallback;
import me.aflak.bluetooth.DeviceCallback;
import me.aflak.bluetooth.DiscoveryCallback;

public class MainActivity extends AppCompatActivity {
    Bluetooth bluetooth;
    Button On,Off,BScanBt,Bpair,ConnectBtn;
    ArrayList<String> PaiedDevices;
    ArrayList<String> DiscovedDevices;
    BluetoothAdapter BTAdapter;
    ListView PairList,DescoverList;
    EditText Enterdevice;
    Boolean OnFlag=false;
    Boolean PairDeviceFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        On.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetooth.onStart();
                bluetooth.enable();
                On.setTextColor(Color.parseColor("#76FF03"));
                Off.setTextColor(Color.parseColor("#2d2d2d"));
                OnFlag=true;
            }
        });

        Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetooth.onStop();
                bluetooth.disable();
                Off.setTextColor(Color.parseColor("#FF3D00"));
                On.setTextColor(Color.parseColor("#2d2d2d"));
                BScanBt.setTextColor(Color.parseColor("#2d2d2d"));
            }
        });

        BScanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
                Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
                ArrayList<String> pairedDevice;
                pairedDevice=new ArrayList<>();

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
                            Toast.makeText(getApplicationContext(),"No device which has obd name",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        BluetoothStateListner();


        Bpair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(OnFlag){
                    PairedDevice();
                    if(PairDeviceFlag){
                        ListMethodForPairdDevice();
                    }
                }
            }
        });


        DiscoveryPairingListner();


        ConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        bluetooth.connectToName(Enterdevice.getText().toString());
                    }
                });
                connectListner();
            }
        });
    }

    public void Init(){
        On=(Button)findViewById(R.id.bOnBt);
        Off=(Button)findViewById(R.id.bOffBt);
        BScanBt=(Button)findViewById(R.id.bTestBt);
        bluetooth = new Bluetooth(getApplicationContext());
        DiscovedDevices=new ArrayList<>();
        PaiedDevices=new ArrayList<>();
        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        PairList=(ListView)findViewById(R.id.pairlist);
        DescoverList=(ListView)findViewById(R.id.descoverlist);
        Bpair=(Button)findViewById(R.id.bpair);
        Enterdevice=(EditText)findViewById(R.id.Enterdevice);
        ConnectBtn=(Button)findViewById(R.id.connectBtn);
    }

    public void BluetoothStateListner(){
        bluetooth.setBluetoothCallback(new BluetoothCallback() {
            @Override
            public void onBluetoothTurningOn() {
                Toast.makeText(getApplicationContext(),"bluethooth is turnning on",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onBluetoothOn() {
                Toast.makeText(getApplicationContext(),"bluethooth is on",Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onBluetoothTurningOff() {
                Toast.makeText(getApplicationContext(),"bluethooth is turnning off",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onBluetoothOff() {
                Toast.makeText(getApplicationContext(),"bluethooth is off",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onUserDeniedActivation() {
                // when using bluetooth.showEnableDialog()
                // you will also have to call bluetooth.onActivityResult()
            }
        });
    }

    public void PairedDevice(){
        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                PaiedDevices.add(device.getName());

                Log.e("DeviceName", String.valueOf(PaiedDevices));
                PairDeviceFlag=true;
            }
        }
    }

    public void DiscoveryPairingListner(){
        bluetooth.setDiscoveryCallback(new DiscoveryCallback() {
            @Override public void onDiscoveryStarted() {
                Toast.makeText(getApplicationContext(),"DiscoveryStarted",Toast.LENGTH_SHORT).show();

            }
            @Override public void onDiscoveryFinished() {
                Toast.makeText(getApplicationContext(),"DiscoveryFinished",Toast.LENGTH_SHORT).show();

            }
            @Override public void onDeviceFound(BluetoothDevice device) {
                Toast.makeText(getApplicationContext(),"DeviceFound",Toast.LENGTH_SHORT).show();
                DiscovedDevices.add(device.getName()+" \n"+device.getAddress());
                Log.e("deviceFound",device.getName());

            }
            @Override public void onDevicePaired(BluetoothDevice device) {
                Toast.makeText(getApplicationContext(),"DevicePaired",Toast.LENGTH_SHORT).show();

            }
            @Override public void onDeviceUnpaired(BluetoothDevice device) {

            }
            @Override public void onError(String message) {

            }
        });
    }

    public void ListMethodForPairdDevice(){
        final ArrayAdapter < String > adapter = new ArrayAdapter< String >
                (MainActivity.this, android.R.layout.simple_list_item_1,
                        PaiedDevices);

        PairList.setAdapter(adapter);

        PairList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String BLT=PaiedDevices.get(i);
                Toast.makeText(getApplicationContext(),BLT,Toast.LENGTH_SHORT).show();
                bluetooth.connectToName(BLT);
            }
        });
    }

    public void ListMethodForDiscoverDevice(){
        final ArrayAdapter < String > adapter = new ArrayAdapter< String >
                (MainActivity.this, android.R.layout.simple_list_item_1,
                        DiscovedDevices);

        DescoverList.setAdapter(adapter);
    }


    public void connectListner(){
        bluetooth.setDeviceCallback(new DeviceCallback() {
            @Override public void onDeviceConnected(BluetoothDevice device) {
                Toast.makeText(getApplicationContext(),"device Connected :"+device.getName(),Toast.LENGTH_SHORT).show();

            }
            @Override public void onDeviceDisconnected(BluetoothDevice device, String message) {
                Toast.makeText(getApplicationContext(),"device Disconnect :"+device.getName(),Toast.LENGTH_SHORT).show();

            }
            @Override public void onMessage(String message) {
                Toast.makeText(getApplicationContext(),"Message:",Toast.LENGTH_SHORT).show();

            }
            @Override public void onError(String message) {
                Toast.makeText(getApplicationContext(),"On error",Toast.LENGTH_SHORT).show();

            }
            @Override public void onConnectError(final BluetoothDevice device, String message) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"on Connection Error :"+device.getName(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }

    private Boolean connect(BluetoothDevice bdDevice) {
        Boolean bool = false;
        try {
            Log.i("Log", "service method is called ");
            Class cl = Class.forName("android.bluetooth.BluetoothDevice");
            Class[] par = {};
            Method method = cl.getMethod("createBond", par);
            Object[] args = {};
            bool = (Boolean) method.invoke(bdDevice);//, args);// this invoke creates the detected devices paired.
            //Log.i("Log", "This is: "+bool.booleanValue());
            //Log.i("Log", "devicesss: "+bdDevice.getName());
        } catch (Exception e) {
            Log.i("Log", "Inside catch of serviceFromDevice Method");
            e.printStackTrace();
        }
        return bool.booleanValue();
    };
}
