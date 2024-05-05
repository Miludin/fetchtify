package com.miludin.fetchtify.client.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class SpotifyArtist extends SpotifyExternalUrl {

    private String id;
    private String name;
    private List<String> genres;

    @Setter
    private List<SpotifyAlbum> albums;
}
