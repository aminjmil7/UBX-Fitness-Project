package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Equipement;
import com.mycompany.myapp.domain.Media;
import com.mycompany.myapp.domain.Park;
import com.mycompany.myapp.domain.Report;
import com.mycompany.myapp.repository.ParkRepository;
import com.mycompany.myapp.service.criteria.ParkCriteria;
import com.mycompany.myapp.service.dto.ParkDTO;
import com.mycompany.myapp.service.mapper.ParkMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ParkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ParkResourceIT {

    private static final String DEFAULT_PARK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PARK_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PARK_ADDRESS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LONGTITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGTITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LONGTITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LATITUDE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_VERIFIED = false;
    private static final Boolean UPDATED_VERIFIED = true;

    private static final Instant DEFAULT_DATE_INSTALL = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INSTALL = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_OPEN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OPEN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_CLOSE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CLOSE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_RESELLER = "AAAAAAAAAA";
    private static final String UPDATED_RESELLER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/parks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ParkRepository parkRepository;

    @Autowired
    private ParkMapper parkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restParkMockMvc;

    private Park park;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Park createEntity(EntityManager em) {
        Park park = new Park()
            .parkName(DEFAULT_PARK_NAME)
            .parkAddress(DEFAULT_PARK_ADDRESS)
            .longtitude(DEFAULT_LONGTITUDE)
            .latitude(DEFAULT_LATITUDE)
            .verified(DEFAULT_VERIFIED)
            .dateInstall(DEFAULT_DATE_INSTALL)
            .dateOpen(DEFAULT_DATE_OPEN)
            .dateClose(DEFAULT_DATE_CLOSE)
            .note(DEFAULT_NOTE)
            .reseller(DEFAULT_RESELLER);
        return park;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Park createUpdatedEntity(EntityManager em) {
        Park park = new Park()
            .parkName(UPDATED_PARK_NAME)
            .parkAddress(UPDATED_PARK_ADDRESS)
            .longtitude(UPDATED_LONGTITUDE)
            .latitude(UPDATED_LATITUDE)
            .verified(UPDATED_VERIFIED)
            .dateInstall(UPDATED_DATE_INSTALL)
            .dateOpen(UPDATED_DATE_OPEN)
            .dateClose(UPDATED_DATE_CLOSE)
            .note(UPDATED_NOTE)
            .reseller(UPDATED_RESELLER);
        return park;
    }

    @BeforeEach
    public void initTest() {
        park = createEntity(em);
    }

    @Test
    @Transactional
    void createPark() throws Exception {
        int databaseSizeBeforeCreate = parkRepository.findAll().size();
        // Create the Park
        ParkDTO parkDTO = parkMapper.toDto(park);
        restParkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkDTO)))
            .andExpect(status().isCreated());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeCreate + 1);
        Park testPark = parkList.get(parkList.size() - 1);
        assertThat(testPark.getParkName()).isEqualTo(DEFAULT_PARK_NAME);
        assertThat(testPark.getParkAddress()).isEqualTo(DEFAULT_PARK_ADDRESS);
        assertThat(testPark.getLongtitude()).isEqualByComparingTo(DEFAULT_LONGTITUDE);
        assertThat(testPark.getLatitude()).isEqualByComparingTo(DEFAULT_LATITUDE);
        assertThat(testPark.getVerified()).isEqualTo(DEFAULT_VERIFIED);
        assertThat(testPark.getDateInstall()).isEqualTo(DEFAULT_DATE_INSTALL);
        assertThat(testPark.getDateOpen()).isEqualTo(DEFAULT_DATE_OPEN);
        assertThat(testPark.getDateClose()).isEqualTo(DEFAULT_DATE_CLOSE);
        assertThat(testPark.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testPark.getReseller()).isEqualTo(DEFAULT_RESELLER);
    }

    @Test
    @Transactional
    void createParkWithExistingId() throws Exception {
        // Create the Park with an existing ID
        park.setId(1L);
        ParkDTO parkDTO = parkMapper.toDto(park);

        int databaseSizeBeforeCreate = parkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restParkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllParks() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList
        restParkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(park.getId().intValue())))
            .andExpect(jsonPath("$.[*].parkName").value(hasItem(DEFAULT_PARK_NAME)))
            .andExpect(jsonPath("$.[*].parkAddress").value(hasItem(DEFAULT_PARK_ADDRESS)))
            .andExpect(jsonPath("$.[*].longtitude").value(hasItem(sameNumber(DEFAULT_LONGTITUDE))))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateInstall").value(hasItem(DEFAULT_DATE_INSTALL.toString())))
            .andExpect(jsonPath("$.[*].dateOpen").value(hasItem(DEFAULT_DATE_OPEN.toString())))
            .andExpect(jsonPath("$.[*].dateClose").value(hasItem(DEFAULT_DATE_CLOSE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].reseller").value(hasItem(DEFAULT_RESELLER)));
    }

    @Test
    @Transactional
    void getPark() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get the park
        restParkMockMvc
            .perform(get(ENTITY_API_URL_ID, park.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(park.getId().intValue()))
            .andExpect(jsonPath("$.parkName").value(DEFAULT_PARK_NAME))
            .andExpect(jsonPath("$.parkAddress").value(DEFAULT_PARK_ADDRESS))
            .andExpect(jsonPath("$.longtitude").value(sameNumber(DEFAULT_LONGTITUDE)))
            .andExpect(jsonPath("$.latitude").value(sameNumber(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.verified").value(DEFAULT_VERIFIED.booleanValue()))
            .andExpect(jsonPath("$.dateInstall").value(DEFAULT_DATE_INSTALL.toString()))
            .andExpect(jsonPath("$.dateOpen").value(DEFAULT_DATE_OPEN.toString()))
            .andExpect(jsonPath("$.dateClose").value(DEFAULT_DATE_CLOSE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.reseller").value(DEFAULT_RESELLER));
    }

    @Test
    @Transactional
    void getParksByIdFiltering() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        Long id = park.getId();

        defaultParkShouldBeFound("id.equals=" + id);
        defaultParkShouldNotBeFound("id.notEquals=" + id);

        defaultParkShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultParkShouldNotBeFound("id.greaterThan=" + id);

        defaultParkShouldBeFound("id.lessThanOrEqual=" + id);
        defaultParkShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllParksByParkNameIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where parkName equals to DEFAULT_PARK_NAME
        defaultParkShouldBeFound("parkName.equals=" + DEFAULT_PARK_NAME);

        // Get all the parkList where parkName equals to UPDATED_PARK_NAME
        defaultParkShouldNotBeFound("parkName.equals=" + UPDATED_PARK_NAME);
    }

    @Test
    @Transactional
    void getAllParksByParkNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where parkName not equals to DEFAULT_PARK_NAME
        defaultParkShouldNotBeFound("parkName.notEquals=" + DEFAULT_PARK_NAME);

        // Get all the parkList where parkName not equals to UPDATED_PARK_NAME
        defaultParkShouldBeFound("parkName.notEquals=" + UPDATED_PARK_NAME);
    }

    @Test
    @Transactional
    void getAllParksByParkNameIsInShouldWork() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where parkName in DEFAULT_PARK_NAME or UPDATED_PARK_NAME
        defaultParkShouldBeFound("parkName.in=" + DEFAULT_PARK_NAME + "," + UPDATED_PARK_NAME);

        // Get all the parkList where parkName equals to UPDATED_PARK_NAME
        defaultParkShouldNotBeFound("parkName.in=" + UPDATED_PARK_NAME);
    }

    @Test
    @Transactional
    void getAllParksByParkNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where parkName is not null
        defaultParkShouldBeFound("parkName.specified=true");

        // Get all the parkList where parkName is null
        defaultParkShouldNotBeFound("parkName.specified=false");
    }

    @Test
    @Transactional
    void getAllParksByParkNameContainsSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where parkName contains DEFAULT_PARK_NAME
        defaultParkShouldBeFound("parkName.contains=" + DEFAULT_PARK_NAME);

        // Get all the parkList where parkName contains UPDATED_PARK_NAME
        defaultParkShouldNotBeFound("parkName.contains=" + UPDATED_PARK_NAME);
    }

    @Test
    @Transactional
    void getAllParksByParkNameNotContainsSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where parkName does not contain DEFAULT_PARK_NAME
        defaultParkShouldNotBeFound("parkName.doesNotContain=" + DEFAULT_PARK_NAME);

        // Get all the parkList where parkName does not contain UPDATED_PARK_NAME
        defaultParkShouldBeFound("parkName.doesNotContain=" + UPDATED_PARK_NAME);
    }

    @Test
    @Transactional
    void getAllParksByParkAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where parkAddress equals to DEFAULT_PARK_ADDRESS
        defaultParkShouldBeFound("parkAddress.equals=" + DEFAULT_PARK_ADDRESS);

        // Get all the parkList where parkAddress equals to UPDATED_PARK_ADDRESS
        defaultParkShouldNotBeFound("parkAddress.equals=" + UPDATED_PARK_ADDRESS);
    }

    @Test
    @Transactional
    void getAllParksByParkAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where parkAddress not equals to DEFAULT_PARK_ADDRESS
        defaultParkShouldNotBeFound("parkAddress.notEquals=" + DEFAULT_PARK_ADDRESS);

        // Get all the parkList where parkAddress not equals to UPDATED_PARK_ADDRESS
        defaultParkShouldBeFound("parkAddress.notEquals=" + UPDATED_PARK_ADDRESS);
    }

    @Test
    @Transactional
    void getAllParksByParkAddressIsInShouldWork() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where parkAddress in DEFAULT_PARK_ADDRESS or UPDATED_PARK_ADDRESS
        defaultParkShouldBeFound("parkAddress.in=" + DEFAULT_PARK_ADDRESS + "," + UPDATED_PARK_ADDRESS);

        // Get all the parkList where parkAddress equals to UPDATED_PARK_ADDRESS
        defaultParkShouldNotBeFound("parkAddress.in=" + UPDATED_PARK_ADDRESS);
    }

    @Test
    @Transactional
    void getAllParksByParkAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where parkAddress is not null
        defaultParkShouldBeFound("parkAddress.specified=true");

        // Get all the parkList where parkAddress is null
        defaultParkShouldNotBeFound("parkAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllParksByParkAddressContainsSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where parkAddress contains DEFAULT_PARK_ADDRESS
        defaultParkShouldBeFound("parkAddress.contains=" + DEFAULT_PARK_ADDRESS);

        // Get all the parkList where parkAddress contains UPDATED_PARK_ADDRESS
        defaultParkShouldNotBeFound("parkAddress.contains=" + UPDATED_PARK_ADDRESS);
    }

    @Test
    @Transactional
    void getAllParksByParkAddressNotContainsSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where parkAddress does not contain DEFAULT_PARK_ADDRESS
        defaultParkShouldNotBeFound("parkAddress.doesNotContain=" + DEFAULT_PARK_ADDRESS);

        // Get all the parkList where parkAddress does not contain UPDATED_PARK_ADDRESS
        defaultParkShouldBeFound("parkAddress.doesNotContain=" + UPDATED_PARK_ADDRESS);
    }

    @Test
    @Transactional
    void getAllParksByLongtitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where longtitude equals to DEFAULT_LONGTITUDE
        defaultParkShouldBeFound("longtitude.equals=" + DEFAULT_LONGTITUDE);

        // Get all the parkList where longtitude equals to UPDATED_LONGTITUDE
        defaultParkShouldNotBeFound("longtitude.equals=" + UPDATED_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLongtitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where longtitude not equals to DEFAULT_LONGTITUDE
        defaultParkShouldNotBeFound("longtitude.notEquals=" + DEFAULT_LONGTITUDE);

        // Get all the parkList where longtitude not equals to UPDATED_LONGTITUDE
        defaultParkShouldBeFound("longtitude.notEquals=" + UPDATED_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLongtitudeIsInShouldWork() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where longtitude in DEFAULT_LONGTITUDE or UPDATED_LONGTITUDE
        defaultParkShouldBeFound("longtitude.in=" + DEFAULT_LONGTITUDE + "," + UPDATED_LONGTITUDE);

        // Get all the parkList where longtitude equals to UPDATED_LONGTITUDE
        defaultParkShouldNotBeFound("longtitude.in=" + UPDATED_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLongtitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where longtitude is not null
        defaultParkShouldBeFound("longtitude.specified=true");

        // Get all the parkList where longtitude is null
        defaultParkShouldNotBeFound("longtitude.specified=false");
    }

    @Test
    @Transactional
    void getAllParksByLongtitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where longtitude is greater than or equal to DEFAULT_LONGTITUDE
        defaultParkShouldBeFound("longtitude.greaterThanOrEqual=" + DEFAULT_LONGTITUDE);

        // Get all the parkList where longtitude is greater than or equal to UPDATED_LONGTITUDE
        defaultParkShouldNotBeFound("longtitude.greaterThanOrEqual=" + UPDATED_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLongtitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where longtitude is less than or equal to DEFAULT_LONGTITUDE
        defaultParkShouldBeFound("longtitude.lessThanOrEqual=" + DEFAULT_LONGTITUDE);

        // Get all the parkList where longtitude is less than or equal to SMALLER_LONGTITUDE
        defaultParkShouldNotBeFound("longtitude.lessThanOrEqual=" + SMALLER_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLongtitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where longtitude is less than DEFAULT_LONGTITUDE
        defaultParkShouldNotBeFound("longtitude.lessThan=" + DEFAULT_LONGTITUDE);

        // Get all the parkList where longtitude is less than UPDATED_LONGTITUDE
        defaultParkShouldBeFound("longtitude.lessThan=" + UPDATED_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLongtitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where longtitude is greater than DEFAULT_LONGTITUDE
        defaultParkShouldNotBeFound("longtitude.greaterThan=" + DEFAULT_LONGTITUDE);

        // Get all the parkList where longtitude is greater than SMALLER_LONGTITUDE
        defaultParkShouldBeFound("longtitude.greaterThan=" + SMALLER_LONGTITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where latitude equals to DEFAULT_LATITUDE
        defaultParkShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the parkList where latitude equals to UPDATED_LATITUDE
        defaultParkShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where latitude not equals to DEFAULT_LATITUDE
        defaultParkShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the parkList where latitude not equals to UPDATED_LATITUDE
        defaultParkShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultParkShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the parkList where latitude equals to UPDATED_LATITUDE
        defaultParkShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where latitude is not null
        defaultParkShouldBeFound("latitude.specified=true");

        // Get all the parkList where latitude is null
        defaultParkShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllParksByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultParkShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the parkList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultParkShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultParkShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the parkList where latitude is less than or equal to SMALLER_LATITUDE
        defaultParkShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where latitude is less than DEFAULT_LATITUDE
        defaultParkShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the parkList where latitude is less than UPDATED_LATITUDE
        defaultParkShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllParksByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where latitude is greater than DEFAULT_LATITUDE
        defaultParkShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the parkList where latitude is greater than SMALLER_LATITUDE
        defaultParkShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllParksByVerifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where verified equals to DEFAULT_VERIFIED
        defaultParkShouldBeFound("verified.equals=" + DEFAULT_VERIFIED);

        // Get all the parkList where verified equals to UPDATED_VERIFIED
        defaultParkShouldNotBeFound("verified.equals=" + UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void getAllParksByVerifiedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where verified not equals to DEFAULT_VERIFIED
        defaultParkShouldNotBeFound("verified.notEquals=" + DEFAULT_VERIFIED);

        // Get all the parkList where verified not equals to UPDATED_VERIFIED
        defaultParkShouldBeFound("verified.notEquals=" + UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void getAllParksByVerifiedIsInShouldWork() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where verified in DEFAULT_VERIFIED or UPDATED_VERIFIED
        defaultParkShouldBeFound("verified.in=" + DEFAULT_VERIFIED + "," + UPDATED_VERIFIED);

        // Get all the parkList where verified equals to UPDATED_VERIFIED
        defaultParkShouldNotBeFound("verified.in=" + UPDATED_VERIFIED);
    }

    @Test
    @Transactional
    void getAllParksByVerifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where verified is not null
        defaultParkShouldBeFound("verified.specified=true");

        // Get all the parkList where verified is null
        defaultParkShouldNotBeFound("verified.specified=false");
    }

    @Test
    @Transactional
    void getAllParksByDateInstallIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where dateInstall equals to DEFAULT_DATE_INSTALL
        defaultParkShouldBeFound("dateInstall.equals=" + DEFAULT_DATE_INSTALL);

        // Get all the parkList where dateInstall equals to UPDATED_DATE_INSTALL
        defaultParkShouldNotBeFound("dateInstall.equals=" + UPDATED_DATE_INSTALL);
    }

    @Test
    @Transactional
    void getAllParksByDateInstallIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where dateInstall not equals to DEFAULT_DATE_INSTALL
        defaultParkShouldNotBeFound("dateInstall.notEquals=" + DEFAULT_DATE_INSTALL);

        // Get all the parkList where dateInstall not equals to UPDATED_DATE_INSTALL
        defaultParkShouldBeFound("dateInstall.notEquals=" + UPDATED_DATE_INSTALL);
    }

    @Test
    @Transactional
    void getAllParksByDateInstallIsInShouldWork() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where dateInstall in DEFAULT_DATE_INSTALL or UPDATED_DATE_INSTALL
        defaultParkShouldBeFound("dateInstall.in=" + DEFAULT_DATE_INSTALL + "," + UPDATED_DATE_INSTALL);

        // Get all the parkList where dateInstall equals to UPDATED_DATE_INSTALL
        defaultParkShouldNotBeFound("dateInstall.in=" + UPDATED_DATE_INSTALL);
    }

    @Test
    @Transactional
    void getAllParksByDateInstallIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where dateInstall is not null
        defaultParkShouldBeFound("dateInstall.specified=true");

        // Get all the parkList where dateInstall is null
        defaultParkShouldNotBeFound("dateInstall.specified=false");
    }

    @Test
    @Transactional
    void getAllParksByDateOpenIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where dateOpen equals to DEFAULT_DATE_OPEN
        defaultParkShouldBeFound("dateOpen.equals=" + DEFAULT_DATE_OPEN);

        // Get all the parkList where dateOpen equals to UPDATED_DATE_OPEN
        defaultParkShouldNotBeFound("dateOpen.equals=" + UPDATED_DATE_OPEN);
    }

    @Test
    @Transactional
    void getAllParksByDateOpenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where dateOpen not equals to DEFAULT_DATE_OPEN
        defaultParkShouldNotBeFound("dateOpen.notEquals=" + DEFAULT_DATE_OPEN);

        // Get all the parkList where dateOpen not equals to UPDATED_DATE_OPEN
        defaultParkShouldBeFound("dateOpen.notEquals=" + UPDATED_DATE_OPEN);
    }

    @Test
    @Transactional
    void getAllParksByDateOpenIsInShouldWork() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where dateOpen in DEFAULT_DATE_OPEN or UPDATED_DATE_OPEN
        defaultParkShouldBeFound("dateOpen.in=" + DEFAULT_DATE_OPEN + "," + UPDATED_DATE_OPEN);

        // Get all the parkList where dateOpen equals to UPDATED_DATE_OPEN
        defaultParkShouldNotBeFound("dateOpen.in=" + UPDATED_DATE_OPEN);
    }

    @Test
    @Transactional
    void getAllParksByDateOpenIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where dateOpen is not null
        defaultParkShouldBeFound("dateOpen.specified=true");

        // Get all the parkList where dateOpen is null
        defaultParkShouldNotBeFound("dateOpen.specified=false");
    }

    @Test
    @Transactional
    void getAllParksByDateCloseIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where dateClose equals to DEFAULT_DATE_CLOSE
        defaultParkShouldBeFound("dateClose.equals=" + DEFAULT_DATE_CLOSE);

        // Get all the parkList where dateClose equals to UPDATED_DATE_CLOSE
        defaultParkShouldNotBeFound("dateClose.equals=" + UPDATED_DATE_CLOSE);
    }

    @Test
    @Transactional
    void getAllParksByDateCloseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where dateClose not equals to DEFAULT_DATE_CLOSE
        defaultParkShouldNotBeFound("dateClose.notEquals=" + DEFAULT_DATE_CLOSE);

        // Get all the parkList where dateClose not equals to UPDATED_DATE_CLOSE
        defaultParkShouldBeFound("dateClose.notEquals=" + UPDATED_DATE_CLOSE);
    }

    @Test
    @Transactional
    void getAllParksByDateCloseIsInShouldWork() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where dateClose in DEFAULT_DATE_CLOSE or UPDATED_DATE_CLOSE
        defaultParkShouldBeFound("dateClose.in=" + DEFAULT_DATE_CLOSE + "," + UPDATED_DATE_CLOSE);

        // Get all the parkList where dateClose equals to UPDATED_DATE_CLOSE
        defaultParkShouldNotBeFound("dateClose.in=" + UPDATED_DATE_CLOSE);
    }

    @Test
    @Transactional
    void getAllParksByDateCloseIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where dateClose is not null
        defaultParkShouldBeFound("dateClose.specified=true");

        // Get all the parkList where dateClose is null
        defaultParkShouldNotBeFound("dateClose.specified=false");
    }

    @Test
    @Transactional
    void getAllParksByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where note equals to DEFAULT_NOTE
        defaultParkShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the parkList where note equals to UPDATED_NOTE
        defaultParkShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllParksByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where note not equals to DEFAULT_NOTE
        defaultParkShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the parkList where note not equals to UPDATED_NOTE
        defaultParkShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllParksByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultParkShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the parkList where note equals to UPDATED_NOTE
        defaultParkShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllParksByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where note is not null
        defaultParkShouldBeFound("note.specified=true");

        // Get all the parkList where note is null
        defaultParkShouldNotBeFound("note.specified=false");
    }

    @Test
    @Transactional
    void getAllParksByNoteContainsSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where note contains DEFAULT_NOTE
        defaultParkShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the parkList where note contains UPDATED_NOTE
        defaultParkShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllParksByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where note does not contain DEFAULT_NOTE
        defaultParkShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the parkList where note does not contain UPDATED_NOTE
        defaultParkShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllParksByResellerIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where reseller equals to DEFAULT_RESELLER
        defaultParkShouldBeFound("reseller.equals=" + DEFAULT_RESELLER);

        // Get all the parkList where reseller equals to UPDATED_RESELLER
        defaultParkShouldNotBeFound("reseller.equals=" + UPDATED_RESELLER);
    }

    @Test
    @Transactional
    void getAllParksByResellerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where reseller not equals to DEFAULT_RESELLER
        defaultParkShouldNotBeFound("reseller.notEquals=" + DEFAULT_RESELLER);

        // Get all the parkList where reseller not equals to UPDATED_RESELLER
        defaultParkShouldBeFound("reseller.notEquals=" + UPDATED_RESELLER);
    }

    @Test
    @Transactional
    void getAllParksByResellerIsInShouldWork() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where reseller in DEFAULT_RESELLER or UPDATED_RESELLER
        defaultParkShouldBeFound("reseller.in=" + DEFAULT_RESELLER + "," + UPDATED_RESELLER);

        // Get all the parkList where reseller equals to UPDATED_RESELLER
        defaultParkShouldNotBeFound("reseller.in=" + UPDATED_RESELLER);
    }

    @Test
    @Transactional
    void getAllParksByResellerIsNullOrNotNull() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where reseller is not null
        defaultParkShouldBeFound("reseller.specified=true");

        // Get all the parkList where reseller is null
        defaultParkShouldNotBeFound("reseller.specified=false");
    }

    @Test
    @Transactional
    void getAllParksByResellerContainsSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where reseller contains DEFAULT_RESELLER
        defaultParkShouldBeFound("reseller.contains=" + DEFAULT_RESELLER);

        // Get all the parkList where reseller contains UPDATED_RESELLER
        defaultParkShouldNotBeFound("reseller.contains=" + UPDATED_RESELLER);
    }

    @Test
    @Transactional
    void getAllParksByResellerNotContainsSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        // Get all the parkList where reseller does not contain DEFAULT_RESELLER
        defaultParkShouldNotBeFound("reseller.doesNotContain=" + DEFAULT_RESELLER);

        // Get all the parkList where reseller does not contain UPDATED_RESELLER
        defaultParkShouldBeFound("reseller.doesNotContain=" + UPDATED_RESELLER);
    }

    @Test
    @Transactional
    void getAllParksByEquipementIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);
        Equipement equipement = EquipementResourceIT.createEntity(em);
        em.persist(equipement);
        em.flush();
        park.addEquipement(equipement);
        parkRepository.saveAndFlush(park);
        Long equipementId = equipement.getId();

        // Get all the parkList where equipement equals to equipementId
        defaultParkShouldBeFound("equipementId.equals=" + equipementId);

        // Get all the parkList where equipement equals to (equipementId + 1)
        defaultParkShouldNotBeFound("equipementId.equals=" + (equipementId + 1));
    }

    @Test
    @Transactional
    void getAllParksByMediaIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);
        Media media = MediaResourceIT.createEntity(em);
        em.persist(media);
        em.flush();
        park.addMedia(media);
        parkRepository.saveAndFlush(park);
        Long mediaId = media.getId();

        // Get all the parkList where media equals to mediaId
        defaultParkShouldBeFound("mediaId.equals=" + mediaId);

        // Get all the parkList where media equals to (mediaId + 1)
        defaultParkShouldNotBeFound("mediaId.equals=" + (mediaId + 1));
    }

    @Test
    @Transactional
    void getAllParksByReportIsEqualToSomething() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);
        Report report = ReportResourceIT.createEntity(em);
        em.persist(report);
        em.flush();
        park.addReport(report);
        parkRepository.saveAndFlush(park);
        Long reportId = report.getId();

        // Get all the parkList where report equals to reportId
        defaultParkShouldBeFound("reportId.equals=" + reportId);

        // Get all the parkList where report equals to (reportId + 1)
        defaultParkShouldNotBeFound("reportId.equals=" + (reportId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultParkShouldBeFound(String filter) throws Exception {
        restParkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(park.getId().intValue())))
            .andExpect(jsonPath("$.[*].parkName").value(hasItem(DEFAULT_PARK_NAME)))
            .andExpect(jsonPath("$.[*].parkAddress").value(hasItem(DEFAULT_PARK_ADDRESS)))
            .andExpect(jsonPath("$.[*].longtitude").value(hasItem(sameNumber(DEFAULT_LONGTITUDE))))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].verified").value(hasItem(DEFAULT_VERIFIED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateInstall").value(hasItem(DEFAULT_DATE_INSTALL.toString())))
            .andExpect(jsonPath("$.[*].dateOpen").value(hasItem(DEFAULT_DATE_OPEN.toString())))
            .andExpect(jsonPath("$.[*].dateClose").value(hasItem(DEFAULT_DATE_CLOSE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].reseller").value(hasItem(DEFAULT_RESELLER)));

        // Check, that the count call also returns 1
        restParkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultParkShouldNotBeFound(String filter) throws Exception {
        restParkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restParkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPark() throws Exception {
        // Get the park
        restParkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPark() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        int databaseSizeBeforeUpdate = parkRepository.findAll().size();

        // Update the park
        Park updatedPark = parkRepository.findById(park.getId()).get();
        // Disconnect from session so that the updates on updatedPark are not directly saved in db
        em.detach(updatedPark);
        updatedPark
            .parkName(UPDATED_PARK_NAME)
            .parkAddress(UPDATED_PARK_ADDRESS)
            .longtitude(UPDATED_LONGTITUDE)
            .latitude(UPDATED_LATITUDE)
            .verified(UPDATED_VERIFIED)
            .dateInstall(UPDATED_DATE_INSTALL)
            .dateOpen(UPDATED_DATE_OPEN)
            .dateClose(UPDATED_DATE_CLOSE)
            .note(UPDATED_NOTE)
            .reseller(UPDATED_RESELLER);
        ParkDTO parkDTO = parkMapper.toDto(updatedPark);

        restParkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parkDTO))
            )
            .andExpect(status().isOk());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
        Park testPark = parkList.get(parkList.size() - 1);
        assertThat(testPark.getParkName()).isEqualTo(UPDATED_PARK_NAME);
        assertThat(testPark.getParkAddress()).isEqualTo(UPDATED_PARK_ADDRESS);
        assertThat(testPark.getLongtitude()).isEqualTo(UPDATED_LONGTITUDE);
        assertThat(testPark.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testPark.getVerified()).isEqualTo(UPDATED_VERIFIED);
        assertThat(testPark.getDateInstall()).isEqualTo(UPDATED_DATE_INSTALL);
        assertThat(testPark.getDateOpen()).isEqualTo(UPDATED_DATE_OPEN);
        assertThat(testPark.getDateClose()).isEqualTo(UPDATED_DATE_CLOSE);
        assertThat(testPark.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testPark.getReseller()).isEqualTo(UPDATED_RESELLER);
    }

    @Test
    @Transactional
    void putNonExistingPark() throws Exception {
        int databaseSizeBeforeUpdate = parkRepository.findAll().size();
        park.setId(count.incrementAndGet());

        // Create the Park
        ParkDTO parkDTO = parkMapper.toDto(park);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, parkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPark() throws Exception {
        int databaseSizeBeforeUpdate = parkRepository.findAll().size();
        park.setId(count.incrementAndGet());

        // Create the Park
        ParkDTO parkDTO = parkMapper.toDto(park);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(parkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPark() throws Exception {
        int databaseSizeBeforeUpdate = parkRepository.findAll().size();
        park.setId(count.incrementAndGet());

        // Create the Park
        ParkDTO parkDTO = parkMapper.toDto(park);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(parkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateParkWithPatch() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        int databaseSizeBeforeUpdate = parkRepository.findAll().size();

        // Update the park using partial update
        Park partialUpdatedPark = new Park();
        partialUpdatedPark.setId(park.getId());

        partialUpdatedPark.parkName(UPDATED_PARK_NAME).parkAddress(UPDATED_PARK_ADDRESS);

        restParkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPark))
            )
            .andExpect(status().isOk());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
        Park testPark = parkList.get(parkList.size() - 1);
        assertThat(testPark.getParkName()).isEqualTo(UPDATED_PARK_NAME);
        assertThat(testPark.getParkAddress()).isEqualTo(UPDATED_PARK_ADDRESS);
        assertThat(testPark.getLongtitude()).isEqualByComparingTo(DEFAULT_LONGTITUDE);
        assertThat(testPark.getLatitude()).isEqualByComparingTo(DEFAULT_LATITUDE);
        assertThat(testPark.getVerified()).isEqualTo(DEFAULT_VERIFIED);
        assertThat(testPark.getDateInstall()).isEqualTo(DEFAULT_DATE_INSTALL);
        assertThat(testPark.getDateOpen()).isEqualTo(DEFAULT_DATE_OPEN);
        assertThat(testPark.getDateClose()).isEqualTo(DEFAULT_DATE_CLOSE);
        assertThat(testPark.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testPark.getReseller()).isEqualTo(DEFAULT_RESELLER);
    }

    @Test
    @Transactional
    void fullUpdateParkWithPatch() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        int databaseSizeBeforeUpdate = parkRepository.findAll().size();

        // Update the park using partial update
        Park partialUpdatedPark = new Park();
        partialUpdatedPark.setId(park.getId());

        partialUpdatedPark
            .parkName(UPDATED_PARK_NAME)
            .parkAddress(UPDATED_PARK_ADDRESS)
            .longtitude(UPDATED_LONGTITUDE)
            .latitude(UPDATED_LATITUDE)
            .verified(UPDATED_VERIFIED)
            .dateInstall(UPDATED_DATE_INSTALL)
            .dateOpen(UPDATED_DATE_OPEN)
            .dateClose(UPDATED_DATE_CLOSE)
            .note(UPDATED_NOTE)
            .reseller(UPDATED_RESELLER);

        restParkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPark.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPark))
            )
            .andExpect(status().isOk());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
        Park testPark = parkList.get(parkList.size() - 1);
        assertThat(testPark.getParkName()).isEqualTo(UPDATED_PARK_NAME);
        assertThat(testPark.getParkAddress()).isEqualTo(UPDATED_PARK_ADDRESS);
        assertThat(testPark.getLongtitude()).isEqualByComparingTo(UPDATED_LONGTITUDE);
        assertThat(testPark.getLatitude()).isEqualByComparingTo(UPDATED_LATITUDE);
        assertThat(testPark.getVerified()).isEqualTo(UPDATED_VERIFIED);
        assertThat(testPark.getDateInstall()).isEqualTo(UPDATED_DATE_INSTALL);
        assertThat(testPark.getDateOpen()).isEqualTo(UPDATED_DATE_OPEN);
        assertThat(testPark.getDateClose()).isEqualTo(UPDATED_DATE_CLOSE);
        assertThat(testPark.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testPark.getReseller()).isEqualTo(UPDATED_RESELLER);
    }

    @Test
    @Transactional
    void patchNonExistingPark() throws Exception {
        int databaseSizeBeforeUpdate = parkRepository.findAll().size();
        park.setId(count.incrementAndGet());

        // Create the Park
        ParkDTO parkDTO = parkMapper.toDto(park);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, parkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPark() throws Exception {
        int databaseSizeBeforeUpdate = parkRepository.findAll().size();
        park.setId(count.incrementAndGet());

        // Create the Park
        ParkDTO parkDTO = parkMapper.toDto(park);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(parkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPark() throws Exception {
        int databaseSizeBeforeUpdate = parkRepository.findAll().size();
        park.setId(count.incrementAndGet());

        // Create the Park
        ParkDTO parkDTO = parkMapper.toDto(park);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restParkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(parkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Park in the database
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePark() throws Exception {
        // Initialize the database
        parkRepository.saveAndFlush(park);

        int databaseSizeBeforeDelete = parkRepository.findAll().size();

        // Delete the park
        restParkMockMvc
            .perform(delete(ENTITY_API_URL_ID, park.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Park> parkList = parkRepository.findAll();
        assertThat(parkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
