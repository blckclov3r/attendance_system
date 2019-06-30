package com.example.blckclov3r.arduino_firebase_nodemcu.NavFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.blckclov3r.arduino_firebase_nodemcu.Adapter.LogRecyclerAdapter;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.LogModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebasePackage.FirebaseHelper;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class LogFragment extends Fragment {

    private static final String TAG = LogFragment.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private List<LogModel> mLoglist;
    private RecyclerView mRecyclerView;
    private LogRecyclerAdapter mLogAdapter;
    private DatabaseReference mDatabaseReference;
    private FloatingActionButton mFab;
    private Context mContext;
    private long mLastClickTime = 0;

    public LogFragment() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.log_fragment, container, false);
        mContext = getActivity();
        FirebaseHelper.getInstance(mDatabaseReference, mContext);
        mLoglist = new ArrayList<>();
        mLogAdapter = new LogRecyclerAdapter(mLoglist, mContext);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.logRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Objects.requireNonNull(container).getContext()));
        mRecyclerView.setAdapter(mLogAdapter);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                        .setCustomImage(R.drawable.trash_t)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover all logs")
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
                                if (mLoglist.isEmpty()) {
                                    sDialog.dismissWithAnimation();
                                    ToastError("The log is already empty");
                                    return;
                                }
                                sDialog.dismissWithAnimation();
                                new SweetAlertDialog(Objects.requireNonNull(LogFragment.this.getActivity()), SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Good job!")
                                        .setContentText("Log has been cleared")
                                        .show();
                                mDatabaseReference.child("AppLog").removeValue();
                                mLoglist.clear();
                                mLogAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();

//                Fragment fragment = new HomeFragment();
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        });

        mDatabaseReference.child("AppLog").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mLoglist != null) {
                    mLoglist.clear();
                }
                try {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds.exists()) {
                                LogModel logModel = ds.getValue(LogModel.class);
                                mLoglist.add(logModel);
                            }
                        }
                        Collections.reverse(mLoglist);
                        mLogAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Log.e(COMMON_TAG, TAG + " , onDatachange exception: " + e.getMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ToastInfo(final String s) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.info(mContext, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ToastError(final String s) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Toasty.error(mContext, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoglist = null;
        mRecyclerView = null;
        mLogAdapter = null;
        mDatabaseReference = null;
        mFab = null;
        mContext = null;
    }
}
