package com.example.kanbu.OBD_Connect;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanbu.ConnectViewModel;
import com.example.kanbu.R;
import com.example.kanbu.databinding.FragmentConnectBinding;
import com.example.kanbu.onBackPressedListener;

import java.util.ArrayList;
import java.util.Set;

public class ConnectFragment extends Fragment implements onBackPressedListener {

    // 이미 페어링 되어있는 블루투스 디바이스들 변수
    Set<BluetoothDevice> pairedDevices;
    ArrayList<String> deviceNameArray = new ArrayList<>();
    ArrayList<String> deviceAddressArray = new ArrayList<>();
    BluetoothAdapter bluetoothAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    Context context;

    // MVVM 패턴 변수
    FragmentConnectBinding fragmentConnectBinding;
    ConnectViewModel connectViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setMVVM(view);
        Request_Location_Permission();
        setBluetooth();
        setRecyclerView();
        FindPairedDevices();
        FindConnectableBluetooth();
    }

    //todo mvvm 아키텍처를 사용하기 위한 DataBinding, Lifecycle, ViewModel세팅
    public void setMVVM(View view){
        // 해당 프래그먼트에 대한 데이터바인딩 객체 생성 *DataBindingUtil뒤에 프래그먼트는 bind를 사용하며 액티비티는 setContentView를 사용해준다.* 이유 조사할 것
        fragmentConnectBinding = DataBindingUtil.bind(view);
        // 뷰모델 객체 생성 *requireActivity()는 프래그먼트를 가지고있는 액티비티를 가리키는 것이다.*
        //connectViewModel = ViewModelProviders.of(requireActivity()).get(ConnectViewModel.class);
        // 데이터바인딩 객체에 연결된 액티비티에 데이터가 변경되면 바로 감지하도록 setLifecycleOwner(부모액티비티)를 세팅해준다.
        //fragmentConnectBinding.setLifecycleOwner(requireActivity());
        // 데이터바인딩 객체에 뷰모델을 세팅해준다.
        //fragmentConnectBinding.setConnectViewModel(connectViewModel);
    }

    // 휴대폰 뒤로가기 버튼 클릭시
    @Override
    public void onBackPressed() {
        goToMain();
    }

    // 프래그먼트 종료
    // 뒤로가기 눌렀을때 액티비티가 사라지는게 아닌 프래그먼트가 종료되도록 하기 위한 메소드
    private void goToMain(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(ConnectFragment.this).commit();
        fragmentManager.popBackStack();
    }

    // 위치 권한 요청
    public void Request_Location_Permission(){
        // Get permission
        String[] permission_list = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(requireActivity(), permission_list, 1);
    }

    public void setBluetooth(){
        // 블루투스 활성화 *블루투스가 활성화 되어있지 않으면 요청을 한다. *
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // 블루투스 권한 요청 *안드로이드 12 이상과 이하를 나누어 놓았다.*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_ADVERTISE,
                            Manifest.permission.BLUETOOTH_CONNECT


                    },
                    1);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH

                    },
                    1);
        }
    }

    public void FindConnectableBluetooth(){

        ArrayList<String> deviceAddressList = new ArrayList<>();
        /*ArrayAdapter<String> deviceNameList = new ArrayAdapter<>();

        deviceAddressList.add("aaa");
        deviceNameList.add("222");
        // 어뎁터 세팅
        ConnectAdapter connectAdapter = new ConnectAdapter(deviceAddressList, deviceNameList, requireContext());

        fragmentConnectBinding.rvConnectableDevices.setAdapter(connectAdapter); */
    }
    public void FindPairedDevices(){
        // 일단 페어링 관련 리스트 변수 초기화 안되어 있으면 초기화
        if (deviceNameArray != null) {
            deviceNameArray.clear();
        }
            if (deviceAddressArray != null && !deviceAddressArray.isEmpty()){
                deviceAddressArray.clear();
            }
            // 초기화 했으니 이제 페어링 된 디바이스들 검색
            pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0){
                // pairedDevices에 들어있는 페어링 된 블루투스 객체를 BluetoothDevice에 차례대로 넣는다.
                for (BluetoothDevice device : pairedDevices){
                    // 블루투스 기기의 Name, MAC Address 둘 중 하나라도 검색되지 않으면 아예 ArrayList에 추가하지않는다.
                    if (device.getName() != null && device.getAddress() != null){
                        String deviceName = device.getName();
                        String deviceAddress = device.getAddress(); // MAC Address
                        deviceNameArray.add(deviceName);
                        deviceAddressArray.add(deviceAddress);
                    }
                }
                // 어뎁터 세팅
                ConnectAdapter connectAdapter = new ConnectAdapter(deviceAddressArray, deviceNameArray, requireContext());
                fragmentConnectBinding.rvConnectableDevices.setAdapter(connectAdapter);
            }
    }

    public void setRecyclerView(){
        // 리사이클러뷰에 적용할 리니어 레이아웃 세팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        fragmentConnectBinding.rvConnectableDevices.setLayoutManager(linearLayoutManager);
    }
}
