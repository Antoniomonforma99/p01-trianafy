package com.salesianostriana.Trianafy.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistController {

    private final ArtistRepository repository;


    @GetMapping("/{id}")
    public ResponseEntity<Artist> findOne (@PathVariable("id") Long id ){
        return ResponseEntity
                .of(repository.findById(id));
    }







}
