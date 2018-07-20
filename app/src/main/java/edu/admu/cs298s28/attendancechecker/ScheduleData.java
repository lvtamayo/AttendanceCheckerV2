package edu.admu.cs298s28.attendancechecker;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ScheduleData extends RealmObject {
    @PrimaryKey
    private String subject_id;

    private String subject_title;
    private String subject_desc;
    private String subject_lat;
    private String subject_long;
    private String subject_time;
    private String subject_day;
    private String subject_sy;

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_title() {
        return subject_title;
    }

    public void setSubject_title(String subject_title) {
        this.subject_title = subject_title;
    }

    public String getSubject_desc() {
        return subject_desc;
    }

    public void setSubject_desc(String subject_desc) {
        this.subject_desc = subject_desc;
    }

    public String getSubject_lat() {
        return subject_lat;
    }

    public void setSubject_lat(String subject_lat) {
        this.subject_lat = subject_lat;
    }

    public String getSubject_long() {
        return subject_long;
    }

    public void setSubject_long(String subject_long) {
        this.subject_long = subject_long;
    }

    public String getSubject_time() {
        return subject_time;
    }

    public void setSubject_time(String subject_time) {
        this.subject_time = subject_time;
    }

    public String getSubject_day() {
        return subject_day;
    }

    public void setSubject_day(String subject_day) {
        this.subject_day = subject_day;
    }

    public String getSubject_sy() {
        return subject_sy;
    }

    public void setSubject_sy(String subject_sy) {
        this.subject_sy = subject_sy;
    }

    @Override
    public String toString() {
        return "ScheduleData{" +
                "subject_id='" + subject_id + '\'' +
                ", subject_title='" + subject_title + '\'' +
                ", subject_desc='" + subject_desc + '\'' +
                ", subject_lat='" + subject_lat + '\'' +
                ", subject_long='" + subject_long + '\'' +
                ", subject_time='" + subject_time + '\'' +
                ", subject_day='" + subject_day + '\'' +
                ", subject_sy='" + subject_sy + '\'' +
                '}';
    }
}
