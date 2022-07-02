package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.entities.StreamData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamDataAddMapperImplTest {

    private StreamDataAddMapperImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamDataAddMapperImpl();
    }

    @Test
    void toStreamData() {
        LocalDateTime time = LocalDateTime.now();
        StreamDataAddDto data = new StreamDataAddDto(
                "gameName",
                "title",
                322,
                time
        );
        StreamData expected = new StreamData(
                null,
                "gameName",
                "title",
                322,
                null
        );

        StreamData actual = underTest.toStreamData(data);
        assertEquals(expected, actual);
    }
}