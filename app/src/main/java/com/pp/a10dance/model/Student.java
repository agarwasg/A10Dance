package com.pp.a10dance.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.pp.a10dance.helper.Utils;

/**
 * Created by saketagarwal on 5/18/15.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Student {

    private String name;
    private String phone;
    private String email;
    private String roll;
    private String _id;
    private String _rev;
    private String profClassId;
    private String createdAt;

    public Student() {
    }

    public Student(String classId, String phone, String email, String roll,
            String name) {
        this.profClassId = classId;
        this.phone = phone;
        this.email = email;
        this.roll = roll;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
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

    public String getProfClassId() {
        return profClassId;
    }

    public void setProfClassId(String profClassId) {
        this.profClassId = profClassId;
    }

    @Override
    public String toString() {
        return "Student{" + "name='" + name + '\'' + ", phone='" + phone + '\''
                + ", email='" + email + '\'' + ", roll='" + roll + '\''
                + ", _id='" + _id + '\'' + ", _rev='" + _rev + '\''
                + ", profClassId='" + profClassId + '\'' + '}';
    }

    public String getStudentFirstLetter() {
        if (!Utils.StringUtils.isBlank(name)) {
            return name.substring(0, 1);
        }
        return "";
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
