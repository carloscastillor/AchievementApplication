package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Videogame;
import com.mycompany.myapp.repository.VideogameRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Videogame}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VideogameResource {

    private final Logger log = LoggerFactory.getLogger(VideogameResource.class);

    private static final String ENTITY_NAME = "videogame";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VideogameRepository videogameRepository;

    public VideogameResource(VideogameRepository videogameRepository) {
        this.videogameRepository = videogameRepository;
    }

    /**
     * {@code POST  /videogames} : Create a new videogame.
     *
     * @param videogame the videogame to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new videogame, or with status {@code 400 (Bad Request)} if the videogame has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/videogames")
    public ResponseEntity<Videogame> createVideogame(@RequestBody Videogame videogame) throws URISyntaxException {
        log.debug("REST request to save Videogame : {}", videogame);
        if (videogame.getId() != null) {
            throw new BadRequestAlertException("A new videogame cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Videogame result = videogameRepository.save(videogame);
        return ResponseEntity
            .created(new URI("/api/videogames/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /videogames/:id} : Updates an existing videogame.
     *
     * @param id the id of the videogame to save.
     * @param videogame the videogame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videogame,
     * or with status {@code 400 (Bad Request)} if the videogame is not valid,
     * or with status {@code 500 (Internal Server Error)} if the videogame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/videogames/{id}")
    public ResponseEntity<Videogame> updateVideogame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Videogame videogame
    ) throws URISyntaxException {
        log.debug("REST request to update Videogame : {}, {}", id, videogame);
        if (videogame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, videogame.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!videogameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Videogame result = videogameRepository.save(videogame);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, videogame.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /videogames/:id} : Partial updates given fields of an existing videogame, field will ignore if it is null
     *
     * @param id the id of the videogame to save.
     * @param videogame the videogame to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videogame,
     * or with status {@code 400 (Bad Request)} if the videogame is not valid,
     * or with status {@code 404 (Not Found)} if the videogame is not found,
     * or with status {@code 500 (Internal Server Error)} if the videogame couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/videogames/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Videogame> partialUpdateVideogame(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Videogame videogame
    ) throws URISyntaxException {
        log.debug("REST request to partial update Videogame partially : {}, {}", id, videogame);
        if (videogame.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, videogame.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!videogameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Videogame> result = videogameRepository
            .findById(videogame.getId())
            .map(existingVideogame -> {
                if (videogame.getName() != null) {
                    existingVideogame.setName(videogame.getName());
                }

                return existingVideogame;
            })
            .map(videogameRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, videogame.getId().toString())
        );
    }

    /**
     * {@code GET  /videogames} : get all the videogames.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of videogames in body.
     */
    @GetMapping("/videogames")
    public List<Videogame> getAllVideogames() {
        log.debug("REST request to get all Videogames");
        return videogameRepository.findAll();
    }

    /**
     * {@code GET  /videogames/:id} : get the "id" videogame.
     *
     * @param id the id of the videogame to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the videogame, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/videogames/{id}")
    public ResponseEntity<Videogame> getVideogame(@PathVariable Long id) {
        log.debug("REST request to get Videogame : {}", id);
        Optional<Videogame> videogame = videogameRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(videogame);
    }

    /**
     * {@code DELETE  /videogames/:id} : delete the "id" videogame.
     *
     * @param id the id of the videogame to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/videogames/{id}")
    public ResponseEntity<Void> deleteVideogame(@PathVariable Long id) {
        log.debug("REST request to delete Videogame : {}", id);
        videogameRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
