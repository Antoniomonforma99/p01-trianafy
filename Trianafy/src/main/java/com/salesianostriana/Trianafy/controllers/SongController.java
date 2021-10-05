package com.salesianostriana.Trianafy.controllers;


import com.salesianostriana.Trianafy.models.Song;
import com.salesianostriana.Trianafy.repositories.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/song")
public class SongController {

    private final SongRepository repository;

    @GetMapping("/")
    public ResponseEntity<List<GetSongDto>> findAll() {

        List<Song> data = repository.findAll();

        if (data.isEmpty()) {
            return ResponseEntity.n
        }

    }
}
