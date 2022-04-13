package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Videogame;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Videogame entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideogameRepository extends JpaRepository<Videogame, Long> {}
