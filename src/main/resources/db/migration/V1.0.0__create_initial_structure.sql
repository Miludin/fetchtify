CREATE TABLE artist
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL,
    url         VARCHAR(100),
    spotify_id  VARCHAR(30),
    is_modified BOOLEAN       NOT NULL DEFAULT FALSE,
    genres      VARCHAR(30)[] NOT NULL
);

CREATE TABLE album
(
    id           UUID PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    release_date VARCHAR(10)  NOT NULL,
    url          VARCHAR(100),
    spotify_id   VARCHAR(30),
    total_tracks INTEGER      NOT NULL,
    is_modified  BOOLEAN      NOT NULL DEFAULT FALSE,
    artist_id    UUID         NOT NULL,
    FOREIGN KEY (artist_id) REFERENCES artist (id)
);

CREATE INDEX artist_name_index ON artist USING gin (to_tsvector('english', name));

CREATE INDEX album_name_index ON album USING gin (to_tsvector('english', name));