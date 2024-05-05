package com.miludin.fetchtify.repository;

import com.miludin.fetchtify.model.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID> {

    @Query(
            value = """
                    SELECT DISTINCT * FROM artist
                    WHERE to_tsvector('english', artist.name) @@ to_tsquery(:name)
                    """,
            nativeQuery = true
    )
    List<Artist> findByFullTextSearch(String name);

    List<Artist> findAllBySpotifyIdIn(List<String> spotifyIds);
}
