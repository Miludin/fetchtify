package com.miludin.fetchtify.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ArtistDto {

    private UUID id;
    private String name;
    private String url;
    private String spotifyId;
    private List<String> genres;

    @JsonInclude(Include.NON_NULL)
    private List<AlbumDto> albums;
}
