package com.example.kanbu.DPF_Monitoring;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanbu.OBD_Connect.ConnectAdapter;
import com.example.kanbu.OBD_Connect.ConnectFragment;
import com.example.kanbu.R;
import com.example.kanbu.databinding.FragmentDpfmonitoringBinding;
import com.example.kanbu.onBackPressedListener;

import java.util.ArrayList;

public class DPF_MonitoringFragment extends Fragment implements onBackPressedListener {

    FragmentDpfmonitoringBinding fragmentDpfmonitoringBinding;
    ArrayList<String> data_titleList = new ArrayList<>();
    ArrayList<String> data_dataList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dpfmonitoring, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // MVVM 패턴을 위한 DataBindingUtil 세팅 메소드
        setMVVM(view);
        setDataList();
        setRecyclerView();
    }

    //todo mvvm 아키텍처를 사용하기 위한 DataBinding, Lifecycle, ViewModel세팅
    public void setMVVM(View view){
        // 해당 프래그먼트에 대한 데이터바인딩 객체 생성 *DataBindingUtil뒤에 프래그먼트는 bind를 사용하며 액티비티는 setContentView를 사용해준다.* 이유 조사할 것
        fragmentDpfmonitoringBinding = DataBindingUtil.bind(view);
        // 뷰모델 객체 생성 *requireActivity()는 프래그먼트를 가지고있는 액티비티를 가리키는 것이다.*
        //connectViewModel = ViewModelProviders.of(requireActivity()).get(ConnectViewModel.class);
        // 데이터바인딩 객체에 연결된 액티비티에 데이터가 변경되면 바로 감지하도록 setLifecycleOwner(부모액티비티)를 세팅해준다.
        //fragmentDpfmonitoringBinding.setLifecycleOwner(requireActivity());
        // 데이터바인딩 객체에 뷰모델을 세팅해준다.
        //fragmentDpfmonitoringBinding.setConnectViewModel(connectViewModel);
    }

    // 리사이클러뷰 세팅
    public void setRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        fragmentDpfmonitoringBinding.rvDpflist.setLayoutManager(linearLayoutManager);

        // 어뎁터 세팅
        DPF_MonitoringAdapter dpf_monitoringAdapter = new DPF_MonitoringAdapter(data_titleList, data_dataList, requireContext());
        fragmentDpfmonitoringBinding.rvDpflist.setAdapter(dpf_monitoringAdapter);
    }

    // 리스트에 들어갈 데이터 세팅
    public void setDataList(){
        data_titleList.add("DPF 포집량");
        data_titleList.add("DPF 주행거리");
        data_titleList.add("DPF 온도");
        data_dataList.add("???");
        data_dataList.add("???");
        data_dataList.add("???");
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
        fragmentManager.beginTransaction().remove(DPF_MonitoringFragment.this).commit();
        fragmentManager.popBackStack();
    }
}
