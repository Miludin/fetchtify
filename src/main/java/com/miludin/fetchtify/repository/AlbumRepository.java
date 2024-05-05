package com.miludin.fetchtify.repository;

import com.miludin.fetchtify.model.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<Album, UUID> {

    @Query(
            value = """
                    SELECT DISTINCT * FROM album
                    WHERE to_tsvector('english', album.name) @@ to_tsquery(:name)
                    """,
            nativeQuery = true
    )
    List<Album> findByFullTextSearch(String name);
}
