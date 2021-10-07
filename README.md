# TrianaFy
## _Proyecto 2ª DAM (Acceso a datos / Programación de servicios y procesos)_

[![N|Solid](logo.png)](https://triana.salesianos.edu/colegio//nsolid)



Este proyecto ha sido desarrollado por Alejandro Martín,Antonio Montero y Guillermo De la cruz.
Programas empleados:

- API: IntelliJ idea
- Postman
- VisualStudio code

>## Finalidad
La finalidad de este proyecto es:
- Desarrollar componentes y aplicaciones que acceden o producen información en formato JSON.
- Desarrollar servicios web sencillos utilizando la arquitectura REST,
siguiendo sus principios en cuanto a estructura de rutas y códigos de
respuesta.

>## Funcionalidad
  Crear una aplicación al estilo spotify.
  Realizando los CRUD de las distintas clases aplicando cambios en la forma de mostrar su estructura a través de Dtos.


>## Estructura de paquetes
| Paquete | URL |
| ------ | ------ |
| Controllers | [Trianafy/Controllers]([Trianafy/controllers](https://github.com/Antoniomonforma99/p01-trianafy/tree/main/Trianafy/src/main/java/com/salesianostriana/Trianafy/controllers)) |
| DTOs | [Trianafy/DTOs]([Trianafy/controllers](https://github.com/Antoniomonforma99/p01-trianafy/tree/main/Trianafy/src/main/java/com/salesianostriana/Trianafy/DTOs)) |
| Models | [Trianafy/Models]([Trianafy/controllers](https://github.com/Antoniomonforma99/p01-trianafy/tree/main/Trianafy/src/main/java/com/salesianostriana/Trianafy/models)) |
| Repositories | [Trianafy/Repositories]([Trianafy/controllers](https://github.com/Antoniomonforma99/p01-trianafy/tree/main/Trianafy/src/main/java/com/salesianostriana/Trianafy/repositories)) 

>## Entidades
  Crear una aplicación al estilo spotify.
  Contamos con 3 entidades que son:
  - Artist
  - Song 
  - Playlist
  
>## Asociaciones
### ManytoOne (Song -> Artist)
```sh
@ManyToOne
    private Artist artist;
```

### ManytoMany (Playlist -> Song)
```sh
@ManyToMany(fetch= FetchType.EAGER)
    private List<Song> songs;
```


### Models
Los atributos y anotaciones de las clases modelos son los siguientes:

- Songs
  ```sh
    @Entity
    @NoArgsConstructor
    @Data
    public class Song {


        @Id
        @GeneratedValue
        private Long id;

        private String title;

        private String album;

        private String year;

  ```
- Playlist
    ```sh
        @Data
    @NoArgsConstructor
    @ToString
    @EqualsAndHashCode
    public class Playlist {

        @Id
        @GeneratedValue
        private Long id;

        private String name;

        @Lob
        private String description;
  
  ```
- Artist
  ```sh
    @Entity
    @Data @AllArgsConstructor @NoArgsConstructor
    public class Artist {

        @GeneratedValue
        @Id
        private Long id;

        private String name;
  
  ```

### Controllers
- Controladores asociados a la entidad playlist
  ```sh
        @PostMapping("/")
        public ResponseEntity<Playlist> create(@RequestBody CreatePlaylistDto dto) {

            Playlist nuevo = PlaylistDtoConverter.createPlaylistDtoToPlaylist(dto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(repository.save(nuevo));

        }

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

        
        @PostMapping("/{id1}/songs/{id2}")
        public ResponseEntity<Playlist> addSong (@RequestBody Playlist p,
                                                @PathVariable Long id1,
                                                @PathVariable Long id2){
            List<Song> lista = repository.getById(id1).getSongs();
            if (repository.findById(id1).isEmpty() ||
                    !repository.findById(id1).get().getSongs().contains(SongRepository.getById(id2))) {
                return ResponseEntity.notFound().build();
            } else {
                repository
                        .findById(id1)
                        .get()
                        .getSongs().add(SongRepository.getById(id2));
                return ResponseEntity.noContent().build();
            }


        }

        @DeleteMapping("{id1}/songs/{id2}")
        public ResponseEntity<Playlist>deleteSong(@PathVariable Long id1, @PathVariable Long id2) {
            List<Song> lista = repository.getById(id1).getSongs();
            if (repository.findById(id1).isEmpty() ||
                    !repository.findById(id1).get().getSongs().contains(SongRepository.getById(id2))) {
                return ResponseEntity.notFound().build();
            } else {
                repository
                        .findById(id1)
                        .get()
                        .getSongs().remove(SongRepository.getById(id2));
                return ResponseEntity.noContent().build();
            }
        }

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
        
        @GetMapping("/{id}/songs")
        public ResponseEntity<Playlist> findAllSongs(@PathVariable Long id){
            if (repository.findById(id).isEmpty()){
                return ResponseEntity.notFound().build();
            }
            else {
                return ResponseEntity.of(repository.findById(id));
            }
        }
- Controladores asociados a la entidad Song
  ```sh
    
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

    @GetMapping("/{id}")
    public ResponseEntity<Song> findOne (@PathVariable("id") Long id) {
        if (repository.findById(id).isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        } else {
            return ResponseEntity.of(repository.findById(id));
        }
    }
    
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Song> delete (@PathVariable("id") Long id) {
        if (repository.findById(id).isEmpty()){
        return ResponseEntity.notFound().build();
        }  
      else{
        
      repository.deleteById(id);

        return ResponseEntity
                .noContent()
                .build();
      }
    }
- Controladores asociados a la entidad Artist
  ```sh  
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

    public ResponseEntity<Artist> delete (@PathVariable("id") Long id){
        if (repository.findById(id) == null){
            return ResponseEntity.notFound().build();
        }
        else {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
### DTOs
Para la representación de las entidades usaremos el patrón de diseño DTO:
- Creamos una clase de la entidad:
     ```sh 
    @Data @AllArgsConstructor @NoArgsConstructor
    public class CreateSongDto {

    private String title;
    private String album;
    private String year;
    private Long artistId;
    }
    ```
- Creamos otra clase con el patrón de deseemos que tenga la entidad:
  ```sh 
    @Data
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public class GetSongDto {

        private String title;
        private String album;
        private String year;
        private String artist;
        }
    ```
- Creamos una ultima clase donde convertimos la entidad en un objeto con el patrón de diseño deseado:
    ```sh 
    @Component
    public class SongDtoConverter {



        public Song createSongDtoToSong           (CreateSongDto c) {
            /*
            Devolvemos un objeto del tipo canción con las propiedades de su DTO
            */
            return new Song(
                    c.getTitle(),
                    c.getAlbum(),
                    c.getYear()
            );
        }

        public GetSongDto songToGetSongDto(Song s){
            /*
            Creamos un DTO vacío para luego asignarle las propiedades del objeto
            */
            GetSongDto result = new GetSongDto();

            result.setTitle(s.getTitle());
            result.setAlbum(s.getAlbum());
            result.setArtist(s.getArtist().getName());
            result.setYear(s.getYear());

            return result;
        }
    }
    ```

