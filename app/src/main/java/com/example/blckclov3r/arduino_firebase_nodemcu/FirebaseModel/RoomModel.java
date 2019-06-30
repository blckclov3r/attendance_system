package com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel;

import android.os.Parcel;
import android.os.Parcelable;

public class RoomModel implements Parcelable {

    private String schedCode;
    private String roomCode;
    private String subjectCode;
    private String term;
    private String subjectUnit;
    private String startTime;
    private String endTime;
    private String weekDay;

    public RoomModel(){}

    public RoomModel(String schedCode, String roomCode, String subjectCode,
                     String term, String subjectUnit, String startTime,
                     String endTime, String weekDay) {
        this.schedCode = schedCode;
        this.roomCode = roomCode;
        this.subjectCode = subjectCode;
        this.term = term;
        this.subjectUnit = subjectUnit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekDay = weekDay;
    }

    protected RoomModel(Parcel in) {
        schedCode = in.readString();
        roomCode = in.readString();
        subjectCode = in.readString();
        term = in.readString();
        subjectUnit = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        weekDay = in.readString();
    }

    public static final Creator<RoomModel> CREATOR = new Creator<RoomModel>() {
        @Override
        public RoomModel createFromParcel(Parcel in) {
            return new RoomModel(in);
        }

        @Override
        public RoomModel[] newArray(int size) {
            return new RoomModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(schedCode);
        dest.writeString(roomCode);
        dest.writeString(subjectCode);
        dest.writeString(term);
        dest.writeString(subjectUnit);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(weekDay);
    }
}
