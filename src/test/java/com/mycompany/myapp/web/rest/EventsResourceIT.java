package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Events;
import com.mycompany.myapp.repository.EventsRepository;
import com.mycompany.myapp.service.criteria.EventsCriteria;
import com.mycompany.myapp.service.dto.EventsDTO;
import com.mycompany.myapp.service.mapper.EventsMapper;
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
 * Integration tests for the {@link EventsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventsResourceIT {

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_EVENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;
    private static final Integer SMALLER_USER_ID = 1 - 1;

    private static final String ENTITY_API_URL = "/api/events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private EventsMapper eventsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventsMockMvc;

    private Events events;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Events createEntity(EntityManager em) {
        Events events = new Events().eventName(DEFAULT_EVENT_NAME).eventDate(DEFAULT_EVENT_DATE).user_id(DEFAULT_USER_ID);
        return events;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Events createUpdatedEntity(EntityManager em) {
        Events events = new Events().eventName(UPDATED_EVENT_NAME).eventDate(UPDATED_EVENT_DATE).user_id(UPDATED_USER_ID);
        return events;
    }

    @BeforeEach
    public void initTest() {
        events = createEntity(em);
    }

    @Test
    @Transactional
    void createEvents() throws Exception {
        int databaseSizeBeforeCreate = eventsRepository.findAll().size();
        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);
        restEventsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsDTO)))
            .andExpect(status().isCreated());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeCreate + 1);
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testEvents.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(testEvents.getUser_id()).isEqualTo(DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void createEventsWithExistingId() throws Exception {
        // Create the Events with an existing ID
        events.setId(1L);
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        int databaseSizeBeforeCreate = eventsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList
        restEventsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(events.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].user_id").value(hasItem(DEFAULT_USER_ID)));
    }

    @Test
    @Transactional
    void getEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get the events
        restEventsMockMvc
            .perform(get(ENTITY_API_URL_ID, events.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(events.getId().intValue()))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()))
            .andExpect(jsonPath("$.user_id").value(DEFAULT_USER_ID));
    }

    @Test
    @Transactional
    void getEventsByIdFiltering() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        Long id = events.getId();

        defaultEventsShouldBeFound("id.equals=" + id);
        defaultEventsShouldNotBeFound("id.notEquals=" + id);

        defaultEventsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEventsShouldNotBeFound("id.greaterThan=" + id);

        defaultEventsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEventsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEventsByEventNameIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where eventName equals to DEFAULT_EVENT_NAME
        defaultEventsShouldBeFound("eventName.equals=" + DEFAULT_EVENT_NAME);

        // Get all the eventsList where eventName equals to UPDATED_EVENT_NAME
        defaultEventsShouldNotBeFound("eventName.equals=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByEventNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where eventName not equals to DEFAULT_EVENT_NAME
        defaultEventsShouldNotBeFound("eventName.notEquals=" + DEFAULT_EVENT_NAME);

        // Get all the eventsList where eventName not equals to UPDATED_EVENT_NAME
        defaultEventsShouldBeFound("eventName.notEquals=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByEventNameIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where eventName in DEFAULT_EVENT_NAME or UPDATED_EVENT_NAME
        defaultEventsShouldBeFound("eventName.in=" + DEFAULT_EVENT_NAME + "," + UPDATED_EVENT_NAME);

        // Get all the eventsList where eventName equals to UPDATED_EVENT_NAME
        defaultEventsShouldNotBeFound("eventName.in=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByEventNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where eventName is not null
        defaultEventsShouldBeFound("eventName.specified=true");

        // Get all the eventsList where eventName is null
        defaultEventsShouldNotBeFound("eventName.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByEventNameContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where eventName contains DEFAULT_EVENT_NAME
        defaultEventsShouldBeFound("eventName.contains=" + DEFAULT_EVENT_NAME);

        // Get all the eventsList where eventName contains UPDATED_EVENT_NAME
        defaultEventsShouldNotBeFound("eventName.contains=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByEventNameNotContainsSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where eventName does not contain DEFAULT_EVENT_NAME
        defaultEventsShouldNotBeFound("eventName.doesNotContain=" + DEFAULT_EVENT_NAME);

        // Get all the eventsList where eventName does not contain UPDATED_EVENT_NAME
        defaultEventsShouldBeFound("eventName.doesNotContain=" + UPDATED_EVENT_NAME);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where eventDate equals to DEFAULT_EVENT_DATE
        defaultEventsShouldBeFound("eventDate.equals=" + DEFAULT_EVENT_DATE);

        // Get all the eventsList where eventDate equals to UPDATED_EVENT_DATE
        defaultEventsShouldNotBeFound("eventDate.equals=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where eventDate not equals to DEFAULT_EVENT_DATE
        defaultEventsShouldNotBeFound("eventDate.notEquals=" + DEFAULT_EVENT_DATE);

        // Get all the eventsList where eventDate not equals to UPDATED_EVENT_DATE
        defaultEventsShouldBeFound("eventDate.notEquals=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where eventDate in DEFAULT_EVENT_DATE or UPDATED_EVENT_DATE
        defaultEventsShouldBeFound("eventDate.in=" + DEFAULT_EVENT_DATE + "," + UPDATED_EVENT_DATE);

        // Get all the eventsList where eventDate equals to UPDATED_EVENT_DATE
        defaultEventsShouldNotBeFound("eventDate.in=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllEventsByEventDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where eventDate is not null
        defaultEventsShouldBeFound("eventDate.specified=true");

        // Get all the eventsList where eventDate is null
        defaultEventsShouldNotBeFound("eventDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByUser_idIsEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where user_id equals to DEFAULT_USER_ID
        defaultEventsShouldBeFound("user_id.equals=" + DEFAULT_USER_ID);

        // Get all the eventsList where user_id equals to UPDATED_USER_ID
        defaultEventsShouldNotBeFound("user_id.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllEventsByUser_idIsNotEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where user_id not equals to DEFAULT_USER_ID
        defaultEventsShouldNotBeFound("user_id.notEquals=" + DEFAULT_USER_ID);

        // Get all the eventsList where user_id not equals to UPDATED_USER_ID
        defaultEventsShouldBeFound("user_id.notEquals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllEventsByUser_idIsInShouldWork() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where user_id in DEFAULT_USER_ID or UPDATED_USER_ID
        defaultEventsShouldBeFound("user_id.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID);

        // Get all the eventsList where user_id equals to UPDATED_USER_ID
        defaultEventsShouldNotBeFound("user_id.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllEventsByUser_idIsNullOrNotNull() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where user_id is not null
        defaultEventsShouldBeFound("user_id.specified=true");

        // Get all the eventsList where user_id is null
        defaultEventsShouldNotBeFound("user_id.specified=false");
    }

    @Test
    @Transactional
    void getAllEventsByUser_idIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where user_id is greater than or equal to DEFAULT_USER_ID
        defaultEventsShouldBeFound("user_id.greaterThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the eventsList where user_id is greater than or equal to UPDATED_USER_ID
        defaultEventsShouldNotBeFound("user_id.greaterThanOrEqual=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllEventsByUser_idIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where user_id is less than or equal to DEFAULT_USER_ID
        defaultEventsShouldBeFound("user_id.lessThanOrEqual=" + DEFAULT_USER_ID);

        // Get all the eventsList where user_id is less than or equal to SMALLER_USER_ID
        defaultEventsShouldNotBeFound("user_id.lessThanOrEqual=" + SMALLER_USER_ID);
    }

    @Test
    @Transactional
    void getAllEventsByUser_idIsLessThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where user_id is less than DEFAULT_USER_ID
        defaultEventsShouldNotBeFound("user_id.lessThan=" + DEFAULT_USER_ID);

        // Get all the eventsList where user_id is less than UPDATED_USER_ID
        defaultEventsShouldBeFound("user_id.lessThan=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllEventsByUser_idIsGreaterThanSomething() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        // Get all the eventsList where user_id is greater than DEFAULT_USER_ID
        defaultEventsShouldNotBeFound("user_id.greaterThan=" + DEFAULT_USER_ID);

        // Get all the eventsList where user_id is greater than SMALLER_USER_ID
        defaultEventsShouldBeFound("user_id.greaterThan=" + SMALLER_USER_ID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEventsShouldBeFound(String filter) throws Exception {
        restEventsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(events.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].user_id").value(hasItem(DEFAULT_USER_ID)));

        // Check, that the count call also returns 1
        restEventsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEventsShouldNotBeFound(String filter) throws Exception {
        restEventsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEventsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEvents() throws Exception {
        // Get the events
        restEventsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();

        // Update the events
        Events updatedEvents = eventsRepository.findById(events.getId()).get();
        // Disconnect from session so that the updates on updatedEvents are not directly saved in db
        em.detach(updatedEvents);
        updatedEvents.eventName(UPDATED_EVENT_NAME).eventDate(UPDATED_EVENT_DATE).user_id(UPDATED_USER_ID);
        EventsDTO eventsDTO = eventsMapper.toDto(updatedEvents);

        restEventsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testEvents.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testEvents.getUser_id()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        events.setId(count.incrementAndGet());

        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        events.setId(count.incrementAndGet());

        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        events.setId(count.incrementAndGet());

        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventsWithPatch() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();

        // Update the events using partial update
        Events partialUpdatedEvents = new Events();
        partialUpdatedEvents.setId(events.getId());

        partialUpdatedEvents.eventName(UPDATED_EVENT_NAME).user_id(UPDATED_USER_ID);

        restEventsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvents.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvents))
            )
            .andExpect(status().isOk());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testEvents.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(testEvents.getUser_id()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateEventsWithPatch() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();

        // Update the events using partial update
        Events partialUpdatedEvents = new Events();
        partialUpdatedEvents.setId(events.getId());

        partialUpdatedEvents.eventName(UPDATED_EVENT_NAME).eventDate(UPDATED_EVENT_DATE).user_id(UPDATED_USER_ID);

        restEventsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvents.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvents))
            )
            .andExpect(status().isOk());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
        Events testEvents = eventsList.get(eventsList.size() - 1);
        assertThat(testEvents.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testEvents.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testEvents.getUser_id()).isEqualTo(UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        events.setId(count.incrementAndGet());

        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        events.setId(count.incrementAndGet());

        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvents() throws Exception {
        int databaseSizeBeforeUpdate = eventsRepository.findAll().size();
        events.setId(count.incrementAndGet());

        // Create the Events
        EventsDTO eventsDTO = eventsMapper.toDto(events);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Events in the database
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvents() throws Exception {
        // Initialize the database
        eventsRepository.saveAndFlush(events);

        int databaseSizeBeforeDelete = eventsRepository.findAll().size();

        // Delete the events
        restEventsMockMvc
            .perform(delete(ENTITY_API_URL_ID, events.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Events> eventsList = eventsRepository.findAll();
        assertThat(eventsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
