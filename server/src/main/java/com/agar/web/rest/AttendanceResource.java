package com.agar.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.agar.domain.Attendance;
import com.agar.repository.AttendanceRepository;
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
 * REST controller for managing Attendance.
 */
@RestController
@RequestMapping("/api")
public class AttendanceResource {

    private final Logger log = LoggerFactory.getLogger(AttendanceResource.class);
        
    @Inject
    private AttendanceRepository attendanceRepository;
    
    /**
     * POST  /attendances : Create a new attendance.
     *
     * @param attendance the attendance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attendance, or with status 400 (Bad Request) if the attendance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/attendances",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attendance> createAttendance(@Valid @RequestBody Attendance attendance) throws URISyntaxException {
        log.debug("REST request to save Attendance : {}", attendance);
        if (attendance.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("attendance", "idexists", "A new attendance cannot already have an ID")).body(null);
        }
        Attendance result = attendanceRepository.save(attendance);
        return ResponseEntity.created(new URI("/api/attendances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("attendance", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attendances : Updates an existing attendance.
     *
     * @param attendance the attendance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attendance,
     * or with status 400 (Bad Request) if the attendance is not valid,
     * or with status 500 (Internal Server Error) if the attendance couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/attendances",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attendance> updateAttendance(@Valid @RequestBody Attendance attendance) throws URISyntaxException {
        log.debug("REST request to update Attendance : {}", attendance);
        if (attendance.getId() == null) {
            return createAttendance(attendance);
        }
        Attendance result = attendanceRepository.save(attendance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("attendance", attendance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attendances : get all the attendances.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of attendances in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/attendances",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Attendance>> getAllAttendances(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Attendances");
        Page<Attendance> page = attendanceRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attendances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /attendances/:id : get the "id" attendance.
     *
     * @param id the id of the attendance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attendance, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/attendances/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attendance> getAttendance(@PathVariable Long id) {
        log.debug("REST request to get Attendance : {}", id);
        Attendance attendance = attendanceRepository.findOne(id);
        return Optional.ofNullable(attendance)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /attendances/:id : delete the "id" attendance.
     *
     * @param id the id of the attendance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/attendances/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        log.debug("REST request to delete Attendance : {}", id);
        attendanceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("attendance", id.toString())).build();
    }

}
