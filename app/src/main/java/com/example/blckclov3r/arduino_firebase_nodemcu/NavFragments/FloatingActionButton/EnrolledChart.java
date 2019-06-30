package com.example.blckclov3r.arduino_firebase_nodemcu.NavFragments.FloatingActionButton;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.DialogPackage.EnrolledChartDialog;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.PiechartModel_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage.FirebaseHelper;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class EnrolledChart extends Fragment {

    private static final String TAG = EnrolledChart.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private PieChart mPieChart;
    private DatabaseReference mDatabaseReference;
    private Context mContext;
    private PiechartModel_Singleton mPieChart_singleton;
    private TextView mYear_tv;
    private ArrayList<PieEntry> mPieList;
    private ProgressBar mProgressbar;

    public EnrolledChart() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.enrolled_chart, container, false);
        mPieChart_singleton = PiechartModel_Singleton.getInstance();
        mContext = getActivity();
        FirebaseHelper firebaseHelper = FirebaseHelper.getInstance(mDatabaseReference, mContext);
        mPieChart = (PieChart) view.findViewById(R.id.enrolled_pieChart);
        mYear_tv = (TextView) view.findViewById(R.id.year_textView);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        mProgressbar = (ProgressBar) view.findViewById(R.id.pieChart_progressBar);
        mProgressbar.setIndeterminate(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnrolledChartDialog mDialog = new EnrolledChartDialog();
                mDialog.setTargetFragment(EnrolledChart.this, 101);
                mDialog.show(getFragmentManager(), "EnrolledChartDialog");
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(true);
        mPieChart.getDescription().setTextColor(Color.WHITE);
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        mPieChart.setDragDecelerationFrictionCoef(80f);
        mPieChart.setCenterTextColor(Color.LTGRAY);
        mPieChart.setCenterText("% Year Enrolled");
        mPieChart.setDrawHoleEnabled(false);
//        mPieChart.setHoleColor(R.color.transparent);
//        mPieChart.setTransparentCircleRadius(100);
        mPieList = new ArrayList<>();

        mDatabaseReference.child("PieChart").child("Enrolled").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mPieList.clear();
                    try {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.exists()) {
                                String key = ds.getKey();
                                String yearEnrolled = ds.child("yearEnrolled").getValue().toString();
                                mPieChart_singleton.setPie_key(key);
                                mPieChart_singleton.setPie_number(Integer.valueOf(yearEnrolled));
                                Log.d(COMMON_TAG, TAG + " key: " + mPieChart_singleton.getPie_key() + ", " + mPieChart_singleton.getPie_number());
                                mPieList.add(new PieEntry((float) mPieChart_singleton.getPie_number(), mPieChart_singleton.getPie_key()));
                                if (!mPieChart_singleton.isToastRepeat()) {
                                    Toasty.info(mContext, "Year: " + mPieChart_singleton.getPie_key() + ", Enrolled: "
                                            + mPieChart_singleton.getPie_number(), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                mPieList.add(new PieEntry((float) 100, "There's something wrong in our database"));
                            }

                        }
                        Description description = new Description();
                        description.setText("Enrolled");
                        description.setTextSize(15);
                        description.setTextColor(Color.LTGRAY);
                        mPieChart.setDescription(description);

//                        mPieChart.animateY(500, Easing.EasingOption.EaseInOutCubic);
                        mPieChart.animateY(1000, Easing.EasingOption.EaseOutQuad);

                        PieDataSet dataSet = new PieDataSet(mPieList, "% of Student Enrolled");
                        dataSet.getValueTextColor(Color.WHITE);
                        dataSet.setSliceSpace(5f);
                        dataSet.setSelectionShift(7f);
//                    dataSet.setColors(ColorTemplate.PASTEL_COLORS);
                        dataSet.setColors(new int[]{R.color.two_, R.color.one_, R.color.three_, R.color.four_, R.color.dark, R.color.colorPrimaryDark}
                                , mContext);
                        PieData data = new PieData((dataSet));
                        data.setValueTextSize(10f);
                        data.setValueTextColor(Color.WHITE);
                        mPieChart.setData(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mDatabaseReference.child("PieChart").child("Enrolled").child(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String lyear = dataSnapshot.child("yearEnrolled").getValue().toString();
                                if (!lyear.isEmpty()) {
                                    mYear_tv.setText(lyear);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        mPieChart_singleton.setToastRepeat(true);
                    }
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
        mPieChart = null;
        mDatabaseReference = null;
        mContext = null;
        mPieChart_singleton = null;
        mYear_tv = null;
        mPieList = null;
        mProgressbar = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
