package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LikeMessage;
import com.mycompany.myapp.repository.LikeMessageRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.LikeMessage}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LikeMessageResource {

    private final Logger log = LoggerFactory.getLogger(LikeMessageResource.class);

    private static final String ENTITY_NAME = "likeMessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LikeMessageRepository likeMessageRepository;

    public LikeMessageResource(LikeMessageRepository likeMessageRepository) {
        this.likeMessageRepository = likeMessageRepository;
    }

    /**
     * {@code POST  /like-messages} : Create a new likeMessage.
     *
     * @param likeMessage the likeMessage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new likeMessage, or with status {@code 400 (Bad Request)} if the likeMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/like-messages")
    public ResponseEntity<LikeMessage> createLikeMessage(@RequestBody LikeMessage likeMessage) throws URISyntaxException {
        log.debug("REST request to save LikeMessage : {}", likeMessage);
        if (likeMessage.getId() != null) {
            throw new BadRequestAlertException("A new likeMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LikeMessage result = likeMessageRepository.save(likeMessage);
        return ResponseEntity
            .created(new URI("/api/like-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /like-messages/:id} : Updates an existing likeMessage.
     *
     * @param id the id of the likeMessage to save.
     * @param likeMessage the likeMessage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeMessage,
     * or with status {@code 400 (Bad Request)} if the likeMessage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the likeMessage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/like-messages/{id}")
    public ResponseEntity<LikeMessage> updateLikeMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LikeMessage likeMessage
    ) throws URISyntaxException {
        log.debug("REST request to update LikeMessage : {}, {}", id, likeMessage);
        if (likeMessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeMessage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeMessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LikeMessage result = likeMessageRepository.save(likeMessage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likeMessage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /like-messages/:id} : Partial updates given fields of an existing likeMessage, field will ignore if it is null
     *
     * @param id the id of the likeMessage to save.
     * @param likeMessage the likeMessage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likeMessage,
     * or with status {@code 400 (Bad Request)} if the likeMessage is not valid,
     * or with status {@code 404 (Not Found)} if the likeMessage is not found,
     * or with status {@code 500 (Internal Server Error)} if the likeMessage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/like-messages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LikeMessage> partialUpdateLikeMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LikeMessage likeMessage
    ) throws URISyntaxException {
        log.debug("REST request to partial update LikeMessage partially : {}, {}", id, likeMessage);
        if (likeMessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likeMessage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likeMessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LikeMessage> result = likeMessageRepository
            .findById(likeMessage.getId())
            .map(existingLikeMessage -> {
                if (likeMessage.getLike() != null) {
                    existingLikeMessage.setLike(likeMessage.getLike());
                }

                return existingLikeMessage;
            })
            .map(likeMessageRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likeMessage.getId().toString())
        );
    }

    /**
     * {@code GET  /like-messages} : get all the likeMessages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of likeMessages in body.
     */
    @GetMapping("/like-messages")
    public List<LikeMessage> getAllLikeMessages() {
        log.debug("REST request to get all LikeMessages");
        return likeMessageRepository.findAll();
    }

    /**
     * {@code GET  /like-messages/:id} : get the "id" likeMessage.
     *
     * @param id the id of the likeMessage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the likeMessage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/like-messages/{id}")
    public ResponseEntity<LikeMessage> getLikeMessage(@PathVariable Long id) {
        log.debug("REST request to get LikeMessage : {}", id);
        Optional<LikeMessage> likeMessage = likeMessageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(likeMessage);
    }

    /**
     * {@code DELETE  /like-messages/:id} : delete the "id" likeMessage.
     *
     * @param id the id of the likeMessage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/like-messages/{id}")
    public ResponseEntity<Void> deleteLikeMessage(@PathVariable Long id) {
        log.debug("REST request to delete LikeMessage : {}", id);
        likeMessageRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
