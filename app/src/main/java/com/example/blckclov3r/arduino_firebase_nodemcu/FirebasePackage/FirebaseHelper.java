package com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.LogModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.Progress_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.TempStudent_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.AccessFS.AccessActivity_FullScreen;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.AccessFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.example.blckclov3r.arduino_firebase_nodemcu.Spinner_Model_Package.RoomCode_Spinner;
import com.example.blckclov3r.arduino_firebase_nodemcu.Spinner_Model_Package.SchedCode_Spinner;
import com.example.blckclov3r.arduino_firebase_nodemcu.SplashScreen.SplashScreen_Activity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentFragment.mUid_et;

/*

   Abrenica, Nujla

*/
public class FirebaseHelper {

    private static final String COMMON_TAG = "abrenica_nujla";
    private static final String TAG = FirebaseHelper.class.getSimpleName();
    private static FirebaseHelper instance = null;
    private DatabaseReference databaseReference;
    private static Context mContext;
    private static SchedCode_Spinner schedCode;
    private static RoomCode_Spinner roomCode;
    private RoomModel_Singleton roomModel_singleton = null;
    private TextView mFname_tv, mCourse_tv, mDatetime_tv;
    private CircleImageView mImageView;
    private String mRoom;

    private FirebaseHelper(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
        if (databaseReference != null) {
            databaseReference.keepSynced(true);
            Log.d(COMMON_TAG, TAG + " FirebaseHelper() databaseReference != null");
        }
        Log.d(COMMON_TAG, TAG + ", Firebase constructor invoked");
    }

    public static FirebaseHelper getInstance(DatabaseReference databaseReference, Context context) {
        schedCode = SchedCode_Spinner.getInstance();
        roomCode = RoomCode_Spinner.getInstance();
        if (instance == null) {
            synchronized (FirebaseHelper.class) {
                if (instance == null) {
                    clear();
                    instance = new FirebaseHelper(databaseReference);
                    mContext = context;
                }
            }
        }
        return instance;
    }

