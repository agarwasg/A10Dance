package com.agar.repository;

import com.agar.domain.Subject;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Subject entity.
 */
@SuppressWarnings("unused")
public interface SubjectRepository extends JpaRepository<Subject,Long> {

    @Query("select subject from Subject subject where subject.user.login = ?#{principal.username}")
    List<Subject> findByUserIsCurrentUser();

    @Query("select distinct subject from Subject subject left join fetch subject.students")
    List<Subject> findAllWithEagerRelationships();

    @Query("select subject from Subject subject left join fetch subject.students where subject.id =:id")
    Subject findOneWithEagerRelationships(@Param("id") Long id);

}
