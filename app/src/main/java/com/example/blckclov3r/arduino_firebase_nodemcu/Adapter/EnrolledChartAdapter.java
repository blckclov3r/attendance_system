package com.example.blckclov3r.arduino_firebase_nodemcu.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.PiechartModel_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;

import java.util.List;

public class EnrolledChartAdapter extends RecyclerView.Adapter<EnrolledChartAdapter.ViewHolder> {
    private List<PiechartModel_Singleton> pie_list;
    private Context context;

    public EnrolledChartAdapter(List<PiechartModel_Singleton> pie_list,Context context){
        this.pie_list = pie_list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_piechart_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PiechartModel_Singleton piechartModel_singleton = pie_list.get(i);
        viewHolder.pie_key.setText("Year: "+piechartModel_singleton.getPie_key());
        viewHolder.pie_value.setText("Number of Enrollee: "+String.valueOf(piechartModel_singleton.getPie_number()));
    }

    @Override
    public int getItemCount() {
        return pie_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView pie_key;
        private TextView pie_value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pie_key = (TextView) itemView.findViewById(R.id.pie_key);
            pie_value = (TextView) itemView.findViewById(R.id.pie_value);
        }
    }

}
