package com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.blckclov3r.arduino_firebase_nodemcu.Adapter.RoomRecyclerAdapter;
import com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel.RoomModel;
import com.example.blckclov3r.arduino_firebase_nodemcu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hari.bounceview.BounceView;

public class RoomListFragment extends Fragment implements RoomRecyclerAdapter.OnRoomCLick {

    private static final String TAG = RoomListFragment.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private List<RoomModel> mRoomList;
    private RoomRecyclerAdapter mAdapter;
    private DatabaseReference mDatabaseReference;
    private ProgressBar mProgressbar;
    private EditText mRoomSearch;
    private ValueEventListener valueEventListener;
    private Query mQuery;

    public RoomListFragment() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = firebaseDatabase.getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.roomlist_fragment, container, false);

        Context context = getActivity();
        mRoomList = new ArrayList<>();
        mProgressbar = (ProgressBar) view.findViewById(R.id.roomList_progressBar);
        mProgressbar.setIndeterminate(true);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.roomlist_recyclerView);
        mAdapter = new RoomRecyclerAdapter(mRoomList, context);
        mAdapter.setItemClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter);
        mRoomSearch = (EditText) view.findViewById(R.id.roomSearch);
        BounceView.addAnimTo(mRoomSearch);
        mRoomSearch.requestFocus();


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        valueEventListener = mDatabaseReference.child("Room").orderByChild("schedCode")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            mRoomList.clear();
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.exists()) {
                                        RoomModel roomModel = ds.getValue(RoomModel.class);
                                        mRoomList.add(roomModel);
                                    }
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            Log.e(COMMON_TAG, TAG + " Unable to add room: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        mRoomSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                try {
                    if (s.length() != 0) {
                        mQuery = mDatabaseReference.child("Room").orderByChild("schedCode")
                                .startAt(String.valueOf(s).toUpperCase().trim())
                                .endAt(String.valueOf(s).toUpperCase() + "\uf8ff");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    mQuery = mDatabaseReference.child("Room").orderByChild("schedCode")
                            .startAt(String.valueOf(s).toUpperCase().trim())
                            .endAt(String.valueOf(s).toUpperCase() + "\uf8ff");
                    mQuery.addValueEventListener(valueEventListener);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (s.length() != 0) {
                        mQuery = mDatabaseReference.child("Room").orderByChild("schedCode")
                                .startAt(String.valueOf(s).toUpperCase().trim())
                                .endAt(String.valueOf(s).toUpperCase() + "\uf8ff");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Objects.requireNonNull(getFragmentManager()).beginTransaction().remove(new RoomListFragment()).commit();
        if (mDatabaseReference != null) {
            mDatabaseReference.removeEventListener(valueEventListener);
        }
        if(mListener != null){
            mListener = null;
        }
    }


    @Override
    public void onCLick(int position) {
        RoomModel roomModel = mRoomList.get(position);
        mListener.onClickRoom(roomModel);
    }

    private OnRoomClickInterface mListener;
    public interface OnRoomClickInterface{
        void onClickRoom(RoomModel roomModel);
    }
    public void setOnRoomClickInterface(OnRoomClickInterface mListener){
        this.mListener = mListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnRoomClickInterface){
            mListener = (OnRoomClickInterface) getActivity();
        }else{
            throw new RuntimeException(context.toString()+" must implement OnRoomClickInterface");
        }
    }

}