package com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel;

public class Progress_Singleton {
    private boolean progress = false;

    private static Progress_Singleton instance = null;

    private Progress_Singleton() {
    }

    public synchronized static Progress_Singleton getInstance() {
        if (instance == null) {
            instance = new Progress_Singleton();
        }
        return instance;
    }

    public boolean isProgress() {
        return progress;
    }

    public void setProgress(boolean progress) {
        this.progress = progress;
    }
}
