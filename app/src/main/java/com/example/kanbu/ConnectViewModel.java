package com.example.kanbu;

import android.content.Context;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ConnectViewModel extends ViewModel {
    
    public MutableLiveData<String> connectState = new MutableLiveData<>();
    public ObservableField<String> deviceName= new ObservableField<>();
    public ObservableField<String> deviceAddress= new ObservableField<>();

    public ConnectViewModel(String deviceName, String deviceAddress){
        this.deviceName.set(deviceName);
        this.deviceAddress.set(deviceAddress);
        // 연결 상태에 사용될 데이터 최초 삽입
        connectState.setValue("연결 안됨");
    }
}