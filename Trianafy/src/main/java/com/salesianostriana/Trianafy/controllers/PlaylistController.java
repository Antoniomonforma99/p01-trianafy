package com.salesianostriana.Trianafy.controllers;

import com.salesianostriana.Trianafy.models.Playlist;
import com.salesianostriana.Trianafy.repositories.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/playlist")
public class PlaylistController {

    private final PlaylistRepository repository;


    @GetMapping("/{id}")
    private ResponseEntity<Playlist> findOne(@PathVariable Long id) {

        return Optional
                .ofNullable(repository.findById(id))
                .map( Playlist -> ResponseEntity.ok().body(Playlist))
                .orElseGet( () -> ResponseEntity.notFound().build());
    }


}
