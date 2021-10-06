package com.salesianostriana.Trianafy.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlaylistDto {

    private String name;

    private String description;

    private List<Long> songsId;

}