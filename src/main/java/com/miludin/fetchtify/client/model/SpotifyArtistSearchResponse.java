package com.miludin.fetchtify.client.model;

import lombok.Getter;

import java.util.List;

@Getter
public class SpotifyArtistSearchResponse {
    private List<SpotifyArtist> artists;
}
