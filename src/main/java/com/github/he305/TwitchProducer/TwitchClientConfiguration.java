package com.github.he305.TwitchProducer;

import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.helix.TwitchHelix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitchClientConfiguration {

    @Value("${twitch-client-id-env:default}")
    private String clientId;

    @Value("${twitch-client-secret-env:default}")
    private String clientSecret;

    @Bean
    public TwitchHelix getTwitchClient() {
        return TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .build().getHelix();
    }

}
