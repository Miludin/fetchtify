package com.miludin.fetchtify.client.mapper;

import com.miludin.fetchtify.client.model.SpotifyAlbum;
import com.miludin.fetchtify.model.entity.Album;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SpotifyAlbumMapper {

    @Mapping(source = "externalUrl.spotify", target = "url")
    @Mapping(source = "id", target = "spotifyId")
    @Mapping(target = "id", ignore = true)
    Album mapSpotifyAlbumToAlbum(SpotifyAlbum spotifyAlbum);

    List<Album> mapSpotifyAlbumToAlbum(List<SpotifyAlbum> spotifyAlbums);

    @Mapping(source = "externalUrl.spotify", target = "url")
    @Mapping(source = "id", target = "spotifyId")
    @Mapping(target = "id", ignore = true)
    void mergeSpotifyAlbumWithAlbum(SpotifyAlbum source, @MappingTarget Album target);
}
