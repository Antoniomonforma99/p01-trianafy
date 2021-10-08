package com.salesianostriana.Trianafy.controllers;

import com.salesianostriana.Trianafy.models.Artist;
import com.salesianostriana.Trianafy.models.Song;
import com.salesianostriana.Trianafy.repositories.ArtistRepository;
import com.salesianostriana.Trianafy.repositories.SongRepository;
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
    private final SongRepository songRepository;

    @PostMapping("/")
    public ResponseEntity<Artist> create(@RequestBody Artist a){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repository.save(a));

    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> findOne (@PathVariable("id") Long id ){

        if (repository.findById(id) == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }else{
            return ResponseEntity.of(repository.findById(id));

        }
    }

    @PutMapping("/{id}")
    public ResponseEntity <Artist> edit (
            @RequestBody Artist a,
            @PathVariable Long id) {
        return ResponseEntity.of(
                repository.findById(id).map( m -> {
                    m.setName(a.getName());
                    repository.save(m);
                    return m;
                })
        );

    }

    @GetMapping("/")
    public ResponseEntity<List<Artist>> findAll(){
        List<Artist> todos = repository.findAll();
        if (todos.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().body(todos);
        }
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<Artist> delete (@PathVariable Long id){
        if (repository.findById(id) == null){
            return ResponseEntity.notFound().build();
        }
        else {
            List<Song> s = songRepository.findAll();
            Artist a = repository.findById(id).orElse(null);
            for (Song so : s){
                if(so.getArtist().getId().equals(repository.findById(id)));
                so.setArtist(null);
                songRepository.save(so);
                repository.deleteById(id);
            }
        }
        return ResponseEntity.noContent().build();
    }
}
