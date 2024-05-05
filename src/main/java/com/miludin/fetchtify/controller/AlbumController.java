package com.miludin.fetchtify.controller;

import com.miludin.fetchtify.model.dto.AlbumDto;
import com.miludin.fetchtify.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping
    public ResponseEntity<List<AlbumDto>> getAll() {
        return ResponseEntity.ok(albumService.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<AlbumDto>> getByFullTextSearch(@RequestParam String name) {
        return ResponseEntity.ok(albumService.getByFullTextSearch(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDto> getById(@PathVariable UUID id) {
        return albumService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlbumDto> create(@RequestBody AlbumDto albumDto) {
        AlbumDto createdAlbum = albumService.create(albumDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdAlbum);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumDto> update(@PathVariable UUID id,
                                           @RequestBody AlbumDto albumDto) {
        return albumService.update(id, albumDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
