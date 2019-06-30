package com.example.blckclov3r.arduino_firebase_nodemcu.NavFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.Adapter.AboutRecyclerAdapter;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.NavFragments.FloatingActionButton.AboutDialog;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class AboutFragment extends Fragment {
    private static final String TAG = AboutFragment.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private List<StudentModel> mStudentList;
    private AboutRecyclerAdapter mAdapter;
    private DatabaseReference mDatabaseReference;
    private Context mContext;
    private ProgressBar mProgressbar;
    private StudentModel mStudentModel;
    private FloatingActionButton mFab;

    public AboutFragment() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);

        mStudentModel = new StudentModel();
        mContext = getActivity();
        mStudentList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.aboutRecyclerView);
        mAdapter = new AboutRecyclerAdapter(mStudentList, mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);
        mProgressbar = (ProgressBar) view.findViewById(R.id.about_progressBar);
        mProgressbar.setIndeterminate(false);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mData();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutDialog mDialog = new AboutDialog();
                mDialog.setTargetFragment(AboutFragment.this, 102);
                mDialog.show(Objects.requireNonNull(getFragmentManager()), "AboutDialog");
            }
        });
    }

    private void mData() {
        mDatabaseReference.child("About").child("Project").child("Administrator").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    mProgressbar.setIndeterminate(true);
                    if (dataSnapshot.exists()) {

                        mStudentList.clear();
                        String fullName = String.valueOf(dataSnapshot.child("fullName").getValue());
                        String imageUrl = String.valueOf(dataSnapshot.child("imageUrl").getValue());
                        mStudentModel.setFullName(fullName);
                        mStudentModel.setImageUrl(imageUrl);
                        mStudentList.add(mStudentModel);

                    }
                } catch (Exception e) {
                    Log.e(COMMON_TAG, TAG + " Exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseReference.child("About").child("Project").child("Member").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        mProgressbar.setIndeterminate(true);
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.exists()) {
                                String fullName = ds.child("fullName").getValue().toString();
                                String imageUrl = ds.child("imageUrl").getValue().toString();
                                StudentModel studentModel = new StudentModel();
                                studentModel.setFullName(fullName);
                                studentModel.setImageUrl(imageUrl);
                                mStudentList.add(studentModel);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabaseReference.child("About").child("Project").child("Update").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        mProgressbar.setIndeterminate(true);
                        try {
                            String status = dataSnapshot.child("status").getValue().toString();
                            Toasty.normal(mContext, status, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            mProgressbar.setIndeterminate(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStudentList = null;
        mAdapter = null;
        mDatabaseReference = null;
        mContext = null;
        mProgressbar = null;
        mStudentModel = null;
        mFab = null;
    }


}
