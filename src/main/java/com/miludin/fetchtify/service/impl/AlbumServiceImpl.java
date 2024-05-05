package com.miludin.fetchtify.service.impl;

import com.miludin.fetchtify.mapper.AlbumMapper;
import com.miludin.fetchtify.mapper.ArtistMapper;
import com.miludin.fetchtify.model.dto.AlbumDto;
import com.miludin.fetchtify.model.entity.Album;
import com.miludin.fetchtify.model.entity.Artist;
import com.miludin.fetchtify.repository.AlbumRepository;
import com.miludin.fetchtify.repository.ArtistRepository;
import com.miludin.fetchtify.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    private final AlbumMapper albumMapper;
    private final ArtistMapper artistMapper;

    @Override
    public List<AlbumDto> getAll() {
        return albumRepository.findAll().stream()
                .map(this::mapAlbumWithArtistToAlbumDto)
                .toList();
    }

    @Override
    public List<AlbumDto> getByFullTextSearch(String name) {
        return albumRepository.findByFullTextSearch(name).stream()
                .map(this::mapAlbumWithArtistToAlbumDto)
                .toList();
    }

    @Override
    public Optional<AlbumDto> getById(UUID id) {
        return albumRepository.findById(id)
                .map(this::mapAlbumWithArtistToAlbumDto);
    }

    @Override
    public AlbumDto create(AlbumDto albumDto) {
        Artist artist = artistRepository.findById(albumDto.getArtistId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Artist by id '%s' not found".formatted(albumDto.getArtistId())
                ));

        Album album = albumMapper.mapAlbumDtoToAlbum(albumDto);
        album.setArtist(artist);

        return mapAlbumWithArtistToAlbumDto(albumRepository.save(album));
    }

    @Override
    public Optional<AlbumDto> update(UUID id, AlbumDto albumDto) {
        return albumRepository.findById(id)
                .map(album -> mergeAlbums(albumDto, album))
                .map(albumRepository::save)
                .map(this::mapAlbumWithArtistToAlbumDto);
    }

    private AlbumDto mapAlbumWithArtistToAlbumDto(Album album) {
        AlbumDto albumDto = albumMapper.mapAlbumToAlbumDto(album);
        albumDto.setArtist(artistMapper.mapArtistToArtistDto(album.getArtist()));
        return albumDto;
    }

    private Album mergeAlbums(AlbumDto albumDto, Album album) {
        Album mergedAlbum = albumMapper.mergeAlbumDtoWithAlbum(albumDto, album);
        mergedAlbum.setModified(true);
        return mergedAlbum;
    }

    @Override
    public void delete(UUID id) {
        albumRepository.findById(id)
                .ifPresent(album -> {
                    Artist artist = album.getArtist();
                    artist.getAlbums().remove(album);
                    albumRepository.deleteById(id);
                });
    }
}
