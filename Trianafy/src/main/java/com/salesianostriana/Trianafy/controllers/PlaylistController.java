package com.salesianostriana.Trianafy.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.salesianostriana.Trianafy.DTOs.CreateSongDto;
import com.salesianostriana.Trianafy.DTOs.PlaylistDtoConverter;
import com.salesianostriana.Trianafy.DTOs.SongDtoConverter;
import com.salesianostriana.Trianafy.models.Playlist;
import com.salesianostriana.Trianafy.models.Song;
import com.salesianostriana.Trianafy.repositories.PlaylistRepository;
import com.salesianostriana.Trianafy.repositories.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor

@RequestMapping("/list")
public class PlaylistController {

    private final PlaylistRepository repository;
    private final SongRepository SongRepository;



    @PostMapping("/{id1}/songs/{id2}")
    public ResponseEntity<Playlist> addSong (@RequestBody Playlist p,
                                             @PathVariable("id") Long id1,
                                             @PathVariable("id") Long id2){

        if ( (repository.findById(id1) == null)
                || ( SongRepository.findById(id2) == null)){

            return ResponseEntity.badRequest().build();

        }else{

            Song song = SongRepository.getById(id2);
            p.getSongs().add(song);

            return ResponseEntity.of(
                    repository.findById(id1).map(m -> {
                        m.setName(p.getName());
                        m.setDescription(p.getDescription());
                        m.setSongs(p.getSongs());
                        repository.save(m);
                        return m;
                    })
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Playlist> delete (@PathVariable Long id) {
        if (!repository.findById(id).isPresent()) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            repository.deleteById(id);

            return ResponseEntity
                    .noContent()
                    .build();
        }


    }
}
