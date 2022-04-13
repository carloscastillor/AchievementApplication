package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Walkthrough;
import com.mycompany.myapp.repository.WalkthroughRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Walkthrough}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WalkthroughResource {

    private final Logger log = LoggerFactory.getLogger(WalkthroughResource.class);

    private static final String ENTITY_NAME = "walkthrough";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WalkthroughRepository walkthroughRepository;

    public WalkthroughResource(WalkthroughRepository walkthroughRepository) {
        this.walkthroughRepository = walkthroughRepository;
    }

    /**
     * {@code POST  /walkthroughs} : Create a new walkthrough.
     *
     * @param walkthrough the walkthrough to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new walkthrough, or with status {@code 400 (Bad Request)} if the walkthrough has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/walkthroughs")
    public ResponseEntity<Walkthrough> createWalkthrough(@RequestBody Walkthrough walkthrough) throws URISyntaxException {
        log.debug("REST request to save Walkthrough : {}", walkthrough);
        if (walkthrough.getId() != null) {
            throw new BadRequestAlertException("A new walkthrough cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Walkthrough result = walkthroughRepository.save(walkthrough);
        return ResponseEntity
            .created(new URI("/api/walkthroughs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /walkthroughs/:id} : Updates an existing walkthrough.
     *
     * @param id the id of the walkthrough to save.
     * @param walkthrough the walkthrough to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated walkthrough,
     * or with status {@code 400 (Bad Request)} if the walkthrough is not valid,
     * or with status {@code 500 (Internal Server Error)} if the walkthrough couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/walkthroughs/{id}")
    public ResponseEntity<Walkthrough> updateWalkthrough(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Walkthrough walkthrough
    ) throws URISyntaxException {
        log.debug("REST request to update Walkthrough : {}, {}", id, walkthrough);
        if (walkthrough.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, walkthrough.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walkthroughRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Walkthrough result = walkthroughRepository.save(walkthrough);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, walkthrough.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /walkthroughs/:id} : Partial updates given fields of an existing walkthrough, field will ignore if it is null
     *
     * @param id the id of the walkthrough to save.
     * @param walkthrough the walkthrough to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated walkthrough,
     * or with status {@code 400 (Bad Request)} if the walkthrough is not valid,
     * or with status {@code 404 (Not Found)} if the walkthrough is not found,
     * or with status {@code 500 (Internal Server Error)} if the walkthrough couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/walkthroughs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Walkthrough> partialUpdateWalkthrough(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Walkthrough walkthrough
    ) throws URISyntaxException {
        log.debug("REST request to partial update Walkthrough partially : {}, {}", id, walkthrough);
        if (walkthrough.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, walkthrough.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!walkthroughRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Walkthrough> result = walkthroughRepository
            .findById(walkthrough.getId())
            .map(existingWalkthrough -> {
                if (walkthrough.getTitle() != null) {
                    existingWalkthrough.setTitle(walkthrough.getTitle());
                }
                if (walkthrough.getDescription() != null) {
                    existingWalkthrough.setDescription(walkthrough.getDescription());
                }

                return existingWalkthrough;
            })
            .map(walkthroughRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, walkthrough.getId().toString())
        );
    }

    /**
     * {@code GET  /walkthroughs} : get all the walkthroughs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of walkthroughs in body.
     */
    @GetMapping("/walkthroughs")
    public List<Walkthrough> getAllWalkthroughs() {
        log.debug("REST request to get all Walkthroughs");
        return walkthroughRepository.findAll();
    }

    /**
     * {@code GET  /walkthroughs/:id} : get the "id" walkthrough.
     *
     * @param id the id of the walkthrough to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the walkthrough, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/walkthroughs/{id}")
    public ResponseEntity<Walkthrough> getWalkthrough(@PathVariable Long id) {
        log.debug("REST request to get Walkthrough : {}", id);
        Optional<Walkthrough> walkthrough = walkthroughRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(walkthrough);
    }

    /**
     * {@code DELETE  /walkthroughs/:id} : delete the "id" walkthrough.
     *
     * @param id the id of the walkthrough to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/walkthroughs/{id}")
    public ResponseEntity<Void> deleteWalkthrough(@PathVariable Long id) {
        log.debug("REST request to delete Walkthrough : {}", id);
        walkthroughRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
