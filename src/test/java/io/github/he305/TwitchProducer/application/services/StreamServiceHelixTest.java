package io.github.he305.TwitchProducer.application.services;

import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.StreamList;
import com.netflix.hystrix.HystrixCommand;
import io.github.he305.TwitchProducer.application.services.StreamServiceHelix;
import io.github.he305.TwitchProducer.common.entities.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StreamServiceHelixTest {

    @Mock
    private TwitchHelix twitchClient;
    @Mock
    private HystrixCommand<StreamList> hystrixCommand;
    private StreamServiceHelix underTest;

    @BeforeEach
    public void setUp() {
        twitchClient = Mockito.mock(TwitchHelix.class, Mockito.RETURNS_DEEP_STUBS);
        underTest = new StreamServiceHelix(twitchClient);
    }

    public com.github.twitch4j.helix.domain.Stream getValidStream() {
        try {
            Class<com.github.twitch4j.helix.domain.Stream> streamClass = com.github.twitch4j.helix.domain.Stream.class;
            Object stream = streamClass.newInstance();
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

            return (com.github.twitch4j.helix.domain.Stream) stream;

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

        Stream expected = new Stream(
                false,
                null,
                null,
                null,
                null
        );

        Stream actual = underTest.getStream(nickname);
        assertEquals(expected, actual);
    }

    @Test
    void getStream_hasValidEntry() {
        String nickname = "";
        com.github.twitch4j.helix.domain.Stream validStream = getValidStream();

        Mockito.when(twitchClient.getStreams(
                null, null, null, 100, null, null, null,
                List.of(nickname)

        ).execute().getStreams()).thenReturn(List.of(
                validStream
        ));
        Stream expected = new Stream(
                true,
                "gameName",
                "title",
                0,
                LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0)
        );

        Stream actual = underTest.getStream(nickname);

        assertEquals(expected, actual);
    }
}