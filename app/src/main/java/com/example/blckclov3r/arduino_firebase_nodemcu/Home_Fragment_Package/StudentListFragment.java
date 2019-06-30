package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.blckclov3r.arduino_firebase_nodemcu.Adapter.StudentRecyclerAdapter;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hari.bounceview.BounceView;

public class StudentListFragment extends Fragment implements StudentRecyclerAdapter.StudentClickListener {

    private static final String TAG = StudentListFragment.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";


    private List<StudentModel> mStudentList;
    private StudentRecyclerAdapter mAdapter;
    private DatabaseReference mDatabaseReference;
    private ProgressBar mProgressbar;
    private ValueEventListener mValueEventListener;
    private Query mQuery;

    public StudentListFragment() {
        Log.d(COMMON_TAG, TAG + " StudentListFragment() invoked");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.studentlist_fragment, container, false);
        Log.d(COMMON_TAG, TAG + " onCreateView invoked");
        mStudentList = new ArrayList<>();
        Context context = getActivity();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.studentlist_recyclerView);
        mAdapter = new StudentRecyclerAdapter(mStudentList, context);
        mAdapter.setStudentClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter);
        mProgressbar = (ProgressBar) view.findViewById(R.id.studentlist_progressBar);
        EditText studentSearch = (EditText) view.findViewById(R.id.studentSearch);
        BounceView.addAnimTo(studentSearch);

        mProgressbar.setIndeterminate(true);
        studentSearch.requestFocus();

        studentSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                try {
                    mQuery = mDatabaseReference.child("User").orderByChild("fullName")
                            .startAt(String.valueOf(s).toUpperCase().trim())
                            .endAt(String.valueOf(s) + "\uf8ff");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    mQuery = mDatabaseReference.child("User").orderByChild("fullName")
                            .startAt(String.valueOf(s).toUpperCase().trim())
                            .endAt(String.valueOf(s) + "\uf8ff");
                    mQuery.addValueEventListener(mValueEventListener);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressbar.setIndeterminate(false);
                    }
                }, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    mQuery = mDatabaseReference.child("User").orderByChild("fullName")
                            .startAt(String.valueOf(s).toUpperCase().trim())
                            .endAt(String.valueOf(s) + "\uf8ff");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });



        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(COMMON_TAG, TAG + " onViewCreated invoked");
        mValueEventListener = mDatabaseReference.child("User")
                .orderByChild("fullName")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if(mStudentList!=null) {
                                mStudentList.clear();
                            }
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.hasChild("fullName")) {
                                        StudentModel studentModel = ds.getValue(StudentModel.class);
                                        mStudentList.add(studentModel);
                                    }
                                }
                            }

                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e(COMMON_TAG, TAG + ", Exception: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(COMMON_TAG, TAG + " onDestroy");
        mStudentList = null;
        mAdapter = null;
        mDatabaseReference = null;
        mProgressbar = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(COMMON_TAG, TAG + " onDetach");

        if (mDatabaseReference != null) {
            mDatabaseReference.removeEventListener(mValueEventListener);
            mDatabaseReference = null;
        }
        if (mListener != null) {
            mListener = null;
        }

    }



    @Override
    public void itemClick(int position) {
        StudentModel studentModel = mStudentList.get(position);
        mListener.onClickList(studentModel);
    }

    private OnClickStudentList mListener;

    public interface OnClickStudentList {
        void onClickList(StudentModel studentModel);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClickStudentList) {
            mListener = (OnClickStudentList) getActivity();
        } else {
            throw new RuntimeException(context.toString() + " must implement OnClickStudentList");
        }
    }

    public void setOnClickStudentList(OnClickStudentList listener){
        mListener = listener;
    }


}
