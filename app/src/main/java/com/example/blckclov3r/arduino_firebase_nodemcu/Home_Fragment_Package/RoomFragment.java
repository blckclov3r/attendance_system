package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.LogModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage.FirebaseHelper;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import hari.bounceview.BounceView;

//insert room fragment
public class RoomFragment extends Fragment {

    private static final String COMMON_TAG = "blckclov3r";
    private static final String TAG = RoomFragment.class.getSimpleName();

    //components
    private EditText mSchedCode, mRoomCode, mSubjectCode;
    private Spinner mTerm;
    private Spinner mSubjectUnit_spinner;
    private Spinner mWeekday_spinner;
    private ImageView mStarttime_imageView, mEndtime_imageView;
    private Button mRoom_btn;
    private TextView mTimestart_tv, mTimeend_tv;
    private ProgressBar mProgressbar;

    //vars
    private DatabaseReference mDatabaseReference;
    private FirebaseHelper mFirebasedatabaseHelper;
    private long mLastClickTime = 0;

    //Time Popup Dialog
    private int f_mHour = 0;
    private int f_mMinute = 0;
    private int l_mHour = 0;
    private int l_mMinute = 0;
    private int x_fmHour = 0;
    private TimePickerDialog timePickerDialog;
    private Context contex;


    public RoomFragment() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();
        mFirebasedatabaseHelper = FirebaseHelper.getInstance(mDatabaseReference, getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.room_fragment, container, false);
        contex = getActivity();
        mSchedCode = (EditText) view.findViewById(R.id.schedCode_editText);
        mSchedCode.requestFocus();
        mRoomCode = (EditText) view.findViewById(R.id.roomCode_editText);
        mSubjectCode = (EditText) view.findViewById(R.id.subjectCode);
        mTerm = (Spinner) view.findViewById(R.id.term_spinner);
        mTimestart_tv = (TextView) view.findViewById(R.id.time_start);
        mTimeend_tv = (TextView) view.findViewById(R.id.time_end);
        mSubjectUnit_spinner = (Spinner) view.findViewById(R.id.subjectUnit_spinner);
        mWeekday_spinner = (Spinner) view.findViewById(R.id.weekDay_spinner);
        mStarttime_imageView = (ImageView) view.findViewById(R.id.startTime_imageView);
        mEndtime_imageView = (ImageView) view.findViewById(R.id.endTime_imageView);
        mRoom_btn = (Button) view.findViewById(R.id.room_insertBtn);
        mProgressbar = (ProgressBar) view.findViewById(R.id.roomFragment_progressBar);
        mProgressbar.setIndeterminate(false);
//        android.R.layout.simple_spinner_dropdown_item -> R.layout.spinner_item

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.unit_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSubjectUnit_spinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> week_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.weekday_array, R.layout.spinner_item);
        week_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mWeekday_spinner.setAdapter(week_adapter);

        ArrayAdapter<CharSequence> semAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.sem_array, R.layout.spinner_item);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTerm.setAdapter(semAdapter);

