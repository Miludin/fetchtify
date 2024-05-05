package com.miludin.fetchtify.config;

import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;

@Configuration
public class SpotifyWebClientConfig {

    private static final String REGISTRATION_ID = "spotify";

    @Bean
    public ReactiveClientRegistrationRepository clientRegistration(
            ClientRegistrationRepository clientRegistrationRepository
    ) {
        return new InMemoryReactiveClientRegistrationRepository(
                clientRegistrationRepository.findByRegistrationId(REGISTRATION_ID)
        );
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientService spotifyAuthorizedClientService(
            ReactiveClientRegistrationRepository clientRegistration
    ) {
        return new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistration);
    }

    @Bean
    public WebClientCustomizer oauth2WebClientCustomizer(
            ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager
    ) {
        ServerOAuth2AuthorizedClientExchangeFilterFunction oAuth2AuthorizedClientExchangeFilterFunction =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(reactiveOAuth2AuthorizedClientManager);
        oAuth2AuthorizedClientExchangeFilterFunction.setDefaultClientRegistrationId(REGISTRATION_ID);

        return webClientBuilder -> webClientBuilder.filter(oAuth2AuthorizedClientExchangeFilterFunction);
    }

    @Bean
    public ReactiveOAuth2AuthorizedClientManager reactiveOAuth2AuthorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistration,
            ReactiveOAuth2AuthorizedClientService spotifyAuthorizedClientService
    ) {
        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
                        clientRegistration,
                        spotifyAuthorizedClientService
                );
        authorizedClientManager.setAuthorizedClientProvider(
                new ClientCredentialsReactiveOAuth2AuthorizedClientProvider()
        );
        return authorizedClientManager;
    }
}
