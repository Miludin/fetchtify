package com.miludin.fetchtify.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public abstract class SpotifyExternalUrl {

    @JsonProperty("external_urls")
    private ExternalUrl externalUrl;

    @Getter
    public static class ExternalUrl {
        private String spotify;
    }
}
