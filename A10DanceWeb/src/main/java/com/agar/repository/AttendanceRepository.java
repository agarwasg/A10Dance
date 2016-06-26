package com.agar.repository;

import com.agar.domain.Attendance;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Attendance entity.
 */
@SuppressWarnings("unused")
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {

}
