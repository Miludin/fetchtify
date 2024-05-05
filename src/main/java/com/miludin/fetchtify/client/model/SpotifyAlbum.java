package com.miludin.fetchtify.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SpotifyAlbum extends SpotifyExternalUrl {

    private String id;
    private String name;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("total_tracks")
    private int totalTracks;
}
