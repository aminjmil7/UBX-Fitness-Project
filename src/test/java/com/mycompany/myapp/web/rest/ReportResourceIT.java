package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Equipement;
import com.mycompany.myapp.domain.Media;
import com.mycompany.myapp.domain.Park;
import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.repository.ReportRepository;
import com.mycompany.myapp.service.dto.ReportDTO;
import com.mycompany.myapp.service.mapper.ReportMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportResourceIT {

    private static final String DEFAULT_MAIL = "AAAAAAAAAA";
    private static final String UPDATED_MAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportMockMvc;

    private Report report;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createEntity(EntityManager em) {
        Report report = new Report().mail(DEFAULT_MAIL).message(DEFAULT_MESSAGE).date(DEFAULT_DATE);
        return report;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Report createUpdatedEntity(EntityManager em) {
        Report report = new Report().mail(UPDATED_MAIL).message(UPDATED_MESSAGE).date(UPDATED_DATE);
        return report;
    }

    @BeforeEach
    public void initTest() {
        report = createEntity(em);
    }

    @Test
    @Transactional
    void createReport() throws Exception {
        int databaseSizeBeforeCreate = reportRepository.findAll().size();
        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);
        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isCreated());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate + 1);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getMail()).isEqualTo(DEFAULT_MAIL);
        assertThat(testReport.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testReport.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createReportWithExistingId() throws Exception {
        // Create the Report with an existing ID
        report.setId(1L);
        ReportDTO reportDTO = reportMapper.toDto(report);

        int databaseSizeBeforeCreate = reportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReports() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get the report
        restReportMockMvc
            .perform(get(ENTITY_API_URL_ID, report.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(report.getId().intValue()))
            .andExpect(jsonPath("$.mail").value(DEFAULT_MAIL))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getReportsByIdFiltering() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        Long id = report.getId();

        defaultReportShouldBeFound("id.equals=" + id);
        defaultReportShouldNotBeFound("id.notEquals=" + id);

        defaultReportShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReportShouldNotBeFound("id.greaterThan=" + id);

        defaultReportShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReportShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportsByMailIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where mail equals to DEFAULT_MAIL
        defaultReportShouldBeFound("mail.equals=" + DEFAULT_MAIL);

        // Get all the reportList where mail equals to UPDATED_MAIL
        defaultReportShouldNotBeFound("mail.equals=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllReportsByMailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where mail not equals to DEFAULT_MAIL
        defaultReportShouldNotBeFound("mail.notEquals=" + DEFAULT_MAIL);

        // Get all the reportList where mail not equals to UPDATED_MAIL
        defaultReportShouldBeFound("mail.notEquals=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllReportsByMailIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where mail in DEFAULT_MAIL or UPDATED_MAIL
        defaultReportShouldBeFound("mail.in=" + DEFAULT_MAIL + "," + UPDATED_MAIL);

        // Get all the reportList where mail equals to UPDATED_MAIL
        defaultReportShouldNotBeFound("mail.in=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllReportsByMailIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where mail is not null
        defaultReportShouldBeFound("mail.specified=true");

        // Get all the reportList where mail is null
        defaultReportShouldNotBeFound("mail.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByMailContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where mail contains DEFAULT_MAIL
        defaultReportShouldBeFound("mail.contains=" + DEFAULT_MAIL);

        // Get all the reportList where mail contains UPDATED_MAIL
        defaultReportShouldNotBeFound("mail.contains=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllReportsByMailNotContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where mail does not contain DEFAULT_MAIL
        defaultReportShouldNotBeFound("mail.doesNotContain=" + DEFAULT_MAIL);

        // Get all the reportList where mail does not contain UPDATED_MAIL
        defaultReportShouldBeFound("mail.doesNotContain=" + UPDATED_MAIL);
    }

    @Test
    @Transactional
    void getAllReportsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where message equals to DEFAULT_MESSAGE
        defaultReportShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the reportList where message equals to UPDATED_MESSAGE
        defaultReportShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllReportsByMessageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where message not equals to DEFAULT_MESSAGE
        defaultReportShouldNotBeFound("message.notEquals=" + DEFAULT_MESSAGE);

        // Get all the reportList where message not equals to UPDATED_MESSAGE
        defaultReportShouldBeFound("message.notEquals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllReportsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultReportShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the reportList where message equals to UPDATED_MESSAGE
        defaultReportShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllReportsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where message is not null
        defaultReportShouldBeFound("message.specified=true");

        // Get all the reportList where message is null
        defaultReportShouldNotBeFound("message.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByMessageContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where message contains DEFAULT_MESSAGE
        defaultReportShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the reportList where message contains UPDATED_MESSAGE
        defaultReportShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllReportsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where message does not contain DEFAULT_MESSAGE
        defaultReportShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the reportList where message does not contain UPDATED_MESSAGE
        defaultReportShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllReportsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where date equals to DEFAULT_DATE
        defaultReportShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the reportList where date equals to UPDATED_DATE
        defaultReportShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReportsByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where date not equals to DEFAULT_DATE
        defaultReportShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the reportList where date not equals to UPDATED_DATE
        defaultReportShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReportsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where date in DEFAULT_DATE or UPDATED_DATE
        defaultReportShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the reportList where date equals to UPDATED_DATE
        defaultReportShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllReportsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        // Get all the reportList where date is not null
        defaultReportShouldBeFound("date.specified=true");

        // Get all the reportList where date is null
        defaultReportShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllReportsByMediaIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);
        Media media = MediaResourceIT.createEntity(em);
        em.persist(media);
        em.flush();
        report.addMedia(media);
        reportRepository.saveAndFlush(report);
        Long mediaId = media.getId();

        // Get all the reportList where media equals to mediaId
        defaultReportShouldBeFound("mediaId.equals=" + mediaId);

        // Get all the reportList where media equals to (mediaId + 1)
        defaultReportShouldNotBeFound("mediaId.equals=" + (mediaId + 1));
    }

    @Test
    @Transactional
    void getAllReportsByEquipementIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);
        Equipement equipement = EquipementResourceIT.createEntity(em);
        em.persist(equipement);
        em.flush();
        report.setEquipement(equipement);
        reportRepository.saveAndFlush(report);
        Long equipementId = equipement.getId();

        // Get all the reportList where equipement equals to equipementId
        defaultReportShouldBeFound("equipementId.equals=" + equipementId);

        // Get all the reportList where equipement equals to (equipementId + 1)
        defaultReportShouldNotBeFound("equipementId.equals=" + (equipementId + 1));
    }

    @Test
    @Transactional
    void getAllReportsByParkIsEqualToSomething() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);
        Park park = ParkResourceIT.createEntity(em);
        em.persist(park);
        em.flush();
        report.setPark(park);
        reportRepository.saveAndFlush(report);
        Long parkId = park.getId();

        // Get all the reportList where park equals to parkId
        defaultReportShouldBeFound("parkId.equals=" + parkId);

        // Get all the reportList where park equals to (parkId + 1)
        defaultReportShouldNotBeFound("parkId.equals=" + (parkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportShouldBeFound(String filter) throws Exception {
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(report.getId().intValue())))
            .andExpect(jsonPath("$.[*].mail").value(hasItem(DEFAULT_MAIL)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));

        // Check, that the count call also returns 1
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportShouldNotBeFound(String filter) throws Exception {
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReport() throws Exception {
        // Get the report
        restReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report
        Report updatedReport = reportRepository.findById(report.getId()).get();
        // Disconnect from session so that the updates on updatedReport are not directly saved in db
        em.detach(updatedReport);
        updatedReport.mail(UPDATED_MAIL).message(UPDATED_MESSAGE).date(UPDATED_DATE);
        ReportDTO reportDTO = reportMapper.toDto(updatedReport);

        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testReport.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testReport.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport.mail(UPDATED_MAIL);

        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReport))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testReport.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testReport.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateReportWithPatch() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeUpdate = reportRepository.findAll().size();

        // Update the report using partial update
        Report partialUpdatedReport = new Report();
        partialUpdatedReport.setId(report.getId());

        partialUpdatedReport.mail(UPDATED_MAIL).message(UPDATED_MESSAGE).date(UPDATED_DATE);

        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReport))
            )
            .andExpect(status().isOk());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
        Report testReport = reportList.get(reportList.size() - 1);
        assertThat(testReport.getMail()).isEqualTo(UPDATED_MAIL);
        assertThat(testReport.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testReport.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReport() throws Exception {
        int databaseSizeBeforeUpdate = reportRepository.findAll().size();
        report.setId(count.incrementAndGet());

        // Create the Report
        ReportDTO reportDTO = reportMapper.toDto(report);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Report in the database
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReport() throws Exception {
        // Initialize the database
        reportRepository.saveAndFlush(report);

        int databaseSizeBeforeDelete = reportRepository.findAll().size();

        // Delete the report
        restReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, report.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Report> reportList = reportRepository.findAll();
        assertThat(reportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
