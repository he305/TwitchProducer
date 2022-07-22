package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.entities.StreamData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamDataResponseMapperImplTest {

    private StreamDataResponseMapperImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamDataResponseMapperImpl();
    }

    @Test
    void toDto_fullData() {
        LocalDateTime time = LocalDateTime.of(
                2022,
                7,
                2,
                16,
                12
        );

        Stream stream = new Stream();
        Long streamId = 90L;
        stream.setId(streamId);

        StreamData data = new StreamData(
                999L,
                "some game",
                "some title",
                322,
                stream,
                time
        );
        data.setCreatedAt(time);

        StreamDataResponseDto expected = new StreamDataResponseDto(
                999L,
                "some game",
                "some title",
                322,
                time,
                streamId
        );

        StreamDataResponseDto actual = underTest.toDto(data);
        assertEquals(expected, actual);
    }

    @Test
    void toDto_noDate() {
        Stream stream = new Stream();
        Long streamId = 90L;
        stream.setId(streamId);

        StreamData data = new StreamData(
                999L,
                "some game",
                "some title",
                322,
                stream,
                null
        );

        StreamDataResponseDto expected = new StreamDataResponseDto(
                999L,
                "some game",
                "some title",
                322,
                null,
                streamId
        );

        StreamDataResponseDto actual = underTest.toDto(data);
        assertEquals(expected, actual);
    }
}