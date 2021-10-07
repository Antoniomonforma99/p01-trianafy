package com.salesianostriana.Trianafy.controllers;

import com.salesianostriana.Trianafy.DTOs.CreateSongDto;
import com.salesianostriana.Trianafy.DTOs.GetSongDto;
import com.salesianostriana.Trianafy.DTOs.SongDtoConverter;
import com.salesianostriana.Trianafy.models.Artist;
import com.salesianostriana.Trianafy.models.Song;
import com.salesianostriana.Trianafy.repositories.ArtistRepository;
import com.salesianostriana.Trianafy.repositories.SongRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Mostrar todas las canciones de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devuelve todas las canciones",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class)) }),
            @ApiResponse(responseCode = "404", description = "Canciones no encontradas",
                    content = @Content) })
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


    @Operation(summary = "Mostrar una canción por la id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se muestra la canción",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class))}),
            @ApiResponse(responseCode = "404", description = "Si no se encuentra manda un notFound",
                    content = @Content)
    })
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
    @Operation(summary = "Crear una cancion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Se crea la canción",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Song.class))}),
            @ApiResponse(responseCode = "400", description = "Si se introduce mal un dato manda BadRequest",
                    content = @Content)
    })
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
    @Operation(summary = "Editar un canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edita la canción",
            content = {@Content(mediaType = "application/json",
            schema = @Schema(implementation = Song.class))
            }),
            @ApiResponse(responseCode = "400", description = "Algún dato se envia mal",
            content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontra la id de la canción",
            content = @Content)
    })
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

    @Operation(summary = "Eliminar una canción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Se elimina la canción",
            content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encuentra la id de la canción",
            content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Song> delete (@PathVariable("id") Long id) {
        if (repository.findById(id) == null){
        return ResponseEntity.notFound().build();
        }  
      else{
        
      repository.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
    }
    }
}
