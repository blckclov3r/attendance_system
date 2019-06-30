package com.example.blckclov3r.arduino_firebase_nodemcu.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;

import java.util.List;

public class RoomRecyclerAdapter extends RecyclerView.Adapter<RoomRecyclerAdapter.ViewHolder> {

    private List<RoomModel> roomData;
    private Context context;

    public RoomRecyclerAdapter(List<RoomModel> roomData, Context context) {
        this.roomData = roomData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_roomlist, viewGroup, false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        RoomModel roomModel = roomData.get(i);
        viewHolder.scheduleCode_textView.setText("Schedule code: " + roomModel.getSchedCode());
        viewHolder.roomCode_textView.setText("Room code: " + roomModel.getRoomCode());
        viewHolder.subjectCode_textView.setText("Subject code: " + roomModel.getSubjectCode());

    }

    @Override
    public int getItemCount() {
        return roomData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        private TextView scheduleCode_textView;
        private TextView roomCode_textView;
        private TextView subjectCode_textView;
        public OnRoomCLick listener;

        public ViewHolder(@NonNull View itemView,OnRoomCLick listener) {
            super(itemView);
            view = itemView;
            this.listener = listener;
            scheduleCode_textView = (TextView) view.findViewById(R.id.scheduleCode_textView);
            roomCode_textView = (TextView) view.findViewById(R.id.roomCode_textView);
            subjectCode_textView = (TextView) view.findViewById(R.id.subjectCode_textView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(listener!=null) {
                listener.onCLick(position);
            }
        }
    }

    public interface OnRoomCLick{
        void onCLick(int position);
    }
    private OnRoomCLick mListener;
    public void setItemClickListener(OnRoomCLick listener) {
        mListener = listener;
    }
}
