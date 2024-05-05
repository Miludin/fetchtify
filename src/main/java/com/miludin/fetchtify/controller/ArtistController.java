package com.miludin.fetchtify.controller;

import com.miludin.fetchtify.model.dto.ArtistDto;
import com.miludin.fetchtify.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping
    public ResponseEntity<List<ArtistDto>> getAll() {
        return ResponseEntity.ok(artistService.getAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArtistDto>> getByFullTextSearch(@RequestParam String name) {
        return ResponseEntity.ok(artistService.getByFullTextSearch(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getById(@PathVariable UUID id) {
        return artistService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArtistDto> create(@RequestBody ArtistDto artistDto) {
        ArtistDto createdArtist = artistService.create(artistDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdArtist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDto> update(@PathVariable UUID id,
                                            @RequestBody ArtistDto artistDto) {
        return artistService.update(id, artistDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        artistService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
