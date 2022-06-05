package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.interfaces.StreamDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StreamDataControllerTest {

    @Mock
    private StreamDataService streamDataService;
    private StreamDataController underTest;

    @BeforeEach
    public void setUp() {
        underTest = new StreamDataController(streamDataService);
    }

    @Test
    void getStream_noStreamData() {
        String data = "";
        StreamData expected = StreamData.emptyStream();

        Mockito.when(streamDataService.getStream(data)).thenReturn(StreamData.emptyStream());
        StreamData actual = underTest.getStream("");

        assertEquals(expected, actual);
    }

    @Test
    void getStream_existStreamData() {
        String data = "";
        StreamData expected = StreamData.builder()
                .gameName("gameName")
                .isLive(true)
                .startedAt(LocalDateTime.ofEpochSecond(1, 1, ZoneOffset.UTC))
                .title("title")
                .viewerCount(0)
                .build();

        Mockito.when(streamDataService.getStream(data)).thenReturn(expected);
        StreamData actual = underTest.getStream(data);

        assertEquals(expected, actual);
    }
}