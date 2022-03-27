package com.example.kanbu;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kanbu.OBD_Connect.ConnectedThread;

import java.util.ArrayList;
import java.util.UUID;

public class ConnectViewModel {

    public ObservableField<String> connectState = new ObservableField<>();
    public ObservableField<String> deviceName = new ObservableField<>();
    public ObservableField<String> deviceAddress = new ObservableField<>();
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    BluetoothAdapter bluetoothAdapter;
    Context context;
    boolean bt_flag = true;
    ConnectedThread connectedThread; // 연결 동작을 위한 연결 스레드 클래스

    public ConnectViewModel(Context context, String deviceName, String deviceAddress, BluetoothAdapter bluetoothAdapter,String connectState){
        this.context = context;
        this.bluetoothAdapter = bluetoothAdapter;
        this.deviceName.set(deviceName);
        this.deviceAddress.set(deviceAddress);
        this.connectState.set(connectState);
    }

    public void changeConnectState(View view){
        Toast myToast = Toast.makeText(context,"연결 시도", Toast.LENGTH_SHORT);
        connectState.set("연결 중");
        myToast.show();
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress.get());
        try {
            BluetoothSocket bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
            bluetoothSocket.connect();
            connectedThread = new ConnectedThread(bluetoothSocket);
            connectedThread.run();
        } catch (Exception e) {
            bt_flag = false;
            Toast myToast2 = Toast.makeText(context,"연결 실패", Toast.LENGTH_SHORT);
            connectState.set("연결 실패");
            myToast2.show();
            e.printStackTrace();
        }

        if (bt_flag){
            Toast myToast3 = Toast.makeText(context,"연결 성공", Toast.LENGTH_SHORT);
            myToast3.show();
            connectState.set("연결됨");
        }
    }
}