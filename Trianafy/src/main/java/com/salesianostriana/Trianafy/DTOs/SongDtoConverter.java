package com.salesianostriana.Trianafy.DTOs;

import com.salesianostriana.Trianafy.models.Song;
import org.springframework.stereotype.Component;

@Component
public class SongDtoConverter {



    public Song createSongDtoToSong (CreateSongDto c) {
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
        result.setArtist(s.getArtist()==null?null:s.getArtist().getName());
        result.setYear(s.getYear());

        return result;
    }
}
