package com.miludin.fetchtify.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Album {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String name;

    @Column
    private String releaseDate;

    @Column
    private String url;

    @Column
    private String spotifyId;

    @Column
    private int totalTracks;

    @Column
    private boolean isModified;

    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    private Artist artist;
}
