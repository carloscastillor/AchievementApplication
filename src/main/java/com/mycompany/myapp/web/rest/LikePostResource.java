package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LikePost;
import com.mycompany.myapp.repository.LikePostRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LikePost}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LikePostResource {

    private final Logger log = LoggerFactory.getLogger(LikePostResource.class);

    private static final String ENTITY_NAME = "likePost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LikePostRepository likePostRepository;

    public LikePostResource(LikePostRepository likePostRepository) {
        this.likePostRepository = likePostRepository;
    }

    /**
     * {@code POST  /like-posts} : Create a new likePost.
     *
     * @param likePost the likePost to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new likePost, or with status {@code 400 (Bad Request)} if the likePost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/like-posts")
    public ResponseEntity<LikePost> createLikePost(@RequestBody LikePost likePost) throws URISyntaxException {
        log.debug("REST request to save LikePost : {}", likePost);
        if (likePost.getId() != null) {
            throw new BadRequestAlertException("A new likePost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LikePost result = likePostRepository.save(likePost);
        return ResponseEntity
            .created(new URI("/api/like-posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /like-posts/:id} : Updates an existing likePost.
     *
     * @param id the id of the likePost to save.
     * @param likePost the likePost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likePost,
     * or with status {@code 400 (Bad Request)} if the likePost is not valid,
     * or with status {@code 500 (Internal Server Error)} if the likePost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/like-posts/{id}")
    public ResponseEntity<LikePost> updateLikePost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LikePost likePost
    ) throws URISyntaxException {
        log.debug("REST request to update LikePost : {}, {}", id, likePost);
        if (likePost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likePost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likePostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LikePost result = likePostRepository.save(likePost);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likePost.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /like-posts/:id} : Partial updates given fields of an existing likePost, field will ignore if it is null
     *
     * @param id the id of the likePost to save.
     * @param likePost the likePost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated likePost,
     * or with status {@code 400 (Bad Request)} if the likePost is not valid,
     * or with status {@code 404 (Not Found)} if the likePost is not found,
     * or with status {@code 500 (Internal Server Error)} if the likePost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/like-posts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LikePost> partialUpdateLikePost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LikePost likePost
    ) throws URISyntaxException {
        log.debug("REST request to partial update LikePost partially : {}, {}", id, likePost);
        if (likePost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, likePost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!likePostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LikePost> result = likePostRepository
            .findById(likePost.getId())
            .map(existingLikePost -> {
                if (likePost.getLike() != null) {
                    existingLikePost.setLike(likePost.getLike());
                }

                return existingLikePost;
            })
            .map(likePostRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, likePost.getId().toString())
        );
    }

    /**
     * {@code GET  /like-posts} : get all the likePosts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of likePosts in body.
     */
    @GetMapping("/like-posts")
    public List<LikePost> getAllLikePosts() {
        log.debug("REST request to get all LikePosts");
        return likePostRepository.findAll();
    }

    /**
     * {@code GET  /like-posts/:id} : get the "id" likePost.
     *
     * @param id the id of the likePost to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the likePost, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/like-posts/{id}")
    public ResponseEntity<LikePost> getLikePost(@PathVariable Long id) {
        log.debug("REST request to get LikePost : {}", id);
        Optional<LikePost> likePost = likePostRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(likePost);
    }

    /**
     * {@code DELETE  /like-posts/:id} : delete the "id" likePost.
     *
     * @param id the id of the likePost to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/like-posts/{id}")
    public ResponseEntity<Void> deleteLikePost(@PathVariable Long id) {
        log.debug("REST request to delete LikePost : {}", id);
        likePostRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
