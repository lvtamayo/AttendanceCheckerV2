package edu.admu.cs298s28.attendancechecker;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserSchedule extends RealmObject {
    @PrimaryKey
    private String userSched_ID =  UUID.randomUUID().toString();

    private String userSched_uID;
    private String getUserSched_subjID;

    public String getUserSched_ID() {
        return userSched_ID;
    }

    public void setUserSched_ID(String userSched_ID) {
        this.userSched_ID = userSched_ID;
    }

    public String getUserSched_uID() {
        return userSched_uID;
    }

    public void setUserSched_uID(String userSched_uID) {
        this.userSched_uID = userSched_uID;
    }

    public String getGetUserSched_subjID() {
        return getUserSched_subjID;
    }

    public void setGetUserSched_subjID(String getUserSched_subjID) {
        this.getUserSched_subjID = getUserSched_subjID;
    }

    @Override
    public String toString() {
        return "UserSchedule{" +
                "userSched_ID='" + userSched_ID + '\'' +
                ", userSched_uID='" + userSched_uID + '\'' +
                ", getUserSched_subjID='" + getUserSched_subjID + '\'' +
                '}';
    }
}
