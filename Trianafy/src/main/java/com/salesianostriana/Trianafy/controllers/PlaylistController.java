package com.salesianostriana.Trianafy.controllers;

import com.salesianostriana.Trianafy.DTOs.GetPlaylistDto;
import com.salesianostriana.Trianafy.models.Playlist;
import com.salesianostriana.Trianafy.repositories.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor

@RequestMapping("/list")
public class PlaylistController {

    private final PlaylistRepository repository;
    private final SongRepository songRepository;
    private final PlaylistDtoConverter dtoConverter;


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

    @GetMapping("/")
    public ResponseEntity<List<GetPlaylistDto>> findAll() {
        List<Playlist> playlists = repository.findAll();
        if (playlists.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            List<GetPlaylistDto> todas = playlists.stream()
                    .map(dtoConverter::playlistToGetPlaylistDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(todas);
        }
    }

    @PostMapping("/{id1}/songs/{id2}")
    public ResponseEntity<Playlist> addSong (@RequestBody Playlist p,
                                             @PathVariable("id") Long id1,
                                             @PathVariable("id") Long id2){

        if ( (repository.findById(id1) == null)
                || ( songRepository.findById(id2) == null)){

            return ResponseEntity.badRequest().build();

        }else{

            Song song = songRepository.getById(id2);
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

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> findOne (@PathVariable Long id) {
        if (!repository.findById(id).isPresent()) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity
                    .of(repository.findById(id));
        }
    }

    @GetMapping("/{id}/songs/{id2}")
    public ResponseEntity<Song>findOneSongFromPlaylist(@PathVariable Long id1,
                                                 @PathVariable Long id2) {
        if (!repository.findById(id1).isPresent() ||
                !repository.findById(id1).get().getSongs().contains(songRepository.findById(id2))) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity
                    .of(songRepository.findById(id2));
        }
    }
}
