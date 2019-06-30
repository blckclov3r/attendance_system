package com.example.blckclov3r.arduino_firebase_nodemcu.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.LogModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;

import java.util.List;

public class LogRecyclerAdapter extends RecyclerView.Adapter<LogRecyclerAdapter.ViewHolder>{

    private List<LogModel> logList;
    private Context context;

    public LogRecyclerAdapter(List<LogModel> logList,Context context){
        this.logList = logList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_log,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        LogModel logModel = logList.get(i);
        viewHolder.logMsg_textView.setText(logModel.getLogMsg());
        viewHolder.logAccess_textView.setText(logModel.getLogAccess());
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView logMsg_textView;
        public TextView logAccess_textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logMsg_textView = (TextView) itemView.findViewById(R.id.logMsg_textView);
            logAccess_textView = (TextView) itemView.findViewById(R.id.logAccess_textView);

        }
    }
}
