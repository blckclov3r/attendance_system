package com.example.blckclov3r.arduino_firebase_nodemcu.ActivityPackage;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.StudentModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage.FirebaseHelper;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.RoomData.RoomList_Data_Activity;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.RoomListFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentData.StudentRoomActivity;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentListFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.NavFragments.AboutFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.NavFragments.HomeFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.NavFragments.LogFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.example.blckclov3r.arduino_firebase_nodemcu.SplashScreen.SplashScreen_Activity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import spencerstudios.com.bungeelib.Bungee;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnClickFragment,
        StudentListFragment.OnClickStudentList, RoomListFragment.OnRoomClickInterface {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    //vars
    private FirebaseAuth mAuth;
    private FirebaseHelper mFirebaseHelper;
    private FirebaseUser firebaseUser;

    //components
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemTextColor(ColorStateList.valueOf(Color.DKGRAY));

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        mFirebaseHelper = FirebaseHelper.getInstance(mDatabaseReference, MainActivity.this);
        firebaseUser = mAuth.getCurrentUser();
        firebaseInstance();

        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            mFragment = new HomeFragment();
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, mFragment).commit();
            mNavigationView.setCheckedItem(R.id.nav_home);
        }

        new HomeFragment().setOnCLickFragment(this);
        new StudentListFragment().setOnClickStudentList(this);
        new RoomListFragment().setOnRoomClickInterface(this);
    }

    private void firebaseInstance() {
        if (firebaseUser != null) {
            if (mFirebaseHelper.firebaseAuthTest(mAuth)) {
                Log.d(COMMON_TAG, TAG + ", not logged in");
                finish();
            } else {
                Log.d(COMMON_TAG, TAG + ", you already logged in");
            }
        } else {
            Intent intent = new Intent(this, SplashScreen_Activity.class);
            startActivity(intent);
            Bungee.fade(this);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mFragment != null) {
                for (int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++) {
                    mFragmentManager.popBackStackImmediate();
                }
                mFragment = new HomeFragment();
                mNavigationView.setCheckedItem(R.id.nav_home);
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, mFragment)
                        .commit();
                mToolbar.setTitle("Smart Attendance");
            }else{
                mFragment = new HomeFragment();
                mNavigationView.setCheckedItem(R.id.nav_home);
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, mFragment)
                        .commit();
                mToolbar.setTitle("Smart Attendance");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        for (int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++) {
            mFragmentManager.popBackStackImmediate();
        }
        int id = item.getItemId();
        if (id == R.id.action_home) {
            mFragment = new HomeFragment();
            mNavigationView.getMenu().getItem(0).setChecked(true);
            mFragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_container, mFragment)
                    .commit();
            mToolbar.setTitle("Smart Attendance");
            item.setChecked(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if (mFragment != null) {
                for (int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++) {
                    mFragmentManager.popBackStackImmediate();
                }
            }
            mFragment = new HomeFragment();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .setAllowOptimization(true)
                            .replace(R.id.fragment_container, mFragment)
                            .commit();
                    mToolbar.setTitle("Smart Attendance");
                }
            }, 300);
            item.setChecked(true);
        } else if (id == R.id.nav_credit) {
            if (mFragment != null) {
                for (int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++) {
                    mFragmentManager.popBackStackImmediate();
                }
            }
            mFragment = new LogFragment();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFragmentManager.beginTransaction()
                            .setAllowOptimization(true)
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_container, mFragment)
                            .commit();
                    mToolbar.setTitle("Logs");
                }
            }, 300);
            item.setChecked(true);
        } else if (id == R.id.nav_about) {
            if (mFragment != null) {
                for (int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++) {
                    mFragmentManager.popBackStackImmediate();
                }
            }
            mFragment = new AboutFragment();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mFragmentManager.beginTransaction()
                            .setAllowOptimization(true)
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.fragment_container, mFragment)
                            .commit();
                    mToolbar.setTitle("About");
                }
            }, 300);
            item.setChecked(true);
        } else if (id == R.id.nav_share) {
            item.setChecked(false);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        ArrayList<Uri> uris = new ArrayList<Uri>();
                        Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        sendIntent.setType("*/*");
                        uris.add(Uri.fromFile(new File(getApplicationInfo().publicSourceDir)));
                        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                        startActivity(Intent.createChooser(sendIntent, null));
                    } catch (Exception e) {
                        Log.d(COMMON_TAG, TAG + " Share app exception: " + e.getMessage());
                    }
                }
            });
        } else if (id == R.id.nav_logout) {
            try {
                item.setChecked(false);
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setCustomImage(R.drawable.exit_o)
                        .setTitleText("Are you sure?")
                        .setContentText("You want to logout?")
                        .setConfirmText("Yes")
                        .setCancelText("No")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                mAuth.signOut();
                                firebaseUser = mAuth.getCurrentUser();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (firebaseUser == null) {
                                            Intent intent = new Intent(MainActivity.this, SplashScreen_Activity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Log.e(COMMON_TAG, TAG + " logout error");
                                        }
                                    }
                                }, 500);

                            }
                        })
                        .show();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(COMMON_TAG, TAG + " nav_logout: " + e.getMessage());
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(COMMON_TAG, TAG + " onStart invoked");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(COMMON_TAG, TAG + " onStop invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClickFragment(Fragment fragment) {
        if (fragment != null) {
            for (int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++) {
                mFragmentManager.popBackStackImmediate();
            }
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onClickList(StudentModel studentModel) {

        Log.d(COMMON_TAG, TAG + " studentModel: " + studentModel.toString());
        Intent intent = new Intent(MainActivity.this, StudentRoomActivity.class);
        intent.putExtra("selected_student", studentModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Bungee.slideLeft(MainActivity.this);
    }

    @Override
    public void onClickRoom(RoomModel roomModel) {
        Intent intent = new Intent(MainActivity.this, RoomList_Data_Activity.class);
        intent.putExtra("selected_room", roomModel);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Bungee.slideLeft(MainActivity.this);

    }

}
