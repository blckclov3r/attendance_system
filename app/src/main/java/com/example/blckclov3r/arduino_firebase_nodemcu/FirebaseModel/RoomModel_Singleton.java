package com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel;

import android.util.Log;

public class RoomModel_Singleton {

    private static final String TAG = RoomModel_Singleton.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private String schedCode;
    private String roomCode;
    private String subjectCode;
    private String term;
    private String subjectUnit;
    private String startTime;
    private String endTime;
    private String weekDay;
    private String uid;
    private String fHour;
    private String fMinute;
    private String lHour;
    private String lMinute;

    private RoomModel_Singleton() {
    }

    private static RoomModel_Singleton instance = null;

    public synchronized static RoomModel_Singleton getInstance() {
        if (instance == null) {
                    Log.d(COMMON_TAG, TAG + ", synchronized, instance == null");
                    instance = new RoomModel_Singleton();
        }
        return instance;
    }

    public void setData(String schedCode, String roomCode, String subjectCode, String term, String subjectUnit, String startTime, String endTime, String weekDay) {
        this.schedCode = schedCode;
        this.roomCode = roomCode;
        this.subjectCode = subjectCode;
        this.term = term;
        this.subjectUnit = subjectUnit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekDay = weekDay;
    }

    public String getlHour() {
        return lHour;
    }

    public void setlHour(String lHour) {
        this.lHour = lHour;
    }

    public String getlMinute() {
        return lMinute;
    }

    public void setlMinute(String lMinute) {
        this.lMinute = lMinute;
    }

    public String getfMinute() {
        return fMinute;
    }

    public void setfMinute(String fMinute) {
        this.fMinute = fMinute;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSchedCode() {
        return schedCode;
    }

    public void setSchedCode(String schedCode) {
        this.schedCode = schedCode;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getSubjectUnit() {
        return subjectUnit;
    }

    public void setSubjectUnit(String subjectUnit) {
        this.subjectUnit = subjectUnit;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getfHour() {
        return fHour;
    }

    public void setfHour(String fHour) {
        this.fHour = fHour;
    }

    public void clear() {
        schedCode = "";
        roomCode = "";
        subjectCode = "";
        term = "";
        subjectUnit = "";
        startTime = "";
        endTime = "";
        weekDay = "";
        uid = "";
        fHour = "";
        fMinute = "";
        lHour = "";
        lMinute = "";
    }
}
