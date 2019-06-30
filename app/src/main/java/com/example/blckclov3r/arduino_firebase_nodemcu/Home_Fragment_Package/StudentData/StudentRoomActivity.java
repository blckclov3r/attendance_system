package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentData;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.Adapter.StudentRoomRecyclerView;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel_Singleton;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class StudentRoomActivity extends AppCompatActivity implements StudentRoomRecyclerView.StudentRoomClickInterface {

    private static final String TAG = "StudentRoomActivity";
    private static final String COMMON_TAG = "abrenica_nujla";

    private List<StudentModel> mStudentList;
    private StudentRoomRecyclerView mRecyclerView;
    private ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_room);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        mToolbar.setTitle("Student Room");

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseReference = firebaseDatabase.getReference();

        mStudentList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.studentroom_recyclerView);
        mRecyclerView = new StudentRoomRecyclerView(mStudentList, StudentRoomActivity.this);
        mRecyclerView.setStudentRoomClickInterface(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mRecyclerView);
        mProgressbar = (ProgressBar) findViewById(R.id.studentRoom_progressBar);
        mProgressbar.setIndeterminate(true);


        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        if (getIntent().getParcelableExtra("selected_student") != null) {

            StudentModel studentModel = getIntent().getParcelableExtra("selected_student");

            mDatabaseReference.child("User").child(String.valueOf(studentModel.getUID())).child("private_data")
                    .addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (ds.hasChild("schedCode") && ds.hasChild("subjectCode")) {
                                            StudentModel sm = ds.getValue(StudentModel.class);
                                            mStudentList.add(sm);
                                        }
                                    }
                                    mRecyclerView.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                Log.e(COMMON_TAG, TAG + ", Exception: " + e.getMessage());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
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
    protected void onDestroy() {
        super.onDestroy();
        clearModel();
    }

    private void clearModel() {
        new StudentModel("", "", "", "", "",
                "", "", "", "", "", "", "",
                "", "", "", "", "", "", "");
        new RoomModel("", "", "",
                "", "", "", "", "");
        StudentModel_Singleton studentModel_singleton = StudentModel_Singleton.getInstance();
        RoomModel_Singleton roomModel_singleton = RoomModel_Singleton.getInstance();
        roomModel_singleton.clear();
        studentModel_singleton.studentModel_clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Bungee.slideRight(StudentRoomActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } else if (id == R.id.help_menu) {
            Toasty.normal(getApplicationContext(), R.string.student_room_menu, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemClick(int position) {
        StudentModel studentModel = mStudentList.get(position);
        if (getIntent().getParcelableExtra("selected_student") != null) {
            StudentModel sm = getIntent().getParcelableExtra("selected_student");
            studentModel.setImageUrl(sm.getImageUrl());
        }
        Log.d(COMMON_TAG, TAG + " student room: " + studentModel);
        Intent intent = new Intent(StudentRoomActivity.this, StudentInfoActivity.class);
        intent.putExtra("selected_room_student", studentModel);
        startActivity(intent);
        Bungee.slideLeft(StudentRoomActivity.this);
    }


}
