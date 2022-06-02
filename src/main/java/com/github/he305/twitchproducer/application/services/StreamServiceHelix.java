package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.interfaces.StreamService;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.StreamList;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@AllArgsConstructor
public class StreamServiceHelix implements StreamService {
    @Autowired
    private final TwitchHelix twitchClient;

    @Override
    public Stream getStream(String nickname) {
        StreamList streamList = twitchClient.getStreams(null, null, null, 100, null, null, null, List.of(nickname)).execute();
        return buildStream(streamList);
    }

    private Stream buildStream(StreamList streamList) {
        if (streamList.getStreams().size() == 0)
            return buildEmptyStream();

        return buildFullStream(streamList.getStreams().get(0));
    }

    private Stream buildFullStream(com.github.twitch4j.helix.domain.Stream rawStream) {
        return Stream.builder()
                .gameName(rawStream.getGameName())
                .startedAt(LocalDateTime.ofInstant(rawStream.getStartedAtInstant(), ZoneOffset.UTC))
                .title(rawStream.getTitle())
                .viewerCount(rawStream.getViewerCount())
                .isLive(true)
                .build();
    }

    private Stream buildEmptyStream() {
        return Stream.emptyStream();
    }

}
