package edu.admu.cs298s28.attendancechecker;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class TransactionData extends RealmObject {

    @PrimaryKey
    private String trans_id;

    private String trans_userid;
    private String trans_schedid;
    private String trans_datetime;

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getTrans_userid() {
        return trans_userid;
    }

    public void setTrans_userid(String trans_userid) {
        this.trans_userid = trans_userid;
    }

    public String getTrans_schedid() {
        return trans_schedid;
    }

    public void setTrans_schedid(String trans_schedid) {
        this.trans_schedid = trans_schedid;
    }

    public String getTrans_datetime() {
        return trans_datetime;
    }

    public void setTrans_datetime(String trans_datetime) {
        this.trans_datetime = trans_datetime;
    }

    @Override
    public String toString() {
        return "TransactionData{" +
                "trans_id='" + trans_id + '\'' +
                ", trans_userid='" + trans_userid + '\'' +
                ", trans_schedid='" + trans_schedid + '\'' +
                ", trans_datetime='" + trans_datetime + '\'' +
                '}';
    }
}
