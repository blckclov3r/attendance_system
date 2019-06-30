package com.example.blckclov3r.arduino_firebase_nodemcu.Spinner_Model_Package;

import android.widget.ArrayAdapter;

import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.AccessFragment;

import java.util.ArrayList;
import java.util.List;

public class RoomCode_Spinner {

    private List<String> roomCode = new ArrayList<>();
    private static RoomCode_Spinner instance = null;

    private RoomCode_Spinner(){}

    public synchronized static RoomCode_Spinner getInstance(){
        if(instance == null){
            instance = new RoomCode_Spinner();
        }
        return instance;
    }

    public List<String> getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(List<String> roomCode) {
        this.roomCode = roomCode;
    }

    public static void setInstance(RoomCode_Spinner instance) {
        RoomCode_Spinner.instance = instance;
    }
    public void adapter(ArrayAdapter<String> adapter){
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AccessFragment.mAccess_spinner.setAdapter(adapter);
    }
}
