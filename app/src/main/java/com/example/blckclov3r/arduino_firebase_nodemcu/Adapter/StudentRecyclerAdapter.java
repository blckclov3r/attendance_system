package com.example.blckclov3r.arduino_firebase_nodemcu.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage.FirebaseHelper;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.ViewHolder> {

    private List<StudentModel> mStudentList;
    private Context mContext;
    private StudentClickListener mListener;
    public interface StudentClickListener {
        void itemClick(int position);
    }

    public StudentRecyclerAdapter(List<StudentModel> mStudentList, Context mContext) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        this.mStudentList = mStudentList;
        this.mContext = mContext;
        FirebaseHelper.getInstance(databaseReference, mContext);
    }

    public void setStudentClickListener(StudentClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_studentlist, viewGroup, false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder viewHolder, int i) {
        StudentModel studentModel = mStudentList.get(i);
        viewHolder.fullName.setText("Fullname: " + studentModel.getFullName());
        viewHolder.course.setText("Course: " + studentModel.getCourse());
        viewHolder.uid.setText("Student id: " + studentModel.getUID());

        if (!studentModel.getImageUrl().equals("")) {
            Picasso.get()
                    .load(studentModel.getImageUrl())
                    .noFade()
                    .placeholder(R.drawable.person)
                    .into(viewHolder.imageView_studentList);
        }

    }

    @Override
    public int getItemCount() {
        return mStudentList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View view;
        private TextView fullName;
        private TextView course;
        private TextView uid;
        public StudentClickListener listener;
        private CircleImageView imageView_studentList;

        public ViewHolder(@NonNull View itemView,StudentClickListener listener) {
            super(itemView);
            this.listener = listener;
            view = itemView;
            fullName = (TextView) view.findViewById(R.id.fullName_student_textView);
            course = (TextView) view.findViewById(R.id.course_student_textView);
            uid = (TextView) view.findViewById(R.id.uid_student_textView);
            imageView_studentList = (CircleImageView) view.findViewById(R.id.imageView_studentList);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(listener!=null) {
                listener.itemClick(getAdapterPosition());
            }
        }
    }
}