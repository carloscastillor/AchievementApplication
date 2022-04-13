package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LikePost;
import com.mycompany.myapp.repository.LikePostRepository;
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
 * Integration tests for the {@link LikePostResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LikePostResourceIT {

    private static final Boolean DEFAULT_LIKE = false;
    private static final Boolean UPDATED_LIKE = true;

    private static final String ENTITY_API_URL = "/api/like-posts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLikePostMockMvc;

    private LikePost likePost;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikePost createEntity(EntityManager em) {
        LikePost likePost = new LikePost().like(DEFAULT_LIKE);
        return likePost;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LikePost createUpdatedEntity(EntityManager em) {
        LikePost likePost = new LikePost().like(UPDATED_LIKE);
        return likePost;
    }

    @BeforeEach
    public void initTest() {
        likePost = createEntity(em);
    }

    @Test
    @Transactional
    void createLikePost() throws Exception {
        int databaseSizeBeforeCreate = likePostRepository.findAll().size();
        // Create the LikePost
        restLikePostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likePost)))
            .andExpect(status().isCreated());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeCreate + 1);
        LikePost testLikePost = likePostList.get(likePostList.size() - 1);
        assertThat(testLikePost.getLike()).isEqualTo(DEFAULT_LIKE);
    }

    @Test
    @Transactional
    void createLikePostWithExistingId() throws Exception {
        // Create the LikePost with an existing ID
        likePost.setId(1L);

        int databaseSizeBeforeCreate = likePostRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLikePostMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likePost)))
            .andExpect(status().isBadRequest());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLikePosts() throws Exception {
        // Initialize the database
        likePostRepository.saveAndFlush(likePost);

        // Get all the likePostList
        restLikePostMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(likePost.getId().intValue())))
            .andExpect(jsonPath("$.[*].like").value(hasItem(DEFAULT_LIKE.booleanValue())));
    }

    @Test
    @Transactional
    void getLikePost() throws Exception {
        // Initialize the database
        likePostRepository.saveAndFlush(likePost);

        // Get the likePost
        restLikePostMockMvc
            .perform(get(ENTITY_API_URL_ID, likePost.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(likePost.getId().intValue()))
            .andExpect(jsonPath("$.like").value(DEFAULT_LIKE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingLikePost() throws Exception {
        // Get the likePost
        restLikePostMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLikePost() throws Exception {
        // Initialize the database
        likePostRepository.saveAndFlush(likePost);

        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();

        // Update the likePost
        LikePost updatedLikePost = likePostRepository.findById(likePost.getId()).get();
        // Disconnect from session so that the updates on updatedLikePost are not directly saved in db
        em.detach(updatedLikePost);
        updatedLikePost.like(UPDATED_LIKE);

        restLikePostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLikePost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLikePost))
            )
            .andExpect(status().isOk());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
        LikePost testLikePost = likePostList.get(likePostList.size() - 1);
        assertThat(testLikePost.getLike()).isEqualTo(UPDATED_LIKE);
    }

    @Test
    @Transactional
    void putNonExistingLikePost() throws Exception {
        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();
        likePost.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikePostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, likePost.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likePost))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLikePost() throws Exception {
        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();
        likePost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikePostMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(likePost))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLikePost() throws Exception {
        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();
        likePost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikePostMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(likePost)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLikePostWithPatch() throws Exception {
        // Initialize the database
        likePostRepository.saveAndFlush(likePost);

        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();

        // Update the likePost using partial update
        LikePost partialUpdatedLikePost = new LikePost();
        partialUpdatedLikePost.setId(likePost.getId());

        restLikePostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikePost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikePost))
            )
            .andExpect(status().isOk());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
        LikePost testLikePost = likePostList.get(likePostList.size() - 1);
        assertThat(testLikePost.getLike()).isEqualTo(DEFAULT_LIKE);
    }

    @Test
    @Transactional
    void fullUpdateLikePostWithPatch() throws Exception {
        // Initialize the database
        likePostRepository.saveAndFlush(likePost);

        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();

        // Update the likePost using partial update
        LikePost partialUpdatedLikePost = new LikePost();
        partialUpdatedLikePost.setId(likePost.getId());

        partialUpdatedLikePost.like(UPDATED_LIKE);

        restLikePostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLikePost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLikePost))
            )
            .andExpect(status().isOk());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
        LikePost testLikePost = likePostList.get(likePostList.size() - 1);
        assertThat(testLikePost.getLike()).isEqualTo(UPDATED_LIKE);
    }

    @Test
    @Transactional
    void patchNonExistingLikePost() throws Exception {
        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();
        likePost.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLikePostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, likePost.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likePost))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLikePost() throws Exception {
        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();
        likePost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikePostMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(likePost))
            )
            .andExpect(status().isBadRequest());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLikePost() throws Exception {
        int databaseSizeBeforeUpdate = likePostRepository.findAll().size();
        likePost.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLikePostMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(likePost)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LikePost in the database
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLikePost() throws Exception {
        // Initialize the database
        likePostRepository.saveAndFlush(likePost);

        int databaseSizeBeforeDelete = likePostRepository.findAll().size();

        // Delete the likePost
        restLikePostMockMvc
            .perform(delete(ENTITY_API_URL_ID, likePost.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
