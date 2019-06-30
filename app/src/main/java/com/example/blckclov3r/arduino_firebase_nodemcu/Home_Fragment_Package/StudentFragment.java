package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.LogModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage.FirebaseHelper;
import com.example.blckclov3r.arduino_firebase_nodemcu.PickerPackage.DatePickerFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import hari.bounceview.BounceView;

import static android.app.Activity.RESULT_OK;

public class StudentFragment extends Fragment {

    private static final String TAG = StudentFragment.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    @SuppressLint("StaticFieldLeak")
    public static EditText mUid_et;
    @SuppressLint("StaticFieldLeak")
    public static SmartMaterialSpinner mCodesched_spinner;
    @SuppressLint("StaticFieldLeak")
    public static EditText mFirstname_et;
    @SuppressLint("StaticFieldLeak")
    public static EditText mLastname_et;
    @SuppressLint("StaticFieldLeak")
    public static EditText mMiddlename_et;
    @SuppressLint("StaticFieldLeak")
    public static EditText mYear_et;
    @SuppressLint("StaticFieldLeak")
    public static EditText mContact_et;
    @SuppressLint("StaticFieldLeak")
    public static EditText mAddress_et;
    @SuppressLint("StaticFieldLeak")
    public static EditText mCourse_et;
    @SuppressLint("StaticFieldLeak")
    public static ImageView mDob_imageView;
    private CircleImageView mStudent_imageView;
    @SuppressLint("StaticFieldLeak")
    public static TextView mDob_tv;
    private Button mInsert_btn;
    private ImageView mCheckUid;

    //vars
    private DatabaseReference databaseReference;
    private FirebaseHelper firebaseHelper;
    @SuppressLint("StaticFieldLeak")
    public static Context mContext;
    private Uri mImage = null;
    private StorageReference storageReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ProgressBar progressBar;
    private String uid;
    private String schedCode;
    private String firstName;
    private String lastName;
    private String middleName;
    private String year;
    private String contact;
    private String address;
    private String dob;
    private String course;
    private String fullName;
    private String student_uid = "";
    private StudentModel_Singleton sm;

    public StudentFragment() {
        Log.d(COMMON_TAG, TAG + "StudentFragment Constructor invoked");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(COMMON_TAG, TAG + " onCreateView invoked");

        View view = inflater.inflate(R.layout.student_fragment, container, false);
        mContext = getActivity();
        firebaseHelper = FirebaseHelper.getInstance(databaseReference, mContext);
        sm = StudentModel_Singleton.getInstance();
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();
        firebaseHelper.retreiveSchedCode();
        mUid_et = (EditText) view.findViewById(R.id.uid_editText);
        mUid_et.requestFocus();

        mCodesched_spinner = (SmartMaterialSpinner) view.findViewById(R.id.schedCode_spinner);
        mCodesched_spinner.setSearchable(true);
        mCodesched_spinner.setEnableSearchHeader(true);

        mFirstname_et = (EditText) view.findViewById(R.id.firstName_editText);
        mLastname_et = (EditText) view.findViewById(R.id.lastName_editText);
        mMiddlename_et = (EditText) view.findViewById(R.id.middleName_editText);
        mYear_et = (EditText) view.findViewById(R.id.year_editText);
        mContact_et = (EditText) view.findViewById(R.id.contact_editText);
        mAddress_et = (EditText) view.findViewById(R.id.address_editText);
        mDob_imageView = (ImageView) view.findViewById(R.id.dob_imageView);
        mDob_tv = (TextView) view.findViewById(R.id.dob_editText);
        mInsert_btn = (Button) view.findViewById(R.id.insert_studentBtn);
        mCourse_et = (EditText) view.findViewById(R.id.course_editText);
        mStudent_imageView = (CircleImageView) view.findViewById(R.id.studentfragment_circleImageView);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_horizontal);
        progressBar.setProgress(0);
        mCheckUid = (ImageView) view.findViewById(R.id.checkUid);


