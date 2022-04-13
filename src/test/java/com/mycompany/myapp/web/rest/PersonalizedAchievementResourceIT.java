package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PersonalizedAchievement;
import com.mycompany.myapp.repository.PersonalizedAchievementRepository;
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
 * Integration tests for the {@link PersonalizedAchievementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PersonalizedAchievementResourceIT {

    private static final String ENTITY_API_URL = "/api/personalized-achievements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonalizedAchievementRepository personalizedAchievementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalizedAchievementMockMvc;

    private PersonalizedAchievement personalizedAchievement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalizedAchievement createEntity(EntityManager em) {
        PersonalizedAchievement personalizedAchievement = new PersonalizedAchievement();
        return personalizedAchievement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalizedAchievement createUpdatedEntity(EntityManager em) {
        PersonalizedAchievement personalizedAchievement = new PersonalizedAchievement();
        return personalizedAchievement;
    }

    @BeforeEach
    public void initTest() {
        personalizedAchievement = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonalizedAchievement() throws Exception {
        int databaseSizeBeforeCreate = personalizedAchievementRepository.findAll().size();
        // Create the PersonalizedAchievement
        restPersonalizedAchievementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizedAchievement))
            )
            .andExpect(status().isCreated());

        // Validate the PersonalizedAchievement in the database
        List<PersonalizedAchievement> personalizedAchievementList = personalizedAchievementRepository.findAll();
        assertThat(personalizedAchievementList).hasSize(databaseSizeBeforeCreate + 1);
        PersonalizedAchievement testPersonalizedAchievement = personalizedAchievementList.get(personalizedAchievementList.size() - 1);
    }

    @Test
    @Transactional
    void createPersonalizedAchievementWithExistingId() throws Exception {
        // Create the PersonalizedAchievement with an existing ID
        personalizedAchievement.setId(1L);

        int databaseSizeBeforeCreate = personalizedAchievementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalizedAchievementMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizedAchievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalizedAchievement in the database
        List<PersonalizedAchievement> personalizedAchievementList = personalizedAchievementRepository.findAll();
        assertThat(personalizedAchievementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPersonalizedAchievements() throws Exception {
        // Initialize the database
        personalizedAchievementRepository.saveAndFlush(personalizedAchievement);

        // Get all the personalizedAchievementList
        restPersonalizedAchievementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalizedAchievement.getId().intValue())));
    }

    @Test
    @Transactional
    void getPersonalizedAchievement() throws Exception {
        // Initialize the database
        personalizedAchievementRepository.saveAndFlush(personalizedAchievement);

        // Get the personalizedAchievement
        restPersonalizedAchievementMockMvc
            .perform(get(ENTITY_API_URL_ID, personalizedAchievement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalizedAchievement.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPersonalizedAchievement() throws Exception {
        // Get the personalizedAchievement
        restPersonalizedAchievementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPersonalizedAchievement() throws Exception {
        // Initialize the database
        personalizedAchievementRepository.saveAndFlush(personalizedAchievement);

        int databaseSizeBeforeUpdate = personalizedAchievementRepository.findAll().size();

        // Update the personalizedAchievement
        PersonalizedAchievement updatedPersonalizedAchievement = personalizedAchievementRepository
            .findById(personalizedAchievement.getId())
            .get();
        // Disconnect from session so that the updates on updatedPersonalizedAchievement are not directly saved in db
        em.detach(updatedPersonalizedAchievement);

        restPersonalizedAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonalizedAchievement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonalizedAchievement))
            )
            .andExpect(status().isOk());

        // Validate the PersonalizedAchievement in the database
        List<PersonalizedAchievement> personalizedAchievementList = personalizedAchievementRepository.findAll();
        assertThat(personalizedAchievementList).hasSize(databaseSizeBeforeUpdate);
        PersonalizedAchievement testPersonalizedAchievement = personalizedAchievementList.get(personalizedAchievementList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingPersonalizedAchievement() throws Exception {
        int databaseSizeBeforeUpdate = personalizedAchievementRepository.findAll().size();
        personalizedAchievement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalizedAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalizedAchievement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizedAchievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalizedAchievement in the database
        List<PersonalizedAchievement> personalizedAchievementList = personalizedAchievementRepository.findAll();
        assertThat(personalizedAchievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalizedAchievement() throws Exception {
        int databaseSizeBeforeUpdate = personalizedAchievementRepository.findAll().size();
        personalizedAchievement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizedAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizedAchievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalizedAchievement in the database
        List<PersonalizedAchievement> personalizedAchievementList = personalizedAchievementRepository.findAll();
        assertThat(personalizedAchievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalizedAchievement() throws Exception {
        int databaseSizeBeforeUpdate = personalizedAchievementRepository.findAll().size();
        personalizedAchievement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizedAchievementMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalizedAchievement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalizedAchievement in the database
        List<PersonalizedAchievement> personalizedAchievementList = personalizedAchievementRepository.findAll();
        assertThat(personalizedAchievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalizedAchievementWithPatch() throws Exception {
        // Initialize the database
        personalizedAchievementRepository.saveAndFlush(personalizedAchievement);

        int databaseSizeBeforeUpdate = personalizedAchievementRepository.findAll().size();

        // Update the personalizedAchievement using partial update
        PersonalizedAchievement partialUpdatedPersonalizedAchievement = new PersonalizedAchievement();
        partialUpdatedPersonalizedAchievement.setId(personalizedAchievement.getId());

        restPersonalizedAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalizedAchievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalizedAchievement))
            )
            .andExpect(status().isOk());

        // Validate the PersonalizedAchievement in the database
        List<PersonalizedAchievement> personalizedAchievementList = personalizedAchievementRepository.findAll();
        assertThat(personalizedAchievementList).hasSize(databaseSizeBeforeUpdate);
        PersonalizedAchievement testPersonalizedAchievement = personalizedAchievementList.get(personalizedAchievementList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdatePersonalizedAchievementWithPatch() throws Exception {
        // Initialize the database
        personalizedAchievementRepository.saveAndFlush(personalizedAchievement);

        int databaseSizeBeforeUpdate = personalizedAchievementRepository.findAll().size();

        // Update the personalizedAchievement using partial update
        PersonalizedAchievement partialUpdatedPersonalizedAchievement = new PersonalizedAchievement();
        partialUpdatedPersonalizedAchievement.setId(personalizedAchievement.getId());

        restPersonalizedAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalizedAchievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalizedAchievement))
            )
            .andExpect(status().isOk());

        // Validate the PersonalizedAchievement in the database
        List<PersonalizedAchievement> personalizedAchievementList = personalizedAchievementRepository.findAll();
        assertThat(personalizedAchievementList).hasSize(databaseSizeBeforeUpdate);
        PersonalizedAchievement testPersonalizedAchievement = personalizedAchievementList.get(personalizedAchievementList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingPersonalizedAchievement() throws Exception {
        int databaseSizeBeforeUpdate = personalizedAchievementRepository.findAll().size();
        personalizedAchievement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalizedAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalizedAchievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalizedAchievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalizedAchievement in the database
        List<PersonalizedAchievement> personalizedAchievementList = personalizedAchievementRepository.findAll();
        assertThat(personalizedAchievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalizedAchievement() throws Exception {
        int databaseSizeBeforeUpdate = personalizedAchievementRepository.findAll().size();
        personalizedAchievement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizedAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalizedAchievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalizedAchievement in the database
        List<PersonalizedAchievement> personalizedAchievementList = personalizedAchievementRepository.findAll();
        assertThat(personalizedAchievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalizedAchievement() throws Exception {
        int databaseSizeBeforeUpdate = personalizedAchievementRepository.findAll().size();
        personalizedAchievement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalizedAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalizedAchievement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalizedAchievement in the database
        List<PersonalizedAchievement> personalizedAchievementList = personalizedAchievementRepository.findAll();
        assertThat(personalizedAchievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalizedAchievement() throws Exception {
        // Initialize the database
        personalizedAchievementRepository.saveAndFlush(personalizedAchievement);

        int databaseSizeBeforeDelete = personalizedAchievementRepository.findAll().size();

        // Delete the personalizedAchievement
        restPersonalizedAchievementMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalizedAchievement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonalizedAchievement> personalizedAchievementList = personalizedAchievementRepository.findAll();
        assertThat(personalizedAchievementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
