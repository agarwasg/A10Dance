package com.agar.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Attendance.
 */
@Entity
@Table(name = "attendance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "attendance_date", nullable = false)
    private ZonedDateTime attendanceDate;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "is_present", nullable = false)
    private Boolean isPresent;

    @ManyToOne
    @NotNull
    private Subject subject;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @NotNull
    @JoinTable(name = "attendance_student",
               joinColumns = @JoinColumn(name="attendances_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="students_id", referencedColumnName="ID"))
    private Set<Student> students = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(ZonedDateTime attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isIsPresent() {
        return isPresent;
    }

    public void setIsPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Attendance attendance = (Attendance) o;
        if(attendance.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, attendance.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Attendance{" +
            "id=" + id +
            ", attendanceDate='" + attendanceDate + "'" +
            ", description='" + description + "'" +
            ", isPresent='" + isPresent + "'" +
            '}';
    }
}
