package com.example.kanbu.OBD_Connect;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanbu.ConnectViewModel;
import com.example.kanbu.R;
import com.example.kanbu.databinding.BluetoothconnectDeviceItemBinding;
import com.example.kanbu.databinding.FragmentConnectBinding;
import com.example.kanbu.databinding.FragmentConnectBindingImpl;

import java.util.ArrayList;

public class ConnectAdapter extends RecyclerView.Adapter<ConnectAdapter.ViewHolder> {

    ArrayList<String> deviceAddressList;
    ArrayList<String> deviceNameList;
    BluetoothAdapter bluetoothAdapter;
    Context context;
    
    // 생성자
    public ConnectAdapter(ArrayList<String> deviceAddressList, ArrayList<String> deviceNameList,
                          BluetoothAdapter bluetoothAdapter, Context context){
        this.deviceAddressList = deviceAddressList;
        this.deviceNameList = deviceNameList;
        this.bluetoothAdapter = bluetoothAdapter;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.bluetoothconnect_device_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItem(deviceAddressList.get(position), deviceNameList.get(position));
    }

    @Override
    public int getItemCount() {
        return deviceAddressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindItem(String deviceAddressList, String deviceNameList){
            BluetoothconnectDeviceItemBinding bluetoothconnectDeviceItemBinding = DataBindingUtil.bind(itemView);
            bluetoothconnectDeviceItemBinding.setConnectViewModel(new ConnectViewModel(context, deviceNameList, deviceAddressList, bluetoothAdapter,"연결 안됨"));
        }
    }
}
