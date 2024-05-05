package com.miludin.fetchtify.scheduler;

import com.miludin.fetchtify.client.SpotifyClient;
import com.miludin.fetchtify.client.mapper.SpotifyAlbumMapper;
import com.miludin.fetchtify.client.mapper.SpotifyArtistMapper;
import com.miludin.fetchtify.client.model.SpotifyAlbum;
import com.miludin.fetchtify.client.model.SpotifyArtist;
import com.miludin.fetchtify.model.entity.Album;
import com.miludin.fetchtify.model.entity.Artist;
import com.miludin.fetchtify.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class SpotifySyncScheduler {

    private static final long ONE_MINUTE = 60000;

    private final SpotifyClient spotifyClient;

    private final ArtistRepository artistRepository;

    private final SpotifyArtistMapper spotifyArtistMapper;
    private final SpotifyAlbumMapper spotifyAlbumMapper;

    @Value("${spotify.artist.ids}")
    private List<String> fetchedSpotifyArtistIds;

    @Transactional
    @Scheduled(fixedDelay = ONE_MINUTE)
    public void fetchArtistsAndAlbums() {
        artistRepository.saveAll(createOrUpdateArtists());
    }

    private List<Artist> createOrUpdateArtists() {
        List<SpotifyArtist> spotifyArtists = getSpotifyArtists();

        Map<String, Artist> spotifyIdToArtistInDb = artistRepository.findAllBySpotifyIdIn(fetchedSpotifyArtistIds)
                .stream()
                .collect(Collectors.toMap(Artist::getSpotifyId, Function.identity()));

        List<Artist> artistsToSaveOrUpdate = new ArrayList<>();

        for (SpotifyArtist spotifyArtist : spotifyArtists) {
            String spotifyArtistId = spotifyArtist.getId();
            if (spotifyIdToArtistInDb.containsKey(spotifyArtistId)) {
                Artist artistInDb = spotifyIdToArtistInDb.get(spotifyArtistId);
                updateArtist(spotifyArtist, artistInDb);
                artistsToSaveOrUpdate.add(artistInDb);
            } else {
                artistsToSaveOrUpdate.add(createArtist(spotifyArtist));
            }
        }
        return artistsToSaveOrUpdate;
    }

    private List<SpotifyArtist> getSpotifyArtists() {
        List<SpotifyArtist> spotifyArtists = spotifyClient.getArtistsByIds(fetchedSpotifyArtistIds);
        for (SpotifyArtist spotifyArtist : spotifyArtists) {
            List<SpotifyAlbum> albums = spotifyClient.getAlbumsByArtistId(spotifyArtist.getId());
            spotifyArtist.setAlbums(albums);
        }
        return spotifyArtists;
    }

    private void updateArtist(SpotifyArtist spotifyArtist, Artist artistInDb) {
        if (!artistInDb.isModified()) {
            spotifyArtistMapper.mergeSpotifyArtistWithArtist(spotifyArtist, artistInDb);
        }
        createOrUpdateAlbums(spotifyArtist, artistInDb);
    }

    private Artist createArtist(SpotifyArtist spotifyArtist) {
        Artist artistToSave = spotifyArtistMapper.mapSpotifyArtistToArtist(spotifyArtist);
        artistToSave.setAlbums(spotifyAlbumMapper.mapSpotifyAlbumToAlbum(spotifyArtist.getAlbums()));
        artistToSave.getAlbums().forEach(album -> album.setArtist(artistToSave));
        return artistToSave;
    }

    private void createOrUpdateAlbums(SpotifyArtist spotifyArtist, Artist artistInDb) {
        Map<String, Album> spotifyIdToAlbumInDb = artistInDb.getAlbums()
                .stream()
                .collect(Collectors.toMap(Album::getSpotifyId, Function.identity()));

        for (SpotifyAlbum spotifyAlbum : spotifyArtist.getAlbums()) {
            String spotifyAlbumId = spotifyAlbum.getId();
            if (spotifyIdToAlbumInDb.containsKey(spotifyAlbumId)) {
                Album albumInDb = spotifyIdToAlbumInDb.get(spotifyAlbumId);
                if (!albumInDb.isModified()) {
                    spotifyAlbumMapper.mergeSpotifyAlbumWithAlbum(spotifyAlbum, albumInDb);
                }
            } else {
                Album albumToSave = spotifyAlbumMapper.mapSpotifyAlbumToAlbum(spotifyAlbum);
                albumToSave.setArtist(artistInDb);
                artistInDb.getAlbums().add(albumToSave);
            }
        }
    }
}
