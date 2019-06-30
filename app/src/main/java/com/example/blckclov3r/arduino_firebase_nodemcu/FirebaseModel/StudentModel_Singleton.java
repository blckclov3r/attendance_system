package com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel;

import android.util.Log;

public class StudentModel_Singleton {

    private static final String TAG = StudentModel_Singleton.class.getSimpleName();
    private static final String COMMON_TAG = "abrenica_nujla";

    private String uid;
    private String schedCode;
    private String firstName;
    private String lastName;
    private String middleName;
    private String year;
    private String contact;
    private String address;
    private String dob;
    private String course;
    //RoomModel
    private String subjectCode;
    private String term;
    private String subjectUnit;
    private String startTime;
    private String endTime;
    private String weekDay;
    private String roomCode;
    private String fullName;
    private String date_access;
    private String imageUrl;
    private static StudentModel_Singleton instance = null;

    private StudentModel_Singleton() {
    }

    public synchronized static StudentModel_Singleton getInstance() {
        if (instance == null) {
            Log.d(COMMON_TAG, TAG + ", synchronized instance == null");
            instance = new StudentModel_Singleton();
        }
        return instance;
    }

    public void studentModel_clear() {
        this.uid = "";
        this.schedCode = "";
        this.firstName = "";
        this.lastName = "";
        this.middleName = "";
        this.year = "";
        this.contact = "";
        this.address = "";
        this.dob = "";
        this.course = "";
        this.subjectCode = "";
        this.term = "";
        this.subjectUnit = "";
        this.startTime = "";
        this.endTime = "";
        this.weekDay = "";
        this.roomCode = "";
        this.fullName = "";
        this.date_access = "";
        this.imageUrl = "";
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDate_access() {
        return date_access;
    }

    public void setDate_access(String date_access) {
        this.date_access = date_access;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }


}
