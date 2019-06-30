package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.AccessFS;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.AccessFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;

import spencerstudios.com.bungeelib.Bungee;

public class AccessActivity_FullScreen extends AppCompatActivity {
    private FullScreen_Singleton fullScreen = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.access_activity_fullscreen);
            Fragment fragment = new AccessFragment();
            fullScreen = FullScreen_Singleton.getInstance();
            fullScreen.setImageFS(false);
            getSupportFragmentManager().beginTransaction().replace(R.id.accessFullscreen_frameLayout, fragment).commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fullScreen.setImageFS(true);
        finish();
        Bungee.shrink(AccessActivity_FullScreen.this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fullScreen.setImageFS(true);
    }
}
