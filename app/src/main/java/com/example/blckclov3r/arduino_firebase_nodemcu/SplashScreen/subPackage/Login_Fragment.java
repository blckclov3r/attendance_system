package com.example.blckclov3r.arduino_firebase_nodemcu.SplashScreen.subPackage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.ActivityPackage.MainActivity;
import com.example.blckclov3r.arduino_firebase_nodemcu.DialogPackage.AccessCustomDialog;
import com.example.blckclov3r.arduino_firebase_nodemcu.DialogPackage.OnInputSelected;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.LogModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage.FirebaseHelper;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class Login_Fragment extends Fragment implements OnInputSelected {

    //vars constant
    private static final String TAG = Login_Fragment.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    //vars
    private DatabaseReference mDatabaseReference;
    private FirebaseHelper mFirebaseHelper;
    private FirebaseAuth mAuth;
    private Spinner mLoginSpinner;
    private long mlastClickTime = 0;
    private int mPosition = 0;

    //components
    private FloatingActionButton mFab;
    private ProgressBar mProgressbar;
    private Button mLoginBtn;
    private ProgressBar mLoginFragment_progressBar;
    private EditText mUser_et;
    private EditText mPwd_et;

    public Login_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        mFirebaseHelper = FirebaseHelper.getInstance(mDatabaseReference, getActivity());
        mUser_et = (EditText) view.findViewById(R.id.loginFragment_user_editText);
        mPwd_et = (EditText) view.findViewById(R.id.loginFragment_pwd_editText);
        mLoginBtn = (Button) view.findViewById(R.id.loginFragment_loginBtn);
        mLoginFragment_progressBar = (ProgressBar) view.findViewById(R.id.loginFragment_progressBar);
        mLoginSpinner = (Spinner) view.findViewById(R.id.login_spinner);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progressBar_horizontal);
        mProgressbar.setIndeterminate(true);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.login_as, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item2);
        mLoginSpinner.setAdapter(adapter);
        mLoginFragment_progressBar.setVisibility(View.INVISIBLE);



        new AccessCustomDialog().setOnInputSelected(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLogin();
        setLogin_spinner();
        setFab();
    }

    private void setFab() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mlastClickTime < 1000) {
                    return;
                }
                mlastClickTime = SystemClock.elapsedRealtime();
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                ArrayList<Uri> uris = new ArrayList<Uri>();
                Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                sendIntent.setType("*/*");
                uris.add(Uri.fromFile(new File(Objects.requireNonNull(getActivity()).getApplicationInfo().publicSourceDir)));
                sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                startActivity(Intent.createChooser(sendIntent, null));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mLoginSpinner.setSelection(0);
        mUser_et.setBackgroundResource(R.drawable.bg_et);
        mPwd_et.setBackgroundResource(R.drawable.bg_et);
    }


    private void setLogin_spinner() {
        mLoginSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                if (mPosition == 0) {
                    mLoginSpinner.setSelection(0);
                    mUser_et.setBackgroundResource(R.drawable.bg_et);
                    mPwd_et.setBackgroundResource(R.drawable.bg_et);
                } else if (mPosition == 1) {
                    mLoginSpinner.setSelection(0);
                    AccessCustomDialog mDialog = new AccessCustomDialog();
                    mDialog.setTargetFragment(Login_Fragment.this, 1);
                    mDialog.show(Objects.requireNonNull(getFragmentManager()), "AccessCustomDialog");

                } else {
                    mLoginSpinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setLogin() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mlastClickTime < 2500) {
                    return;
                }
                mlastClickTime = SystemClock.elapsedRealtime();
                mLoginFragment_progressBar.setVisibility(View.VISIBLE);
                String email = mUser_et.getText().toString().trim();
                String pwd = mPwd_et.getText().toString().trim();
                if (email.isEmpty()) {
                    mUser_et.setError("Don't leave this field empty");
                    mUser_et.requestFocus();
                    mLoginFragment_progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (pwd.isEmpty()) {
                    mPwd_et.setError("Password must not empty");
                    mPwd_et.requestFocus();
                    mLoginFragment_progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (pwd.length() <= 5) {
                    mPwd_et.setError("Password must be at least 6 characters ");
                    mPwd_et.requestFocus();
                    mLoginFragment_progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mLoginFragment_progressBar.setVisibility(View.INVISIBLE);
                                }
                            }, 1000);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            Toasty.info(Objects.requireNonNull(getActivity()), "Welcome!, you are logged in as Administrator", Toast.LENGTH_LONG).show();

                            Log.d(COMMON_TAG, TAG + " setLogin(): success");
                            String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                            LogModel logModel = new LogModel();
                            logModel.setLogMsg("Login as administrator [Login]");
                            logModel.setLogAccess(mydate_time);
                            mDatabaseReference.child("AppLog").push().setValue(logModel);
                            getActivity().finish();
                        } else {
                            Log.e(COMMON_TAG, TAG + ", setLogin(): fail!");
                            mLoginFragment_progressBar.setVisibility(View.INVISIBLE);
                            String mydate_time = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                            LogModel logModel = new LogModel();
                            logModel.setLogMsg("Login failed [Login]");
                            logModel.setLogAccess(mydate_time);
                            mDatabaseReference.child("AppLog").push().setValue(logModel);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(COMMON_TAG, TAG + " setLogin(): " + e.getMessage());
                        Toast_Error(e.getMessage());
                        mLoginFragment_progressBar.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
    }

    private void Toast_Error(final String s) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.error(Objects.requireNonNull(getActivity()), s, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void sendInput(final String input) {
        mFirebaseHelper.setInputChecker(input, mDatabaseReference);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseReference = null;
        mFirebaseHelper = null;
        mAuth = null;
        mLoginSpinner = null;
        mlastClickTime = 0;
        mFab = null;
        mProgressbar = null;
        mLoginBtn = null;
        mLoginFragment_progressBar = null;
        mUser_et = null;
        mPwd_et = null;
    }
}
