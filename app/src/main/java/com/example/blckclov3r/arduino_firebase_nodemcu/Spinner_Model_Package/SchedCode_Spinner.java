package com.example.blckclov3r.arduino_firebase_nodemcu.Spinner_Model_Package;

import android.widget.ArrayAdapter;

import com.example.blckclov3r.arduino_firebase_nodemcu.Home_Fragment_Package.StudentFragment;

import java.util.ArrayList;
import java.util.List;

public class SchedCode_Spinner {
    private List<String> schedCode = new ArrayList<>();

    public static SchedCode_Spinner instance = null;

    private SchedCode_Spinner(){}

    public synchronized static SchedCode_Spinner getInstance(){
        if(instance == null){
            return new SchedCode_Spinner();
        }
        return instance;
    }

    public List<String> getSchedCode() {
        return schedCode;
    }
    public void adapter(ArrayAdapter<String> adapter){
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        StudentFragment.mCodesched_spinner.setAdapter(adapter);
    }

}
