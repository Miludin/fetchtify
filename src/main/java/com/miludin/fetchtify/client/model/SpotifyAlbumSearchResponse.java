package com.miludin.fetchtify.client.model;

import lombok.Getter;

import java.util.List;

@Getter
public class SpotifyAlbumSearchResponse {
    private List<SpotifyAlbum> items;
}
