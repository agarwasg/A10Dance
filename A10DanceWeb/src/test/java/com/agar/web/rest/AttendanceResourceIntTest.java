package com.agar.web.rest;

import com.agar.A10DanceWebApp;
import com.agar.domain.Attendance;
import com.agar.repository.AttendanceRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AttendanceResource REST controller.
 *
 * @see AttendanceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = A10DanceWebApp.class)
@WebAppConfiguration
@IntegrationTest
public class AttendanceResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_ATTENDANCE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_ATTENDANCE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_ATTENDANCE_DATE_STR = dateTimeFormatter.format(DEFAULT_ATTENDANCE_DATE);
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Boolean DEFAULT_IS_PRESENT = false;
    private static final Boolean UPDATED_IS_PRESENT = true;

    @Inject
    private AttendanceRepository attendanceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAttendanceMockMvc;

    private Attendance attendance;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AttendanceResource attendanceResource = new AttendanceResource();
        ReflectionTestUtils.setField(attendanceResource, "attendanceRepository", attendanceRepository);
        this.restAttendanceMockMvc = MockMvcBuilders.standaloneSetup(attendanceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        attendance = new Attendance();
        attendance.setAttendanceDate(DEFAULT_ATTENDANCE_DATE);
        attendance.setDescription(DEFAULT_DESCRIPTION);
        attendance.setIsPresent(DEFAULT_IS_PRESENT);
    }

    @Test
    @Transactional
    public void createAttendance() throws Exception {
        int databaseSizeBeforeCreate = attendanceRepository.findAll().size();

        // Create the Attendance

        restAttendanceMockMvc.perform(post("/api/attendances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(attendance)))
                .andExpect(status().isCreated());

        // Validate the Attendance in the database
        List<Attendance> attendances = attendanceRepository.findAll();
        assertThat(attendances).hasSize(databaseSizeBeforeCreate + 1);
        Attendance testAttendance = attendances.get(attendances.size() - 1);
        assertThat(testAttendance.getAttendanceDate()).isEqualTo(DEFAULT_ATTENDANCE_DATE);
        assertThat(testAttendance.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAttendance.isIsPresent()).isEqualTo(DEFAULT_IS_PRESENT);
    }

    @Test
    @Transactional
    public void getAllAttendances() throws Exception {
        // Initialize the database
        attendanceRepository.saveAndFlush(attendance);

        // Get all the attendances
        restAttendanceMockMvc.perform(get("/api/attendances?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(attendance.getId().intValue())))
                .andExpect(jsonPath("$.[*].attendanceDate").value(hasItem(DEFAULT_ATTENDANCE_DATE_STR)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].isPresent").value(hasItem(DEFAULT_IS_PRESENT.booleanValue())));
    }

    @Test
    @Transactional
    public void getAttendance() throws Exception {
        // Initialize the database
        attendanceRepository.saveAndFlush(attendance);

        // Get the attendance
        restAttendanceMockMvc.perform(get("/api/attendances/{id}", attendance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(attendance.getId().intValue()))
            .andExpect(jsonPath("$.attendanceDate").value(DEFAULT_ATTENDANCE_DATE_STR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.isPresent").value(DEFAULT_IS_PRESENT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAttendance() throws Exception {
        // Get the attendance
        restAttendanceMockMvc.perform(get("/api/attendances/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttendance() throws Exception {
        // Initialize the database
        attendanceRepository.saveAndFlush(attendance);
        int databaseSizeBeforeUpdate = attendanceRepository.findAll().size();

        // Update the attendance
        Attendance updatedAttendance = new Attendance();
        updatedAttendance.setId(attendance.getId());
        updatedAttendance.setAttendanceDate(UPDATED_ATTENDANCE_DATE);
        updatedAttendance.setDescription(UPDATED_DESCRIPTION);
        updatedAttendance.setIsPresent(UPDATED_IS_PRESENT);

        restAttendanceMockMvc.perform(put("/api/attendances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAttendance)))
                .andExpect(status().isOk());

        // Validate the Attendance in the database
        List<Attendance> attendances = attendanceRepository.findAll();
        assertThat(attendances).hasSize(databaseSizeBeforeUpdate);
        Attendance testAttendance = attendances.get(attendances.size() - 1);
        assertThat(testAttendance.getAttendanceDate()).isEqualTo(UPDATED_ATTENDANCE_DATE);
        assertThat(testAttendance.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAttendance.isIsPresent()).isEqualTo(UPDATED_IS_PRESENT);
    }

    @Test
    @Transactional
    public void deleteAttendance() throws Exception {
        // Initialize the database
        attendanceRepository.saveAndFlush(attendance);
        int databaseSizeBeforeDelete = attendanceRepository.findAll().size();

        // Get the attendance
        restAttendanceMockMvc.perform(delete("/api/attendances/{id}", attendance.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Attendance> attendances = attendanceRepository.findAll();
        assertThat(attendances).hasSize(databaseSizeBeforeDelete - 1);
    }
}
