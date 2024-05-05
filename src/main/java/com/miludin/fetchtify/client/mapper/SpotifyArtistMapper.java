package com.miludin.fetchtify.client.mapper;

import com.miludin.fetchtify.client.model.SpotifyArtist;
import com.miludin.fetchtify.model.entity.Artist;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SpotifyArtistMapper {

    @Mapping(source = "externalUrl.spotify", target = "url")
    @Mapping(source = "id", target = "spotifyId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "albums", ignore = true)
    Artist mapSpotifyArtistToArtist(SpotifyArtist spotifyArtist);

    @Mapping(source = "externalUrl.spotify", target = "url")
    @Mapping(source = "id", target = "spotifyId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "albums", ignore = true)
    void mergeSpotifyArtistWithArtist(SpotifyArtist source, @MappingTarget Artist target);
}
