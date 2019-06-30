package com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel;

public class LogModel {
    String logMsg;
    String logAccess;

    public LogModel(){}

    public String getLogMsg() {
        return logMsg;
    }

    public void setLogMsg(String logMsg) {
        this.logMsg = logMsg;
    }

    public String getLogAccess() {
        return logAccess;
    }

    public void setLogAccess(String logAccess) {
        this.logAccess = logAccess;
    }
}
