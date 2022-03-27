package com.example.kanbu;

import androidx.databinding.ObservableField;

public class DPF_MonitoringViewModel {

    public ObservableField<String> data_state = new ObservableField<>(); // 이미지 src @drawble로 경로를 문자열로 보낼 생각이다.
    public ObservableField<String> data_title= new ObservableField<>();
    public ObservableField<String> data_data= new ObservableField<>();

    public DPF_MonitoringViewModel(String data_title, String data_data){
        this.data_title.set(data_title);
        this.data_data.set(data_data);
    }
}
