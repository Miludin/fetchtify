package com.miludin.fetchtify.mapper;

import com.miludin.fetchtify.model.dto.ArtistDto;
import com.miludin.fetchtify.model.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ArtistMapper {

    @Mapping(target = "albums", ignore = true)
    ArtistDto mapArtistToArtistDto(Artist artist);

    @Mapping(target = "albums", ignore = true)
    Artist mapArtistDtoToArtist(ArtistDto artistDto);

    @Mapping(target = "albums", ignore = true)
    @Mapping(target = "id", ignore = true)
    Artist mergeArtistDtoWithArtist(ArtistDto source, @MappingTarget Artist target);
}