        return view;
    }

    private void openFileChooser() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.d(COMMON_TAG, TAG + ", onActivityResult if condition invoked");
            mImage = data.getData();
            if(mImage != null) {
                Picasso.get().load(mImage).noFade().into(mStudent_imageView);
            }
        }
    }

    private void ToastInfo(String s) {
        Toasty.info(Objects.requireNonNull(getActivity()), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(COMMON_TAG, TAG + "onViewCreated invoked");
        mStudent_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        if (firebaseHelper.retrieveUnregisterdUID()) {
            Log.d(COMMON_TAG, TAG + " firebaseHelper.retrieveUnregisteredUID() invoked success");
        } else {
            Log.d(COMMON_TAG, TAG + " firebaseHelper.retrieveUnregisteredUID() invoked fail");
        }

        mCheckUid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                clearModel();
                student_uid = mUid_et.getText().toString().trim();
                if (student_uid.isEmpty()) {
                    mUid_et.setError("Don't leave this field empty");
                    mUid_et.requestFocus();
                    return;
                }
                progressBar.setProgress(50);
                databaseReference.child("User").child(student_uid)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                progressBar.setProgress(75);
                                try {
                                    if (dataSnapshot.exists()) {

                                        StudentModel studentModel = dataSnapshot.getValue(StudentModel.class);
                                        mFirstname_et.setText("");
                                        mLastname_et.setText("");
                                        mMiddlename_et.setText("");
                                        mYear_et.setText("");
                                        mContact_et.setText("");
                                        mAddress_et.setText("");
                                        mCourse_et.setText("");
                                        mDob_tv.setText("");

                                        mFirstname_et.setText(studentModel.getFirstName());
                                        mLastname_et.setText(studentModel.getLastName());
                                        mMiddlename_et.setText(studentModel.getMiddleName());
                                        mYear_et.setText(studentModel.getYear());
                                        mContact_et.setText(studentModel.getContact());
                                        mAddress_et.setText(studentModel.getAddress());
                                        mCourse_et.setText(studentModel.getCourse());
                                        mDob_tv.setText(studentModel.getDob());

                                        if (!sm.getImageUrl().equals("")) {
                                            sm.setImageUrl(studentModel.getImageUrl());
                                        }

                                        Picasso.get().load(studentModel.getImageUrl())
                                                .noFade()
                                                .placeholder(R.drawable.person)
                                                .into(mStudent_imageView);

                                        progressBar.setProgress(100);
                                        String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                        LogModel logModel = new LogModel();
                                        logModel.setLogMsg("Student id " + student_uid + ", information data successfully retrieved [+Student]");
                                        logModel.setLogAccess("Access date and time: " + mydate_time);
                                        databaseReference.child("AppLog").push().setValue(logModel);
//                                        mStudent_imageView.setEnabled(false);
                                    } else {
                                        progressBar.setProgress(100);
                                        mFirstname_et.setText("");
                                        mLastname_et.setText("");
                                        mMiddlename_et.setText("");
                                        mYear_et.setText("");
                                        mContact_et.setText("");
                                        mAddress_et.setText("");
                                        mCourse_et.setText("");
                                        mDob_tv.setText("");
                                        mStudent_imageView.setImageDrawable(getResources().getDrawable(R.drawable.person));
                                        String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                        LogModel logModel = new LogModel();
                                        logModel.setLogMsg("Student id " + student_uid + ", doest not exist [+Student]");
                                        logModel.setLogAccess("Access date &amp; time: " + mydate_time);
                                        databaseReference.child("AppLog").push().setValue(logModel);
                                        ToastInfo("Student id " + student_uid + " doest not exist");
//                                        mStudent_imageView.setEnabled(true);
                                        clearModel();
                                    }
                                } catch (Exception e) {
                                    Log.d(COMMON_TAG, TAG + " mCheckUid catchblock invoked: " + e.getMessage());
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setProgress(0);
                                    }
                                }, 500);
                                return;
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(0);
                    }
                }, 500);
            }
        });

        mInsert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    uid = mUid_et.getText().toString().trim();
                    schedCode = mCodesched_spinner.getSelectedItem().toString();
                    Log.d(COMMON_TAG,TAG+" ++++++++schedCode: "+schedCode);
                    firstName = mFirstname_et.getText().toString().trim();
                    lastName = mLastname_et.getText().toString().trim();
                    middleName = mMiddlename_et.getText().toString().trim();
                    year = mYear_et.getText().toString().trim();
                    contact = mContact_et.getText().toString().trim();
                    address = mAddress_et.getText().toString().trim();
                    dob = mDob_tv.getText().toString().trim();
                    course = mCourse_et.getText().toString().trim();
                    fullName = firstName + " " + middleName + " " + lastName;
                    if (uid.isEmpty()) {
                        mUid_et.setError("Don't leave this field empty");
                        mUid_et.requestFocus();
                        return;
                    }
                    if (firstName.isEmpty()) {
                        mFirstname_et.setError("Don't leave this field empty");
                        mFirstname_et.requestFocus();
                        return;
                    }
                    if (lastName.isEmpty()) {
                        mLastname_et.setError("Don't leave this field empty");
                        mLastname_et.requestFocus();
                        return;
                    }
                    if (middleName.isEmpty()) {
                        mMiddlename_et.setError("Don't leave this field empty");
                        mMiddlename_et.requestFocus();
                        return;
                    }
                    if (course.isEmpty()) {
                        mCourse_et.setError("Don't leave this filed empty");
                        mCourse_et.requestFocus();
                        return;
                    }
                    if (year.isEmpty()) {
                        mYear_et.setError("Don't leave this field empty");
                        mYear_et.requestFocus();
                        return;
                    }
                    if (contact.isEmpty()) {
                        mContact_et.setError("Don't leave this field empty");
                        mContact_et.requestFocus();
                        return;
                    }
                    if (address.isEmpty()) {
                        mAddress_et.setError("Don't leave this field empty");
                        mAddress_et.requestFocus();
                        return;
                    }
                    if (dob.isEmpty()) {
                        mDob_tv.setError("Don't leave this field empty");
                        mDob_tv.requestFocus();
                        return;
                    }
                    if (schedCode.isEmpty()) {
                        Log.d(COMMON_TAG, TAG + " schedule code is empty, retrieveRoomData");
                        return;
                    }
                    if (sm.getImageUrl().equals("")) {
                        if (firebaseHelper.retreieveRoomData(uid, schedCode, firstName,
                                lastName, middleName, year, contact, address, dob, fullName,
                                course, storageReference, mImage, progressBar)) {
                            Log.d(COMMON_TAG, TAG + " success: [1]");
                        } else {
                            Log.d(COMMON_TAG, TAG + " retrieveRoomData else invoked [1]");
                        }
                    } else {
                        if (firebaseHelper.retreieveRoomData1(uid, schedCode, firstName,
                                lastName, middleName, year, contact, address, dob, fullName,
                                course, progressBar)) {
                            Log.d(COMMON_TAG, TAG + " success: [2]");
                        } else {
                            Log.d(COMMON_TAG, TAG + " retrieveRoomData else invoked [2]");
                        }
                    }
                } catch (Exception e) {
                    Log.d(COMMON_TAG, TAG + " mInsert_btn: " + e.getMessage());
                }
            }
        });
        BounceView.addAnimTo(mInsert_btn);

        ArrayAdapter<String> areasAdapter = new ArrayAdapter<>(mContext, R.layout.spinner_item, firebaseHelper.retreiveSchedCode());
        areasAdapter.setDropDownViewResource(R.layout.spinner_item2);
        mCodesched_spinner.setAdapter(areasAdapter);

        mDob_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "date_picker");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mDob_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "date_picker");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(COMMON_TAG, TAG + " onDetach invoked");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearModel();
        mUid_et = null;
        mCodesched_spinner = null;
        mFirstname_et = null;
        mLastname_et = null;
        mMiddlename_et = null;
        mYear_et = null;
        mContact_et = null;
        mAddress_et = null;
        mCourse_et = null;
        mDob_imageView = null;
        mStudent_imageView = null;
        mDob_tv = null;
        mInsert_btn = null;
        mCheckUid = null;
        databaseReference = null;
        firebaseHelper = null;
        mContext = null;
        mImage = null;
        storageReference = null;
        progressBar = null;
        uid = null;
        schedCode = null;
        firstName = null;
        lastName = null;
        middleName = null;
        year = null;
        contact = null;
        address = null;
        dob = null;
        course = null;
        fullName = null;
        student_uid = null;

    }

    private void clearModel() {
        new StudentModel("", "", "", "", "",
                "", "", "", "", "", "", "",
                "", "", "", "", "", "", "");
        new RoomModel("", "", "",
                "", "", "", "", "");
        RoomModel_Singleton roomModel_singleton = RoomModel_Singleton.getInstance();
        roomModel_singleton.clear();
        sm.studentModel_clear();
    }


}