package com.miludin.fetchtify.mapper;

import com.miludin.fetchtify.model.dto.AlbumDto;
import com.miludin.fetchtify.model.entity.Album;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AlbumMapper {

    @Mapping(target = "artist", ignore = true)
    AlbumDto mapAlbumToAlbumDto(Album album);

    List<AlbumDto> mapAlbumToAlbumDto(List<Album> album);

    @Mapping(target = "artist", ignore = true)
    Album mapAlbumDtoToAlbum(AlbumDto albumDto);

    List<Album> mapAlbumDtoToAlbum(List<AlbumDto> albumDtos);

    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "id", ignore = true)
    Album mergeAlbumDtoWithAlbum(AlbumDto source, @MappingTarget Album target);
}
