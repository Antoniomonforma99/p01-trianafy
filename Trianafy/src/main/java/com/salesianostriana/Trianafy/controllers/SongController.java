package com.salesianostriana.Trianafy.controllers;

import com.salesianostriana.Trianafy.DTOs.CreateSongDto;
import com.salesianostriana.Trianafy.DTOs.GetSongDto;
import com.salesianostriana.Trianafy.DTOs.SongDtoConverter;
import com.salesianostriana.Trianafy.models.Artist;
import com.salesianostriana.Trianafy.models.Song;
import com.salesianostriana.Trianafy.repositories.ArtistRepository;
import com.salesianostriana.Trianafy.repositories.SongRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/songs")
public class SongController {

    private final SongRepository repository;
    private final SongDtoConverter dtoConverter;
    private final ArtistRepository artistRepository;

    @GetMapping("/")
    public ResponseEntity<List<GetSongDto>> findAll() {
        List<Song> canciones = repository.findAll();
        if (canciones.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<GetSongDto> todas = canciones.stream()
                    .map(dtoConverter::songToGetSongDto)
                    .collect(Collectors.toList());
            return ResponseEntity
                    .ok()
                    .body(todas);
        }
    }

  

    @GetMapping("/{id}")
    public ResponseEntity<Song> findOne (@PathVariable("id") Long id) {
        if (repository.findById(id) == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity.of(repository.findById(id));
        }
    }
    @PostMapping("/")
        public ResponseEntity<Song> create (@RequestBody CreateSongDto dto){

            if (dto.getArtistId() == null){
                return ResponseEntity.badRequest().build();
            }
            Song nueva = dtoConverter.createSongDtoToSong(dto);
            Artist artist = artistRepository.findById(dto.getArtistId()).orElse(null);
            nueva.setArtist(artist);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(repository.save(nueva));
        }
    @PutMapping("/{id}")
    public ResponseEntity<Song> edit (@RequestBody Song so, @PathVariable("id") Long id){
        return ResponseEntity.of(repository.findById(id).map(s -> {
                    s.setTitle(so.getTitle());
                    s.setAlbum(so.getAlbum());
                    s.setYear(so.getYear());
                    repository.save(s);
                    return s;
                })
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Song> delete (@PathVariable("id") Long id) {
        repository.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
