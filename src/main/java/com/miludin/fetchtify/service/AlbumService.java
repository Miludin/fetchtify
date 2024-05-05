package com.miludin.fetchtify.service;

import com.miludin.fetchtify.model.dto.AlbumDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlbumService {

    List<AlbumDto> getAll();

    List<AlbumDto> getByFullTextSearch(String name);

    Optional<AlbumDto> getById(UUID id);

    AlbumDto create(AlbumDto albumDto);

    Optional<AlbumDto> update(UUID id, AlbumDto albumDto);

    void delete(UUID id);
}
