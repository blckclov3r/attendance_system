package com.example.blckclov3r.arduino_firebase_nodemcu.NavFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.AccessFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.RoomFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.RoomListFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.RoomSearchFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentListFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentSearchFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentUpdateFragment;
import com.example.blckclov3r.arduino_firebase_nodemcu.NavFragments.FloatingActionButton.EnrolledChart;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.fujiyuu75.sequent.Animation;
import com.fujiyuu75.sequent.Sequent;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import hari.bounceview.BounceView;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    //components
    private LinearLayout mInsertRoom_linearLayout;
    private LinearLayout mAccess_linearLayout;
    private LinearLayout mInsertStudent_linearLayout;
    private LinearLayout mRoomList_linearLayout;
    private LinearLayout mStudentList_linearLayout;
    private LinearLayout mSearchStudent_linearLayout;
    private LinearLayout mSearchRoom_linearLayout;
    private LinearLayout mExit_linearLayout;
    private LinearLayout mStudentUpdate_linearLayout;
    private FloatingActionButton mFab;

    //vars
    private Fragment mFragment = null;
    private OnClickFragment mListener;

    public void setOnCLickFragment(OnClickFragment mListener) {
        this.mListener = mListener;
    }

    public interface OnClickFragment {
        void onClickFragment(Fragment fragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClickFragment) {
            mListener = (OnClickFragment) getActivity();
        } else {
            throw new RuntimeException(context.toString() + " must implement OnClickFragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mListener != null) {
            mListener = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        mInsertRoom_linearLayout = (LinearLayout) view.findViewById(R.id.insert_room_linearLayout);
        mAccess_linearLayout = (LinearLayout) view.findViewById(R.id.access_linearLayout);
        mInsertStudent_linearLayout = (LinearLayout) view.findViewById(R.id.insert_student_linearLayout);
        mRoomList_linearLayout = (LinearLayout) view.findViewById(R.id.roomList_linearLayout);
        mStudentList_linearLayout = (LinearLayout) view.findViewById(R.id.studentlist_linearLayout);
        mSearchStudent_linearLayout = (LinearLayout) view.findViewById(R.id.searchStudent_linearLayout);
        mSearchRoom_linearLayout = (LinearLayout) view.findViewById(R.id.searchroom_linearLayout);
        mExit_linearLayout = (LinearLayout) view.findViewById(R.id.exit_linearLayout);
        mStudentUpdate_linearLayout = (LinearLayout) view.findViewById(R.id.studentupdate_linearLayout);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        ProgressBar progress = (ProgressBar) view.findViewById(R.id.home_progressBar);
        progress.setIndeterminate(true);
        LinearLayout mGroup_linearLayout = (LinearLayout) view.findViewById(R.id.group_linearLayout);
        RelativeLayout mDashboard_relativeLayout = (RelativeLayout) view.findViewById(R.id.dashboard_relativeLayout);

        if (savedInstanceState == null) {
            try {

                Sequent.origin(mDashboard_relativeLayout).duration(500) // option.
                        .delay(0) // option. StartOffSet time.
                        .offset(10) // option. Interval time.
                        .anim(getActivity(), Animation.FADE_IN_DOWN)
                        .start();

                Sequent.origin(mGroup_linearLayout).duration(100) // option.
                        .delay(0) // option. StartOffSet time.
                        .offset(10) // option. Interval time.
                        .anim(getActivity(), Animation.BOUNCE_IN)
                        .start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            mRoomList_linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment = new RoomListFragment();
                    mListener.onClickFragment(mFragment);
                }
            });


            mAccess_linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment = new AccessFragment();
                    mListener.onClickFragment(mFragment);
                }
            });


            mInsertRoom_linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment = new RoomFragment();
                    mListener.onClickFragment(mFragment);
                }
            });


            mInsertStudent_linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment = new StudentFragment();
                    mListener.onClickFragment(mFragment);
                }
            });


            mStudentList_linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment = new StudentListFragment();
                    mListener.onClickFragment(mFragment);
                }
            });


            mSearchStudent_linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment = new StudentSearchFragment();
                    mListener.onClickFragment(mFragment);
                }
            });


            mSearchRoom_linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment = new RoomSearchFragment();
                    mListener.onClickFragment(mFragment);
                }
            });


            mStudentUpdate_linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment = new StudentUpdateFragment();
                    mListener.onClickFragment(mFragment);
                }
            });


            mExit_linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(Objects.requireNonNull(getActivity()), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                            .setCustomImage(R.drawable.exit_o)
                            .setTitleText("Are you sure?")
                            .setContentText("Do you want to exit this app?")
                            .setConfirmText("Yes")
                            .setCancelText("No")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    getActivity().finish();
                                }
                            }).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }).show();
                }
            });


            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment = new EnrolledChart();
                    mListener.onClickFragment(mFragment);
                }
            });
            BounceView.addAnimTo(mExit_linearLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(COMMON_TAG, TAG + " onDestroy");
    }


}
