package com.example.blckclov3r.arduino_firebase_nodemcu.FirebaseModel;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentModel implements Parcelable {
    //StudentModel
    private String uid;
    private String schedCode; //
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
    //new
    private String fullName;
    private String date_access;
    private String imageUrl;

    private String fHour;
    private String fMinute;
    private String lHour;
    private String lMinute;

    public StudentModel(String uid, String firstName, String lastName,
                        String middleName, String year, String contact,
                        String address, String dob, String subjectCode,
                        String term, String subjectUnit, String startTime,
                        String endTime, String weekDay, String fullName,
                        String course, String date_access, String schedCode, String roomCode) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.year = year;
        this.contact = contact;
        this.address = address;
        this.dob = dob;
        this.subjectCode = subjectCode;
        this.term = term;
        this.subjectUnit = subjectUnit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekDay = weekDay;
        this.fullName = fullName;
        this.course = course;
        this.date_access = date_access;
        this.schedCode = schedCode;
        this.roomCode = roomCode;
    }

    public StudentModel(String uid, String course, String fullName, String imageUrl) {
        this.uid = uid;
        this.course = course;
        this.fullName = fullName;
        this.imageUrl = imageUrl;
    }

    public StudentModel(String subjectCode, String schedCode, String weekDay, String startTime, String endTime,String roomCode) {
        this.subjectCode = subjectCode;
        this.schedCode = schedCode;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomCode = roomCode;
    }


    public StudentModel(String uid, String firstName, String lastName,
                        String middleName, String year, String contact,
                        String address, String dob, String subjectCode,
                        String term, String subjectUnit, String startTime,
                        String endTime, String weekDay, String roomCode) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.year = year;
        this.contact = contact;
        this.address = address;
        this.dob = dob;
        this.subjectCode = subjectCode;
        this.term = term;
        this.subjectUnit = subjectUnit;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekDay = weekDay;
        this.roomCode = roomCode;
    }

    protected StudentModel(Parcel in) {
        uid = in.readString();
        schedCode = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        middleName = in.readString();
        year = in.readString();
        contact = in.readString();
        address = in.readString();
        dob = in.readString();
        course = in.readString();
        subjectCode = in.readString();
        term = in.readString();
        subjectUnit = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        weekDay = in.readString();
        roomCode = in.readString();
        fullName = in.readString();
        date_access = in.readString();
        imageUrl = in.readString();
        fHour = in.readString();
        fMinute = in.readString();
        lHour = in.readString();
        lMinute = in.readString();
    }

    public static final Creator<StudentModel> CREATOR = new Creator<StudentModel>() {
        @Override
        public StudentModel createFromParcel(Parcel in) {
            return new StudentModel(in);
        }

        @Override
        public StudentModel[] newArray(int size) {
            return new StudentModel[size];
        }
    };

    public String getfHour() {
        return fHour;
    }

    public String getfMinute() {
        return fMinute;
    }

    public void setfMinute(String fMinute) {
        this.fMinute = fMinute;
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

    public void setfHour(String fHour) {
        this.fHour = fHour;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate_access() {
        return date_access;
    }

    public void setDate_access(String date_access) {
        this.date_access = date_access;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public StudentModel() {
    }


    public StudentModel(String uid, String schedCode, String firstName, String lastName, String middleName, String year, String contact, String address, String dob) {
        this.uid = uid;
        this.schedCode = schedCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.year = year;
        this.contact = contact;
        this.address = address;
        this.dob = dob;
    }

    public void setUID(String uid) {
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

    public String getUID() {
        return uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(schedCode);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(middleName);
        dest.writeString(year);
        dest.writeString(contact);
        dest.writeString(address);
        dest.writeString(dob);
        dest.writeString(course);
        dest.writeString(subjectCode);
        dest.writeString(term);
        dest.writeString(subjectUnit);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(weekDay);
        dest.writeString(roomCode);
        dest.writeString(fullName);
        dest.writeString(date_access);
        dest.writeString(imageUrl);
        dest.writeString(fHour);
        dest.writeString(fMinute);
        dest.writeString(lHour);
        dest.writeString(lMinute);
    }

    @Override
    public String toString() {
        return "StudentModel{" +
                "uid='" + uid + '\'' +
                ", schedCode='" + schedCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", year='" + year + '\'' +
                ", contact='" + contact + '\'' +
                ", address='" + address + '\'' +
                ", dob='" + dob + '\'' +
                ", course='" + course + '\'' +
                ", subjectCode='" + subjectCode + '\'' +
                ", term='" + term + '\'' +
                ", subjectUnit='" + subjectUnit + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", weekDay='" + weekDay + '\'' +
                ", roomCode='" + roomCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", date_access='" + date_access + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", fHour='" + fHour + '\'' +
                ", fMinute='" + fMinute + '\'' +
                ", lHour='" + lHour + '\'' +
                ", lMinute='" + lMinute + '\'' +
                '}';
    }
}
