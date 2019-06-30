package com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel;

public class TempStudent_Singleton {

    private String firstName;
    private String lastName;
    private String mInitial;
    private String course;
    private String subjectCode;
    private String cardID;
    private String date_access;
    private String fullName;

    private static TempStudent_Singleton instance = null;

    public synchronized static TempStudent_Singleton getInstance() {
        if (instance == null) {
            instance = new TempStudent_Singleton();
        }
        return instance;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getmInitial() {
        return mInitial;
    }

    public void setmInitial(String mInitial) {
        this.mInitial = mInitial;
    }

    public static void setInstance(TempStudent_Singleton instance) {
        TempStudent_Singleton.instance = instance;
    }

    public String getAndroid_dateTime() {
        return date_access;
    }

    public void setAndroid_dateTime(String date_access) {
        this.date_access = date_access;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
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

    public void clear() {
        firstName = "";
        lastName = "";
        mInitial = "";
        course = "";
        subjectCode = "";
        cardID = "";
        date_access = "";
        fullName = "";
    }
}
