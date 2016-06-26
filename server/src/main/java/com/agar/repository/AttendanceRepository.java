package com.agar.repository;

import com.agar.domain.Attendance;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Attendance entity.
 */
@SuppressWarnings("unused")
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {

    @Query("select distinct attendance from Attendance attendance left join fetch attendance.students")
    List<Attendance> findAllWithEagerRelationships();

    @Query("select attendance from Attendance attendance left join fetch attendance.students where attendance.id =:id")
    Attendance findOneWithEagerRelationships(@Param("id") Long id);

}
