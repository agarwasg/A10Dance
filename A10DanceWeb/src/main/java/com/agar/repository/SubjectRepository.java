package com.agar.repository;

import com.agar.domain.Subject;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Subject entity.
 */
@SuppressWarnings("unused")
public interface SubjectRepository extends JpaRepository<Subject,Long> {

    @Query("select subject from Subject subject where subject.user.login = ?#{principal.username}")
    List<Subject> findByUserIsCurrentUser();

}
