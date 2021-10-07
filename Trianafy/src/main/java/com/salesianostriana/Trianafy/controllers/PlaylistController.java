package com.salesianostriana.Trianafy.controllers;

import com.salesianostriana.Trianafy.DTOs.CreatePlaylistDto;
import com.salesianostriana.Trianafy.models.Playlist;
import com.salesianostriana.Trianafy.repositories.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.salesianostriana.Trianafy.DTOs.PlaylistDtoConverter;
import com.salesianostriana.Trianafy.models.Song;
import com.salesianostriana.Trianafy.repositories.SongRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor

@RequestMapping("/list")
public class PlaylistController {

    private final PlaylistRepository repository;
    private final SongRepository SongRepository;

    @PostMapping("/")
    public ResponseEntity<Playlist> create(@RequestBody CreatePlaylistDto dto) {

        Playlist nuevo = PlaylistDtoConverter.createPlaylistDtoToPlaylist(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repository.save(nuevo));

    }


    @PutMapping("/{id}")
    public ResponseEntity<Playlist> edit (@RequestBody Playlist pl,@PathVariable("id") Long id){
        return ResponseEntity.of(repository.findById(id).map(p -> {
                    p.setName(pl.getName());
                    p.setDescription(pl.getDescription());
                    repository.save(p);
                    return p;
                })
            );
    }

    @PostMapping("/{id1}/songs/{id2}")
    public ResponseEntity<Playlist> addSong (@RequestBody Playlist p,
                                             @PathVariable Long id1,
                                             @PathVariable Long id2){
        List<Song> cOld = repository.getById(id1).getSongs();
        Optional<Playlist> pl =repository.findById(id1);
        Optional<Song> s =SongRepository.findById(id2);

        if  (pl.isEmpty()
                ||s.isEmpty()){

            return ResponseEntity.notFound().build();

        }else{
            cOld.add(SongRepository.getById(id2));
            //Song song = SongRepository.getById(id2);
            //p.getSongs().add(song);

            return ResponseEntity.of(
                    repository.findById(id1).map(m -> {
                        m.setName(m.getName());
                        m.setDescription(m.getDescription());
                        m.setSongs(cOld);
                        repository.save(m);
                        return m;
                    })
            );
        }
    }

    @DeleteMapping("{id1}/songs/{id2}")
    public ResponseEntity<Playlist>deleteSong(@PathVariable Long id1, @PathVariable Long id2) {
        List<Song> lista = repository.getById(id1).getSongs();
        if (repository.findById(id1).isEmpty() ||
                !repository.findById(id1).get().getSongs().contains(SongRepository.getById(id2))) {
            return ResponseEntity.notFound().build();
        } else {
            repository
                    .findById(id1)
                    .get()
                    .getSongs().remove(SongRepository.getById(id2));
            return ResponseEntity.noContent().build();
        }
    }
    }
