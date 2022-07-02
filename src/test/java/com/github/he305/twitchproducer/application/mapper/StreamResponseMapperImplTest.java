package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.mapper.StreamDataResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StreamResponseMapperImplTest {

    @Mock
    private StreamDataResponseMapper dataMapper;
    private StreamResponseMapperImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamResponseMapperImpl(dataMapper);
    }

    @Test
    void toDto() {
        LocalDateTime time = LocalDateTime.now();
        Stream stream = new Stream(
                0L,
                time,
                time,
                322,
                new ArrayList<>()
        );
        StreamData data = new StreamData(
                999L,
                "some",
                "title",
                322,
                stream
        );
        stream.addStreamData(data);

        StreamDataResponseDto expectedData = new StreamDataResponseDto(
                999L,
                "some",
                "title",
                322,
                time
        );
        Mockito.when(dataMapper.toDto(Mockito.any())).thenReturn(expectedData);

        StreamResponseDto expected = new StreamResponseDto(
                0L,
                time,
                time,
                322,
                List.of(expectedData)
        );

        StreamResponseDto actual = underTest.toDto(stream);
        assertEquals(expected, actual);
    }
}