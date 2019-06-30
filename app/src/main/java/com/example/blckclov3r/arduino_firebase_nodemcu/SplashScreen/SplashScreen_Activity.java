package com.example.blckclov3r.arduino_firebase_nodemcu.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import com.example.blckclov3r.arduino_firebase_nodemcu.ActivityPackage.MainActivity;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.FirebasePersistence;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.example.blckclov3r.arduino_firebase_nodemcu.SplashScreen.subPackage.Login_Fragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.SplashScreen.subPackage.Splashscreen_fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import cn.pedant.SweetAlert.SweetAlertDialog;
import spencerstudios.com.bungeelib.Bungee;


public class SplashScreen_Activity extends AppCompatActivity {

    private static final String TAG = "SplashScreen_Activity";
    private static final String COMMON_TAG = "abrenica_nujla";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebasePersistence firebasePersistence = FirebasePersistence.getInstance();

        if(savedInstanceState == null) {
            if (!firebasePersistence.isPersistence()) {
                if (!firebasePersistence.isPersistence()) {
                    Log.d(COMMON_TAG, TAG + " firebaseDatabase.setPersistenceEnabled(true) invoked");
                    firebaseDatabase.setPersistenceEnabled(true);
                }
                firebasePersistence.setPersistence(true);
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.splashScreen_frameLayout, new Splashscreen_fragment())
                    .commit();
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3300);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.splashScreen_frameLayout, new Login_Fragment())
                                .commit();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            mThread.start();
        } else {
            Thread mThread  = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3300);
                        Intent intent = new Intent(SplashScreen_Activity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Bungee.shrink(SplashScreen_Activity.this);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            mThread.start();
        }
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setCustomImage(R.drawable.logout_a)
                .setTitleText("Are you sure?")
                .setContentText("Do you want to close this app?")
                .setConfirmText("Yes")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        finish();
                    }
                }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        }).show();
//        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




}
