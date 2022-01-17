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
import com.mycompany.myapp.domain.enumeration.AuthType;
import com.mycompany.myapp.repository.MediaRepository;
import com.mycompany.myapp.service.dto.MediaDTO;
import com.mycompany.myapp.service.mapper.MediaMapper;
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
 * Integration tests for the {@link MediaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MediaResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_TYPE = "BBBBBBBBBB";

    private static final AuthType DEFAULT_AUTH_TYPE = AuthType.LEARN;
    private static final AuthType UPDATED_AUTH_TYPE = AuthType.TECHFILE;

    private static final String ENTITY_API_URL = "/api/media";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MediaMapper mediaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMediaMockMvc;

    private Media media;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Media createEntity(EntityManager em) {
        Media media = new Media()
            .fileName(DEFAULT_FILE_NAME)
            .filePath(DEFAULT_FILE_PATH)
            .fileType(DEFAULT_FILE_TYPE)
            .authType(DEFAULT_AUTH_TYPE);
        return media;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Media createUpdatedEntity(EntityManager em) {
        Media media = new Media()
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .fileType(UPDATED_FILE_TYPE)
            .authType(UPDATED_AUTH_TYPE);
        return media;
    }

    @BeforeEach
    public void initTest() {
        media = createEntity(em);
    }

    @Test
    @Transactional
    void createMedia() throws Exception {
        int databaseSizeBeforeCreate = mediaRepository.findAll().size();
        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);
        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isCreated());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeCreate + 1);
        Media testMedia = mediaList.get(mediaList.size() - 1);
        assertThat(testMedia.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testMedia.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testMedia.getFileType()).isEqualTo(DEFAULT_FILE_TYPE);
        assertThat(testMedia.getAuthType()).isEqualTo(DEFAULT_AUTH_TYPE);
    }

    @Test
    @Transactional
    void createMediaWithExistingId() throws Exception {
        // Create the Media with an existing ID
        media.setId(1L);
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        int databaseSizeBeforeCreate = mediaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMediaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(media.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].authType").value(hasItem(DEFAULT_AUTH_TYPE.toString())));
    }

    @Test
    @Transactional
    void getMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get the media
        restMediaMockMvc
            .perform(get(ENTITY_API_URL_ID, media.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(media.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.fileType").value(DEFAULT_FILE_TYPE))
            .andExpect(jsonPath("$.authType").value(DEFAULT_AUTH_TYPE.toString()));
    }

    @Test
    @Transactional
    void getMediaByIdFiltering() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        Long id = media.getId();

        defaultMediaShouldBeFound("id.equals=" + id);
        defaultMediaShouldNotBeFound("id.notEquals=" + id);

        defaultMediaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMediaShouldNotBeFound("id.greaterThan=" + id);

        defaultMediaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMediaShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMediaByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileName equals to DEFAULT_FILE_NAME
        defaultMediaShouldBeFound("fileName.equals=" + DEFAULT_FILE_NAME);

        // Get all the mediaList where fileName equals to UPDATED_FILE_NAME
        defaultMediaShouldNotBeFound("fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllMediaByFileNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileName not equals to DEFAULT_FILE_NAME
        defaultMediaShouldNotBeFound("fileName.notEquals=" + DEFAULT_FILE_NAME);

        // Get all the mediaList where fileName not equals to UPDATED_FILE_NAME
        defaultMediaShouldBeFound("fileName.notEquals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllMediaByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileName in DEFAULT_FILE_NAME or UPDATED_FILE_NAME
        defaultMediaShouldBeFound("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME);

        // Get all the mediaList where fileName equals to UPDATED_FILE_NAME
        defaultMediaShouldNotBeFound("fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllMediaByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileName is not null
        defaultMediaShouldBeFound("fileName.specified=true");

        // Get all the mediaList where fileName is null
        defaultMediaShouldNotBeFound("fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByFileNameContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileName contains DEFAULT_FILE_NAME
        defaultMediaShouldBeFound("fileName.contains=" + DEFAULT_FILE_NAME);

        // Get all the mediaList where fileName contains UPDATED_FILE_NAME
        defaultMediaShouldNotBeFound("fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllMediaByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileName does not contain DEFAULT_FILE_NAME
        defaultMediaShouldNotBeFound("fileName.doesNotContain=" + DEFAULT_FILE_NAME);

        // Get all the mediaList where fileName does not contain UPDATED_FILE_NAME
        defaultMediaShouldBeFound("fileName.doesNotContain=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllMediaByFilePathIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where filePath equals to DEFAULT_FILE_PATH
        defaultMediaShouldBeFound("filePath.equals=" + DEFAULT_FILE_PATH);

        // Get all the mediaList where filePath equals to UPDATED_FILE_PATH
        defaultMediaShouldNotBeFound("filePath.equals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllMediaByFilePathIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where filePath not equals to DEFAULT_FILE_PATH
        defaultMediaShouldNotBeFound("filePath.notEquals=" + DEFAULT_FILE_PATH);

        // Get all the mediaList where filePath not equals to UPDATED_FILE_PATH
        defaultMediaShouldBeFound("filePath.notEquals=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllMediaByFilePathIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where filePath in DEFAULT_FILE_PATH or UPDATED_FILE_PATH
        defaultMediaShouldBeFound("filePath.in=" + DEFAULT_FILE_PATH + "," + UPDATED_FILE_PATH);

        // Get all the mediaList where filePath equals to UPDATED_FILE_PATH
        defaultMediaShouldNotBeFound("filePath.in=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllMediaByFilePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where filePath is not null
        defaultMediaShouldBeFound("filePath.specified=true");

        // Get all the mediaList where filePath is null
        defaultMediaShouldNotBeFound("filePath.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByFilePathContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where filePath contains DEFAULT_FILE_PATH
        defaultMediaShouldBeFound("filePath.contains=" + DEFAULT_FILE_PATH);

        // Get all the mediaList where filePath contains UPDATED_FILE_PATH
        defaultMediaShouldNotBeFound("filePath.contains=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllMediaByFilePathNotContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where filePath does not contain DEFAULT_FILE_PATH
        defaultMediaShouldNotBeFound("filePath.doesNotContain=" + DEFAULT_FILE_PATH);

        // Get all the mediaList where filePath does not contain UPDATED_FILE_PATH
        defaultMediaShouldBeFound("filePath.doesNotContain=" + UPDATED_FILE_PATH);
    }

    @Test
    @Transactional
    void getAllMediaByFileTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileType equals to DEFAULT_FILE_TYPE
        defaultMediaShouldBeFound("fileType.equals=" + DEFAULT_FILE_TYPE);

        // Get all the mediaList where fileType equals to UPDATED_FILE_TYPE
        defaultMediaShouldNotBeFound("fileType.equals=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByFileTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileType not equals to DEFAULT_FILE_TYPE
        defaultMediaShouldNotBeFound("fileType.notEquals=" + DEFAULT_FILE_TYPE);

        // Get all the mediaList where fileType not equals to UPDATED_FILE_TYPE
        defaultMediaShouldBeFound("fileType.notEquals=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByFileTypeIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileType in DEFAULT_FILE_TYPE or UPDATED_FILE_TYPE
        defaultMediaShouldBeFound("fileType.in=" + DEFAULT_FILE_TYPE + "," + UPDATED_FILE_TYPE);

        // Get all the mediaList where fileType equals to UPDATED_FILE_TYPE
        defaultMediaShouldNotBeFound("fileType.in=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByFileTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileType is not null
        defaultMediaShouldBeFound("fileType.specified=true");

        // Get all the mediaList where fileType is null
        defaultMediaShouldNotBeFound("fileType.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByFileTypeContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileType contains DEFAULT_FILE_TYPE
        defaultMediaShouldBeFound("fileType.contains=" + DEFAULT_FILE_TYPE);

        // Get all the mediaList where fileType contains UPDATED_FILE_TYPE
        defaultMediaShouldNotBeFound("fileType.contains=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByFileTypeNotContainsSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where fileType does not contain DEFAULT_FILE_TYPE
        defaultMediaShouldNotBeFound("fileType.doesNotContain=" + DEFAULT_FILE_TYPE);

        // Get all the mediaList where fileType does not contain UPDATED_FILE_TYPE
        defaultMediaShouldBeFound("fileType.doesNotContain=" + UPDATED_FILE_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByAuthTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where authType equals to DEFAULT_AUTH_TYPE
        defaultMediaShouldBeFound("authType.equals=" + DEFAULT_AUTH_TYPE);

        // Get all the mediaList where authType equals to UPDATED_AUTH_TYPE
        defaultMediaShouldNotBeFound("authType.equals=" + UPDATED_AUTH_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByAuthTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where authType not equals to DEFAULT_AUTH_TYPE
        defaultMediaShouldNotBeFound("authType.notEquals=" + DEFAULT_AUTH_TYPE);

        // Get all the mediaList where authType not equals to UPDATED_AUTH_TYPE
        defaultMediaShouldBeFound("authType.notEquals=" + UPDATED_AUTH_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByAuthTypeIsInShouldWork() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where authType in DEFAULT_AUTH_TYPE or UPDATED_AUTH_TYPE
        defaultMediaShouldBeFound("authType.in=" + DEFAULT_AUTH_TYPE + "," + UPDATED_AUTH_TYPE);

        // Get all the mediaList where authType equals to UPDATED_AUTH_TYPE
        defaultMediaShouldNotBeFound("authType.in=" + UPDATED_AUTH_TYPE);
    }

    @Test
    @Transactional
    void getAllMediaByAuthTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        // Get all the mediaList where authType is not null
        defaultMediaShouldBeFound("authType.specified=true");

        // Get all the mediaList where authType is null
        defaultMediaShouldNotBeFound("authType.specified=false");
    }

    @Test
    @Transactional
    void getAllMediaByParkIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);
        Park park = ParkResourceIT.createEntity(em);
        em.persist(park);
        em.flush();
        media.setPark(park);
        mediaRepository.saveAndFlush(media);
        Long parkId = park.getId();

        // Get all the mediaList where park equals to parkId
        defaultMediaShouldBeFound("parkId.equals=" + parkId);

        // Get all the mediaList where park equals to (parkId + 1)
        defaultMediaShouldNotBeFound("parkId.equals=" + (parkId + 1));
    }

    @Test
    @Transactional
    void getAllMediaByEquipementIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);
        Equipement equipement = EquipementResourceIT.createEntity(em);
        em.persist(equipement);
        em.flush();
        media.setEquipement(equipement);
        mediaRepository.saveAndFlush(media);
        Long equipementId = equipement.getId();

        // Get all the mediaList where equipement equals to equipementId
        defaultMediaShouldBeFound("equipementId.equals=" + equipementId);

        // Get all the mediaList where equipement equals to (equipementId + 1)
        defaultMediaShouldNotBeFound("equipementId.equals=" + (equipementId + 1));
    }

    @Test
    @Transactional
    void getAllMediaByReportIsEqualToSomething() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);
        Report report = ReportResourceIT.createEntity(em);
        em.persist(report);
        em.flush();
        media.setReport(report);
        mediaRepository.saveAndFlush(media);
        Long reportId = report.getId();

        // Get all the mediaList where report equals to reportId
        defaultMediaShouldBeFound("reportId.equals=" + reportId);

        // Get all the mediaList where report equals to (reportId + 1)
        defaultMediaShouldNotBeFound("reportId.equals=" + (reportId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMediaShouldBeFound(String filter) throws Exception {
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(media.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].fileType").value(hasItem(DEFAULT_FILE_TYPE)))
            .andExpect(jsonPath("$.[*].authType").value(hasItem(DEFAULT_AUTH_TYPE.toString())));

        // Check, that the count call also returns 1
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMediaShouldNotBeFound(String filter) throws Exception {
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMediaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMedia() throws Exception {
        // Get the media
        restMediaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();

        // Update the media
        Media updatedMedia = mediaRepository.findById(media.getId()).get();
        // Disconnect from session so that the updates on updatedMedia are not directly saved in db
        em.detach(updatedMedia);
        updatedMedia.fileName(UPDATED_FILE_NAME).filePath(UPDATED_FILE_PATH).fileType(UPDATED_FILE_TYPE).authType(UPDATED_AUTH_TYPE);
        MediaDTO mediaDTO = mediaMapper.toDto(updatedMedia);

        restMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mediaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
        Media testMedia = mediaList.get(mediaList.size() - 1);
        assertThat(testMedia.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testMedia.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testMedia.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testMedia.getAuthType()).isEqualTo(UPDATED_AUTH_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();
        media.setId(count.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mediaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();
        media.setId(count.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();
        media.setId(count.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMediaWithPatch() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();

        // Update the media using partial update
        Media partialUpdatedMedia = new Media();
        partialUpdatedMedia.setId(media.getId());

        partialUpdatedMedia.fileName(UPDATED_FILE_NAME).fileType(UPDATED_FILE_TYPE);

        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedia))
            )
            .andExpect(status().isOk());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
        Media testMedia = mediaList.get(mediaList.size() - 1);
        assertThat(testMedia.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testMedia.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testMedia.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testMedia.getAuthType()).isEqualTo(DEFAULT_AUTH_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateMediaWithPatch() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();

        // Update the media using partial update
        Media partialUpdatedMedia = new Media();
        partialUpdatedMedia.setId(media.getId());

        partialUpdatedMedia.fileName(UPDATED_FILE_NAME).filePath(UPDATED_FILE_PATH).fileType(UPDATED_FILE_TYPE).authType(UPDATED_AUTH_TYPE);

        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMedia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMedia))
            )
            .andExpect(status().isOk());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
        Media testMedia = mediaList.get(mediaList.size() - 1);
        assertThat(testMedia.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testMedia.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testMedia.getFileType()).isEqualTo(UPDATED_FILE_TYPE);
        assertThat(testMedia.getAuthType()).isEqualTo(UPDATED_AUTH_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();
        media.setId(count.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mediaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();
        media.setId(count.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mediaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMedia() throws Exception {
        int databaseSizeBeforeUpdate = mediaRepository.findAll().size();
        media.setId(count.incrementAndGet());

        // Create the Media
        MediaDTO mediaDTO = mediaMapper.toDto(media);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMediaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mediaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Media in the database
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMedia() throws Exception {
        // Initialize the database
        mediaRepository.saveAndFlush(media);

        int databaseSizeBeforeDelete = mediaRepository.findAll().size();

        // Delete the media
        restMediaMockMvc
            .perform(delete(ENTITY_API_URL_ID, media.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Media> mediaList = mediaRepository.findAll();
        assertThat(mediaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
