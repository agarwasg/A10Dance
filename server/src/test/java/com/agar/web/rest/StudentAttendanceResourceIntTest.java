package com.agar.web.rest;

import com.agar.A10DanceApp;
import com.agar.domain.StudentAttendance;
import com.agar.repository.StudentAttendanceRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StudentAttendanceResource REST controller.
 *
 * @see StudentAttendanceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = A10DanceApp.class)
@WebAppConfiguration
@IntegrationTest
public class StudentAttendanceResourceIntTest {


    private static final Boolean DEFAULT_IS_PRESENT = false;
    private static final Boolean UPDATED_IS_PRESENT = true;

    @Inject
    private StudentAttendanceRepository studentAttendanceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStudentAttendanceMockMvc;

    private StudentAttendance studentAttendance;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StudentAttendanceResource studentAttendanceResource = new StudentAttendanceResource();
        ReflectionTestUtils.setField(studentAttendanceResource, "studentAttendanceRepository", studentAttendanceRepository);
        this.restStudentAttendanceMockMvc = MockMvcBuilders.standaloneSetup(studentAttendanceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        studentAttendance = new StudentAttendance();
        studentAttendance.setIsPresent(DEFAULT_IS_PRESENT);
    }

    @Test
    @Transactional
    public void createStudentAttendance() throws Exception {
        int databaseSizeBeforeCreate = studentAttendanceRepository.findAll().size();

        // Create the StudentAttendance

        restStudentAttendanceMockMvc.perform(post("/api/student-attendances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(studentAttendance)))
                .andExpect(status().isCreated());

        // Validate the StudentAttendance in the database
        List<StudentAttendance> studentAttendances = studentAttendanceRepository.findAll();
        assertThat(studentAttendances).hasSize(databaseSizeBeforeCreate + 1);
        StudentAttendance testStudentAttendance = studentAttendances.get(studentAttendances.size() - 1);
        assertThat(testStudentAttendance.isIsPresent()).isEqualTo(DEFAULT_IS_PRESENT);
    }

    @Test
    @Transactional
    public void checkIsPresentIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentAttendanceRepository.findAll().size();
        // set the field null
        studentAttendance.setIsPresent(null);

        // Create the StudentAttendance, which fails.

        restStudentAttendanceMockMvc.perform(post("/api/student-attendances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(studentAttendance)))
                .andExpect(status().isBadRequest());

        List<StudentAttendance> studentAttendances = studentAttendanceRepository.findAll();
        assertThat(studentAttendances).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStudentAttendances() throws Exception {
        // Initialize the database
        studentAttendanceRepository.saveAndFlush(studentAttendance);

        // Get all the studentAttendances
        restStudentAttendanceMockMvc.perform(get("/api/student-attendances?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(studentAttendance.getId().intValue())))
                .andExpect(jsonPath("$.[*].isPresent").value(hasItem(DEFAULT_IS_PRESENT.booleanValue())));
    }

    @Test
    @Transactional
    public void getStudentAttendance() throws Exception {
        // Initialize the database
        studentAttendanceRepository.saveAndFlush(studentAttendance);

        // Get the studentAttendance
        restStudentAttendanceMockMvc.perform(get("/api/student-attendances/{id}", studentAttendance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(studentAttendance.getId().intValue()))
            .andExpect(jsonPath("$.isPresent").value(DEFAULT_IS_PRESENT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStudentAttendance() throws Exception {
        // Get the studentAttendance
        restStudentAttendanceMockMvc.perform(get("/api/student-attendances/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudentAttendance() throws Exception {
        // Initialize the database
        studentAttendanceRepository.saveAndFlush(studentAttendance);
        int databaseSizeBeforeUpdate = studentAttendanceRepository.findAll().size();

        // Update the studentAttendance
        StudentAttendance updatedStudentAttendance = new StudentAttendance();
        updatedStudentAttendance.setId(studentAttendance.getId());
        updatedStudentAttendance.setIsPresent(UPDATED_IS_PRESENT);

        restStudentAttendanceMockMvc.perform(put("/api/student-attendances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStudentAttendance)))
                .andExpect(status().isOk());

        // Validate the StudentAttendance in the database
        List<StudentAttendance> studentAttendances = studentAttendanceRepository.findAll();
        assertThat(studentAttendances).hasSize(databaseSizeBeforeUpdate);
        StudentAttendance testStudentAttendance = studentAttendances.get(studentAttendances.size() - 1);
        assertThat(testStudentAttendance.isIsPresent()).isEqualTo(UPDATED_IS_PRESENT);
    }

    @Test
    @Transactional
    public void deleteStudentAttendance() throws Exception {
        // Initialize the database
        studentAttendanceRepository.saveAndFlush(studentAttendance);
        int databaseSizeBeforeDelete = studentAttendanceRepository.findAll().size();

        // Get the studentAttendance
        restStudentAttendanceMockMvc.perform(delete("/api/student-attendances/{id}", studentAttendance.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StudentAttendance> studentAttendances = studentAttendanceRepository.findAll();
        assertThat(studentAttendances).hasSize(databaseSizeBeforeDelete - 1);
    }
}