    public void setInputChecker(final String input, DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
        this.databaseReference.child("AppCondition").child("AccessPasscode").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String passcode = dataSnapshot.child("passcode").getValue().toString();
                    if (input.equals(passcode)) {
                        Intent intent = new Intent(mContext, AccessActivity_FullScreen.class);
                        mContext.startActivity(intent);
                    } else {
                        ToastError("The passcode you entered is invalid");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public boolean firebaseAuthTest(FirebaseAuth mAuth) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(mContext, SplashScreen_Activity.class);
            mContext.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    public boolean retrieveUnregisterdUID() {
        try {
            databaseReference.child("Unregistered_ID").child("card_id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null || dataSnapshot.exists()) {
                        String card_id = dataSnapshot.getValue().toString();
                        if(!card_id.isEmpty()) {
                            mUid_et.setText("");
                            mUid_et.setText(String.valueOf(card_id).trim());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
            return true;
        } catch (Exception e) {
            ToastError(e.getMessage());
            Log.e(COMMON_TAG, TAG + ",retrieveUnregisteredUID() Exception e: " + e.getMessage());
            return false;
        }
    }

    public boolean insertRoomSched(final String schedCode, final RoomModel roomModel, final String f_mHour, final String f_mMinute, final String l_mHour, final String l_mMinute) {
        if (roomModel != null && !schedCode.equals("")) {
            databaseReference.child("Room").child(schedCode).setValue(roomModel).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(COMMON_TAG, TAG + ", insertRoomSched: onFailure, " + e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    databaseReference.child("Room").child(String.valueOf(schedCode)).child("fHour").setValue(f_mHour);
                    databaseReference.child("Room").child(String.valueOf(schedCode)).child("fMinute").setValue(f_mMinute);
                    databaseReference.child("Room").child(String.valueOf(schedCode)).child("lHour").setValue(l_mHour);
                    databaseReference.child("Room").child(String.valueOf(schedCode)).child("lMinute").setValue(l_mMinute);
                }
            });
            return true;
        } else {
            return false;
        }
    }

    public boolean setUniqueID(String uid) {
        if (uid.equals("")) {
            Log.e(COMMON_TAG, TAG + ", setUniqueID: uid must not empty");
            return false;
        }
        databaseReference.child("UNIQUE_ID").child(uid).setValue(uid).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(COMMON_TAG, TAG + ", setUniqueID onfailure invoked: " + e.getMessage());
            }
        });
        databaseReference.child("Unregistered_ID").child("card_id").setValue("");
        return true;
    }

    public List<String> retreiveSchedCode() {
        databaseReference.child("Room").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    fetchSchedCodeData(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return schedCode.getSchedCode();
    }

    private void fetchSchedCodeData(DataSnapshot dataSnapshot) {
        schedCode.getSchedCode().clear();
        try {
            if (dataSnapshot.exists()) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String sched = ds.getValue(StudentModel.class).getSchedCode();
                    schedCode.getSchedCode().add(sched);
                }
                Log.d(COMMON_TAG, TAG + " schedule: " + schedCode.getSchedCode());
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(StudentFragment.mContext, R.layout.spinner_item, schedCode.getSchedCode());
                schedCode.adapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            ToastError(e.getMessage());
            Log.e(COMMON_TAG, TAG + ", fetchSchedCodeData() Exception: " + e.getMessage());
        }
    }

    public List<String> retrieveRoomCode() {
        databaseReference.child("Room").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomCode.getRoomCode().clear();
                try {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String room = ds.getValue(RoomModel.class).getRoomCode();
                            roomCode.getRoomCode().add(room);
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AccessFragment.context,
                                R.layout.spinner_item, roomCode.getRoomCode());
                        arrayAdapter.notifyDataSetChanged();
                        roomCode.adapter(arrayAdapter);
                    }
                } catch (Exception e) {
                    Log.e(COMMON_TAG, TAG + "retrieveCode() Exception: " + e.getMessage());
//                    ToastError(e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return roomCode.getRoomCode();
    }

    private StorageTask storageTask;
    private ProgressBar retrieve_progressBar;
    private String suid, sschedCode, sfirstName, slastName, smiddleName, syear, scontact, saddress, sdob, sfullName, scourse;
    private StorageReference mStorage;

    public Boolean retreieveRoomData(String uid, final String schedCode,
                                     String firstName, String lastName,
                                     String middleName, String year, String contact,
                                     String address, String dob, String fullName,
                                     String course, StorageReference storage,
                                     final Uri uri, ProgressBar progressBar) {
        suid = uid;
        sfirstName = firstName;
        slastName = lastName;
        smiddleName = middleName;
        syear = year;
        scontact = contact;
        saddress = address;
        sdob = dob;
        sfullName = fullName;
        scourse = course;
        sschedCode = schedCode;
        mStorage = storage;
        retrieve_progressBar = progressBar;
        final Progress_Singleton progress_singleton = Progress_Singleton.getInstance();
        roomModel_singleton = RoomModel_Singleton.getInstance();

        databaseReference.child("Room").child(schedCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        String schedCode = dataSnapshot.getValue(RoomModel_Singleton.class).getSchedCode();
                        String roomCode = dataSnapshot.getValue(RoomModel_Singleton.class).getRoomCode();
                        roomModel_singleton.setRoomCode(roomCode);
                        roomModel_singleton.setSchedCode(schedCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        if (storageTask != null && storageTask.isInProgress()) {
            Toast("Upload in Progress, please wait");
            return false;
        } else {
            if (uri != null) {
                databaseReference.child("User").child(suid).child("private_data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(roomModel_singleton.getRoomCode()).exists()) {
                            ToastInfo("Student is already registered, schedule code: " + schedCode);
                            Log.d(COMMON_TAG, TAG + " fHour: " + roomModel_singleton.getfHour());
                        } else {
                            try {
                                StorageReference user_profile = mStorage.child(System.currentTimeMillis() + "." + getFileExtension(uri));
                                storageTask = user_profile.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                        databaseReference.child("Room").child(sschedCode).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    final String schedCode = dataSnapshot.getValue(RoomModel_Singleton.class).getSchedCode();
                                                    final String roomCode = dataSnapshot.getValue(RoomModel_Singleton.class).getRoomCode();
                                                    String subjectCode = dataSnapshot.getValue(RoomModel_Singleton.class).getSubjectCode();
                                                    String term = dataSnapshot.getValue(RoomModel_Singleton.class).getTerm();
                                                    String unit = dataSnapshot.getValue(RoomModel_Singleton.class).getSubjectUnit();
                                                    String startTime = dataSnapshot.getValue(RoomModel_Singleton.class).getStartTime();
                                                    String endTime = dataSnapshot.getValue(RoomModel_Singleton.class).getEndTime();
                                                    String weekDay = dataSnapshot.getValue(RoomModel_Singleton.class).getWeekDay();

                                                    String fHour = dataSnapshot.getValue(RoomModel_Singleton.class).getfHour();
                                                    String fMinute = dataSnapshot.getValue(RoomModel_Singleton.class).getfMinute();
                                                    String lHour = dataSnapshot.getValue(RoomModel_Singleton.class).getlHour();
                                                    String lMinute = dataSnapshot.getValue(RoomModel_Singleton.class).getlMinute();

                                                    roomModel_singleton.setData(schedCode, roomCode, subjectCode, term, unit, startTime, endTime, weekDay);
                                                    roomModel_singleton.setfHour(fHour);
                                                    roomModel_singleton.setfMinute(fMinute);
                                                    roomModel_singleton.setlHour(lHour);
                                                    roomModel_singleton.setlMinute(lMinute);

                                                    databaseReference.child("User").child(String.valueOf(suid)).child("private_data").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.child(roomModel_singleton.getRoomCode()).exists()) {
                                                                ToastInfo("Student is already registered in room " + roomCode + ", schedule code: " + schedCode);
                                                            } else {
                                                                StudentModel studentModel = new StudentModel(suid, sfirstName, slastName, smiddleName, syear, scontact, saddress, sdob,
                                                                        roomModel_singleton.getSubjectCode(), roomModel_singleton.getTerm(), roomModel_singleton.getSubjectUnit(),
                                                                        roomModel_singleton.getStartTime(), roomModel_singleton.getEndTime(), roomModel_singleton.getWeekDay(),
                                                                        sfullName, scourse, "------", roomModel_singleton.getSchedCode(), roomModel_singleton.getRoomCode());

                                                                studentModel.setfHour(String.valueOf(roomModel_singleton.getfHour()));
                                                                studentModel.setfMinute(String.valueOf(roomModel_singleton.getfMinute()));
                                                                studentModel.setlHour(String.valueOf(roomModel_singleton.getlHour()));
                                                                studentModel.setlMinute(String.valueOf(roomModel_singleton.getlMinute()));

                                                                studentModel.setImageUrl(taskSnapshot.getMetadata().getDownloadUrl().toString());
                                                                if (insertStudent(studentModel, suid)) {
                                                                    String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                                                    LogModel logModel = new LogModel();
                                                                    logModel.setLogMsg("Student id " + suid + ", successfully created [+Student]");
                                                                    logModel.setLogAccess(mydate_time);
                                                                    databaseReference.child("AppLog").push().setValue(logModel);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                retrieve_progressBar.setProgress(0);
                                            }
                                        }, 500);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(COMMON_TAG, TAG + ", retrieveRoomData() addOnFailuteListener() Exception: " + e.getMessage());
                                        retrieve_progressBar.setProgress(0);
                                        ToastError(String.valueOf(e.getMessage()));
                                    }
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                        retrieve_progressBar.setProgress((int) progress);
                                        if (!progress_singleton.isProgress()) {
                                            Toast("Please wait, while the image is not yet uploaded");
                                            progress_singleton.setProgress(true);
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(COMMON_TAG, TAG + ", " + e.getMessage());
                            }
                        }
                        progress_singleton.setProgress(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                progress_singleton.setProgress(false);
            } else {
                databaseReference.child("User").child(suid).child("private_data").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(roomModel_singleton.getRoomCode()).exists()) {
                            ToastInfo("Student is already registered, schedule code: " + schedCode);
                            Log.d(COMMON_TAG, TAG + " fHour: " + roomModel_singleton.getfHour());
                        } else {
                            try {
                                databaseReference.child("Room").child(schedCode).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            retrieve_progressBar.setProgress(50);

                                            final String schedCode = dataSnapshot.getValue(RoomModel_Singleton.class).getSchedCode();
                                            final String roomCode = dataSnapshot.getValue(RoomModel_Singleton.class).getRoomCode();

                                            String subjectCode = dataSnapshot.getValue(RoomModel_Singleton.class).getSubjectCode();
                                            String term = dataSnapshot.getValue(RoomModel_Singleton.class).getTerm();
                                            String unit = dataSnapshot.getValue(RoomModel_Singleton.class).getSubjectUnit();
                                            String startTime = dataSnapshot.getValue(RoomModel_Singleton.class).getStartTime();
                                            String endTime = dataSnapshot.getValue(RoomModel_Singleton.class).getEndTime();
                                            String weekDay = dataSnapshot.getValue(RoomModel_Singleton.class).getWeekDay();

                                            String fHour = dataSnapshot.getValue(RoomModel_Singleton.class).getfHour();
                                            String fMinute = dataSnapshot.getValue(RoomModel_Singleton.class).getfMinute();
                                            String lHour = dataSnapshot.getValue(RoomModel_Singleton.class).getlHour();
                                            String lMinute = dataSnapshot.getValue(RoomModel_Singleton.class).getlMinute();

                                            roomModel_singleton.setData(schedCode, roomCode, subjectCode, term, unit, startTime, endTime, weekDay);
                                            roomModel_singleton.setfHour(fHour);
                                            roomModel_singleton.setfMinute(fMinute);
                                            roomModel_singleton.setlHour(lHour);
                                            roomModel_singleton.setlMinute(lMinute);

                                            databaseReference.child("User").child(suid).child("private_data").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.child(roomModel_singleton.getRoomCode()).exists()) {
                                                        ToastInfo("Student is already registered in room " + roomCode + ", schedule code: " + schedCode);
                                                    } else {
                                                        StudentModel studentModel = new StudentModel(suid, sfirstName, slastName, smiddleName, syear, scontact, saddress, sdob,
                                                                roomModel_singleton.getSubjectCode(), roomModel_singleton.getTerm(), roomModel_singleton.getSubjectUnit(),
                                                                roomModel_singleton.getStartTime(), roomModel_singleton.getEndTime(), roomModel_singleton.getWeekDay(),
                                                                sfullName, scourse, "------", roomModel_singleton.getSchedCode(), roomModel_singleton.getRoomCode());

                                                        studentModel.setfHour(String.valueOf(roomModel_singleton.getfHour()));
                                                        studentModel.setfMinute(String.valueOf(roomModel_singleton.getfMinute()));
                                                        studentModel.setlHour(String.valueOf(roomModel_singleton.getlHour()));
                                                        studentModel.setlMinute(String.valueOf(roomModel_singleton.getlMinute()));

                                                        studentModel.setImageUrl("https://i.ibb.co/r2xQp7P/test.png");

                                                        if (insertStudent(studentModel, suid)) {
                                                            String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                                            LogModel logModel = new LogModel();
                                                            logModel.setLogMsg("Student id " + suid + ", successfully created [+Student]");
                                                            logModel.setLogAccess(mydate_time);
                                                            databaseReference.child("AppLog").push().setValue(logModel);
                                                        }
                                                        retrieve_progressBar.setProgress(100);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    retrieve_progressBar.setProgress(0);
                                                }
                                            }, 500);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        retrieve_progressBar.setProgress(0);
                                    }
                                }, 500);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(COMMON_TAG, TAG + ", " + e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;
            }

        }
        retrieve_progressBar.setProgress(0);
        return true;
    }

    public Boolean retreieveRoomData1(String uid, final String schedCode,
                                      String firstName, String lastName,
                                      String middleName, String year, String contact,
                                      String address, final String dob, String fullName,
                                      String course, ProgressBar progressBar) {
        suid = uid;
        sschedCode = schedCode;
        sfirstName = firstName;
        slastName = lastName;
        smiddleName = middleName;
        syear = year;
        scontact = contact;
        saddress = address;
        sdob = dob;
        sfullName = fullName;
        scourse = course;
        this.retrieve_progressBar = progressBar;
        roomModel_singleton = RoomModel_Singleton.getInstance();
        Log.d(COMMON_TAG, TAG + " fHour: " + roomModel_singleton.getfHour());
        databaseReference.child("Room").child(sschedCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        String schedCode = dataSnapshot.getValue(RoomModel_Singleton.class).getSchedCode();
                        String roomCode = dataSnapshot.getValue(RoomModel_Singleton.class).getRoomCode();
                        roomModel_singleton.setRoomCode(roomCode);
                        roomModel_singleton.setSchedCode(schedCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child("User").child(suid).child("private_data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(roomModel_singleton.getRoomCode()).exists()) {
                    ToastInfo("Student is already registered, schedule code: " + schedCode);
                } else {
                    try {
                        databaseReference.child("Room").child(schedCode).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    retrieve_progressBar.setProgress(50);

                                    final String schedCode = dataSnapshot.getValue(RoomModel_Singleton.class).getSchedCode();
                                    final String roomCode = dataSnapshot.getValue(RoomModel_Singleton.class).getRoomCode();

                                    String subjectCode = dataSnapshot.getValue(RoomModel_Singleton.class).getSubjectCode();
                                    String term = dataSnapshot.getValue(RoomModel_Singleton.class).getTerm();
                                    String unit = dataSnapshot.getValue(RoomModel_Singleton.class).getSubjectUnit();
                                    String startTime = dataSnapshot.getValue(RoomModel_Singleton.class).getStartTime();
                                    String endTime = dataSnapshot.getValue(RoomModel_Singleton.class).getEndTime();
                                    String weekDay = dataSnapshot.getValue(RoomModel_Singleton.class).getWeekDay();

                                    String fHour = dataSnapshot.getValue(RoomModel_Singleton.class).getfHour();
                                    String fMinute = dataSnapshot.getValue(RoomModel_Singleton.class).getfMinute();
                                    String lHour = dataSnapshot.getValue(RoomModel_Singleton.class).getlHour();
                                    String lMinute = dataSnapshot.getValue(RoomModel_Singleton.class).getlMinute();

                                    roomModel_singleton = RoomModel_Singleton.getInstance();
                                    roomModel_singleton.setfHour(fHour);
                                    roomModel_singleton.setfMinute(fMinute);
                                    roomModel_singleton.setlHour(lHour);
                                    roomModel_singleton.setlMinute(lMinute);

                                    roomModel_singleton.setData(schedCode, roomCode, subjectCode, term, unit, startTime, endTime, weekDay);
                                    databaseReference.child("User").child(suid).child("private_data").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.child(roomModel_singleton.getRoomCode()).exists()) {
                                                ToastInfo("Student is already registered in room " + roomCode + ", schedule code: " + schedCode);
                                            } else {
                                                StudentModel studentModel = new StudentModel(suid, sfirstName, slastName, smiddleName, syear, scontact, saddress, sdob,
                                                        roomModel_singleton.getSubjectCode(), roomModel_singleton.getTerm(), roomModel_singleton.getSubjectUnit(),
                                                        roomModel_singleton.getStartTime(), roomModel_singleton.getEndTime(), roomModel_singleton.getWeekDay(),
                                                        sfullName, scourse, "------", roomModel_singleton.getSchedCode(), roomModel_singleton.getRoomCode());
                                                StudentModel_Singleton sm = StudentModel_Singleton.getInstance();

                                                studentModel.setfHour(String.valueOf(roomModel_singleton.getfHour()));
                                                studentModel.setfMinute(String.valueOf(roomModel_singleton.getfMinute()));
                                                studentModel.setlHour(String.valueOf(roomModel_singleton.getlHour()));
                                                studentModel.setlMinute(String.valueOf(roomModel_singleton.getlMinute()));

                                                studentModel.setImageUrl(sm.getImageUrl());

                                                if (insertStudent(studentModel, suid)) {
                                                    String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                                    LogModel logModel = new LogModel();
                                                    logModel.setLogMsg("Student id " + suid + ", successfully created [+Student]");
                                                    logModel.setLogAccess(mydate_time);
                                                    databaseReference.child("AppLog").push().setValue(logModel);
//                                                ToastInfo("Student successfully created");
                                                }
                                                retrieve_progressBar.setProgress(100);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            retrieve_progressBar.setProgress(0);
                                        }
                                    }, 500);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                retrieve_progressBar.setProgress(0);
                            }
                        }, 500);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(COMMON_TAG, TAG + ", " + e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        retrieve_progressBar.setProgress(0);
        return true;
    }


    private String getFileExtension(Uri uri) {
        Context applicationContext = StudentFragment.mContext;
        ContentResolver content = applicationContext.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(content.getType(uri));
    }

    public boolean insertStudent(final StudentModel studentModel, final String uid) {
        try {
            if (studentModel == null) {
                return false;
            } else {
                //object
                databaseReference.child("User").child(uid).child("private_data").child(roomModel_singleton.getRoomCode())
                        .setValue(studentModel).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(COMMON_TAG, TAG + ", insertStudent: " + e.getMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
//                        Toasty.normal(mContext, "Student successfully created", Toast.LENGTH_SHORT).show();
                    }
                });
                //manual code
                databaseReference.child("User").child(uid).child("fullName").setValue(studentModel.getFullName());
                databaseReference.child("User").child(uid).child("firstName").setValue(studentModel.getFirstName());
                databaseReference.child("User").child(uid).child("lastName").setValue(studentModel.getLastName());
                databaseReference.child("User").child(uid).child("middleName").setValue(studentModel.getMiddleName());
                databaseReference.child("User").child(uid).child("course").setValue(studentModel.getCourse());
                databaseReference.child("User").child(uid).child("uid").setValue(studentModel.getUID());
                databaseReference.child("User").child(uid).child("imageUrl").setValue(studentModel.getImageUrl());
                databaseReference.child("User").child(uid).child("dob").setValue(studentModel.getDob());
                databaseReference.child("User").child(uid).child("course").setValue(studentModel.getCourse());
                databaseReference.child("User").child(uid).child("year").setValue(studentModel.getYear());
                databaseReference.child("User").child(uid).child("contact").setValue(studentModel.getContact());
                databaseReference.child("User").child(uid).child("address").setValue(studentModel.getAddress());
                if (setUniqueID(uid)) {
                    Log.d(COMMON_TAG, TAG + " insert_studentBtn: setOnClick success!");
                } else {
                    Log.d(COMMON_TAG, TAG + " firebaseHelper.setUniqueID else invoked");
                }

                databaseReference.child("PieChart").child("Enrolled").child(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String year = dataSnapshot.child("yearEnrolled").getValue().toString();
                            int enrolled = Integer.valueOf(year) + 1;
                            databaseReference.child("PieChart").child("Enrolled")
                                    .child(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))).child("yearEnrolled").setValue(enrolled);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        } catch (Exception e) {
            Log.e(COMMON_TAG, TAG + ", insertStudent() Exception: " + e.getMessage());
            ToastError(e.getMessage());
            return false;
        }
        return true;
    }


    private TempStudent_Singleton temp;
    private ProgressBar access_progressBar;
    private LogModel logModel;
    private String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

    public void retrieveAccessFragment(final TextView fname_textView,
                                       final TextView course_textView,
                                       final TextView android_dateTime_textView,
                                       final String dateStr, final String room,
                                       final CircleImageView imageView,
                                       ProgressBar pr) {
        if(fname_textView.getText().toString().isEmpty() || course_textView.getText().toString().isEmpty()){
            Log.d(COMMON_TAG,TAG+" there's something wrong");
            return;
        }

        mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        logModel = new LogModel();
        access_progressBar = pr;
        databaseReference.child("TempData").child("cardID").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        access_progressBar.setIndeterminate(true);
                        String cardID = dataSnapshot.getValue().toString();
                        if (!cardID.isEmpty()) {
                            temp = TempStudent_Singleton.getInstance();
                            temp.setCardID(cardID);
                            temp.setAndroid_dateTime(mydate_time);
                            access2(fname_textView, course_textView, android_dateTime_textView, imageView, room, dateStr);
                            access0(room, dateStr);
                            try {
                                databaseReference.child("SensorData").child("sensor").removeValue();
                                databaseReference.child("TempData").child("cardID").removeValue();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    access_progressBar.setIndeterminate(false);
                                    temp.clear();
                                }
                            }, 1200);
                        }
                    }
                } catch (Exception e) {
                    Log.e(COMMON_TAG, TAG + ", retrieveAccessFragment Exception: " + e.getMessage());
                    ToastError(e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(COMMON_TAG, TAG + "databaseError: " + databaseError);
            }
        });

        access_progressBar.setIndeterminate(false);
    }

    private void access2(final TextView fname_textView, final TextView course_textView, final TextView android_dateTime_textView,
                         final CircleImageView imageView, final String room, final String dateStr) {
        try {
            temp = TempStudent_Singleton.getInstance();
            databaseReference.child("User").child(temp.getCardID()).child("private_data").child(room).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        if (dataSnapshot.exists()) {
                            String fHour = String.valueOf(dataSnapshot.child("fHour").getValue());
                            String fMinute = String.valueOf(dataSnapshot.child("fMinute").getValue());
                            String lHour = String.valueOf(dataSnapshot.child("lHour").getValue());
                            String lMinute = String.valueOf(dataSnapshot.child("lMinute").getValue());
                            int x = Integer.valueOf(fHour);
                            int f_minute = Integer.valueOf(fMinute);
                            int l_hour = Integer.valueOf(lHour);
                            int l_minute = Integer.valueOf(lMinute);
                            Calendar aCalander = Calendar.getInstance();
                            int hour = aCalander.get(Calendar.HOUR_OF_DAY);
                            int minute = aCalander.get(Calendar.MINUTE);

                            Log.d(COMMON_TAG, " ----------------------------------- ");
                            Log.d(COMMON_TAG, TAG + " [1] fHour: " + fHour + ", fMinute: " + fMinute);
                            Log.d(COMMON_TAG, TAG + " [1] lHour: " + lHour + ", lMinute: " + lMinute);
                            Log.d(COMMON_TAG, TAG + " [1] x: " + x + ", y: " + f_minute + ", a: " + l_hour + ", b: " + l_minute);
                            Log.d(COMMON_TAG, " ----------------------------------- ");

                            if (Integer.valueOf(fHour) < hour) {
                                x_access1(fname_textView, course_textView, android_dateTime_textView, imageView, room);
                                databaseReference.child("User").child(String.valueOf(temp.getCardID())).child("lastTimeAccess").setValue("false");
                            } else if (Integer.valueOf(fHour) >= hour) {
                                Log.d(COMMON_TAG, TAG + " x value: " + String.valueOf(x) + ", hour: " + hour);
                                if (x == 0 || x == 24) {
                                    Log.d(COMMON_TAG, TAG + " millitary time 24 hour clock invoked");
                                    if (x == 24 && hour == 0) {
                                        getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                                imageView, room, dateStr);
                                    } else {
                                        x_access1(fname_textView, course_textView, android_dateTime_textView, imageView, room);
                                    }
                                } else if (x == 1 && hour != 2 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 2 && hour != 3 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 3 && hour != 4 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 4 && hour != 5 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 5 && hour != 6 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 6 && hour != 7 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 7 && hour != 8 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 8 && hour != 9 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 9 && hour != 10 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 10 && hour != 11 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 11 && hour != 12 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 12 && hour != 0 && hour != 13 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 13 && hour != 14 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 14 && hour != 15 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 15 && hour != 16 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 16 && hour != 17 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 17 && hour != 18 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 18 && hour != 19 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 19 && hour != 20 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 20 && hour != 21 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 21 && hour != 22 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 22 && hour != 23 && hour >= x) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else if (x == 23 && hour >= 24) {
                                    getAccessCondition(x, f_minute, hour, minute, fname_textView, course_textView, android_dateTime_textView,
                                            imageView, room, dateStr);
                                } else {
                                    x_access1(fname_textView, course_textView, android_dateTime_textView, imageView, room);
//                                ToastError("Access Denied, Please comeback in " + dataSnapshot.getValue(StudentModel.class).getStartTime());
                                    databaseReference.child("User").child(String.valueOf(temp.getCardID())).child("lastTimeAccess").setValue("false");
                                }
                            }
                            Log.d(COMMON_TAG, " ----------------------------------- ");
                            Log.d(COMMON_TAG, TAG + " [2] fHour: " + fHour + ", fMinute: " + fMinute);
                            Log.d(COMMON_TAG, TAG + " [2] lHour: " + lHour + ", lMinute: " + lMinute);
                            Log.d(COMMON_TAG, TAG + " [2] x: " + x + ", y: " + f_minute + ", a: " + l_hour + ", b: " + l_minute);
                            Log.d(COMMON_TAG, " ----------------------------------- ");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void clear() {
        TempStudent_Singleton.getInstance().clear();
        StudentModel_Singleton.getInstance().studentModel_clear();
        RoomModel_Singleton.getInstance().clear();
    }

    private void getAccessCondition(int x, int f_minute, int hour, int minute,
                                    TextView fname_textView, TextView course_textView,
                                    TextView android_dateTime_textView, CircleImageView
                                            imageView, String room, String dateStr) {
        databaseReference.child("User").child(String.valueOf(temp.getCardID())).child("lastTimeAccess").setValue("true");
        if (f_minute >= 30 && x >= hour) {
            Log.d(COMMON_TAG, TAG + " f_minute >= minute invoked");
            showAccessData(fname_textView, course_textView, android_dateTime_textView, imageView, room, dateStr);
        } else if (f_minute < 30 && x >= hour) {
            Log.d(COMMON_TAG, TAG + " minute < 30 invoked*");
            if (minute >= 30) {
                Log.d(COMMON_TAG, TAG + " f_minute < 30 && x >= hour * minute >= 30 invoked");
                showAccessData01(fname_textView, course_textView, android_dateTime_textView, imageView, room, dateStr);
            } else {
                Log.d(COMMON_TAG, TAG + " f_minute < 30 && x >= hour * else invoked");
                showAccessData(fname_textView, course_textView, android_dateTime_textView, imageView, room, dateStr);
            }
        } else {
            Log.d(COMMON_TAG, TAG + ", getAccessCondtion error invoked");
            databaseReference.child("User").child(String.valueOf(temp.getCardID())).child("lastTimeAccess").setValue("false");
        }

    }


    private void showAccessData(final TextView fname_textView, final TextView course_textView, final TextView android_dateTime_textView,
                                final CircleImageView imageView, final String room, final String dateStr) {
        Log.d(COMMON_TAG, TAG + " showAccessData invoked");
        temp = TempStudent_Singleton.getInstance();
        access1(fname_textView, course_textView, android_dateTime_textView, imageView, room);
        databaseReference.child("User").child(temp.getCardID()).child("private_data")
                .child(room).child("firebase_time_log/data_access").push().child("data_access").setValue(dateStr);
        mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        databaseReference.child("User").child(temp.getCardID()).child("private_data")
                .child(room).child("firebase_time_log/date_access").push().child("date_access").setValue(mydate_time);
    }

    private void showAccessData01(final TextView fname_textView, final TextView course_textView, final TextView android_dateTime_textView,
                                  final CircleImageView imageView, final String room, final String dateStr) {
        Log.d(COMMON_TAG, TAG + " showAccessData01 invoked");
        temp = TempStudent_Singleton.getInstance();
        access1(fname_textView, course_textView, android_dateTime_textView, imageView, room);
        databaseReference.child("User").child(temp.getCardID()).child("private_data")
                .child(room).child("firebase_time_log/data_access").push().child("data_access").setValue(dateStr);
        mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        databaseReference.child("User").child(temp.getCardID()).child("private_data")
                .child(room).child("firebase_time_log/date_access").push().child("date_access").setValue("Late: " + mydate_time);
    }

    private void x_access1(final TextView fname_textView, final TextView course_textView, final TextView android_dateTime_textView,
                           final CircleImageView imageView, final String room) {
        Log.d(COMMON_TAG, TAG + " x_access1 invoked");
        temp = TempStudent_Singleton.getInstance();
        databaseReference.child("User").child(temp.getCardID()).child("private_data").child(room).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                access_progressBar.setIndeterminate(true);
                if (dataSnapshot.exists() && dataSnapshot.hasChild("fullName")) {
                    try {
                        mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        databaseReference.child("User").child(temp.getCardID()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String fullName = dataSnapshot.getValue(StudentModel.class).getFullName();
                                    String course = dataSnapshot.getValue(StudentModel.class).getCourse();
                                    Picasso.get().load(String.valueOf(dataSnapshot.getValue(StudentModel.class).getImageUrl()))
                                            .noFade()
                                            .placeholder(R.drawable.person)
                                            .into(imageView);
                                    fname_textView.setText(fullName);
                                    course_textView.setText(course);
                                    android_dateTime_textView.setText("------");
                                    logModel.setLogMsg("Student id " + temp.getCardID() + ", enter at room " + room);
                                    logModel.setLogAccess("Late: " + mydate_time);
                                    databaseReference.child("AppLog").push().setValue(logModel);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    private void access1(TextView fname_textView, TextView course_textView, TextView android_dateTime_textView,
                         CircleImageView imageView, String room) {

        mFname_tv = fname_textView;
        mCourse_tv = course_textView;
        mDatetime_tv = android_dateTime_textView;
        mImageView = imageView;
        mRoom = room;

        Log.d(COMMON_TAG, TAG + " access1 invoked");
        temp = TempStudent_Singleton.getInstance();
        databaseReference.child("User").child(temp.getCardID()).child("private_data").child(room).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                access_progressBar.setIndeterminate(true);
                if (dataSnapshot.exists() && dataSnapshot.hasChild("fullName")) {
                    mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                    if (!temp.getCardID().isEmpty()) {
                        databaseReference.child("User").child(temp.getCardID()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String fullName = dataSnapshot.getValue(StudentModel.class).getFullName();
                                    String course = dataSnapshot.getValue(StudentModel.class).getCourse();
                                    Picasso.get().load(String.valueOf(dataSnapshot.getValue(StudentModel.class).getImageUrl()))
                                            .noFade()
                                            .placeholder(R.drawable.person)
                                            .into(mImageView);
                                    mFname_tv.setText(fullName);
                                    mCourse_tv.setText(course);
                                    mDatetime_tv.setText(mydate_time);

                                    logModel.setLogMsg("Student id " + temp.getCardID() + ", enter at room " + mRoom);
                                    logModel.setLogAccess(mydate_time);
                                    databaseReference.child("AppLog").push().setValue(logModel);

                                    databaseReference.child("User").child(temp.getCardID()).child("private_data")
                                            .child(String.valueOf(mRoom)).child("date_access").setValue(mydate_time);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    } else {
                        Log.d(COMMON_TAG, TAG + " temp.getCardId is empty");
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void access0(String room, final String dateStr) {
        mRoom = room;
        Log.d(COMMON_TAG, TAG + " access0 invoked");
        temp = TempStudent_Singleton.getInstance();
        databaseReference.child("SensorData").child("sensor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        String sensor = dataSnapshot.child(temp.getCardID()).getValue().toString();
                        Log.d(COMMON_TAG, TAG + " sensor: " + sensor);
                        if (!sensor.isEmpty()) {
                            databaseReference.child("User").child(temp.getCardID()).child("private_data")
                                    .child(mRoom).child("firebase_time_log/pirSensor1").push().child("sensor").setValue(dateStr);
                            mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                            databaseReference.child("User").child(temp.getCardID()).child("private_data")
                                    .child(mRoom).child("firebase_time_log/pirSensor2").push().child("sensor").setValue(mydate_time);
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

    public void searchStudent(final String student_uid, final CircleImageView studentSearch_image, final TextView studentSearch_fname_textView, final TextView studentSearch_course_textView,
                              final TextView studentSearch_year_textView, final TextView studentSearch_dob_textView,
                              final TextView studentSearch_address_textView, final TextView studentSearch_contact_textView) {
        databaseReference.child("User").child(student_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.exists()) {
                        String fullName = dataSnapshot.child("fullName").getValue().toString();
                        final String imageUrl = dataSnapshot.child("imageUrl").getValue().toString();
                        String address = dataSnapshot.child("address").getValue().toString();
                        String dob = dataSnapshot.child("dob").getValue().toString();
                        String contact = dataSnapshot.child("contact").getValue().toString();
                        String year = dataSnapshot.child("year").getValue().toString();
                        String course = dataSnapshot.child("course").getValue().toString();
                        Picasso.get().load(imageUrl)
                                .noFade()
                                .placeholder(R.drawable.person)
                                .into(studentSearch_image);
                        studentSearch_address_textView.setText(address);
                        studentSearch_contact_textView.setText(contact);
                        studentSearch_fname_textView.setText(fullName);
                        studentSearch_course_textView.setText(course);
                        studentSearch_year_textView.setText(year);
                        studentSearch_dob_textView.setText(dob);
                        String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        LogModel logModel = new LogModel();
                        logModel.setLogMsg("Student id " + student_uid + ", information data successfully retrieved [Student Search]");
                        logModel.setLogAccess(mydate_time);
                        databaseReference.child("AppLog").push().setValue(logModel);
                        ToastInfo("Student data successfully retrieved");

                    } else {

                        String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        LogModel logModel = new LogModel();
                        logModel.setLogMsg("Student id " + student_uid + " does not exist [Student Search]");
                        logModel.setLogAccess(mydate_time);
                        databaseReference.child("AppLog").push().setValue(logModel);
                        ToastError("Student id does not exist in our database");

                    }
                } catch (Exception e) {
                    ToastError(e.getMessage());
                    Log.e(COMMON_TAG, TAG + " student search: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void Toast(String s) {
        Toasty.normal(mContext, s, Toast.LENGTH_SHORT).show();
    }

    private void ToastError(String s) {
        Toasty.error(mContext, s, Toast.LENGTH_SHORT).show();
    }

    private void ToastInfo(String s) {
        Toasty.normal(mContext, s, Toast.LENGTH_SHORT).show();
    }
}