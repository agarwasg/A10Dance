package com.pp.a10dance.document;

public class StudentAttendance {
    private String attendanceId;
    private String studentId;
    private String _id;
    private String _rev;
    private String createdAt;
    private boolean isPresent;

    public StudentAttendance() {

    }

    public StudentAttendance(String attendanceId, String studentId,
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    @Override
    public String toString() {
        return "StudentAttendance{" + "attendanceId='" + attendanceId + '\''
                + ", studentId='" + studentId + '\'' + ", _id='" + _id + '\''
                + ", _rev='" + _rev + '\'' + ", createdAt='" + createdAt + '\''
                + ", isPresent=" + isPresent + '}';
    }
}
