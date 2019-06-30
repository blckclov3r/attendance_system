package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentData;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class StudentInfoAttendaceActivity extends AppCompatActivity {

    private static final String TAG = "StudentInfoAttendaceAct";
    private static final String COMMON_TAG = "abrenica_aljun";


    private CalendarPickerView daterPicker;
    private List<Date> mDateList;
    private SimpleDateFormat mDateFormat;
    private HashSet mHashset;
    private ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info_attendace);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mToolbar.setTitle("Date Access");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        TextView fullName, uid, subject, schedule;

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        StudentModel studentModel = getIntent().getParcelableExtra("selected_attendance");

        mProgressbar = (ProgressBar) findViewById(R.id.studentInfo_progressBar);
        fullName = (TextView) findViewById(R.id.calendar_fullName_textView);
        uid = (TextView) findViewById(R.id.calendar_uid_textView);
        subject = (TextView) findViewById(R.id.calendar_subject_textView);
        schedule = (TextView) findViewById(R.id.calendar_schedCode_textView);
        mProgressbar.setIndeterminate(true);
        daterPicker = (CalendarPickerView) findViewById(R.id.studentInfo_calendar);
        CircleImageView calendar_imageView = (CircleImageView) findViewById(R.id.calendar_imageView);
        mDateList = new ArrayList<>();

        if (studentModel != null) {
            String student_name = studentModel.getFirstName() + " " + studentModel.getMiddleName() + " " + studentModel.getLastName();
            String student_uid = studentModel.getUID();
            String student_subject = studentModel.getSubjectCode();
            String student_schedule = studentModel.getSchedCode();

            fullName.setText("Fullname: " + student_name);
            uid.setText("Student id: " + student_uid);
            subject.setText("Subject: " + student_subject);
            schedule.setText("Schedule: " + student_schedule);

            if (!studentModel.getImageUrl().isEmpty()) {
                Picasso.get()
                        .load(studentModel.getImageUrl())
                        .placeholder(R.drawable.person)
                        .noFade()
                        .into(calendar_imageView);
            }

            daterPicker.init(lastYear.getTime(), nextYear.getTime()).withSelectedDate(new Date()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);
            mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            mHashset = new HashSet();
            Log.d(COMMON_TAG, TAG + " room code: " + studentModel.getRoomCode());
            databaseReference.child("User")
                    .child(studentModel.getUID())
                    .child("private_data")
                    .child(studentModel.getRoomCode())
                    .child("firebase_time_log")
                    .child("data_access").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (dataSnapshot.exists()) {
                                String date = ds.child("data_access").getValue().toString();
                                try {
                                    mDateList.add(mDateFormat.parse(date));
                                    mHashset.addAll(mDateList);
                                    mDateList.clear();
                                    mDateList.addAll(mHashset);
//                            mDateList.add(mDateFormat.parse(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Log.d(COMMON_TAG, TAG + " DATE: " + mDateList);
                        daterPicker.highlightDates(mDateList);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            daterPicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
                @Override
                public void onDateSelected(Date date) {
                    SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String DateStr = timeStampFormat.format(date);
                    DateStr = DateStr.replace("-", "/");
                    Toast(DateStr);
                }

                @Override
                public void onDateUnselected(Date date) {
                }
            });

            daterPicker.setOnInvalidDateSelectedListener(null);
            daterPicker.setDateSelectableFilter(new CalendarPickerView.DateSelectableFilter() {
                Calendar cal = Calendar.getInstance();

                @Override
                public boolean isDateSelectable(Date date) {
                    boolean isSelecteable = true;
                    cal.setTime(date);
                    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                    if (dayOfWeek == Calendar.SUNDAY) {
                        isSelecteable = false;
                    }
                    return isSelecteable;
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } else if (id == R.id.help_menu) {
            Toasty.normal(getApplicationContext(), R.string.student_attendance_menu, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        daterPicker = null;
        mDateList = null;
        mDateFormat = null;
        mHashset = null;
        mProgressbar = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Bungee.shrink(StudentInfoAttendaceActivity.this);
    }

    private void Toast(String s) {
        Toasty.normal(getApplicationContext(), String.valueOf(s), Toast.LENGTH_SHORT).show();
    }
}
