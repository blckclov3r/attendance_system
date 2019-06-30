package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.TempStudent_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage.FirebaseHelper;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentData.RecyclerView.doorAccessListActivity;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentData.RecyclerView.pirDetectionListActivity;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class StudentInfoActivity extends AppCompatActivity {

    private ProgressBar mProgressbar;
    private static final String TAG = "StudentInfoActivity";
    private static final String COMMON_TAG = "abrenica_aljun";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);
        
        TextView uid, firstName, middleName, lastName, course, year, dob, address, contact,
                schedCode, subjectCode, subjectUnit, term, weekDay, startTime, endTime, dateAccess;
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mToolbar.setTitle("Student Information");
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        Context context = getApplicationContext();
        FirebaseHelper.getInstance(databaseReference, context);
        FirebaseHelper.getInstance(databaseReference, context);
        uid = (TextView) findViewById(R.id.uid_studentInfo_textView);
        firstName = (TextView) findViewById(R.id.firstName_studentInfo_textView);
        lastName = (TextView) findViewById(R.id.lastName_studentInfo_textView);
        middleName = (TextView) findViewById(R.id.middleName_studentInfo_textView);
        course = (TextView) findViewById(R.id.course_studentInfo_textView);
        year = (TextView) findViewById(R.id.year_studentInfo_textView);
        dob = (TextView) findViewById(R.id.dob_studentInfo_textView);
        address = (TextView) findViewById(R.id.address_studentInfo_textView);
        contact = (TextView) findViewById(R.id.contact_studentInfo_textView);
        schedCode = (TextView) findViewById(R.id.schedCode_studentInfo_textView);
        subjectCode = (TextView) findViewById(R.id.subjectCode_studentInfo_textView);
        subjectUnit = (TextView) findViewById(R.id.subjectUnit_studentInfo_textView);
        term = (TextView) findViewById(R.id.term_studentInfo_textView);
        weekDay = (TextView) findViewById(R.id.weekday_studentInfo_textView);
        startTime = (TextView) findViewById(R.id.startTime_studentInfo_textView);
        endTime = (TextView) findViewById(R.id.endTime_studentInfo_textView);
        dateAccess = (TextView) findViewById(R.id.dateaccess_studentInfo_textView);
        mProgressbar = (ProgressBar) findViewById(R.id.studentInfo_progressBar);
        mProgressbar.setIndeterminate(true);
        ImageView attendance_imageView = (ImageView) findViewById(R.id.attendance_studentInfo_imageView);
        CircleImageView student_info_imageView = (CircleImageView) findViewById(R.id.student_info_imageView);
        ImageView attendance_doorLockList_imageView = (ImageView) findViewById(R.id.attendance_doorLockList_imageView);
        ImageView pir_list_imageView = (ImageView) findViewById(R.id.pir_list_imageView);
        ImageView pir_calendar_imageView = (ImageView) findViewById(R.id.pir_calendar_imageView);
        TextView dashboard = (TextView) findViewById(R.id.dashboard);


        if (getIntent().getParcelableExtra("selected_room_student") != null) {
            StudentModel studentModel = getIntent().getParcelableExtra("selected_room_student");
            Log.d(COMMON_TAG, TAG + " student model: " + studentModel);
            uid.setText(" Student id: " + studentModel.getUID());
            firstName.setText(" Fullname: " + studentModel.getFirstName());
            lastName.setText(studentModel.getLastName());
            middleName.setText(studentModel.getMiddleName());
            course.setText(" Course: " + studentModel.getCourse());
            year.setText(" Year: " + studentModel.getYear());
            dob.setText(" Date of Birth: " + studentModel.getDob());
            address.setText(" Address: " + studentModel.getAddress());
            contact.setText(" Contact number: " + studentModel.getContact());
            //swap schedCode to room code
            schedCode.setText(" Room code: " + studentModel.getSchedCode());
            subjectCode.setText(" Subject code: " + studentModel.getSubjectCode());
            subjectUnit.setText(" Subject unit: " + studentModel.getSubjectUnit());
            term.setText(" Term: " + studentModel.getTerm());
            weekDay.setText(" Weekday: " + studentModel.getWeekDay());
            startTime.setText(" Start time: " + studentModel.getStartTime());
            endTime.setText(" End time: " + studentModel.getEndTime());
            dateAccess.setText(" Date access: " + studentModel.getDate_access());
            dashboard.setText("Sched. Code: " + studentModel.getSchedCode());

            if (!studentModel.getImageUrl().isEmpty()) {
                Picasso.get()
                        .load(studentModel.getImageUrl())
                        .noFade()
                        .placeholder(R.drawable.person)
                        .into(student_info_imageView);
            }

        }

        pir_list_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentModel studentModel = getIntent().getParcelableExtra("selected_room_student");
                Intent intent = new Intent(StudentInfoActivity.this, pirDetectionListActivity.class);
                intent.putExtra("selected_attendance", studentModel);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        attendance_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentModel studentModel = getIntent().getParcelableExtra("selected_room_student");
                Intent intent = new Intent(StudentInfoActivity.this, StudentInfoAttendaceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("selected_attendance", studentModel);
                startActivity(intent);
            }
        });

        attendance_doorLockList_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentModel studentModel = getIntent().getParcelableExtra("selected_room_student");
                Intent intent = new Intent(StudentInfoActivity.this, doorAccessListActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("selected_attendance", studentModel);
                startActivity(intent);
            }
        });

        pir_calendar_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentModel studentModel = getIntent().getParcelableExtra("selected_room_student");
                Intent intent = new Intent(StudentInfoActivity.this, PirAttendanceActivity.class);
                intent.putExtra("selected_attendance", studentModel);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            }
            finish(); // close this activity and return to preview activity (if there is any)
        } else if (id == R.id.help_menu) {
            Toasty.normal(getApplicationContext(), R.string.student_info_menu, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressbar = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Bungee.slideRight(StudentInfoActivity.this);
    }


}
