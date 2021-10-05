package com.salesianostriana.Trianafy.repositories;

import com.salesianostriana.Trianafy.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
