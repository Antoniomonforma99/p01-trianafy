package com.salesianostriana.Trianafy.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

    @ManyToOne
    private Artist artist;

}

