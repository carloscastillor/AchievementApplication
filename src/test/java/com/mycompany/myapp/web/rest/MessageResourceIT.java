package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Message;
import com.mycompany.myapp.repository.MessageRepository;
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
 * Integration tests for the {@link MessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MessageResourceIT {

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMessageMockMvc;

    private Message message;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createEntity(EntityManager em) {
        Message message = new Message().body(DEFAULT_BODY);
        return message;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createUpdatedEntity(EntityManager em) {
        Message message = new Message().body(UPDATED_BODY);
        return message;
    }

    @BeforeEach
    public void initTest() {
        message = createEntity(em);
    }

    @Test
    @Transactional
    void createMessage() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();
        // Create the Message
        restMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isCreated());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate + 1);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getBody()).isEqualTo(DEFAULT_BODY);
    }

    @Test
    @Transactional
    void createMessageWithExistingId() throws Exception {
        // Create the Message with an existing ID
        message.setId(1L);

        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMessages() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList
        restMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId().intValue())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)));
    }

    @Test
    @Transactional
    void getMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get the message
        restMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(message.getId().intValue()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY));
    }

    @Test
    @Transactional
    void getNonExistingMessage() throws Exception {
        // Get the message
        restMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message
        Message updatedMessage = messageRepository.findById(message.getId()).get();
        // Disconnect from session so that the updates on updatedMessage are not directly saved in db
        em.detach(updatedMessage);
        updatedMessage.body(UPDATED_BODY);

        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMessage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMessage))
            )
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getBody()).isEqualTo(UPDATED_BODY);
    }

    @Test
    @Transactional
    void putNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, message.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(message))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(message))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMessageWithPatch() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message using partial update
        Message partialUpdatedMessage = new Message();
        partialUpdatedMessage.setId(message.getId());

        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMessage))
            )
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getBody()).isEqualTo(DEFAULT_BODY);
    }

    @Test
    @Transactional
    void fullUpdateMessageWithPatch() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message using partial update
        Message partialUpdatedMessage = new Message();
        partialUpdatedMessage.setId(message.getId());

        partialUpdatedMessage.body(UPDATED_BODY);

        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMessage))
            )
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getBody()).isEqualTo(UPDATED_BODY);
    }

    @Test
    @Transactional
    void patchNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, message.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(message))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(message))
            )
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();
        message.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMessageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        int databaseSizeBeforeDelete = messageRepository.findAll().size();

        // Delete the message
        restMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, message.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
