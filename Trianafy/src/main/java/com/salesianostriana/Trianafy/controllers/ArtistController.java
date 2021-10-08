package com.salesianostriana.Trianafy.controllers;

import com.salesianostriana.Trianafy.models.Artist;
import com.salesianostriana.Trianafy.repositories.ArtistRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Table;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
@Tag(name = "Artist", description = "Controlador de entidad Artista")
public class ArtistController {

    private final ArtistRepository repository;


    @Operation(summary = "Crea un nuevo artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                description = "Se ha creado un artista correctamente",
                content = {
                    @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Artist.class))
                }),
            @ApiResponse(responseCode = "400",
                description = "Error en los datos del artista",
                content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<Artist> create(@RequestBody Artist a){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repository.save(a));

    }


    @Operation(summary = "Muestra un artista buscado por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha mostrado un artista correctamente",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Artist.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado al artista",
                    content = @Content),
    })
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



    @Operation(summary = "Edita un artista mediante el id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha modificado un artista correctamente",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Artist.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Error en los datos del artista",
                    content = @Content),
    })
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



    @Operation(summary = "Muestra todos los artistas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se han mostrado todos los artistas correctamente",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Artist.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "No se han encontrado artistas",
                    content = @Content),
    })
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



    @Operation(summary = "Elimina un artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha creado un artista correctamente",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Artist.class))
                    }),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Artist> delete (@PathVariable("id") Long id){
        if (repository.findById(id) == null){
            return ResponseEntity.notFound().build();
        }
        else {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}
