package com.miludin.fetchtify.service.impl;

import com.miludin.fetchtify.mapper.AlbumMapper;
import com.miludin.fetchtify.mapper.ArtistMapper;
import com.miludin.fetchtify.model.dto.AlbumDto;
import com.miludin.fetchtify.model.dto.ArtistDto;
import com.miludin.fetchtify.model.entity.Artist;
import com.miludin.fetchtify.repository.ArtistRepository;
import com.miludin.fetchtify.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    private final ArtistMapper artistMapper;
    private final AlbumMapper albumMapper;

    @Override
    public List<ArtistDto> getAll() {
        return artistRepository.findAll().stream()
                .map(this::mapArtistWithAlbumsToArtistDto)
                .toList();
    }

    @Override
    public List<ArtistDto> getByFullTextSearch(String name) {
        return artistRepository.findByFullTextSearch(name).stream()
                .map(this::mapArtistWithAlbumsToArtistDto)
                .toList();
    }

    @Override
    public Optional<ArtistDto> getById(UUID id) {
        return artistRepository.findById(id)
                .map(this::mapArtistWithAlbumsToArtistDto);
    }

    @Override
    @Transactional
    public ArtistDto create(ArtistDto artistDto) {
        Artist artist = artistMapper.mapArtistDtoToArtist(artistDto);

        List<AlbumDto> albumDtos = artistDto.getAlbums();
        if (!CollectionUtils.isEmpty(albumDtos)) {
            artist.setAlbums(albumMapper.mapAlbumDtoToAlbum(albumDtos));
            artist.getAlbums().forEach(album -> album.setArtist(artist));
        }

        return mapArtistWithAlbumsToArtistDto(artistRepository.save(artist));
    }

    @Override
    public Optional<ArtistDto> update(UUID id, ArtistDto artistDto) {
        return artistRepository.findById(id)
                .map(artist -> mergeArtists(artistDto, artist))
                .map(artistRepository::save)
                .map(this::mapArtistWithAlbumsToArtistDto);
    }

    private ArtistDto mapArtistWithAlbumsToArtistDto(Artist artist) {
        ArtistDto artistDto = artistMapper.mapArtistToArtistDto(artist);
        artistDto.setAlbums(albumMapper.mapAlbumToAlbumDto(artist.getAlbums()));
        return artistDto;
    }

    private Artist mergeArtists(ArtistDto artistDto, Artist artist) {
        Artist mergedArtist = artistMapper.mergeArtistDtoWithArtist(artistDto, artist);
        mergedArtist.setModified(true);
        return mergedArtist;
    }

    @Override
    public void delete(UUID id) {
        artistRepository.deleteById(id);
    }
}
