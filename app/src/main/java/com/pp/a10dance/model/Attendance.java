package com.pp.a10dance.model;

/**
 * Created by saketagarwal on 5/18/15.
 */
public class Attendance {

    private String classId;
    private String dateTime;
    private String _id;
    private String _rev;

    public Attendance() {
    }

    public Attendance(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String date) {
        this.dateTime = date;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    @Override
    public String toString() {
        return "Attendance{" + "classId='" + classId + '\'' + ", dateTime='"
                + dateTime + '\'' + ", _id='" + _id + '\'' + ", _rev='" + _rev
                + '\'' + '}';
    }
}
