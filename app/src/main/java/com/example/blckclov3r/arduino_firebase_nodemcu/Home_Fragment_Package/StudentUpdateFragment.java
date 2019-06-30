package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.TempStudent_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage.FirebaseHelper;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;
import hari.bounceview.BounceView;

import static android.app.Activity.RESULT_OK;

public class StudentUpdateFragment extends Fragment {

    private static final String TAG = StudentUpdateFragment.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImage = null;
    private EditText student_uid;
    private ImageView checkUid;
    private ImageView studentImage;
    private EditText firstName;
    private EditText lastName;
    private EditText middleName;
    private EditText course;
    private EditText year;
    private EditText contact;
    private EditText dob;
    private EditText address;
    private Button updateBtn;
    private ProgressBar progress_horizontal;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;
    private FirebaseHelper firebaseHelper;
    private Context context;
    private long mLastClickTime =0;
    private RoomModel_Singleton roomModel_singleton;
    private StudentModel_Singleton studentModel_singleton;

    public StudentUpdateFragment() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.studentupdate_fragment, container, false);
        context = getActivity();
        firebaseHelper = FirebaseHelper.getInstance(databaseReference,context);
        student_uid = view.findViewById(R.id.studentUpdate_uid_editText);
        student_uid.requestFocus();
        checkUid = view.findViewById(R.id.studentUpdate_checkUid);
        studentImage = view.findViewById(R.id.studentUpdate_imageView);
        firstName = view.findViewById(R.id.studentUpdate_firstName_editText);
        lastName = view.findViewById(R.id.studentUpdate_lastName_editText);
        middleName = view.findViewById(R.id.studentUpdate_middleName_editText);
        course = view.findViewById(R.id.studentUpdate_course_editText);
        year = view.findViewById(R.id.studentUpdate_year_editText);
        contact = view.findViewById(R.id.studentUpdate_contact_editText);
        dob = view.findViewById(R.id.studentUpdate_dob_editText);
        address = view.findViewById(R.id.studentUpdate_address_editText);
        updateBtn = view.findViewById(R.id.studentUpdate_update_studentBtn);
        updateBtn.setEnabled(false);
        progress_horizontal = view.findViewById(R.id.progress_horizontal);
        progress_horizontal.setProgress(0);

        firstName.setEnabled(false);
        firstName.setClickable(false);
        lastName.setEnabled(false);
        lastName.setClickable(false);
        middleName.setEnabled(false);
        middleName.setClickable(false);
        course.setEnabled(false);
        course.setClickable(false);
        year.setEnabled(false);
        year.setClickable(false);
        contact.setEnabled(false);
        contact.setClickable(false);
        dob.setEnabled(false);
        dob.setClickable(false);
        address.setEnabled(false);
        address.setClickable(false);
        studentImage.setEnabled(false);
        updateBtn.setTextColor(Color.GRAY);

        BounceView.addAnimTo(student_uid);
        BounceView.addAnimTo(firstName);
        BounceView.addAnimTo(lastName);
        BounceView.addAnimTo(middleName);
        BounceView.addAnimTo(course);
        BounceView.addAnimTo(year);
        BounceView.addAnimTo(dob);
        BounceView.addAnimTo(address);

        setCheckUid();
        setUpdateBtn();
        studentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        clearModel();
    }

    private void setCheckUid() {
        checkUid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearModel();
                progress_horizontal.setProgress(0);
                updateBtn.setEnabled(false);
                studentImage.setEnabled(false);
                updateBtn.setTextColor(Color.GRAY);
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                final String uid = student_uid.getText().toString().trim();
                if(uid.isEmpty()){
                    updateBtn.setEnabled(false);
                    return;
                }
                progress_horizontal.setProgress(25);
                databaseReference.child("User").child(uid).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            if (dataSnapshot.exists()) {
                                progress_horizontal.setProgress(50);
                                firstName.setEnabled(true);
                                firstName.setClickable(true);
                                lastName.setEnabled(true);
                                lastName.setClickable(true);
                                middleName.setEnabled(true);
                                middleName.setClickable(true);
                                course.setEnabled(true);
                                course.setClickable(true);
                                year.setEnabled(true);
                                year.setClickable(true);
                                contact.setEnabled(true);
                                contact.setClickable(true);
                                dob.setEnabled(true);
                                dob.setClickable(true);
                                address.setEnabled(true);
                                address.setClickable(true);

                                StudentModel studentModel = dataSnapshot.getValue(StudentModel.class);

                                if(!studentModel.getImageUrl().equals("")) {
                                    Picasso.get().load(
                                            studentModel.getImageUrl())
                                            .placeholder(R.drawable.person)
                                            .noFade().into(studentImage);
                                }

                                progress_horizontal.setProgress(75);
                                progress_horizontal.setProgress(75);
                                firstName.setText(studentModel.getFirstName());
                                lastName.setText(studentModel.getLastName());
                                middleName.setText(studentModel.getMiddleName());
                                course.setText(studentModel.getCourse());
                                year.setText(studentModel.getYear());
                                contact.setText(studentModel.getContact());
                                dob.setText(studentModel.getDob());
                                address.setText(studentModel.getAddress());
//                                Toast("Student data successfully retrieved");
                                progress_horizontal.setProgress(100);
                                updateBtn.setEnabled(true);
                                studentImage.setEnabled(true);
                                updateBtn.setTextColor(Color.DKGRAY);
                                firstName.requestFocus();
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress_horizontal.setProgress(0);
                                    }
                                },500);
                                updateBtn.setEnabled(false);
                                Toasty.error(context,"Student id "+uid+" does not exist");
                                Log.e(COMMON_TAG, TAG + ", setCheckUid dataSnapshot else condition invoked");
                                return;
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progress_horizontal.setProgress(0);
                                }
                            },500);
                        } catch (Exception e) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progress_horizontal.setProgress(0);
                                }
                            },500);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progress_horizontal.setProgress(0);
                            }
                        },500);
                    }
                });
            }
        });
    }

    private StorageTask storageTask;
    boolean testUpdateBtn = false;
    private void setUpdateBtn() {
        progress_horizontal.setProgress(0);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000){
//                    return;
//                }
//                mLastClickTime = SystemClock.elapsedRealtime();
                if (storageTask != null && storageTask.isInProgress()) {
                    Toast("Upload in Progress, please wait");
                    return;
                }
                final String uid = student_uid.getText().toString().trim();
                final String _firstName = firstName.getText().toString().trim();
                final String _lastName = lastName.getText().toString().trim();
                final String _middleName = middleName.getText().toString().trim();
                final String _course = course.getText().toString().trim();
                final String _year = year.getText().toString().trim();
                final String _contact = contact.getText().toString().trim();
                final String _dob = dob.getText().toString().trim();
                final String _address = address.getText().toString().trim();

                if(uid.isEmpty()){
                    student_uid.setError("Don't leave this field empty");
                    student_uid.requestFocus();
                    return;
                }
                if(_firstName.isEmpty()){
                    firstName.setError("Don't leave this field empty");
                    firstName.requestFocus();
                    return;
                }
                if(_lastName.isEmpty()){
                    lastName.setError("Don't leave this field empty");
                    lastName.requestFocus();
                    return;
                }
                if(_middleName.isEmpty()){
                    middleName.setError("Don't leave this field empty");
                    middleName.requestFocus();
                    return;
                }
                if(_course.isEmpty()){
                    course.setError("Don't leave this field empty");
                    course.requestFocus();
                    return;
                }
                if(_year.isEmpty()){
                    year.setError("Don't leave this field empty");
                    year.requestFocus();
                    return;
                }
                if(_contact.isEmpty()){
                    contact.setError("Don't leave this field empty");
                    contact.requestFocus();
                    return;
                }
                if(_dob.isEmpty()){
                    dob.setError("Don't leave this field empty");
                    dob.requestFocus();
                    return;
                }
                if(_address.isEmpty()){
                    address.setError("Don't leave this field empty");
                    address.requestFocus();
                    return;
                }
                try {
                    if (mImage != null) {
                        Log.d(COMMON_TAG,TAG+" mImage != null");
                        StorageReference userProfile = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(mImage));
                        storageTask = userProfile.putFile(mImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                databaseReference.child("User").child(uid).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            StudentModel studentModel = new StudentModel();
                                            studentModel.setFirstName(_firstName);
                                            studentModel.setLastName(_lastName);
                                            studentModel.setMiddleName(_middleName);
                                            studentModel.setCourse(_course);
                                            studentModel.setYear(_year);
                                            studentModel.setContact(_contact);
                                            studentModel.setDob(_dob);
                                            studentModel.setAddress(_address);
                                            studentModel.setImageUrl(taskSnapshot.getMetadata().getDownloadUrl().toString());
                                            studentModel.setFullName(_firstName + " " + _middleName + " " + _lastName);
                                            databaseReference.child("User").child(uid).child("imageUrl").setValue(studentModel.getImageUrl());
                                            databaseReference.child("User").child(uid).child("firstName").setValue(studentModel.getFirstName());
                                            databaseReference.child("User").child(uid).child("lastName").setValue(studentModel.getLastName());
                                            databaseReference.child("User").child(uid).child("middleName").setValue(studentModel.getMiddleName());
                                            databaseReference.child("User").child(uid).child("course").setValue(studentModel.getCourse());
                                            databaseReference.child("User").child(uid).child("year").setValue(studentModel.getYear());
                                            databaseReference.child("User").child(uid).child("contact").setValue(studentModel.getContact());
                                            databaseReference.child("User").child(uid).child("dob").setValue(studentModel.getDob());
                                            databaseReference.child("User").child(uid).child("address").setValue(studentModel.getAddress());
                                            databaseReference.child("User").child(uid).child("fullName").
                                                    setValue(studentModel.getFirstName() + " " + studentModel.getMiddleName() + " " + studentModel.getLastName());
                                            Toast("Student successfully updated");
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        progress_horizontal.setProgress(0);
                                    }
                                });
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progress_horizontal.setProgress((int) progress);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress_horizontal.setProgress(0);
                                    }
                                },500);
                            }
                        });

                    } else {
                        Log.d(COMMON_TAG,TAG+" else invoked");
                        progress_horizontal.setProgress(25);
                        databaseReference.child("User").child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    StudentModel studentModel = new StudentModel();
                                    studentModel.setFirstName(_firstName);
                                    studentModel.setLastName(_lastName);
                                    studentModel.setMiddleName(_middleName);
                                    studentModel.setCourse(_course);
                                    studentModel.setYear(_year);
                                    studentModel.setContact(_contact);
                                    studentModel.setDob(_dob);
                                    studentModel.setAddress(_address);
                                    studentModel.setFullName(_firstName + " " + _middleName + " " + _lastName);
                                    progress_horizontal.setProgress(75);
                                    databaseReference.child("User").child(uid).child("firstName").setValue(studentModel.getFirstName());
                                    databaseReference.child("User").child(uid).child("lastName").setValue(studentModel.getLastName());
                                    databaseReference.child("User").child(uid).child("middleName").setValue(studentModel.getMiddleName());
                                    databaseReference.child("User").child(uid).child("course").setValue(studentModel.getCourse());
                                    databaseReference.child("User").child(uid).child("year").setValue(studentModel.getYear());
                                    databaseReference.child("User").child(uid).child("contact").setValue(studentModel.getContact());
                                    databaseReference.child("User").child(uid).child("dob").setValue(studentModel.getDob());
                                    databaseReference.child("User").child(uid).child("address").setValue(studentModel.getAddress());
                                    databaseReference.child("User").child(uid).child("fullName").
                                            setValue(studentModel.getFirstName() + " " + studentModel.getMiddleName() + " " + studentModel.getLastName());
                                    progress_horizontal.setProgress(100);
                                    if(!testUpdateBtn) {
                                        Toast("Student successfully updated");
                                        testUpdateBtn = true;
                                    }
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
//                                        }
//                                    },1000);
                                }
                                testUpdateBtn = false;
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress_horizontal.setProgress(0);
                                    }
                                },500);
                            }
                        });
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progress_horizontal.setProgress(0);
                        }
                    },500);

                }catch (Exception e){
                    progress_horizontal.setProgress(0);
                    Log.e(COMMON_TAG,TAG+" setUpdateBtn catch invoked");
                    e.printStackTrace();
                }
            }
        });
        BounceView.addAnimTo(updateBtn);
    }

    private String getFileExtension(Uri uri) {
        Context applicationContext = this.getActivity();
        ContentResolver content = applicationContext.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(content.getType(uri));
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Log.d(COMMON_TAG, TAG + ", onActivityResult if condition invoked");
            mImage = data.getData();
            Picasso.get().load(mImage).noFade().into(studentImage);
//            studentImage.setImageURI(mImage);
        }
    }

    private void Toast(String s) {
        Toasty.normal(context, s, Toast.LENGTH_SHORT).show();
    }

    private void clearModel() {
        new StudentModel("", "", "", "", "",
                "", "", "", "", "", "", "",
                "", "", "", "", "", "", "");
        new RoomModel("", "", "",
                "", "", "", "", "");
        roomModel_singleton = RoomModel_Singleton.getInstance();
        roomModel_singleton.clear();
        studentModel_singleton = StudentModel_Singleton.getInstance();
        studentModel_singleton.studentModel_clear();
        TempStudent_Singleton.getInstance().clear();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clearModel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearModel();
    }
}
