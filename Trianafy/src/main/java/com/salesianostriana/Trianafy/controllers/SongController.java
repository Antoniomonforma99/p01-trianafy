package com.salesianostriana.Trianafy.controllers;

import com.salesianostriana.Trianafy.models.Song;
import com.salesianostriana.Trianafy.repositories.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/songs")
public class SongController {

    private final SongRepository repository;
    private final SongDtoConverter dtoConverter;

    @GetMapping("/")
    public ResponseEntity<List<GetSongDto>> findAll(){
        List<Song> canciones = repository.findAll();
        if (canciones.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            List<GetSongDto> todas = canciones
                    .stream().map(dtoConverter::songToGetSongDto)
                    .collect(Collectors.toList());
            return ResponseEntity
                    .ok()
                    .body(todas);
        }
    }
}
