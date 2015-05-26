package com.pp.a10dance.model;

/**
 * Created by saketagarwal on 5/18/15.
 */
public class StudentAttendace {

    private String attendanceId;
    private String studentId;
    private boolean isPresent;
    private String _id;
    private String _rev;

    public StudentAttendace() {
    }

    public StudentAttendace(String attendanceId, String studentId,
            boolean isPresent) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.isPresent = isPresent;
    }

    public String getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(String attendanceId) {
        this.attendanceId = attendanceId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
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
        return "StudentAttendace{" + "attendanceId='" + attendanceId + '\''
                + ", studentId='" + studentId + '\'' + ", isPresent="
                + isPresent + ", _id='" + _id + '\'' + ", _rev='" + _rev + '\''
                + '}';
    }
}
