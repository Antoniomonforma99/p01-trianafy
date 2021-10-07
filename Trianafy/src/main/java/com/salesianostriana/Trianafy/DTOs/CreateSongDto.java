package com.salesianostriana.Trianafy.DTOs;


import com.salesianostriana.Trianafy.models.Artist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CreateSongDto {

    private String title;
    private String album;
    private String year;
    private Long artistId;
}
