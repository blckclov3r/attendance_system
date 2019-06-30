package com.example.blckclov3r.arduino_firebase_nodemcu.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.DateAccessModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;

import java.util.List;

public class DoorAccessAdapter extends RecyclerView.Adapter<DoorAccessAdapter.ViewHolder>{
    private List<DateAccessModel> model;
    private Context context;

    public DoorAccessAdapter(List<DateAccessModel> dateAccess, Context context){
        this.model = dateAccess;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_door_access_list,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        DateAccessModel dateAccessModel = model.get(i);
        viewHolder.date_access.setText(dateAccessModel.getDate_access());
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView date_access;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date_access = (TextView) itemView.findViewById(R.id.custom_door_access_textView);
        }
    }
}
