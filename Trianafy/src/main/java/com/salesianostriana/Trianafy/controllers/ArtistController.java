package com.salesianostriana.Trianafy.controllers;

import com.salesianostriana.Trianafy.models.Artist;
import com.salesianostriana.Trianafy.repositories.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistRepository repository;


    
    @GetMapping("/{id}")
    public ResponseEntity<List<Artist>> findOne (@PathVariable("id") Long id ){

        if (repository.findById(id) == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }else{
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(repository.findById(id));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity <Artist> edit (
            @RequestBody Artist a,
            @PathVariable Long id) {
        return ResponseEntity.of(
                repository.findById(id).map( m -> {
                    m.setName(a.getName());
                    return m;
                })
        );

    }







}
