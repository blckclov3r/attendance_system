package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.LogModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.NavFragments.HomeFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import hari.bounceview.BounceView;

public class RoomSearchFragment extends Fragment {

    private static final String TAG = RoomSearchFragment.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private EditText mSchedCode;
    private TextView mRoomCode;
    private TextView mSubjectCode;
    private TextView mUnit;
    private TextView mTerm;
    private TextView mWeekday;
    private TextView mStarttime;
    private TextView mEndtime;
    private ImageView roomSearch_okBtn;
    private Button mSearch_remove_btn;
    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private ProgressBar mProgressbar;
    private long mLastClickTime = 0;

    public RoomSearchFragment() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.roomsearch_fragment, container, false);
        Log.d(COMMON_TAG, TAG + " onCreateView invoked");
        mContext = getActivity();
        mSchedCode = (EditText) view.findViewById(R.id.roomSearch_schedCode_editText);
        mSchedCode.requestFocus();
        BounceView.addAnimTo(mSchedCode);
        mRoomCode = (TextView) view.findViewById(R.id.roomSearch_roomCode_textView);
        mSubjectCode = (TextView) view.findViewById(R.id.roomSearch_subjectCode_textView);
        mUnit = (TextView) view.findViewById(R.id.roomSearch_unit_textView);
        mTerm = (TextView) view.findViewById(R.id.roomSearch_term_textView);
        mWeekday = (TextView) view.findViewById(R.id.roomSearch_weekday_textView);
        mStarttime = (TextView) view.findViewById(R.id.roomSearch_startTime_textView);
        mEndtime = (TextView) view.findViewById(R.id.roomSearch_endTime_textView);
        roomSearch_okBtn = (ImageView) view.findViewById(R.id.roomSearch_okBtn);
        mSearch_remove_btn = (Button) view.findViewById(R.id.roomSearch_removeBtn);
        mProgressbar = (ProgressBar) view.findViewById(R.id.roomSearch_progressBar);
        mProgressbar.setProgress(0);
        mSearch_remove_btn.setEnabled(false);

        roomSearch_okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearch_remove_btn.setEnabled(false);
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                final String code = mSchedCode.getText().toString().trim();
                if (code.equals("") || code.isEmpty()) {
                    mSearch_remove_btn.setEnabled(false);
                    mSchedCode.setError("Schedule code field is empty");
                    mSchedCode.requestFocus();
                    return;
                }


                mDatabaseReference.child("Room").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.exists()) {
                                mProgressbar.setProgress(100);
                                String roomCode_ = dataSnapshot.child("mRoomCode").getValue().toString();
                                String subjectCode_ = dataSnapshot.child("mSubjectCode").getValue().toString();
                                String unit_ = dataSnapshot.child("subjectUnit").getValue().toString();
                                String term_ = dataSnapshot.child("mTerm").getValue().toString();
                                String weekDay_ = dataSnapshot.child("mWeekday").getValue().toString();
                                String startTime_ = dataSnapshot.child("mStarttime").getValue().toString();
                                String endTime_ = dataSnapshot.child("mEndtime").getValue().toString();

                                mRoomCode.setText("  Room code: " + roomCode_);
                                mSubjectCode.setText("  Subject code: " + subjectCode_);
                                mUnit.setText("  Unit: " + unit_);
                                mTerm.setText("  Term: " + term_);
                                mWeekday.setText("  Weekday: " + weekDay_);
                                mStarttime.setText("  Start time: " + startTime_);
                                mEndtime.setText("  End Time: " + endTime_);
                                mSearch_remove_btn.setEnabled(true);
                                String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                LogModel logModel = new LogModel();
                                logModel.setLogMsg("Room code: " + code + " information data successfully retrieved [Room Search]");
                                logModel.setLogAccess(mydate_time);
                                mDatabaseReference.child("AppLog").push().setValue(logModel);
                            } else {
                                String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                LogModel logModel = new LogModel();
                                logModel.setLogMsg("Room code: " + code + " doest not exist [Room Search]");
                                logModel.setLogAccess(mydate_time);
                                mDatabaseReference.child("AppLog").push().setValue(logModel);
                                Toasty.error(mContext, "Schedule Code does not exist", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(COMMON_TAG, TAG + " roomSearch_okBtn " + e.getMessage());
                            mProgressbar.setProgress(0);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressbar.setProgress(0);
                    }
                }, 1200);
            }
        });

        mSearch_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                final String code = mSchedCode.getText().toString().trim();
                if (code.equals("") || code.isEmpty()) {
                    return;
                }
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover once it deleted")
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                mDatabaseReference.child("Room").child(code).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Fragment fragment = new HomeFragment();
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                                        Log.d(COMMON_TAG, TAG + " mSearch_remove_btn: onSuccess remove");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(COMMON_TAG, TAG + " mSearch_remove_btn: onFailure, " + e.getMessage());
                                        mSearch_remove_btn.setEnabled(false);
                                        Toasty.warning(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .show();
            }
        });
        BounceView.addAnimTo(mSearch_remove_btn);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(COMMON_TAG, TAG + " onDetach invoked");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
