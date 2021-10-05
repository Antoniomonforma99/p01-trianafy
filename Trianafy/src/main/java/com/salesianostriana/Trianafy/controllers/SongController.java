package com.salesianostriana.Trianafy.controllers;

import com.salesianostriana.Trianafy.DTOs.GetSongDto;
import com.salesianostriana.Trianafy.DTOs.SongDtoConverter;
import com.salesianostriana.Trianafy.models.Song;
import com.salesianostriana.Trianafy.repositories.SongRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/songs")
public class SongController {

    private final SongRepository repository;
    private final SongDtoConverter dtoConverter;

    @GetMapping("/")
    public ResponseEntity<List<GetSongDto>> findAll() {
        List<Song> canciones = repository.findAll();
        if (canciones.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<GetSongDto> todas = canciones
                    .stream()
                    .map(dtoConverter::songToGetSongDto)
                    .collect(Collectors.toList());
            return ResponseEntity
                    .ok()
                    .body(todas);
        }
    }@PutMapping("/{id}")
    public ResponseEntity<Song> edit (@RequestBody Song so, @PathVariable Long id){
        return ResponseEntity.of(repository.findById(id).map(s -> {
                    s.setTitle(so.getTitle());
                    s.setAlbum(so.getAlbum());
                    s.setYear(so.getYear());
                    repository.save(s);
                    return s;
                })
        );
    }

}