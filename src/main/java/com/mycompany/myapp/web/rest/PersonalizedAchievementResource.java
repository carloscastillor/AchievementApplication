package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PersonalizedAchievement;
import com.mycompany.myapp.repository.PersonalizedAchievementRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PersonalizedAchievement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PersonalizedAchievementResource {

    private final Logger log = LoggerFactory.getLogger(PersonalizedAchievementResource.class);

    private static final String ENTITY_NAME = "personalizedAchievement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonalizedAchievementRepository personalizedAchievementRepository;

    public PersonalizedAchievementResource(PersonalizedAchievementRepository personalizedAchievementRepository) {
        this.personalizedAchievementRepository = personalizedAchievementRepository;
    }

    /**
     * {@code POST  /personalized-achievements} : Create a new personalizedAchievement.
     *
     * @param personalizedAchievement the personalizedAchievement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personalizedAchievement, or with status {@code 400 (Bad Request)} if the personalizedAchievement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/personalized-achievements")
    public ResponseEntity<PersonalizedAchievement> createPersonalizedAchievement(
        @RequestBody PersonalizedAchievement personalizedAchievement
    ) throws URISyntaxException {
        log.debug("REST request to save PersonalizedAchievement : {}", personalizedAchievement);
        if (personalizedAchievement.getId() != null) {
            throw new BadRequestAlertException("A new personalizedAchievement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonalizedAchievement result = personalizedAchievementRepository.save(personalizedAchievement);
        return ResponseEntity
            .created(new URI("/api/personalized-achievements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personalized-achievements/:id} : Updates an existing personalizedAchievement.
     *
     * @param id the id of the personalizedAchievement to save.
     * @param personalizedAchievement the personalizedAchievement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalizedAchievement,
     * or with status {@code 400 (Bad Request)} if the personalizedAchievement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personalizedAchievement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/personalized-achievements/{id}")
    public ResponseEntity<PersonalizedAchievement> updatePersonalizedAchievement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonalizedAchievement personalizedAchievement
    ) throws URISyntaxException {
        log.debug("REST request to update PersonalizedAchievement : {}, {}", id, personalizedAchievement);
        if (personalizedAchievement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalizedAchievement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalizedAchievementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonalizedAchievement result = personalizedAchievementRepository.save(personalizedAchievement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalizedAchievement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personalized-achievements/:id} : Partial updates given fields of an existing personalizedAchievement, field will ignore if it is null
     *
     * @param id the id of the personalizedAchievement to save.
     * @param personalizedAchievement the personalizedAchievement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalizedAchievement,
     * or with status {@code 400 (Bad Request)} if the personalizedAchievement is not valid,
     * or with status {@code 404 (Not Found)} if the personalizedAchievement is not found,
     * or with status {@code 500 (Internal Server Error)} if the personalizedAchievement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/personalized-achievements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonalizedAchievement> partialUpdatePersonalizedAchievement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PersonalizedAchievement personalizedAchievement
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonalizedAchievement partially : {}, {}", id, personalizedAchievement);
        if (personalizedAchievement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalizedAchievement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalizedAchievementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonalizedAchievement> result = personalizedAchievementRepository
            .findById(personalizedAchievement.getId())
            .map(existingPersonalizedAchievement -> {
                return existingPersonalizedAchievement;
            })
            .map(personalizedAchievementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalizedAchievement.getId().toString())
        );
    }

    /**
     * {@code GET  /personalized-achievements} : get all the personalizedAchievements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personalizedAchievements in body.
     */
    @GetMapping("/personalized-achievements")
    public List<PersonalizedAchievement> getAllPersonalizedAchievements() {
        log.debug("REST request to get all PersonalizedAchievements");
        return personalizedAchievementRepository.findAll();
    }

    /**
     * {@code GET  /personalized-achievements/:id} : get the "id" personalizedAchievement.
     *
     * @param id the id of the personalizedAchievement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personalizedAchievement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/personalized-achievements/{id}")
    public ResponseEntity<PersonalizedAchievement> getPersonalizedAchievement(@PathVariable Long id) {
        log.debug("REST request to get PersonalizedAchievement : {}", id);
        Optional<PersonalizedAchievement> personalizedAchievement = personalizedAchievementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(personalizedAchievement);
    }

    /**
     * {@code DELETE  /personalized-achievements/:id} : delete the "id" personalizedAchievement.
     *
     * @param id the id of the personalizedAchievement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/personalized-achievements/{id}")
    public ResponseEntity<Void> deletePersonalizedAchievement(@PathVariable Long id) {
        log.debug("REST request to delete PersonalizedAchievement : {}", id);
        personalizedAchievementRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
