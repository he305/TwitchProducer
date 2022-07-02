package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.StreamAddDto;
import com.github.he305.twitchproducer.common.entities.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamAddMapperImplTest {

    private StreamAddMapperImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamAddMapperImpl();
    }

    @Test
    void toStream() {
        LocalDateTime time = LocalDateTime.now();
        StreamAddDto data = new StreamAddDto(
                time
        );
        Stream expected = new Stream(
                null,
                time,
                null,
                0,
                Collections.emptyList(),
                null
        );
        Stream actual = underTest.toStream(data);
        assertEquals(expected, actual);
    }
}