//        try {
//            BounceView.addAnimTo(mSchedCode);
//            BounceView.addAnimTo(mRoomCode);
//            BounceView.addAnimTo(mSubjectCode);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRoom_insertBtn();
        mTimestart_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String amPm ="";
                        if (hourOfDay >= 12) {
                            f_mMinute = minute;
                            f_mHour = hourOfDay;
                            x_fmHour = hourOfDay;
                            if (f_mHour > 12 && f_mHour == 13) {
                                f_mHour = 1;
                            } else if (f_mHour > 12 && f_mHour == 14) {
                                f_mHour = 2;
                            } else if (f_mHour > 12 && f_mHour == 15) {
                                f_mHour = 3;
                            } else if (f_mHour > 12 && f_mHour == 16) {
                                f_mHour = 4;
                            } else if (f_mHour > 12 && f_mHour == 17) {
                                f_mHour = 5;
                            } else if (f_mHour > 12 && f_mHour == 18) {
                                f_mHour = 6;
                            } else if (f_mHour > 12 && f_mHour == 19) {
                                f_mHour = 7;
                            } else if (f_mHour > 12 && f_mHour == 20) {
                                f_mHour = 8;
                            } else if (f_mHour > 12 && f_mHour == 21) {
                                f_mHour = 9;
                            } else if (f_mHour > 12 && f_mHour == 22) {
                                f_mHour = 10;
                            } else if (f_mHour > 12 && f_mHour == 23) {
                                f_mHour = 11;
                            } else if (f_mHour > 12 && f_mHour == 24) {
                                f_mHour = 12;
                            }
                            amPm = "PM";
                        } else {
                            f_mMinute = minute;
                            f_mHour = hourOfDay;
                            x_fmHour = hourOfDay;
                            if (f_mHour == 0) {
                                f_mHour = 12;
                            }
                            amPm = "AM";
                        }
                        mTimestart_tv.setText(String.format("%02d:%02d", f_mHour, f_mMinute) + " " + amPm);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        mTimeend_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String amPm = "";
                        if (hourOfDay >= 12) {
                            l_mMinute = minute;
                            l_mHour = hourOfDay;
                            if (l_mHour > 12 && l_mHour == 13) {
                                l_mHour = 1;
                            } else if (l_mHour > 12 && l_mHour == 14) {
                                l_mHour = 2;
                            } else if (l_mHour > 12 && l_mHour == 15) {
                                l_mHour = 3;
                            } else if (l_mHour > 12 && l_mHour == 16) {
                                l_mHour = 4;
                            } else if (l_mHour > 12 && l_mHour == 17) {
                                l_mHour = 5;
                            } else if (l_mHour > 12 && l_mHour == 18) {
                                l_mHour = 6;
                            } else if (l_mHour > 12 && l_mHour == 19) {
                                l_mHour = 7;
                            } else if (l_mHour > 12 && l_mHour == 20) {
                                l_mHour = 8;
                            } else if (l_mHour > 12 && l_mHour == 21) {
                                l_mHour = 9;
                            } else if (l_mHour > 12 && l_mHour == 22) {
                                l_mHour = 10;
                            } else if (l_mHour > 12 && l_mHour == 23) {
                                l_mHour = 11;
                            } else if (l_mHour > 12 && l_mHour == 24) {
                                l_mHour = 12;
                            }
                            amPm = "PM";
                        } else {
                            l_mMinute = minute;
                            l_mHour = hourOfDay;
                            if (l_mHour == 0) {
                                l_mHour = 12;
                            }
                            amPm = "AM";
                        }
                        mTimeend_tv.setText(String.format("%02d:%02d", l_mHour, l_mMinute) + " " + amPm);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });

        mStarttime_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String amPm = "";
                        if (hourOfDay >= 12) {
                            f_mMinute = minute;
                            f_mHour = hourOfDay;
                            x_fmHour = hourOfDay;
                            if (f_mHour > 12 && f_mHour == 13) {
                                f_mHour = 1;
                            } else if (f_mHour > 12 && f_mHour == 14) {
                                f_mHour = 2;
                            } else if (f_mHour > 12 && f_mHour == 15) {
                                f_mHour = 3;
                            } else if (f_mHour > 12 && f_mHour == 16) {
                                f_mHour = 4;
                            } else if (f_mHour > 12 && f_mHour == 17) {
                                f_mHour = 5;
                            } else if (f_mHour > 12 && f_mHour == 18) {
                                f_mHour = 6;
                            } else if (f_mHour > 12 && f_mHour == 19) {
                                f_mHour = 7;
                            } else if (f_mHour > 12 && f_mHour == 20) {
                                f_mHour = 8;
                            } else if (f_mHour > 12 && f_mHour == 21) {
                                f_mHour = 9;
                            } else if (f_mHour > 12 && f_mHour == 22) {
                                f_mHour = 10;
                            } else if (f_mHour > 12 && f_mHour == 23) {
                                f_mHour = 11;
                            } else if (f_mHour > 12 && f_mHour == 24) {
                                f_mHour = 12;
                            }
                            amPm = "PM";
                        } else {
                            f_mMinute = minute;
                            f_mHour = hourOfDay;
                            x_fmHour = hourOfDay;
                            if (f_mHour == 0) {
                                f_mHour = 12;
                            }
                            amPm = "AM";
                        }
                        mTimestart_tv.setText(String.format("%02d:%02d", f_mHour, f_mMinute) + " " + amPm);
                    }
                }, 0, 0, false);

                timePickerDialog.show();
            }
        });

        mEndtime_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint({"DefaultLocale", "SetTextI18n"})
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String amPm = "";
                        if (hourOfDay >= 12) {
                            l_mMinute = minute;
                            l_mHour = hourOfDay;
                            if (l_mHour > 12 && l_mHour == 13) {
                                l_mHour = 1;
                            } else if (l_mHour > 12 && l_mHour == 14) {
                                l_mHour = 2;
                            } else if (l_mHour > 12 && l_mHour == 15) {
                                l_mHour = 3;
                            } else if (l_mHour > 12 && l_mHour == 16) {
                                l_mHour = 4;
                            } else if (l_mHour > 12 && l_mHour == 17) {
                                l_mHour = 5;
                            } else if (l_mHour > 12 && l_mHour == 18) {
                                l_mHour = 6;
                            } else if (l_mHour > 12 && l_mHour == 19) {
                                l_mHour = 7;
                            } else if (l_mHour > 12 && l_mHour == 20) {
                                l_mHour = 8;
                            } else if (l_mHour > 12 && l_mHour == 21) {
                                l_mHour = 9;
                            } else if (l_mHour > 12 && l_mHour == 22) {
                                l_mHour = 10;
                            } else if (l_mHour > 12 && l_mHour == 23) {
                                l_mHour = 11;
                            } else if (l_mHour > 12 && l_mHour == 24) {
                                l_mHour = 12;
                            }
                            amPm = "PM";
                        } else {
                            l_mMinute = minute;
                            l_mHour = hourOfDay;
                            if (l_mHour == 0) {
                                l_mHour = 12;
                            }
                            amPm = "AM";
                        }
                        mTimeend_tv.setText(String.format("%02d:%02d", l_mHour, l_mMinute) + " " + amPm);
                    }
                }, 0, 0, false);
                timePickerDialog.show();
            }
        });
    }

    private void setRoom_insertBtn() {
        mRoom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                mProgressbar.setProgress(100);
                String sched = mSchedCode.getText().toString().trim();
                String room = mRoomCode.getText().toString().trim();
                String subject = mSubjectCode.getText().toString();
                String thisterm = mTerm.getSelectedItem().toString().trim();
                String unit = mSubjectUnit_spinner.getSelectedItem().toString().trim();
                String start = mTimestart_tv.getText().toString().trim();
                String end = mTimeend_tv.getText().toString().trim();
                String wday = mWeekday_spinner.getSelectedItem().toString();

                if (sched.isEmpty()) {
                    mProgressbar.setProgress(0);
                    mSchedCode.setFocusable(true);
                    mSchedCode.setError("Don't leave this field empty");
                    return;
                }if (room.isEmpty()) {
                    mProgressbar.setProgress(0);
                    mRoomCode.setFocusable(true);
                    mRoomCode.setError("Don't leave this field empty");
                    return;
                }if (subject.isEmpty()) {
                    mProgressbar.setProgress(0);
                    ToastInfo("Don't leave this field empty");
                    mSubjectCode.setFocusable(true);
                    mSubjectCode.setError("Don't leave this field empty");
                    return;
                }if (start.equals("Time Start")) {
                    mProgressbar.setProgress(0);
                    ToastInfo("Please specify the time start");
                    return;
                }if (end.equals("Time End")) {
                    mProgressbar.setProgress(0);
                    ToastInfo("Please specify the time end");
                    return;
                }
                RoomModel roomModel = new RoomModel(sched.toUpperCase(), room, subject, thisterm, unit, start, end, wday);
                //f_mHour -> x_fmHour
                if (mFirebasedatabaseHelper.insertRoomSched(sched, roomModel,String.valueOf(x_fmHour),String.valueOf(f_mMinute),String.valueOf(l_mHour),String.valueOf(l_mMinute))) {
                    String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    LogModel logModel = new LogModel();
                    logModel.setLogMsg("+Room successfully Created [+Room]");
                    logModel.setLogAccess("Access date &amp; time: "+mydate_time);
                    mDatabaseReference.child("AppLog").push().setValue(logModel);
                    ToastInfo("+Room successfully created");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressbar.setProgress(0);
                        }
                    }, 1200);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressbar.setProgress(0);
                        }
                    }, 1200);
                    ToastInfo("Check you internet connection, or ask the system administrator");
                    return;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressbar.setProgress(0);
                    }
                }, 1200);
            }
        });
        BounceView.addAnimTo(mRoom_btn);

    }

    private void Toast(String s) {
        Toasty.normal(contex, s, Toast.LENGTH_SHORT).show();
    }
    private void ToastSuccess(String s) {
        Toasty.success(contex, s, Toast.LENGTH_SHORT).show();
    }

    private void ToastInfo(final String s) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.info(contex, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(COMMON_TAG,TAG+" onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(COMMON_TAG,TAG+" onDetach");
    }



}
