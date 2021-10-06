package com.salesianostriana.Trianafy.controllers;

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


    @GetMapping("/{id}")
    private ResponseEntity<Playlist> findOne(@PathVariable Long id) {

        return Optional
                .ofNullable(repository.findById(id))
                .map( Playlist -> ResponseEntity.ok().body(Playlist))
                .orElseGet( () -> ResponseEntity.notFound().build());
    }


    @PostMapping("/{id1}/songs/{id2}")
    public ResponseEntity<Playlist> addSong (@RequestBody Playlist p,
                                             @PathVariable("id") Long id1,
                                             @PathVariable("id") Long id2){

        if ( (repository.findById(id1) == null)
                &&( SongRepository.findById(id2) == null)){

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



    
}
