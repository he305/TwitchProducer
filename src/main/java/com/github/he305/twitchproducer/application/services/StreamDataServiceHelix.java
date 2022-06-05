package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.interfaces.StreamDataService;
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
public class StreamDataServiceHelix implements StreamDataService {
    @Autowired
    private final TwitchHelix twitchClient;

    @Override
    public StreamData getStream(String nickname) {
        StreamList streamList = twitchClient.getStreams(null, null, null, 100, null, null, null, List.of(nickname)).execute();
        return buildStream(streamList);
    }

    private StreamData buildStream(StreamList streamList) {
        if (streamList.getStreams().isEmpty())
            return buildEmptyStream();

        return buildFullStream(streamList.getStreams().get(0));
    }

    private StreamData buildFullStream(com.github.twitch4j.helix.domain.Stream rawStream) {
        return StreamData.builder()
                .gameName(rawStream.getGameName())
                .startedAt(LocalDateTime.ofInstant(rawStream.getStartedAtInstant(), ZoneOffset.UTC))
                .title(rawStream.getTitle())
                .viewerCount(rawStream.getViewerCount())
                .isLive(true)
                .build();
    }

    private StreamData buildEmptyStream() {
        return StreamData.emptyStream();
    }

}
