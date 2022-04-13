package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Resource;
import com.mycompany.myapp.repository.ResourceRepository;
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
 * Integration tests for the {@link ResourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ResourceResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_URL_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_RESOURCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/resources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResourceMockMvc;

    private Resource resource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createEntity(EntityManager em) {
        Resource resource = new Resource()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .urlSource(DEFAULT_URL_SOURCE)
            .resourceType(DEFAULT_RESOURCE_TYPE);
        return resource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resource createUpdatedEntity(EntityManager em) {
        Resource resource = new Resource()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .urlSource(UPDATED_URL_SOURCE)
            .resourceType(UPDATED_RESOURCE_TYPE);
        return resource;
    }

    @BeforeEach
    public void initTest() {
        resource = createEntity(em);
    }

    @Test
    @Transactional
    void createResource() throws Exception {
        int databaseSizeBeforeCreate = resourceRepository.findAll().size();
        // Create the Resource
        restResourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isCreated());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate + 1);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testResource.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testResource.getUrlSource()).isEqualTo(DEFAULT_URL_SOURCE);
        assertThat(testResource.getResourceType()).isEqualTo(DEFAULT_RESOURCE_TYPE);
    }

    @Test
    @Transactional
    void createResourceWithExistingId() throws Exception {
        // Create the Resource with an existing ID
        resource.setId(1L);

        int databaseSizeBeforeCreate = resourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResources() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get all the resourceList
        restResourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].urlSource").value(hasItem(DEFAULT_URL_SOURCE)))
            .andExpect(jsonPath("$.[*].resourceType").value(hasItem(DEFAULT_RESOURCE_TYPE)));
    }

    @Test
    @Transactional
    void getResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        // Get the resource
        restResourceMockMvc
            .perform(get(ENTITY_API_URL_ID, resource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resource.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.urlSource").value(DEFAULT_URL_SOURCE))
            .andExpect(jsonPath("$.resourceType").value(DEFAULT_RESOURCE_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingResource() throws Exception {
        // Get the resource
        restResourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource
        Resource updatedResource = resourceRepository.findById(resource.getId()).get();
        // Disconnect from session so that the updates on updatedResource are not directly saved in db
        em.detach(updatedResource);
        updatedResource
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .urlSource(UPDATED_URL_SOURCE)
            .resourceType(UPDATED_RESOURCE_TYPE);

        restResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedResource))
            )
            .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testResource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testResource.getUrlSource()).isEqualTo(UPDATED_URL_SOURCE);
        assertThat(testResource.getResourceType()).isEqualTo(UPDATED_RESOURCE_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();
        resource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resource))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();
        resource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resource))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();
        resource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResourceWithPatch() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource using partial update
        Resource partialUpdatedResource = new Resource();
        partialUpdatedResource.setId(resource.getId());

        partialUpdatedResource.description(UPDATED_DESCRIPTION);

        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResource))
            )
            .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testResource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testResource.getUrlSource()).isEqualTo(DEFAULT_URL_SOURCE);
        assertThat(testResource.getResourceType()).isEqualTo(DEFAULT_RESOURCE_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateResourceWithPatch() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

        // Update the resource using partial update
        Resource partialUpdatedResource = new Resource();
        partialUpdatedResource.setId(resource.getId());

        partialUpdatedResource
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .urlSource(UPDATED_URL_SOURCE)
            .resourceType(UPDATED_RESOURCE_TYPE);

        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResource))
            )
            .andExpect(status().isOk());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
        Resource testResource = resourceList.get(resourceList.size() - 1);
        assertThat(testResource.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testResource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testResource.getUrlSource()).isEqualTo(UPDATED_URL_SOURCE);
        assertThat(testResource.getResourceType()).isEqualTo(UPDATED_RESOURCE_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();
        resource.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resource))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();
        resource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resource))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResource() throws Exception {
        int databaseSizeBeforeUpdate = resourceRepository.findAll().size();
        resource.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResourceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(resource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resource in the database
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResource() throws Exception {
        // Initialize the database
        resourceRepository.saveAndFlush(resource);

        int databaseSizeBeforeDelete = resourceRepository.findAll().size();

        // Delete the resource
        restResourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, resource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resource> resourceList = resourceRepository.findAll();
        assertThat(resourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
