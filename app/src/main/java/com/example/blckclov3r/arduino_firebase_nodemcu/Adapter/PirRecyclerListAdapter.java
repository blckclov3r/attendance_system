package com.example.blckclov3r.arduino_firebase_nodemcu.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.PirModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;

import java.util.List;

public class PirRecyclerListAdapter extends RecyclerView.Adapter<PirRecyclerListAdapter.ViewHolder> {
    private List<PirModel> pirList;
    private Context context;

    public PirRecyclerListAdapter(List<PirModel> pirList,Context context){
        this.pirList = pirList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_pir_access_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PirModel pirModel = pirList.get(i);
        viewHolder.sensor_textView.setText(pirModel.getSensor());
    }

    @Override
    public int getItemCount() {
        return pirList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView sensor_textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sensor_textView = (TextView) itemView.findViewById(R.id.custom_pir_access_textView);
        }
    }
}
