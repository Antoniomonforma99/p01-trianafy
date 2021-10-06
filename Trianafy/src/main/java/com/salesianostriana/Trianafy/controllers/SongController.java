package com.salesianostriana.Trianafy.controllers;

import com.salesianostriana.Trianafy.DTOs.CreateSongDto;
import com.salesianostriana.Trianafy.DTOs.GetSongDto;
import com.salesianostriana.Trianafy.DTOs.SongDtoConverter;
import com.salesianostriana.Trianafy.models.Song;
import com.salesianostriana.Trianafy.repositories.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
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

    @GetMapping("/{id}")
    public ResponseEntity<Song> findOne (@PathVariable Long id) {
        if (repository.findById(id) == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity.of(repository.findById(id));
        }
    }

/*    @GetMapping("/{id}")
    public ResponseEntity<Song> findOne(@PathVariable Long id) {

        if (repository.findById(id) == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {

            GetSongDto result = new CreateSongDto(repository.getById(id));

            return result;

        }

    }*/
}
