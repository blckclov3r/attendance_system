package com.example.blckclov3r.arduino_firebase_nodemcu.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutRecyclerAdapter extends RecyclerView.Adapter<AboutRecyclerAdapter.ViewHolder> {

    private List<StudentModel> studentData;
    private Context context;

    public AboutRecyclerAdapter(List<StudentModel> studentData,Context context){
        this.studentData = studentData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_about_x,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        StudentModel studentModel = studentData.get(i);
        viewHolder.fullName.setText(studentModel.getFullName());
        Picasso.get().load(studentModel.getImageUrl())
                .noFade()
                .placeholder(R.drawable.person)
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return studentData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView imageView;
        private TextView fullName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.about_imageView);
            fullName = (TextView) itemView.findViewById(R.id.about_fullNameTxtView);
        }
    }
}
