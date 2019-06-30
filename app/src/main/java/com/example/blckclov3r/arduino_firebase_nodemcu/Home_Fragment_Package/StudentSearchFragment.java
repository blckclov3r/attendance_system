package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package;

import android.graphics.Color;
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

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage.FirebaseHelper;
import com.example.blckclov3r.arduino_firebase_nodemcu.NavFragments.HomeFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import hari.bounceview.BounceView;

public class StudentSearchFragment extends Fragment {
    private static final String TAG = StudentSearchFragment.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private EditText mSearch_et;
    private CircleImageView mImageView;
    private TextView mFname_tv;
    private TextView mCourse_tv;
    private TextView mYear_tv;
    private TextView mDob_tv;
    private TextView mAddress_tv;
    private TextView mContact_tv;
    private Button mSearch_remove_btn;
    private DatabaseReference mDatabaseReference;
    private FirebaseHelper mFirebaseHelper;
    private ProgressBar mProgressbar;
    private long mLastClickTime = 0;


    public StudentSearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_search, container, false);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();
        mFirebaseHelper = FirebaseHelper.getInstance(mDatabaseReference, getActivity());
        mSearch_et = (EditText) view.findViewById(R.id.studentSearch_editText);
        mSearch_et.requestFocus();
        BounceView.addAnimTo(mSearch_et);
        mImageView = (CircleImageView) view.findViewById(R.id.studentSearch_image);
        mFname_tv = (TextView) view.findViewById(R.id.studentSearch_fname_textView);
        mCourse_tv = (TextView) view.findViewById(R.id.studentSearch_course_textView);
        mYear_tv = (TextView) view.findViewById(R.id.studentSearch_year_textView);
        mDob_tv = (TextView) view.findViewById(R.id.studentSearch_dob_textView);
        mAddress_tv = (TextView) view.findViewById(R.id.studentSearch_address_textView);
        mContact_tv = (TextView) view.findViewById(R.id.studentSearch_contact_textView);
        mSearch_remove_btn = (Button) view.findViewById(R.id.student_search_removeBtn);
        mProgressbar = (ProgressBar) view.findViewById(R.id.studentSearch_progressBar);
        mProgressbar.setProgress(0);
        mProgressbar.setIndeterminate(false);
        mSearch_remove_btn.setEnabled(false);
        mSearch_remove_btn.setTextColor(Color.GRAY);
        ImageView mSearch_imageView = (ImageView) view.findViewById(R.id.studentSearch_search_imageView);

        mSearch_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                mProgressbar.setIndeterminate(true);
                mSearch_remove_btn.setEnabled(false);
                final String student_uid = mSearch_et.getText().toString().trim();
                if (student_uid.equals("")) {
                    Log.d(COMMON_TAG, TAG + " mSearch_remove_btn: string student uid is empty");
                    return;
                }
                mDatabaseReference.child("UNIQUE_ID").child(student_uid).removeValue();
                mDatabaseReference.child("User").child(student_uid).removeValue().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mSearch_et.setText("");
                        mSearch_et.setError("There's something wrong in the database, please report to the administrator immediately");
                        Log.e(COMMON_TAG, TAG + " removeValue: " + e.getMessage());
                        mProgressbar.setIndeterminate(false);
                        mProgressbar.setProgress(0);
                        mSearch_remove_btn.setTextColor(Color.DKGRAY);
                        mSearch_remove_btn.setEnabled(false);
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Fragment newFragment = new HomeFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, newFragment);
                        transaction.commit();
                        Log.d(COMMON_TAG, TAG + " Student successfully remove");
                        mProgressbar.setIndeterminate(false);
                        mProgressbar.setProgress(0);
                        mSearch_remove_btn.setTextColor(Color.DKGRAY);
                        mSearch_remove_btn.setEnabled(true);
                    }
                });
            }
        });

        mSearch_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                mProgressbar.setIndeterminate(true);
                mSearch_remove_btn.setEnabled(false);
                String student_uid = mSearch_et.getText().toString().trim();
                if (student_uid.equals("")) {
                    mSearch_et.setError("Don't leave this field empty");
                    mSearch_et.requestFocus();
                    mSearch_remove_btn.setEnabled(false);
                    mProgressbar.setIndeterminate(false);
                    mProgressbar.setProgress(0);
                    return;
                }
                mProgressbar.setIndeterminate(true);
                mFirebaseHelper.searchStudent(student_uid, mImageView, mFname_tv, mCourse_tv,
                        mYear_tv, mDob_tv, mAddress_tv, mContact_tv);

                mSearch_remove_btn.setTextColor(Color.DKGRAY);
                mSearch_remove_btn.setEnabled(true);
                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressbar.setIndeterminate(false);
                        }
                    }, 1200);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        BounceView.addAnimTo(mSearch_remove_btn);
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Objects.requireNonNull(getFragmentManager()).beginTransaction().remove(new StudentSearchFragment()).commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
