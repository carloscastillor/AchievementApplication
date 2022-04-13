package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Videogame;
import com.mycompany.myapp.repository.VideogameRepository;
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
 * Integration tests for the {@link VideogameResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VideogameResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/videogames";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VideogameRepository videogameRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVideogameMockMvc;

    private Videogame videogame;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Videogame createEntity(EntityManager em) {
        Videogame videogame = new Videogame().name(DEFAULT_NAME);
        return videogame;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Videogame createUpdatedEntity(EntityManager em) {
        Videogame videogame = new Videogame().name(UPDATED_NAME);
        return videogame;
    }

    @BeforeEach
    public void initTest() {
        videogame = createEntity(em);
    }

    @Test
    @Transactional
    void createVideogame() throws Exception {
        int databaseSizeBeforeCreate = videogameRepository.findAll().size();
        // Create the Videogame
        restVideogameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videogame)))
            .andExpect(status().isCreated());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeCreate + 1);
        Videogame testVideogame = videogameList.get(videogameList.size() - 1);
        assertThat(testVideogame.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createVideogameWithExistingId() throws Exception {
        // Create the Videogame with an existing ID
        videogame.setId(1L);

        int databaseSizeBeforeCreate = videogameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideogameMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videogame)))
            .andExpect(status().isBadRequest());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVideogames() throws Exception {
        // Initialize the database
        videogameRepository.saveAndFlush(videogame);

        // Get all the videogameList
        restVideogameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videogame.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getVideogame() throws Exception {
        // Initialize the database
        videogameRepository.saveAndFlush(videogame);

        // Get the videogame
        restVideogameMockMvc
            .perform(get(ENTITY_API_URL_ID, videogame.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(videogame.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingVideogame() throws Exception {
        // Get the videogame
        restVideogameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVideogame() throws Exception {
        // Initialize the database
        videogameRepository.saveAndFlush(videogame);

        int databaseSizeBeforeUpdate = videogameRepository.findAll().size();

        // Update the videogame
        Videogame updatedVideogame = videogameRepository.findById(videogame.getId()).get();
        // Disconnect from session so that the updates on updatedVideogame are not directly saved in db
        em.detach(updatedVideogame);
        updatedVideogame.name(UPDATED_NAME);

        restVideogameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVideogame.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVideogame))
            )
            .andExpect(status().isOk());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeUpdate);
        Videogame testVideogame = videogameList.get(videogameList.size() - 1);
        assertThat(testVideogame.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingVideogame() throws Exception {
        int databaseSizeBeforeUpdate = videogameRepository.findAll().size();
        videogame.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideogameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, videogame.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(videogame))
            )
            .andExpect(status().isBadRequest());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVideogame() throws Exception {
        int databaseSizeBeforeUpdate = videogameRepository.findAll().size();
        videogame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideogameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(videogame))
            )
            .andExpect(status().isBadRequest());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVideogame() throws Exception {
        int databaseSizeBeforeUpdate = videogameRepository.findAll().size();
        videogame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideogameMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(videogame)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVideogameWithPatch() throws Exception {
        // Initialize the database
        videogameRepository.saveAndFlush(videogame);

        int databaseSizeBeforeUpdate = videogameRepository.findAll().size();

        // Update the videogame using partial update
        Videogame partialUpdatedVideogame = new Videogame();
        partialUpdatedVideogame.setId(videogame.getId());

        partialUpdatedVideogame.name(UPDATED_NAME);

        restVideogameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideogame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVideogame))
            )
            .andExpect(status().isOk());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeUpdate);
        Videogame testVideogame = videogameList.get(videogameList.size() - 1);
        assertThat(testVideogame.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateVideogameWithPatch() throws Exception {
        // Initialize the database
        videogameRepository.saveAndFlush(videogame);

        int databaseSizeBeforeUpdate = videogameRepository.findAll().size();

        // Update the videogame using partial update
        Videogame partialUpdatedVideogame = new Videogame();
        partialUpdatedVideogame.setId(videogame.getId());

        partialUpdatedVideogame.name(UPDATED_NAME);

        restVideogameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVideogame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVideogame))
            )
            .andExpect(status().isOk());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeUpdate);
        Videogame testVideogame = videogameList.get(videogameList.size() - 1);
        assertThat(testVideogame.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingVideogame() throws Exception {
        int databaseSizeBeforeUpdate = videogameRepository.findAll().size();
        videogame.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideogameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, videogame.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(videogame))
            )
            .andExpect(status().isBadRequest());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVideogame() throws Exception {
        int databaseSizeBeforeUpdate = videogameRepository.findAll().size();
        videogame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideogameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(videogame))
            )
            .andExpect(status().isBadRequest());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVideogame() throws Exception {
        int databaseSizeBeforeUpdate = videogameRepository.findAll().size();
        videogame.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVideogameMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(videogame))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Videogame in the database
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVideogame() throws Exception {
        // Initialize the database
        videogameRepository.saveAndFlush(videogame);

        int databaseSizeBeforeDelete = videogameRepository.findAll().size();

        // Delete the videogame
        restVideogameMockMvc
            .perform(delete(ENTITY_API_URL_ID, videogame.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Videogame> videogameList = videogameRepository.findAll();
        assertThat(videogameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
