package com.agar.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agar.domain.StudentAttendance;
import com.agar.repository.StudentAttendanceRepository;
import com.agar.web.rest.util.HeaderUtil;
import com.agar.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing StudentAttendance.
 */
@RestController
@RequestMapping("/api")
public class StudentAttendanceResource {

    private final Logger log = LoggerFactory.getLogger(StudentAttendanceResource.class);
        
    @Inject
    private StudentAttendanceRepository studentAttendanceRepository;
    
    /**
     * POST  /student-attendances : Create a new studentAttendance.
     *
     * @param studentAttendance the studentAttendance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new studentAttendance, or with status 400 (Bad Request) if the studentAttendance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/student-attendances",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StudentAttendance> createStudentAttendance(@Valid @RequestBody StudentAttendance studentAttendance) throws URISyntaxException {
        log.debug("REST request to save StudentAttendance : {}", studentAttendance);
        if (studentAttendance.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("studentAttendance", "idexists", "A new studentAttendance cannot already have an ID")).body(null);
        }
        StudentAttendance result = studentAttendanceRepository.save(studentAttendance);
        return ResponseEntity.created(new URI("/api/student-attendances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("studentAttendance", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /student-attendances : Updates an existing studentAttendance.
     *
     * @param studentAttendance the studentAttendance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated studentAttendance,
     * or with status 400 (Bad Request) if the studentAttendance is not valid,
     * or with status 500 (Internal Server Error) if the studentAttendance couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/student-attendances",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StudentAttendance> updateStudentAttendance(@Valid @RequestBody StudentAttendance studentAttendance) throws URISyntaxException {
        log.debug("REST request to update StudentAttendance : {}", studentAttendance);
        if (studentAttendance.getId() == null) {
            return createStudentAttendance(studentAttendance);
        }
        StudentAttendance result = studentAttendanceRepository.save(studentAttendance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("studentAttendance", studentAttendance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /student-attendances : get all the studentAttendances.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of studentAttendances in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/student-attendances",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<StudentAttendance>> getAllStudentAttendances(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StudentAttendances");
        Page<StudentAttendance> page = studentAttendanceRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/student-attendances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /student-attendances/:id : get the "id" studentAttendance.
     *
     * @param id the id of the studentAttendance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the studentAttendance, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/student-attendances/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StudentAttendance> getStudentAttendance(@PathVariable Long id) {
        log.debug("REST request to get StudentAttendance : {}", id);
        StudentAttendance studentAttendance = studentAttendanceRepository.findOne(id);
        return Optional.ofNullable(studentAttendance)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /student-attendances/:id : delete the "id" studentAttendance.
     *
     * @param id the id of the studentAttendance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/student-attendances/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStudentAttendance(@PathVariable Long id) {
        log.debug("REST request to delete StudentAttendance : {}", id);
        studentAttendanceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("studentAttendance", id.toString())).build();
    }

}
