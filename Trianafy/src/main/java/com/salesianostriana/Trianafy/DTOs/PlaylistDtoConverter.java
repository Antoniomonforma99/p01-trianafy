package com.salesianostriana.Trianafy.DTOs;

import com.salesianostriana.Trianafy.models.Playlist;
import org.springframework.stereotype.Component;

@Component
public class PlaylistDtoConverter {

    public static Playlist createPlaylistDtoToPlaylist(CreatePlaylistDto c) {
        return new Playlist(
                c.getName(),
                c.getDescription()
        );
    }

    public GetPlaylistDto playlistToGetPlaylistDto(Playlist p) {

        GetPlaylistDto result = new GetPlaylistDto();
        result.setName(p.getName());
        result.setDescription(p.getDescription());
        result.setNumberOfSongs(p.getSongs().size());
        return result;
    }

}