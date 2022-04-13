package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Walkthrough;
import com.mycompany.myapp.repository.WalkthroughRepository;
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
 * Integration tests for the {@link WalkthroughResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WalkthroughResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/walkthroughs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WalkthroughRepository walkthroughRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWalkthroughMockMvc;

    private Walkthrough walkthrough;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Walkthrough createEntity(EntityManager em) {
        Walkthrough walkthrough = new Walkthrough().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return walkthrough;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Walkthrough createUpdatedEntity(EntityManager em) {
        Walkthrough walkthrough = new Walkthrough().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return walkthrough;
    }

    @BeforeEach
    public void initTest() {
        walkthrough = createEntity(em);
    }

    @Test
    @Transactional
    void createWalkthrough() throws Exception {
        int databaseSizeBeforeCreate = walkthroughRepository.findAll().size();
        // Create the Walkthrough
        restWalkthroughMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(walkthrough)))
            .andExpect(status().isCreated());

        // Validate the Walkthrough in the database
        List<Walkthrough> walkthroughList = walkthroughRepository.findAll();
        assertThat(walkthroughList).hasSize(databaseSizeBeforeCreate + 1);
        Walkthrough testWalkthrough = walkthroughList.get(walkthroughList.size() - 1);
        assertThat(testWalkthrough.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWalkthrough.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createWalkthroughWithExistingId() throws Exception {
        // Create the Walkthrough with an existing ID
        walkthrough.setId(1L);

        int databaseSizeBeforeCreate = walkthroughRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalkthroughMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(walkthrough)))
            .andExpect(status().isBadRequest());

        // Validate the Walkthrough in the database
        List<Walkthrough> walkthroughList = walkthroughRepository.findAll();
        assertThat(walkthroughList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWalkthroughs() throws Exception {
        // Initialize the database
        walkthroughRepository.saveAndFlush(walkthrough);

        // Get all the walkthroughList
        restWalkthroughMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walkthrough.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getWalkthrough() throws Exception {
        // Initialize the database
        walkthroughRepository.saveAndFlush(walkthrough);

        // Get the walkthrough
        restWalkthroughMockMvc
            .perform(get(ENTITY_API_URL_ID, walkthrough.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(walkthrough.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingWalkthrough() throws Exception {
        // Get the walkthrough
        restWalkthroughMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWalkthrough() throws Exception {
        // Initialize the database
        walkthroughRepository.saveAndFlush(walkthrough);

        int databaseSizeBeforeUpdate = walkthroughRepository.findAll().size();

        // Update the walkthrough
        Walkthrough updatedWalkthrough = walkthroughRepository.findById(walkthrough.getId()).get();
        // Disconnect from session so that the updates on updatedWalkthrough are not directly saved in db
        em.detach(updatedWalkthrough);
        updatedWalkthrough.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restWalkthroughMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWalkthrough.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWalkthrough))
            )
            .andExpect(status().isOk());

        // Validate the Walkthrough in the database
        List<Walkthrough> walkthroughList = walkthroughRepository.findAll();
        assertThat(walkthroughList).hasSize(databaseSizeBeforeUpdate);
        Walkthrough testWalkthrough = walkthroughList.get(walkthroughList.size() - 1);
        assertThat(testWalkthrough.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWalkthrough.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingWalkthrough() throws Exception {
        int databaseSizeBeforeUpdate = walkthroughRepository.findAll().size();
        walkthrough.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalkthroughMockMvc
            .perform(
                put(ENTITY_API_URL_ID, walkthrough.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(walkthrough))
            )
            .andExpect(status().isBadRequest());

        // Validate the Walkthrough in the database
        List<Walkthrough> walkthroughList = walkthroughRepository.findAll();
        assertThat(walkthroughList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWalkthrough() throws Exception {
        int databaseSizeBeforeUpdate = walkthroughRepository.findAll().size();
        walkthrough.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalkthroughMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(walkthrough))
            )
            .andExpect(status().isBadRequest());

        // Validate the Walkthrough in the database
        List<Walkthrough> walkthroughList = walkthroughRepository.findAll();
        assertThat(walkthroughList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWalkthrough() throws Exception {
        int databaseSizeBeforeUpdate = walkthroughRepository.findAll().size();
        walkthrough.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalkthroughMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(walkthrough)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Walkthrough in the database
        List<Walkthrough> walkthroughList = walkthroughRepository.findAll();
        assertThat(walkthroughList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWalkthroughWithPatch() throws Exception {
        // Initialize the database
        walkthroughRepository.saveAndFlush(walkthrough);

        int databaseSizeBeforeUpdate = walkthroughRepository.findAll().size();

        // Update the walkthrough using partial update
        Walkthrough partialUpdatedWalkthrough = new Walkthrough();
        partialUpdatedWalkthrough.setId(walkthrough.getId());

        restWalkthroughMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWalkthrough.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWalkthrough))
            )
            .andExpect(status().isOk());

        // Validate the Walkthrough in the database
        List<Walkthrough> walkthroughList = walkthroughRepository.findAll();
        assertThat(walkthroughList).hasSize(databaseSizeBeforeUpdate);
        Walkthrough testWalkthrough = walkthroughList.get(walkthroughList.size() - 1);
        assertThat(testWalkthrough.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testWalkthrough.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateWalkthroughWithPatch() throws Exception {
        // Initialize the database
        walkthroughRepository.saveAndFlush(walkthrough);

        int databaseSizeBeforeUpdate = walkthroughRepository.findAll().size();

        // Update the walkthrough using partial update
        Walkthrough partialUpdatedWalkthrough = new Walkthrough();
        partialUpdatedWalkthrough.setId(walkthrough.getId());

        partialUpdatedWalkthrough.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restWalkthroughMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWalkthrough.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWalkthrough))
            )
            .andExpect(status().isOk());

        // Validate the Walkthrough in the database
        List<Walkthrough> walkthroughList = walkthroughRepository.findAll();
        assertThat(walkthroughList).hasSize(databaseSizeBeforeUpdate);
        Walkthrough testWalkthrough = walkthroughList.get(walkthroughList.size() - 1);
        assertThat(testWalkthrough.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testWalkthrough.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingWalkthrough() throws Exception {
        int databaseSizeBeforeUpdate = walkthroughRepository.findAll().size();
        walkthrough.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalkthroughMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, walkthrough.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(walkthrough))
            )
            .andExpect(status().isBadRequest());

        // Validate the Walkthrough in the database
        List<Walkthrough> walkthroughList = walkthroughRepository.findAll();
        assertThat(walkthroughList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWalkthrough() throws Exception {
        int databaseSizeBeforeUpdate = walkthroughRepository.findAll().size();
        walkthrough.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalkthroughMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(walkthrough))
            )
            .andExpect(status().isBadRequest());

        // Validate the Walkthrough in the database
        List<Walkthrough> walkthroughList = walkthroughRepository.findAll();
        assertThat(walkthroughList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWalkthrough() throws Exception {
        int databaseSizeBeforeUpdate = walkthroughRepository.findAll().size();
        walkthrough.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalkthroughMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(walkthrough))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Walkthrough in the database
        List<Walkthrough> walkthroughList = walkthroughRepository.findAll();
        assertThat(walkthroughList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWalkthrough() throws Exception {
        // Initialize the database
        walkthroughRepository.saveAndFlush(walkthrough);

        int databaseSizeBeforeDelete = walkthroughRepository.findAll().size();

        // Delete the walkthrough
        restWalkthroughMockMvc
            .perform(delete(ENTITY_API_URL_ID, walkthrough.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Walkthrough> walkthroughList = walkthroughRepository.findAll();
        assertThat(walkthroughList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
