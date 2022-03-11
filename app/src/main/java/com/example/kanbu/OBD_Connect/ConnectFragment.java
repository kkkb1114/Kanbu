package com.example.kanbu.OBD_Connect;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ConnectFragment extends Fragment implements onBackPressedListener {

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
        FindConnectableBluetooth();
    }

    //todo mvvm 아키텍처를 사용하기 위한 DataBinding, Lifecycle, ViewModel세팅
    public void setMVVM(View view){
        // 뷰모델 객체 생성 *requireActivity()는 프래그먼트를 가지고있는 액티비티를 가리키는 것이다.*
        connectViewModel = ViewModelProviders.of(requireActivity()).get(ConnectViewModel.class);
        // 해당 프래그먼트에 대한 데이터바인딩 객체 생성 *DataBindingUtil뒤에 프래그먼트는 bind를 사용하며 액티비티는 setContentView를 사용해준다.* 이유 조사할 것
        fragmentConnectBinding = DataBindingUtil.bind(view);
        // 데이터바인딩 객체에 연결된 액티비티에 데이터가 변경되면 바로 감지하도록 setLifecycleOwner(부모액티비티)를 세팅해준다.
        fragmentConnectBinding.setLifecycleOwner(requireActivity());
        // 데이터바인딩 객체에 뷰모델을 세팅해준다.
        fragmentConnectBinding.setConnectViewModel(connectViewModel);
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

    // 블루투스 활성화 *블루투스가 활성화 되어있지 않으면 요청을 한다. *
    public void setBluetooth(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null){
            Log.i("bluetoothAdapter.toString()", bluetoothAdapter.toString());

            Intent Intent_enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(Intent_enableBluetooth, REQUEST_ENABLE_BT);
        }
    }

    public void FindConnectableBluetooth(){
        // 리사이클러뷰에 적용할 리니어 레이아웃 세팅
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        fragmentConnectBinding.rvConnectableDevices.setLayoutManager(linearLayoutManager);

        ArrayList<String> deviceAddressList = new ArrayList<>();
        ArrayList<String> deviceNameList = new ArrayList<>();
        ArrayList<ConnectViewModel> connectViewModelArrayList = new ArrayList<>();

        deviceAddressList.add("asd");
        deviceNameList.add("123");
        connectViewModelArrayList.add(new ConnectViewModel("qwe", "zxc"));
        // 어뎁터 세팅
        ConnectAdapter connectAdapter = new ConnectAdapter(deviceAddressList, deviceNameList, connectViewModelArrayList, requireContext());

        fragmentConnectBinding.rvConnectableDevices.setAdapter(connectAdapter);
    }
}
