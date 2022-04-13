package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LikeMessage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LikeMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LikeMessageRepository extends JpaRepository<LikeMessage, Long> {}
