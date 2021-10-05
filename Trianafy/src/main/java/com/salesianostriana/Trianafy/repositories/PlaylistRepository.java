package com.salesianostriana.Trianafy.repositories;

import com.salesianostriana.Trianafy.models.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist,Long> {
}
