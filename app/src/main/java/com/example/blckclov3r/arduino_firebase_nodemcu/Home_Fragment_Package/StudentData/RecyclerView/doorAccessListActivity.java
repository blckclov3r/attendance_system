package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentData.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.Adapter.DoorAccessAdapter;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.DateAccessModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class doorAccessListActivity extends AppCompatActivity {

    private static final String TAG = doorAccessListActivity.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private List<DateAccessModel> mDatamodelList;
    private DoorAccessAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_access_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        toolbar.setTitle("Date Access Logs");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        Context context = getApplicationContext();
        mDatamodelList = new ArrayList<>();


        TextView fullname, uid, subject, schedule;
        fullname = (TextView) findViewById(R.id.door_fullName_textView);
        uid = (TextView) findViewById(R.id.door_uid_textView);
        subject = (TextView) findViewById(R.id.door_subject_textView);
        schedule = (TextView) findViewById(R.id.door_schedCode_textView);
        CircleImageView imageView = (CircleImageView) findViewById(R.id.doorAccess_list_imageView);
        ProgressBar mProgressbar = (ProgressBar) findViewById(R.id.progressBar_horizontal);
        mProgressbar.setIndeterminate(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.doorAccess_recyclerView);
        mAdapter = new DoorAccessAdapter(mDatamodelList, context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);



        StudentModel studentModel = getIntent().getParcelableExtra("selected_attendance");
        if (studentModel != null) {
            databaseReference.child("User")
                    .child(studentModel.getUID())
                    .child("private_data")
                    .child(studentModel.getRoomCode())
                    .child("firebase_time_log")
                    .child("date_access").orderByChild("date_access").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mDatamodelList.clear();
                    try {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                DateAccessModel dateAccessModel = ds.getValue(DateAccessModel.class);
                                mDatamodelList.add(dateAccessModel);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        Log.e(COMMON_TAG, TAG + " Exception: " + e.getMessage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Picasso.get().load(String.valueOf(studentModel.getImageUrl()))
                    .placeholder(R.drawable.person)
                    .noFade()
                    .into(imageView);

            String student_fullName = studentModel.getFirstName() + " " + studentModel.getMiddleName() + " " + studentModel.getLastName();
            fullname.setText("Student name: " + student_fullName);
            uid.setText("Student id: " + studentModel.getUID());
            subject.setText("Subject code: " + studentModel.getSubjectCode());
            schedule.setText("Schedule code: " + studentModel.getSchedCode());

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
            Toasty.normal(getApplicationContext(), R.string.door_access_list_menu, Toast.LENGTH_SHORT).show();
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
        Bungee.shrink(doorAccessListActivity.this);
    }
}
