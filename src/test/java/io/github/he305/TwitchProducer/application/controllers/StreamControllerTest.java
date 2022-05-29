package io.github.he305.TwitchProducer.application.controllers;

import io.github.he305.TwitchProducer.common.entities.Stream;
import io.github.he305.TwitchProducer.common.interfaces.StreamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StreamControllerTest {

    @Mock
    private StreamService streamService;
    private StreamController underTest;

    @BeforeEach
    public void setUp() {
        underTest = new StreamController(streamService);
    }

    @Test
    void getStream_noStreamData() {
        String data = "";
        Stream expected = Stream.emptyStream();

        Mockito.when(streamService.getStream(data)).thenReturn(Stream.emptyStream());
        Stream actual = underTest.getStream("");

        assertEquals(expected, actual);
    }

    @Test
    void getStream_existStreamData() {
        String data = "";
        Stream expected = Stream.builder()
                .gameName("gameName")
                .isLive(true)
                .startedAt(LocalDateTime.ofEpochSecond(1, 1, ZoneOffset.UTC))
                .title("title")
                .viewerCount(0)
                .build();

        Mockito.when(streamService.getStream(data)).thenReturn(expected);
        Stream actual = underTest.getStream(data);

        assertEquals(expected, actual);
    }
}