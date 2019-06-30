package com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel;

public class FirebasePersistence {

    private boolean persistence = false;

    private FirebasePersistence(){}

    private static FirebasePersistence instance = null;

    public synchronized static FirebasePersistence getInstance(){
        if(instance == null){
                if(instance == null) {
                    instance = new FirebasePersistence();
                }
        }
        return instance;
    }

    public void setPersistence(boolean persistence){
        this.persistence = persistence;
    }

    public boolean isPersistence(){
        return persistence;
    }
}
