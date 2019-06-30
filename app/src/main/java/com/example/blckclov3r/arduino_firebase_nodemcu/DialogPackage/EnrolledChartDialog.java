package com.example.blckclov3r.arduino_firebase_nodemcu.DialogPackage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.blckclov3r.arduino_firebase_nodemcu.Adapter.EnrolledChartAdapter;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.PiechartModel_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnrolledChartDialog extends DialogFragment {

    private static final String TAG = EnrolledChartDialog.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private List<PiechartModel_Singleton> mPieList;
    private DatabaseReference mDatabaseReference;
    private PiechartModel_Singleton mPiechart_singleton;

    public EnrolledChartDialog() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.enrolled_chart_custom_dialog, container, false);
        Context context = getActivity();
        mPieList = new ArrayList<>();
        mPiechart_singleton = PiechartModel_Singleton.getInstance();
        EnrolledChartAdapter enrolledChartAdapter = new EnrolledChartAdapter(mPieList, context);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.pie_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(enrolledChartAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabaseReference.child("PieChart").child("Enrolled").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String key = ds.getKey();
                        String yearEnrolled = Objects.requireNonNull(ds.child("yearEnrolled").getValue()).toString();
                        mPiechart_singleton = ds.getValue(PiechartModel_Singleton.class);
                        Objects.requireNonNull(mPiechart_singleton).setPie_key(key);
                        mPiechart_singleton.setPie_number(Integer.valueOf(yearEnrolled));
                        Log.d(COMMON_TAG, TAG + " key: " + mPiechart_singleton.getPie_key() + ", " + mPiechart_singleton.getPie_number());
                        mPieList.add(mPiechart_singleton);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPieList = null;
        mDatabaseReference = null;
        mPiechart_singleton = null;
    }
}
