package com.miludin.fetchtify.model.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Artist {

    @Id
    @GeneratedValue
    private UUID id;

    @Column
    private String name;

    @Column
    private String url;

    @Column
    private String spotifyId;

    @Column
    private boolean isModified;

    @Type(ListArrayType.class)
    @Column
    private List<String> genres;

    @OneToMany(
            mappedBy = "artist",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<Album> albums;
}
