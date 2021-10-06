package com.salesianostriana.Trianafy.models;

import lombok.*;
import org.hibernate.engine.profile.Fetch;

import javax.persistence.*;
import java.util.List;

@Entity
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

    @ManyToMany(fetch= FetchType.EAGER)
    @ElementCollection
    private List<Song> songs;

    public Playlist(String name, String description, List<Song> songs) {
        this.name = name;
        this.description = description;
        this.songs = songs;
    }

    public Playlist(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
