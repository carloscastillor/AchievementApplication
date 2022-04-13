package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LikeMessage;
import com.mycompany.myapp.repository.LikeMessageRepository;
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
 * Integration tests for the {@link LikeMessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LikeMessageResourceIT {

    private static final Boolean DEFAULT_LIKE = false;
    private static final Boolean UPDATED_LIKE = true;

    private static final String ENTITY_API_URL = "/api/like-messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LikeMessageRepository likeMessageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikeMessageMockMvc;

    private LikeMessage likeMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeMessage createEntity(EntityManager em) {
        LikeMessage likeMessage = new LikeMessage().like(DEFAULT_LIKE);
        return likeMessage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikeMessage createUpdatedEntity(EntityManager em) {
        LikeMessage likeMessage = new LikeMessage().like(UPDATED_LIKE);
        return likeMessage;
    }

    @BeforeEach
    public void initTest() {
        likeMessage = createEntity(em);
    }

    @Test
    @Transactional
    void createLikeMessage() throws Exception {
        int databaseSizeBeforeCreate = likeMessageRepository.findAll().size();
        // Create the LikeMessage
        restLikeMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeMessage)))
            .andExpect(status().isCreated());

        // Validate the LikeMessage in the database
        List<LikeMessage> likeMessageList = likeMessageRepository.findAll();
        assertThat(likeMessageList).hasSize(databaseSizeBeforeCreate + 1);
        LikeMessage testLikeMessage = likeMessageList.get(likeMessageList.size() - 1);
        assertThat(testLikeMessage.getLike()).isEqualTo(DEFAULT_LIKE);
    }

    @Test
    @Transactional
    void createLikeMessageWithExistingId() throws Exception {
        // Create the LikeMessage with an existing ID
        likeMessage.setId(1L);

        int databaseSizeBeforeCreate = likeMessageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikeMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeMessage)))
            .andExpect(status().isBadRequest());

        // Validate the LikeMessage in the database
        List<LikeMessage> likeMessageList = likeMessageRepository.findAll();
        assertThat(likeMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLikeMessages() throws Exception {
        // Initialize the database
        likeMessageRepository.saveAndFlush(likeMessage);

        // Get all the likeMessageList
        restLikeMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likeMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].like").value(hasItem(DEFAULT_LIKE.booleanValue())));
    }

    @Test
    @Transactional
    void getLikeMessage() throws Exception {
        // Initialize the database
        likeMessageRepository.saveAndFlush(likeMessage);

        // Get the likeMessage
        restLikeMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, likeMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(likeMessage.getId().intValue()))
            .andExpect(jsonPath("$.like").value(DEFAULT_LIKE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingLikeMessage() throws Exception {
        // Get the likeMessage
        restLikeMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLikeMessage() throws Exception {
        // Initialize the database
        likeMessageRepository.saveAndFlush(likeMessage);

        int databaseSizeBeforeUpdate = likeMessageRepository.findAll().size();

        // Update the likeMessage
        LikeMessage updatedLikeMessage = likeMessageRepository.findById(likeMessage.getId()).get();
        // Disconnect from session so that the updates on updatedLikeMessage are not directly saved in db
        em.detach(updatedLikeMessage);
        updatedLikeMessage.like(UPDATED_LIKE);

        restLikeMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLikeMessage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLikeMessage))
            )
            .andExpect(status().isOk());

        // Validate the LikeMessage in the database
        List<LikeMessage> likeMessageList = likeMessageRepository.findAll();
        assertThat(likeMessageList).hasSize(databaseSizeBeforeUpdate);
        LikeMessage testLikeMessage = likeMessageList.get(likeMessageList.size() - 1);
        assertThat(testLikeMessage.getLike()).isEqualTo(UPDATED_LIKE);
    }

    @Test
    @Transactional
    void putNonExistingLikeMessage() throws Exception {
        int databaseSizeBeforeUpdate = likeMessageRepository.findAll().size();
        likeMessage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likeMessage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeMessage in the database
        List<LikeMessage> likeMessageList = likeMessageRepository.findAll();
        assertThat(likeMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLikeMessage() throws Exception {
        int databaseSizeBeforeUpdate = likeMessageRepository.findAll().size();
        likeMessage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likeMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeMessage in the database
        List<LikeMessage> likeMessageList = likeMessageRepository.findAll();
        assertThat(likeMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLikeMessage() throws Exception {
        int databaseSizeBeforeUpdate = likeMessageRepository.findAll().size();
        likeMessage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMessageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likeMessage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeMessage in the database
        List<LikeMessage> likeMessageList = likeMessageRepository.findAll();
        assertThat(likeMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikeMessageWithPatch() throws Exception {
        // Initialize the database
        likeMessageRepository.saveAndFlush(likeMessage);

        int databaseSizeBeforeUpdate = likeMessageRepository.findAll().size();

        // Update the likeMessage using partial update
        LikeMessage partialUpdatedLikeMessage = new LikeMessage();
        partialUpdatedLikeMessage.setId(likeMessage.getId());

        restLikeMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikeMessage))
            )
            .andExpect(status().isOk());

        // Validate the LikeMessage in the database
        List<LikeMessage> likeMessageList = likeMessageRepository.findAll();
        assertThat(likeMessageList).hasSize(databaseSizeBeforeUpdate);
        LikeMessage testLikeMessage = likeMessageList.get(likeMessageList.size() - 1);
        assertThat(testLikeMessage.getLike()).isEqualTo(DEFAULT_LIKE);
    }

    @Test
    @Transactional
    void fullUpdateLikeMessageWithPatch() throws Exception {
        // Initialize the database
        likeMessageRepository.saveAndFlush(likeMessage);

        int databaseSizeBeforeUpdate = likeMessageRepository.findAll().size();

        // Update the likeMessage using partial update
        LikeMessage partialUpdatedLikeMessage = new LikeMessage();
        partialUpdatedLikeMessage.setId(likeMessage.getId());

        partialUpdatedLikeMessage.like(UPDATED_LIKE);

        restLikeMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikeMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikeMessage))
            )
            .andExpect(status().isOk());

        // Validate the LikeMessage in the database
        List<LikeMessage> likeMessageList = likeMessageRepository.findAll();
        assertThat(likeMessageList).hasSize(databaseSizeBeforeUpdate);
        LikeMessage testLikeMessage = likeMessageList.get(likeMessageList.size() - 1);
        assertThat(testLikeMessage.getLike()).isEqualTo(UPDATED_LIKE);
    }

    @Test
    @Transactional
    void patchNonExistingLikeMessage() throws Exception {
        int databaseSizeBeforeUpdate = likeMessageRepository.findAll().size();
        likeMessage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikeMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likeMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likeMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeMessage in the database
        List<LikeMessage> likeMessageList = likeMessageRepository.findAll();
        assertThat(likeMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLikeMessage() throws Exception {
        int databaseSizeBeforeUpdate = likeMessageRepository.findAll().size();
        likeMessage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likeMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikeMessage in the database
        List<LikeMessage> likeMessageList = likeMessageRepository.findAll();
        assertThat(likeMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLikeMessage() throws Exception {
        int databaseSizeBeforeUpdate = likeMessageRepository.findAll().size();
        likeMessage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikeMessageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(likeMessage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikeMessage in the database
        List<LikeMessage> likeMessageList = likeMessageRepository.findAll();
        assertThat(likeMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLikeMessage() throws Exception {
        // Initialize the database
        likeMessageRepository.saveAndFlush(likeMessage);

        int databaseSizeBeforeDelete = likeMessageRepository.findAll().size();

        // Delete the likeMessage
        restLikeMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, likeMessage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LikeMessage> likeMessageList = likeMessageRepository.findAll();
        assertThat(likeMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
