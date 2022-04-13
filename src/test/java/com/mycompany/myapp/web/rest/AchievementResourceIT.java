package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Achievement;
import com.mycompany.myapp.repository.AchievementRepository;
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
 * Integration tests for the {@link AchievementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AchievementResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEOGAME = "AAAAAAAAAA";
    private static final String UPDATED_VIDEOGAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETED = false;
    private static final Boolean UPDATED_COMPLETED = true;

    private static final String ENTITY_API_URL = "/api/achievements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAchievementMockMvc;

    private Achievement achievement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Achievement createEntity(EntityManager em) {
        Achievement achievement = new Achievement()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .videogame(DEFAULT_VIDEOGAME)
            .completed(DEFAULT_COMPLETED);
        return achievement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Achievement createUpdatedEntity(EntityManager em) {
        Achievement achievement = new Achievement()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .videogame(UPDATED_VIDEOGAME)
            .completed(UPDATED_COMPLETED);
        return achievement;
    }

    @BeforeEach
    public void initTest() {
        achievement = createEntity(em);
    }

    @Test
    @Transactional
    void createAchievement() throws Exception {
        int databaseSizeBeforeCreate = achievementRepository.findAll().size();
        // Create the Achievement
        restAchievementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isCreated());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeCreate + 1);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAchievement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAchievement.getVideogame()).isEqualTo(DEFAULT_VIDEOGAME);
        assertThat(testAchievement.getCompleted()).isEqualTo(DEFAULT_COMPLETED);
    }

    @Test
    @Transactional
    void createAchievementWithExistingId() throws Exception {
        // Create the Achievement with an existing ID
        achievement.setId(1L);

        int databaseSizeBeforeCreate = achievementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAchievementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAchievements() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        // Get all the achievementList
        restAchievementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(achievement.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].videogame").value(hasItem(DEFAULT_VIDEOGAME)))
            .andExpect(jsonPath("$.[*].completed").value(hasItem(DEFAULT_COMPLETED.booleanValue())));
    }

    @Test
    @Transactional
    void getAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        // Get the achievement
        restAchievementMockMvc
            .perform(get(ENTITY_API_URL_ID, achievement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(achievement.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.videogame").value(DEFAULT_VIDEOGAME))
            .andExpect(jsonPath("$.completed").value(DEFAULT_COMPLETED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingAchievement() throws Exception {
        // Get the achievement
        restAchievementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();

        // Update the achievement
        Achievement updatedAchievement = achievementRepository.findById(achievement.getId()).get();
        // Disconnect from session so that the updates on updatedAchievement are not directly saved in db
        em.detach(updatedAchievement);
        updatedAchievement.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).videogame(UPDATED_VIDEOGAME).completed(UPDATED_COMPLETED);

        restAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAchievement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAchievement))
            )
            .andExpect(status().isOk());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAchievement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAchievement.getVideogame()).isEqualTo(UPDATED_VIDEOGAME);
        assertThat(testAchievement.getCompleted()).isEqualTo(UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    void putNonExistingAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();
        achievement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, achievement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(achievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();
        achievement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAchievementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(achievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();
        achievement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAchievementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(achievement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAchievementWithPatch() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();

        // Update the achievement using partial update
        Achievement partialUpdatedAchievement = new Achievement();
        partialUpdatedAchievement.setId(achievement.getId());

        partialUpdatedAchievement.completed(UPDATED_COMPLETED);

        restAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAchievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAchievement))
            )
            .andExpect(status().isOk());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAchievement.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAchievement.getVideogame()).isEqualTo(DEFAULT_VIDEOGAME);
        assertThat(testAchievement.getCompleted()).isEqualTo(UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    void fullUpdateAchievementWithPatch() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();

        // Update the achievement using partial update
        Achievement partialUpdatedAchievement = new Achievement();
        partialUpdatedAchievement.setId(achievement.getId());

        partialUpdatedAchievement
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .videogame(UPDATED_VIDEOGAME)
            .completed(UPDATED_COMPLETED);

        restAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAchievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAchievement))
            )
            .andExpect(status().isOk());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
        Achievement testAchievement = achievementList.get(achievementList.size() - 1);
        assertThat(testAchievement.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAchievement.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAchievement.getVideogame()).isEqualTo(UPDATED_VIDEOGAME);
        assertThat(testAchievement.getCompleted()).isEqualTo(UPDATED_COMPLETED);
    }

    @Test
    @Transactional
    void patchNonExistingAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();
        achievement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, achievement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(achievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();
        achievement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(achievement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAchievement() throws Exception {
        int databaseSizeBeforeUpdate = achievementRepository.findAll().size();
        achievement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAchievementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(achievement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Achievement in the database
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAchievement() throws Exception {
        // Initialize the database
        achievementRepository.saveAndFlush(achievement);

        int databaseSizeBeforeDelete = achievementRepository.findAll().size();

        // Delete the achievement
        restAchievementMockMvc
            .perform(delete(ENTITY_API_URL_ID, achievement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Achievement> achievementList = achievementRepository.findAll();
        assertThat(achievementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
