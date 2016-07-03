package com.agar.repository;

import com.agar.domain.StudentAttendance;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StudentAttendance entity.
 */
@SuppressWarnings("unused")
public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance,Long> {

}
