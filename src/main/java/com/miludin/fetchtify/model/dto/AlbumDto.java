package com.miludin.fetchtify.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AlbumDto {

    private UUID id;
    private String name;
    private String releaseDate;
    private String url;
    private String spotifyId;
    private int totalTracks;

    @JsonInclude(Include.NON_NULL)
    private UUID artistId;

    @JsonInclude(Include.NON_NULL)
    private ArtistDto artist;
}
