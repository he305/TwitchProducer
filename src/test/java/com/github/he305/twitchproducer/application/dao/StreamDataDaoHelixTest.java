package com.github.he305.twitchproducer.application.dao;

import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.StreamList;
import com.netflix.hystrix.HystrixCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StreamDataDaoHelixTest {

    @Mock
    private TwitchHelix twitchClient;
    @Mock
    private HystrixCommand<StreamList> hystrixCommand;
    private StreamDataServiceHelix underTest;

    @BeforeEach
    public void setUp() {
        twitchClient = Mockito.mock(TwitchHelix.class, Mockito.RETURNS_DEEP_STUBS);
        underTest = new StreamDataServiceHelix(twitchClient);
    }

    public com.github.twitch4j.helix.domain.Stream getValidStream() {
        try {
            Class<com.github.twitch4j.helix.domain.Stream> streamClass = com.github.twitch4j.helix.domain.Stream.class;
            com.github.twitch4j.helix.domain.Stream stream = streamClass.newInstance();
            Field gameName = streamClass.getDeclaredField("gameName");
            Field time = streamClass.getDeclaredField("startedAtInstant");
            Field title = streamClass.getDeclaredField("title");
            Field viewerCount = streamClass.getDeclaredField("viewerCount");
            gameName.setAccessible(true);
            gameName.set(stream, "gameName");
            time.setAccessible(true);
            time.set(stream, Instant.ofEpochMilli(0));
            title.setAccessible(true);
            title.set(stream, "title");
            viewerCount.setAccessible(true);
            viewerCount.set(stream, 0);

            return stream;

        } catch (NoSuchFieldException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getStream_empty() {
        String nickname = "";

        Mockito.when(twitchClient.getStreams(
                null, null, null, 100, null, null, null,
                List.of(nickname)

        ).execute().getStreams()).thenReturn(List.of());
//        Mockito.when(hystrixCommand.execute().getStreams()).thenReturn(List.of());

        StreamData expected = new StreamData();

        StreamData actual = underTest.getStream(nickname);
        assertEquals(expected, actual);
    }
}