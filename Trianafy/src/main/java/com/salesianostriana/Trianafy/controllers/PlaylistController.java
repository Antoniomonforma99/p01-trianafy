package com.salesianostriana.Trianafy.controllers;


import com.salesianostriana.Trianafy.DTOs.CreatePlaylistDto;
import com.salesianostriana.Trianafy.DTOs.GetPlaylistDto;
import com.salesianostriana.Trianafy.models.Playlist;
import com.salesianostriana.Trianafy.repositories.PlaylistRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.salesianostriana.Trianafy.DTOs.PlaylistDtoConverter;
import com.salesianostriana.Trianafy.models.Song;
import com.salesianostriana.Trianafy.repositories.SongRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/list")
@Tag(name = "Playlist", description = "Controlador de entidad playlist")
public class PlaylistController {

    private final PlaylistRepository repository;
    private final SongRepository songRepository;
    private final PlaylistDtoConverter dtoConverter;

    @Operation(summary = "Crea una nueva playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Se ha creado una playlist correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Error en los datos de la playlist",
                    content = @Content),
    })
    @PostMapping("/")
    public ResponseEntity<Playlist> create(@RequestBody CreatePlaylistDto dto) {

        Playlist nuevo = PlaylistDtoConverter.createPlaylistDtoToPlaylist(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(repository.save(nuevo));

    }

    @Operation(summary = "Modifica una playlist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha modificado la playlist correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "400",
                    description = "Error en los datos de la playlist",
                    content = @Content),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Playlist> edit (@RequestBody Playlist pl,@PathVariable("id") Long id){
        return ResponseEntity.of(repository.findById(id).map(p -> {
                    p.setName(pl.getName());
                    p.setDescription(pl.getDescription());
                    repository.save(p);
                    return p;
                })
            );
    }
    @Operation(summary = "Muestra todas las playlists")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han encontrado todas las playlists correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se han encontrado playlists",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<List<GetPlaylistDto>> findAll() {
        List<Playlist> playlists = repository.findAll();
        if (playlists.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            List<GetPlaylistDto> todas = playlists.stream()
                    .map(dtoConverter::playlistToGetPlaylistDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(todas);
        }
    }

    @Operation(summary = "Agrega una cancion a una playlist existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la playlist o la cancion por el ID",
                    content = @Content),
    })
    @PostMapping("/{id1}/songs/{id2}")
    public ResponseEntity<Playlist> addSong (@RequestBody Playlist p,
                                             @PathVariable Long id1,
                                             @PathVariable Long id2){
        Optional<Playlist> playlistOptional= repository.findById(id1);
        Optional<Song> songOptional= songRepository.findById(id2);
        if (songOptional.isEmpty() ||
                playlistOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Song song = songOptional.get();
            Playlist playlist = playlistOptional.get();
            playlist.getSongs().add(song);
            return ResponseEntity.ok(repository.save(playlist));
        }


    }

    @Operation(summary = "Borrado de una cancion a una playlist existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado una cancion de una playlist correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
    })
    @DeleteMapping("{id1}/songs/{id2}")
    public ResponseEntity<Playlist>deleteSong(@PathVariable Long id1, @PathVariable Long id2) {
        Optional<Playlist> playlistOptional= repository.findById(id1);
        Optional<Song> songOptional= songRepository.findById(id2);
        if (playlistOptional.isEmpty() ||
                playlistOptional.get().getSongs().contains(songOptional)) {
            return ResponseEntity.notFound().build();
        } else {
            Playlist playlist= playlistOptional.get();
            Song song = songOptional.get();
            playlist.getSongs().remove(song);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(repository.save(playlist));
        }
    }

    @Operation(summary = "Muestra una playlist existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se ha mostrado una playlist  correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "404",
                    description = "No se ha encontrado la playlist por el ID",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Playlist> findOne (@PathVariable Long id) {
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity
                    .of(repository.findById(id));
        }
    }

    @Operation(summary = "Borra una playlist existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Se ha borrado a la playlist correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Playlist> delete (@PathVariable Long id) {
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            repository.deleteById(id);

            return ResponseEntity
                    .noContent()
                    .build();
        }
    }
    @Operation(summary = "Muestra las canciones de una playlist existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han mostrado las canciones de una playlist correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha encontrado la playlist por el ID",
                    content = @Content),
    })
    @GetMapping("/{id}/songs")
    public ResponseEntity<Playlist> findAllSongs(@PathVariable Long id){
        if (repository.findById(id).isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.of(repository.findById(id));
        }
    }

    @Operation(summary = "Muestra una canció de una playlist existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Se han mostrado la canción de una playlist correctamente",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Playlist.class))}),
            @ApiResponse(responseCode = "400",
                    description = "No se ha encontrado la canción",
                    content = @Content),
    })
    @GetMapping("/{id1}/songs/{id2}")
    public ResponseEntity<Song> getSongFromPlaylist (@PathVariable Long id1,
                                                     @PathVariable Long id2) {

        Optional<Playlist> p = repository.findById(id1);
        Optional<Song> s = songRepository.findById(id2);

        if (p.isEmpty()
            || p.get().getSongs().contains(s.get())){
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity
                    .of(songRepository.findById(id2));
        }
    }
}
