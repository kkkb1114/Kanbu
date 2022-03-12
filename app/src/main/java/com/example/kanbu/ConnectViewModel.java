package com.example.kanbu;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ConnectViewModel {

    public ObservableField<String> connectState = new ObservableField<>();
    public ObservableField<String> deviceName= new ObservableField<>();
    public ObservableField<String> deviceAddress= new ObservableField<>();
    Context context;

    public ConnectViewModel(Context context, String deviceName, String deviceAddress, String connectState){
        this.context = context;
        this.deviceName.set(deviceName);
        this.deviceAddress.set(deviceAddress);
        this.connectState.set(connectState);
    }

    public void changeConnectState(View view){
        Toast myToast = Toast.makeText(context,"클릭", Toast.LENGTH_SHORT);
        myToast.show();
        Log.i("클릭", "클릭");
        connectState.set("연결됨");
    }
}