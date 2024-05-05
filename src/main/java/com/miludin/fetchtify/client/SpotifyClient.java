package com.miludin.fetchtify.client;

import com.miludin.fetchtify.client.model.SpotifyAlbum;
import com.miludin.fetchtify.client.model.SpotifyAlbumSearchResponse;
import com.miludin.fetchtify.client.model.SpotifyArtist;
import com.miludin.fetchtify.client.model.SpotifyArtistSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class SpotifyClient {

    public static final int MAX_SPOTIFY_LIMIT = 50;

    private final WebClient webClient;

    public SpotifyClient(Builder webClientBuilder, @Value("${spotify.url}") String spotifyUrl) {
        this.webClient = webClientBuilder.baseUrl(spotifyUrl).build();
    }

    public List<SpotifyArtist> getArtistsByIds(List<String> ids) {
        String uri = UriComponentsBuilder.fromPath("/artists")
                .queryParam("ids", String.join(",", ids))
                .build()
                .toUriString();

        SpotifyArtistSearchResponse response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(SpotifyArtistSearchResponse.class)
                .block();

        return response != null
                ? response.getArtists()
                : List.of();
    }

    public List<SpotifyAlbum> getAlbumsByArtistId(String artistId) {
        String uri = UriComponentsBuilder.fromPath("/artists/{id}/albums")
                .queryParam("include_groups", "album")
                .queryParam("limit", MAX_SPOTIFY_LIMIT)
                .buildAndExpand(artistId)
                .toUriString();

        SpotifyAlbumSearchResponse response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(SpotifyAlbumSearchResponse.class)
                .block();

        return response != null
                ? response.getItems()
                : List.of();
    }
}

