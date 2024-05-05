package com.miludin.fetchtify;

import com.miludin.fetchtify.model.dto.ArtistDto;
import com.miludin.fetchtify.model.entity.Album;
import com.miludin.fetchtify.model.entity.Artist;
import com.miludin.fetchtify.repository.ArtistRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArtistControllerTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @LocalServerPort
    private Integer port;

    private final ArtistRepository artistRepository;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        artistRepository.deleteAll();
    }

    @Test
    void getAll_shouldReturnAllArtistsWithAlbums() {
        Artist firstArtist = createTestArtist("First Artist", "test-spotify-id");
        Artist secondArtist = createTestArtist("Second Artist", "another-test-spotify-id");

        Album firstAlbum = createTestAlbum("First Album", "test-spotify-id");
        Album secondAlbum = createTestAlbum("Second Album", "another-test-spotify-id");
        Album thirdAlbum = createTestAlbum("Third Album", "and-another-test-spotify-id");

        setBidirectionalReferences(firstArtist, List.of(firstAlbum, secondAlbum));
        setBidirectionalReferences(secondArtist, List.of(thirdAlbum));

        artistRepository.saveAll(List.of(firstArtist, secondArtist));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/v1/artists")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(2))
                .body("name", containsInAnyOrder("First Artist", "Second Artist"))
                .rootPath("find { it.name == 'First Artist' }.albums")
                .body("size()", equalTo(2))
                .body("name", containsInAnyOrder("First Album", "Second Album"))
                .rootPath("find { it.name == 'Second Artist' }.albums")
                .body("size()", equalTo(1))
                .body("name", equalTo(List.of("Third Album")));
    }

    @Test
    void update_withNewNameAndSpotifyId_shouldUpdateArtist() {
        Artist firstArtist = createTestArtist("First Artist", "test-spotify-id");

        Album firstAlbum = createTestAlbum("First Album", "test-spotify-id");
        Album secondAlbum = createTestAlbum("Second Album", "another-test-spotify-id");

        setBidirectionalReferences(firstArtist, List.of(firstAlbum, secondAlbum));

        firstArtist = artistRepository.save(firstArtist);

        ArtistDto artistDto = new ArtistDto();
        artistDto.setName("Updated First Artist");
        artistDto.setSpotifyId("updated-spotify-id");

        given()
                .contentType(ContentType.JSON)
                .body(artistDto)
                .when()
                .put("/v1/artists/{id}", firstArtist.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Updated First Artist"))
                .body("spotifyId", equalTo("updated-spotify-id"))
                .body("albums.size()", equalTo(2));
    }

    private static Artist createTestArtist(String name, String spotifyId) {
        Artist artist = new Artist();
        artist.setName(name);
        artist.setUrl("https://test.for.test/artist/test-spotify-id");
        artist.setSpotifyId(spotifyId);
        artist.setGenres(List.of("pop", "rock", "pop-rock"));
        return artist;
    }

    private static Album createTestAlbum(String name, String spotifyId) {
        Album album = new Album();
        album.setName(name);
        album.setReleaseDate("2021-09-10");
        album.setUrl("https://test.for.test/album/test-spotify-id");
        album.setSpotifyId(spotifyId);
        album.setTotalTracks(10);
        return album;
    }

    private static void setBidirectionalReferences(Artist artist, List<Album> albums) {
        artist.setAlbums(albums);
        artist.getAlbums().forEach(album -> album.setArtist(artist));
    }
}
