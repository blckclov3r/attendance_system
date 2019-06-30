package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage.FirebaseHelper;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.AccessFS.AccessActivity_FullScreen;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.AccessFS.FullScreen_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class AccessFragment extends Fragment {

    private static final String COMMON_TAG = "abrenica_nujla";
    private static final String TAG = AccessFragment.class.getSimpleName();

    //components
    private TextView mCourse_tv, mFname_tv, dateTime_tv;
    @SuppressLint("StaticFieldLeak")
    public static SmartMaterialSpinner mAccess_spinner;
    private ImageView mAccess_roomSearch;
    private CircleImageView mStudent_imageView;
    private ImageView mFullscreen_imageView;

    //vars
    private String mDateStr;
    private String mSelectedRoom;
    private DatabaseReference mDatabaseReference;
    private FirebaseHelper mFirebaseHelper;
    private ProgressBar mAccess_progressBar;
    private FullScreen_Singleton mFullscreen;
    private long mLastClickTime = 0;
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public AccessFragment() {
        Log.d(COMMON_TAG, TAG + " AccessFragment() invoked ");
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mFullscreen = FullScreen_Singleton.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(COMMON_TAG, TAG + " onCreateView invoked");
        View view = inflater.inflate(R.layout.access_fragment, container, false);
        context = getActivity();
        mFirebaseHelper = FirebaseHelper.getInstance(mDatabaseReference, getActivity());

        mCourse_tv = (TextView) view.findViewById(R.id.course_textView);
        mFname_tv = (TextView) view.findViewById(R.id.fname_textView);
        dateTime_tv = (TextView) view.findViewById(R.id.android_dateTime_textView);

        mAccess_spinner = (SmartMaterialSpinner) view.findViewById(R.id.access_roomSpinner);
        mAccess_spinner.setSearchable(true);
        mAccess_spinner.setEnableSearchHeader(true);

        mAccess_roomSearch = (ImageView) view.findViewById(R.id.access_roomSearch);
        mStudent_imageView = (CircleImageView) view.findViewById(R.id.student_imageView);
        mAccess_progressBar = (ProgressBar) view.findViewById(R.id.accessFragment_progressBar);
        mFullscreen_imageView = (ImageView) view.findViewById(R.id.accessFullScreen_imageView);
        mAccess_progressBar.setIndeterminate(false);

        return view;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mFullscreen.getImageFS()) {
            mFullscreen_imageView.setEnabled(true);
        } else {
            try {
                Objects.requireNonNull(getActivity()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                mFullscreen_imageView.setEnabled(false);
                mFullscreen_imageView.setVisibility(View.INVISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mFirebaseHelper.retrieveRoomCode();
        mFname_tv.setText("------ - -----");
        mCourse_tv.setText("------");
        dateTime_tv.setText("------");
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date GetDate = new Date();
        mDateStr = timeStampFormat.format(GetDate);
        mDateStr = mDateStr.replace("/", "-");
        ArrayAdapter<String> areasAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, mFirebaseHelper.retrieveRoomCode());
        areasAdapter.setDropDownViewResource(R.layout.spinner_item);
        mAccess_spinner.setAdapter(areasAdapter);

        mAccess_roomSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                try {
                    mSelectedRoom = mAccess_spinner.getSelectedItem().toString();
                    Toast("Room: " + mSelectedRoom + " has been set");
                    if (mSelectedRoom.isEmpty()) {
                        Toast("Please check your internet connection");
                    } else {
                        mFirebaseHelper.retrieveAccessFragment(mFname_tv, mCourse_tv, dateTime_tv,
                                mDateStr, mSelectedRoom, mStudent_imageView, mAccess_progressBar);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(COMMON_TAG, TAG + ", Exception: mAccess_roomSearch " + e.getMessage());
                }
            }
        });

        mFullscreen_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                mFullscreen_imageView.setEnabled(mFullscreen.getImageFS());
                Intent intent = new Intent(getActivity(), AccessActivity_FullScreen.class);
                startActivity(intent);

            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(COMMON_TAG, TAG + " onDetach invoked");
    }


    private void Toast(String s) {
        Toasty.normal(Objects.requireNonNull(getActivity()), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(COMMON_TAG,TAG+" onDestroy");
    }

}


