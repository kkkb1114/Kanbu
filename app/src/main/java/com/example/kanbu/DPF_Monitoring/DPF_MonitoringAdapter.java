package com.example.kanbu.DPF_Monitoring;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kanbu.DPF_MonitoringViewModel;
import com.example.kanbu.R;
import com.example.kanbu.databinding.DpfMonitoringItemBinding;

import java.util.ArrayList;

public class DPF_MonitoringAdapter extends RecyclerView.Adapter<DPF_MonitoringAdapter.ViewHolder> {

    ArrayList<String> data_titleList;
    ArrayList<String> data_dataList;
    Context context;

    public DPF_MonitoringAdapter(ArrayList<String> data_titleList, ArrayList<String> data_dataList, Context context){
        this.data_titleList = data_titleList;
        this.data_dataList = data_dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dpf_monitoring_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItem(data_titleList.get(position), data_dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return data_titleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindItem(String data_title, String data_data){
            DpfMonitoringItemBinding dpfMonitoringItemBinding = DataBindingUtil.bind(itemView);
            dpfMonitoringItemBinding.setDPFMonitoringViewModel(new DPF_MonitoringViewModel(data_title, data_data));
        }
    }
}
