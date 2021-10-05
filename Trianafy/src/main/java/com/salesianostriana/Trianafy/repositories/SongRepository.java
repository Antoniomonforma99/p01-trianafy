package com.salesianostriana.Trianafy.repositories;


import com.salesianostriana.Trianafy.models.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}
