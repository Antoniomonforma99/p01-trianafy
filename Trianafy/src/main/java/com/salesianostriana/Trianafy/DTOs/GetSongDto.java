package com.salesianostriana.Trianafy.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GetSongDto {

    private String title;
    private String album;
    private String year;
    private String artist;
}
