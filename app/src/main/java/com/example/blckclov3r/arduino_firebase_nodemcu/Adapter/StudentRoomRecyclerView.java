package com.example.blckclov3r.arduino_firebase_nodemcu.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage.FirebaseHelper;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class StudentRoomRecyclerView extends RecyclerView.Adapter<StudentRoomRecyclerView.ViewHolder> {

    private static final String TAG = StudentRoomRecyclerView.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private List<StudentModel> studentData;
    private Context context;


    public interface StudentRoomClickInterface {
        void itemClick(int position);
    }
    private StudentRoomClickInterface mListener;

    public void setStudentRoomClickInterface(StudentRoomClickInterface listener){
        this.mListener = listener;
    }

    public StudentRoomRecyclerView(List<StudentModel> studentData, Context context) {
        this.studentData = studentData;
        this.context = context;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseHelper.getInstance(databaseReference,context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_studentroom_list, viewGroup, false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        StudentModel studentModel = studentData.get(i);
        viewHolder.subjectCode.setText("Subject code: " + studentModel.getSubjectCode());
        viewHolder.schedCode.setText("Schedule code: " + studentModel.getSchedCode());
        viewHolder.weekDay.setText("Weekday: " + studentModel.getWeekDay());
        viewHolder.startTime.setText("Start time: " + studentModel.getStartTime());
        viewHolder.endTime.setText("End time: " + studentModel.getEndTime());

    }

    @Override
    public int getItemCount() {
        return studentData.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        private TextView subjectCode;
        private TextView schedCode;
        private TextView weekDay;
        private TextView startTime;
        private TextView endTime;
        private StudentRoomClickInterface listener;
        public ViewHolder(@NonNull View itemView,StudentRoomClickInterface listener) {
            super(itemView);
            this.listener = listener;
            view = itemView;
            subjectCode = (TextView) view.findViewById(R.id.custom_subjectCode_textView);
            schedCode = (TextView) view.findViewById(R.id.custom_schedCode_textView);
            weekDay = (TextView) view.findViewById(R.id.custom_weekday_textView);
            startTime = (TextView) view.findViewById(R.id.custom_startTime_textView);
            endTime = (TextView) view.findViewById(R.id.custom_endTime_textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
             if(listener != null && position != RecyclerView.NO_POSITION){
                 listener.itemClick(position);
             }
        }


    }

    private void Toast(String s) {
        Toasty.info(context, s, Toast.LENGTH_SHORT).show();
    }
}
