package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentData.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.Adapter.PirRecyclerListAdapter;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.PirModel;
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

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class pirDetectionListActivity extends AppCompatActivity {

    private static final String COMMON_TAG = "abrenica_nujla";
    private static final String TAG = pirDetectionListActivity.class.getSimpleName();

    private List<PirModel> mPirList;
    private PirRecyclerListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pir_access_list);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        Context context = getApplicationContext();
        mPirList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pir_list_recyclerView);
        mAdapter = new PirRecyclerListAdapter(mPirList,context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
        TextView pir_list_fullName_textView = (TextView) findViewById(R.id.pir_list_fullName_textView);
        TextView pir_list_uid_textView = (TextView) findViewById(R.id.pir_list_uid_textView);
        TextView pir_list__subject_textView = (TextView) findViewById(R.id.pir_list__subject_textView);
        TextView pir_list_schedCode_textView = (TextView) findViewById(R.id.pir_list_schedCode_textView);
        ImageView pir_access_list_imageView = (ImageView) findViewById(R.id.pir_access_list_imageView);
        ProgressBar mProgressbar = (ProgressBar) findViewById(R.id.pir_access_list_progressBar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mToolbar);
        toolbar.setTitle("Pir Detection Logs");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        if(getIntent().getParcelableExtra("selected_attendance")!=null){
            StudentModel studentModel = getIntent().getParcelableExtra("selected_attendance");


            Picasso.get().load(studentModel.getImageUrl())
                    .placeholder(R.drawable.person)
                    .noFade()
                    .into(pir_access_list_imageView);

            String student_fullName = studentModel.getFirstName()+" "+studentModel.getMiddleName()+" "+studentModel.getLastName();
            pir_list_fullName_textView.setText("Student name: "+student_fullName);
            pir_list_uid_textView.setText("Student id: "+studentModel.getUID());
            pir_list__subject_textView.setText("Subject code: "+studentModel.getSubjectCode());
            pir_list_schedCode_textView.setText("Schedule code: "+studentModel.getSchedCode());

            databaseReference.child("User")
                    .child(studentModel.getUID())
                    .child("private_data")
                    .child(studentModel.getRoomCode())
                    .child("firebase_time_log")
                    .child("pirSensor2")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(mPirList.size()>0) {
                                mPirList.clear();
                            }
                            try {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if(ds.exists()) {
                                            PirModel pirModel = ds.getValue(PirModel.class);
                                            mPirList.add(pirModel);
//                                        Log.d(COMMON_TAG, TAG + ", " + pirModel.getSensor());
                                        }
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }else{
            Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
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
            Toasty.normal(getApplicationContext(), R.string.student_pir_detection_menu, Toast.LENGTH_SHORT).show();
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
        Bungee.shrink(pirDetectionListActivity.this);
    }
}
