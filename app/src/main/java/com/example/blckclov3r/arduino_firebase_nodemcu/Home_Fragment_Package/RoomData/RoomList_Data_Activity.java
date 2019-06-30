package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.RoomData;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class RoomList_Data_Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list__data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);

        TextView schedCode, roomCode, subjectCode, term, subjectUnit, startTime, endTime, weekDay;
        toolbar.setTitle("Room Information");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        schedCode = (TextView) findViewById(R.id.schedCode_textView);
        roomCode = (TextView) findViewById(R.id.roomCode_textView);
        subjectCode = (TextView) findViewById(R.id.subjectCode_textView);
        term = (TextView) findViewById(R.id.term_textView);
        subjectUnit = (TextView) findViewById(R.id.subjectUnit_textView);
        startTime = (TextView) findViewById(R.id.startTime_textView);
        endTime = (TextView) findViewById(R.id.endTime_textView);
        weekDay = (TextView) findViewById(R.id.weekDay_textView);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.activityRoomList_progressBar);
        progressBar.setIndeterminate(true);

        if (getIntent().getParcelableExtra("selected_room") != null) {
            RoomModel roomModel = getIntent().getParcelableExtra("selected_room");
            String sched_code_ = roomModel.getSchedCode();
            String room_code_ = roomModel.getRoomCode();
            String subject_code_ = roomModel.getSubjectCode();
            String term_ = roomModel.getTerm();
            String subject_unit_ = roomModel.getSubjectUnit();
            String startTime_ = roomModel.getStartTime();
            String endTime_ = roomModel.getEndTime();
            String weekDay_ = roomModel.getWeekDay();

            schedCode.setText(" Schedule code: " + sched_code_);
            roomCode.setText(" Room code: " + room_code_);
            subjectCode.setText(" Subject code: " + subject_code_);
            term.setText(" Term: " + term_);
            subjectUnit.setText(" Subject unit: " + subject_unit_);
            startTime.setText(" Start time: " + startTime_);
            endTime.setText(" End time: " + endTime_);
            weekDay.setText(" Weekday: " + weekDay_);

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setIndeterminate(false);
            }
        }, 1000);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } else if (id == R.id.help_menu) {
            Toasty.normal(this, R.string.room_info_menu, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Bungee.slideRight(RoomList_Data_Activity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    private void Toast(String s) {
        Toasty.normal(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
