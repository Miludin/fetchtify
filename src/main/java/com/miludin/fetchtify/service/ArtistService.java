package com.miludin.fetchtify.service;

import com.miludin.fetchtify.model.dto.ArtistDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArtistService {

    List<ArtistDto> getAll();

    List<ArtistDto> getByFullTextSearch(String name);

    Optional<ArtistDto> getById(UUID id);

    ArtistDto create(ArtistDto artistDto);

    Optional<ArtistDto> update(UUID id, ArtistDto artistDto);

    void delete(UUID id);
}
