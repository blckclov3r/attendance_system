package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentData;

import android.annotation.SuppressLint;
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

public class PirAttendanceActivity extends AppCompatActivity {

    private static final String TAG = "PirAttendanceActivity";
    private static final String COMMON_TAG = "abrenica_nujla";

    private CalendarPickerView mCalPickerView;
    private List<Date> mDateList;
    private SimpleDateFormat mDateFormat;
    private HashSet mHashset;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pir_attendance);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mToolbar.setTitle("PIR Attendance");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        mDateList = new ArrayList<>();
        mCalPickerView = (CalendarPickerView) findViewById(R.id.pir_attendance_cal);
        ProgressBar mProgressbar = (ProgressBar) findViewById(R.id.pirAttendance_progressBar);
        mProgressbar.setIndeterminate(true);
        TextView calendar_fullName_textView = (TextView) findViewById(R.id.calendar_fullName_textView);
        TextView calendar_uid_textView = (TextView) findViewById(R.id.calendar_uid_textView);
        TextView calendar_subject_textView = (TextView) findViewById(R.id.calendar_subject_textView);
        TextView calendar_schedCode_textView = (TextView) findViewById(R.id.calendar_schedCode_textView);
        CircleImageView calendar_imageView = (CircleImageView) findViewById(R.id.calendar_imageView);

        mCalPickerView.init(lastYear.getTime(), nextYear.getTime()).withSelectedDate(new Date()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        mDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        mHashset = new HashSet();

        StudentModel studentModel = getIntent().getParcelableExtra("selected_attendance");
        if (studentModel != null) {
            databaseReference.child("User")
                    .child(studentModel.getUID())
                    .child("private_data")
                    .child(studentModel.getRoomCode())
                    .child("firebase_time_log")
                    .child("pirSensor1").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (dataSnapshot.exists()) {
                                String date = ds.child("sensor").getValue().toString();
                                try {
                                    mDateList.add(mDateFormat.parse(date));
                                    mHashset.addAll(mDateList);
                                    mDateList.clear();
                                    mDateList.addAll(mHashset);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        Log.d(COMMON_TAG, TAG + " DATE: " + mDateList);
                        mCalPickerView.highlightDates(mDateList);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            mCalPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
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

            mCalPickerView.setOnInvalidDateSelectedListener(null);
            mCalPickerView.setDateSelectableFilter(new CalendarPickerView.DateSelectableFilter() {
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

            Picasso.get().load(studentModel.getImageUrl()).noFade().placeholder(R.drawable.person).into(calendar_imageView);
            String student_fullName = studentModel.getFirstName() + " " + studentModel.getMiddleName() + " " + studentModel.getLastName();
            calendar_fullName_textView.setText("Student name: " + student_fullName);
            calendar_uid_textView.setText("Student id: " + studentModel.getUID());
            calendar_subject_textView.setText("Subject code: " + studentModel.getSubjectCode());
            calendar_schedCode_textView.setText("Schedule code: " + studentModel.getSchedCode());


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
            Toasty.normal(getApplicationContext(), R.string.student_pirAttendance_menu, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Bungee.shrink(PirAttendanceActivity.this);
    }

    private void Toast(String s) {
        Toasty.normal(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

}
