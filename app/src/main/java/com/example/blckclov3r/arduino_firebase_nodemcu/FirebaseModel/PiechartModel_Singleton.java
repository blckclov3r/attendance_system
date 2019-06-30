package com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel;

public class PiechartModel_Singleton {
    private String pie_key;
    private int pie_number;
    private boolean toastRepeat = false;

    private PiechartModel_Singleton() {
    }

    private static PiechartModel_Singleton instance = null;

    public synchronized static PiechartModel_Singleton getInstance() {
        if (instance == null) {
            instance = new PiechartModel_Singleton();
        }
        return instance;
    }

    public void setPieChart(String pie_key, int pie_number) {
        this.pie_key = pie_key;
        this.pie_number = pie_number;
    }

    public String getPie_key() {
        return pie_key;
    }

    public void setPie_key(String pie_key) {
        this.pie_key = pie_key;
    }

    public int getPie_number() {
        return pie_number;
    }

    public void setPie_number(int pie_number) {
        this.pie_number = pie_number;
    }

    public boolean isToastRepeat() {
        return toastRepeat;
    }

    public void setToastRepeat(boolean toastRepeat) {
        this.toastRepeat = toastRepeat;
    }
}
