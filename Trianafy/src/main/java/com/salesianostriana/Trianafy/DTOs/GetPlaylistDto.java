package com.salesianostriana.Trianafy.DTOs;

import com.salesianostriana.Trianafy.models.Song;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetPlaylistDto {

    private String name;

    private String description;


}
