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
import com.mycompany.myapp.repository.EquipementRepository;
import com.mycompany.myapp.service.dto.EquipementDTO;
import com.mycompany.myapp.service.mapper.EquipementMapper;
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
 * Integration tests for the {@link EquipementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EquipementResourceIT {

    private static final String DEFAULT_MODEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_INSTRUCTION = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_VERIFIED = false;
    private static final Boolean UPDATED_VERIFIED = true;

    private static final String ENTITY_API_URL = "/api/equipements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EquipementRepository equipementRepository;

    @Autowired
    private EquipementMapper equipementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipementMockMvc;

    private Equipement equipement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipement createEntity(EntityManager em) {
        Equipement equipement = new Equipement()
            .modelName(DEFAULT_MODEL_NAME)
            .modelNumber(DEFAULT_MODEL_NUMBER)
            .instruction(DEFAULT_INSTRUCTION)
            .verified(DEFAULT_VERIFIED);
        return equipement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Equipement createUpdatedEntity(EntityManager em) {
        Equipement equipement = new Equipement()
            .modelName(UPDATED_MODEL_NAME)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .instruction(UPDATED_INSTRUCTION)
            .verified(UPDATED_VERIFIED);
        return equipement;
    }

    @BeforeEach
    public void initTest() {
        equipement = createEntity(em);
    }

    @Test
    @Transactional
    void createEquipement() throws Exception {
        int databaseSizeBeforeCreate = equipementRepository.findAll().size();
        // Create the Equipement
        EquipementDTO equipementDTO = equipementMapper.toDto(equipement);
        restEquipementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipementDTO)))
            .andExpect(status().isCreated());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeCreate + 1);
        Equipement testEquipement = equipementList.get(equipementList.size() - 1);
        assertThat(testEquipement.getModelName()).isEqualTo(DEFAULT_MODEL_NAME);
        assertThat(testEquipement.getModelNumber()).isEqualTo(DEFAULT_MODEL_NUMBER);
        assertThat(testEquipement.getInstruction()).isEqualTo(DEFAULT_INSTRUCTION);
        assertThat(testEquipement.getVerified()).isEqualTo(DEFAULT_VERIFIED);
    }

    @Test
    @Transactional
    void createEquipementWithExistingId() throws Exception {
        // Create the Equipement with an existing ID
        equipement.setId(1L);
        EquipementDTO equipementDTO = equipementMapper.toDto(equipement);

        int databaseSizeBeforeCreate = equipementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEquipements() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList
        restEquipementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipement.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelName").value(hasItem(DEFAULT_MODEL_NAME)))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)))
            .andExpect(jsonPath("$.[*].instruction").value(hasItem(DEFAULT_INSTRUCTION)))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED.booleanValue())));
    }

    @Test
    @Transactional
    void getEquipement() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get the equipement
        restEquipementMockMvc
            .perform(get(ENTITY_API_URL_ID, equipement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipement.getId().intValue()))
            .andExpect(jsonPath("$.modelName").value(DEFAULT_MODEL_NAME))
            .andExpect(jsonPath("$.modelNumber").value(DEFAULT_MODEL_NUMBER))
            .andExpect(jsonPath("$.instruction").value(DEFAULT_INSTRUCTION))
            .andExpect(jsonPath("$.verified").value(DEFAULT_VERIFIED.booleanValue()));
    }

    @Test
    @Transactional
    void getEquipementsByIdFiltering() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        Long id = equipement.getId();

        defaultEquipementShouldBeFound("id.equals=" + id);
        defaultEquipementShouldNotBeFound("id.notEquals=" + id);

        defaultEquipementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEquipementShouldNotBeFound("id.greaterThan=" + id);

        defaultEquipementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEquipementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEquipementsByModelNameIsEqualToSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where modelName equals to DEFAULT_MODEL_NAME
        defaultEquipementShouldBeFound("modelName.equals=" + DEFAULT_MODEL_NAME);

        // Get all the equipementList where modelName equals to UPDATED_MODEL_NAME
        defaultEquipementShouldNotBeFound("modelName.equals=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllEquipementsByModelNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where modelName not equals to DEFAULT_MODEL_NAME
        defaultEquipementShouldNotBeFound("modelName.notEquals=" + DEFAULT_MODEL_NAME);

        // Get all the equipementList where modelName not equals to UPDATED_MODEL_NAME
        defaultEquipementShouldBeFound("modelName.notEquals=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllEquipementsByModelNameIsInShouldWork() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where modelName in DEFAULT_MODEL_NAME or UPDATED_MODEL_NAME
        defaultEquipementShouldBeFound("modelName.in=" + DEFAULT_MODEL_NAME + "," + UPDATED_MODEL_NAME);

        // Get all the equipementList where modelName equals to UPDATED_MODEL_NAME
        defaultEquipementShouldNotBeFound("modelName.in=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllEquipementsByModelNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where modelName is not null
        defaultEquipementShouldBeFound("modelName.specified=true");

        // Get all the equipementList where modelName is null
        defaultEquipementShouldNotBeFound("modelName.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipementsByModelNameContainsSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where modelName contains DEFAULT_MODEL_NAME
        defaultEquipementShouldBeFound("modelName.contains=" + DEFAULT_MODEL_NAME);

        // Get all the equipementList where modelName contains UPDATED_MODEL_NAME
        defaultEquipementShouldNotBeFound("modelName.contains=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllEquipementsByModelNameNotContainsSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where modelName does not contain DEFAULT_MODEL_NAME
        defaultEquipementShouldNotBeFound("modelName.doesNotContain=" + DEFAULT_MODEL_NAME);

        // Get all the equipementList where modelName does not contain UPDATED_MODEL_NAME
        defaultEquipementShouldBeFound("modelName.doesNotContain=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllEquipementsByModelNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where modelNumber equals to DEFAULT_MODEL_NUMBER
        defaultEquipementShouldBeFound("modelNumber.equals=" + DEFAULT_MODEL_NUMBER);

        // Get all the equipementList where modelNumber equals to UPDATED_MODEL_NUMBER
        defaultEquipementShouldNotBeFound("modelNumber.equals=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllEquipementsByModelNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where modelNumber not equals to DEFAULT_MODEL_NUMBER
        defaultEquipementShouldNotBeFound("modelNumber.notEquals=" + DEFAULT_MODEL_NUMBER);

        // Get all the equipementList where modelNumber not equals to UPDATED_MODEL_NUMBER
        defaultEquipementShouldBeFound("modelNumber.notEquals=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllEquipementsByModelNumberIsInShouldWork() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where modelNumber in DEFAULT_MODEL_NUMBER or UPDATED_MODEL_NUMBER
        defaultEquipementShouldBeFound("modelNumber.in=" + DEFAULT_MODEL_NUMBER + "," + UPDATED_MODEL_NUMBER);

        // Get all the equipementList where modelNumber equals to UPDATED_MODEL_NUMBER
        defaultEquipementShouldNotBeFound("modelNumber.in=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllEquipementsByModelNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where modelNumber is not null
        defaultEquipementShouldBeFound("modelNumber.specified=true");

        // Get all the equipementList where modelNumber is null
        defaultEquipementShouldNotBeFound("modelNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipementsByModelNumberContainsSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where modelNumber contains DEFAULT_MODEL_NUMBER
        defaultEquipementShouldBeFound("modelNumber.contains=" + DEFAULT_MODEL_NUMBER);

        // Get all the equipementList where modelNumber contains UPDATED_MODEL_NUMBER
        defaultEquipementShouldNotBeFound("modelNumber.contains=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllEquipementsByModelNumberNotContainsSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where modelNumber does not contain DEFAULT_MODEL_NUMBER
        defaultEquipementShouldNotBeFound("modelNumber.doesNotContain=" + DEFAULT_MODEL_NUMBER);

        // Get all the equipementList where modelNumber does not contain UPDATED_MODEL_NUMBER
        defaultEquipementShouldBeFound("modelNumber.doesNotContain=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllEquipementsByInstructionIsEqualToSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where instruction equals to DEFAULT_INSTRUCTION
        defaultEquipementShouldBeFound("instruction.equals=" + DEFAULT_INSTRUCTION);

        // Get all the equipementList where instruction equals to UPDATED_INSTRUCTION
        defaultEquipementShouldNotBeFound("instruction.equals=" + UPDATED_INSTRUCTION);
    }

    @Test
    @Transactional
    void getAllEquipementsByInstructionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where instruction not equals to DEFAULT_INSTRUCTION
        defaultEquipementShouldNotBeFound("instruction.notEquals=" + DEFAULT_INSTRUCTION);

        // Get all the equipementList where instruction not equals to UPDATED_INSTRUCTION
        defaultEquipementShouldBeFound("instruction.notEquals=" + UPDATED_INSTRUCTION);
    }

    @Test
    @Transactional
    void getAllEquipementsByInstructionIsInShouldWork() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where instruction in DEFAULT_INSTRUCTION or UPDATED_INSTRUCTION
        defaultEquipementShouldBeFound("instruction.in=" + DEFAULT_INSTRUCTION + "," + UPDATED_INSTRUCTION);

        // Get all the equipementList where instruction equals to UPDATED_INSTRUCTION
        defaultEquipementShouldNotBeFound("instruction.in=" + UPDATED_INSTRUCTION);
    }

    @Test
    @Transactional
    void getAllEquipementsByInstructionIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where instruction is not null
        defaultEquipementShouldBeFound("instruction.specified=true");

        // Get all the equipementList where instruction is null
        defaultEquipementShouldNotBeFound("instruction.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipementsByInstructionContainsSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where instruction contains DEFAULT_INSTRUCTION
        defaultEquipementShouldBeFound("instruction.contains=" + DEFAULT_INSTRUCTION);

        // Get all the equipementList where instruction contains UPDATED_INSTRUCTION
        defaultEquipementShouldNotBeFound("instruction.contains=" + UPDATED_INSTRUCTION);
    }

    @Test
    @Transactional
    void getAllEquipementsByInstructionNotContainsSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where instruction does not contain DEFAULT_INSTRUCTION
        defaultEquipementShouldNotBeFound("instruction.doesNotContain=" + DEFAULT_INSTRUCTION);

        // Get all the equipementList where instruction does not contain UPDATED_INSTRUCTION
        defaultEquipementShouldBeFound("instruction.doesNotContain=" + UPDATED_INSTRUCTION);
    }

    @Test
    @Transactional
    void getAllEquipementsByVerifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where verified equals to DEFAULT_VERIFIED
        defaultEquipementShouldBeFound("verified.equals=" + DEFAULT_VERIFIED);

        // Get all the equipementList where verified equals to UPDATED_VERIFIED
        defaultEquipementShouldNotBeFound("verified.equals=" + UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void getAllEquipementsByVerifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where verified not equals to DEFAULT_VERIFIED
        defaultEquipementShouldNotBeFound("verified.notEquals=" + DEFAULT_VERIFIED);

        // Get all the equipementList where verified not equals to UPDATED_VERIFIED
        defaultEquipementShouldBeFound("verified.notEquals=" + UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void getAllEquipementsByVerifiedIsInShouldWork() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where verified in DEFAULT_VERIFIED or UPDATED_VERIFIED
        defaultEquipementShouldBeFound("verified.in=" + DEFAULT_VERIFIED + "," + UPDATED_VERIFIED);

        // Get all the equipementList where verified equals to UPDATED_VERIFIED
        defaultEquipementShouldNotBeFound("verified.in=" + UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void getAllEquipementsByVerifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        // Get all the equipementList where verified is not null
        defaultEquipementShouldBeFound("verified.specified=true");

        // Get all the equipementList where verified is null
        defaultEquipementShouldNotBeFound("verified.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipementsByReportIsEqualToSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);
        Report report = ReportResourceIT.createEntity(em);
        em.persist(report);
        em.flush();
        equipement.addReport(report);
        equipementRepository.saveAndFlush(equipement);
        Long reportId = report.getId();

        // Get all the equipementList where report equals to reportId
        defaultEquipementShouldBeFound("reportId.equals=" + reportId);

        // Get all the equipementList where report equals to (reportId + 1)
        defaultEquipementShouldNotBeFound("reportId.equals=" + (reportId + 1));
    }

    @Test
    @Transactional
    void getAllEquipementsByMediaIsEqualToSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);
        Media media = MediaResourceIT.createEntity(em);
        em.persist(media);
        em.flush();
        equipement.addMedia(media);
        equipementRepository.saveAndFlush(equipement);
        Long mediaId = media.getId();

        // Get all the equipementList where media equals to mediaId
        defaultEquipementShouldBeFound("mediaId.equals=" + mediaId);

        // Get all the equipementList where media equals to (mediaId + 1)
        defaultEquipementShouldNotBeFound("mediaId.equals=" + (mediaId + 1));
    }

    @Test
    @Transactional
    void getAllEquipementsByParkIsEqualToSomething() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);
        Park park = ParkResourceIT.createEntity(em);
        em.persist(park);
        em.flush();
        equipement.setPark(park);
        equipementRepository.saveAndFlush(equipement);
        Long parkId = park.getId();

        // Get all the equipementList where park equals to parkId
        defaultEquipementShouldBeFound("parkId.equals=" + parkId);

        // Get all the equipementList where park equals to (parkId + 1)
        defaultEquipementShouldNotBeFound("parkId.equals=" + (parkId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEquipementShouldBeFound(String filter) throws Exception {
        restEquipementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipement.getId().intValue())))
            .andExpect(jsonPath("$.[*].modelName").value(hasItem(DEFAULT_MODEL_NAME)))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)))
            .andExpect(jsonPath("$.[*].instruction").value(hasItem(DEFAULT_INSTRUCTION)))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED.booleanValue())));

        // Check, that the count call also returns 1
        restEquipementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEquipementShouldNotBeFound(String filter) throws Exception {
        restEquipementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEquipementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEquipement() throws Exception {
        // Get the equipement
        restEquipementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEquipement() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();

        // Update the equipement
        Equipement updatedEquipement = equipementRepository.findById(equipement.getId()).get();
        // Disconnect from session so that the updates on updatedEquipement are not directly saved in db
        em.detach(updatedEquipement);
        updatedEquipement
            .modelName(UPDATED_MODEL_NAME)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .instruction(UPDATED_INSTRUCTION)
            .verified(UPDATED_VERIFIED);
        EquipementDTO equipementDTO = equipementMapper.toDto(updatedEquipement);

        restEquipementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
        Equipement testEquipement = equipementList.get(equipementList.size() - 1);
        assertThat(testEquipement.getModelName()).isEqualTo(UPDATED_MODEL_NAME);
        assertThat(testEquipement.getModelNumber()).isEqualTo(UPDATED_MODEL_NUMBER);
        assertThat(testEquipement.getInstruction()).isEqualTo(UPDATED_INSTRUCTION);
        assertThat(testEquipement.getVerified()).isEqualTo(UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void putNonExistingEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();
        equipement.setId(count.incrementAndGet());

        // Create the Equipement
        EquipementDTO equipementDTO = equipementMapper.toDto(equipement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();
        equipement.setId(count.incrementAndGet());

        // Create the Equipement
        EquipementDTO equipementDTO = equipementMapper.toDto(equipement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(equipementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();
        equipement.setId(count.incrementAndGet());

        // Create the Equipement
        EquipementDTO equipementDTO = equipementMapper.toDto(equipement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(equipementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEquipementWithPatch() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();

        // Update the equipement using partial update
        Equipement partialUpdatedEquipement = new Equipement();
        partialUpdatedEquipement.setId(equipement.getId());

        partialUpdatedEquipement.verified(UPDATED_VERIFIED);

        restEquipementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipement))
            )
            .andExpect(status().isOk());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
        Equipement testEquipement = equipementList.get(equipementList.size() - 1);
        assertThat(testEquipement.getModelName()).isEqualTo(DEFAULT_MODEL_NAME);
        assertThat(testEquipement.getModelNumber()).isEqualTo(DEFAULT_MODEL_NUMBER);
        assertThat(testEquipement.getInstruction()).isEqualTo(DEFAULT_INSTRUCTION);
        assertThat(testEquipement.getVerified()).isEqualTo(UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void fullUpdateEquipementWithPatch() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();

        // Update the equipement using partial update
        Equipement partialUpdatedEquipement = new Equipement();
        partialUpdatedEquipement.setId(equipement.getId());

        partialUpdatedEquipement
            .modelName(UPDATED_MODEL_NAME)
            .modelNumber(UPDATED_MODEL_NUMBER)
            .instruction(UPDATED_INSTRUCTION)
            .verified(UPDATED_VERIFIED);

        restEquipementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEquipement))
            )
            .andExpect(status().isOk());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
        Equipement testEquipement = equipementList.get(equipementList.size() - 1);
        assertThat(testEquipement.getModelName()).isEqualTo(UPDATED_MODEL_NAME);
        assertThat(testEquipement.getModelNumber()).isEqualTo(UPDATED_MODEL_NUMBER);
        assertThat(testEquipement.getInstruction()).isEqualTo(UPDATED_INSTRUCTION);
        assertThat(testEquipement.getVerified()).isEqualTo(UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();
        equipement.setId(count.incrementAndGet());

        // Create the Equipement
        EquipementDTO equipementDTO = equipementMapper.toDto(equipement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, equipementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();
        equipement.setId(count.incrementAndGet());

        // Create the Equipement
        EquipementDTO equipementDTO = equipementMapper.toDto(equipement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(equipementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEquipement() throws Exception {
        int databaseSizeBeforeUpdate = equipementRepository.findAll().size();
        equipement.setId(count.incrementAndGet());

        // Create the Equipement
        EquipementDTO equipementDTO = equipementMapper.toDto(equipement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(equipementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Equipement in the database
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEquipement() throws Exception {
        // Initialize the database
        equipementRepository.saveAndFlush(equipement);

        int databaseSizeBeforeDelete = equipementRepository.findAll().size();

        // Delete the equipement
        restEquipementMockMvc
            .perform(delete(ENTITY_API_URL_ID, equipement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Equipement> equipementList = equipementRepository.findAll();
        assertThat(equipementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
