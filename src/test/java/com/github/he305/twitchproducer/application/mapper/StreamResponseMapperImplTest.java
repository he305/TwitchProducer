package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.mapper.StreamDataResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
        Long channelId = 2L;
        Channel channel = new Channel();
        channel.setId(channelId);
        Stream stream = new Stream(
                0L,
                time,
                time,
                322,
                new ArrayList<>(),
                channel
        );

        StreamResponseDto expected = new StreamResponseDto(
                0L,
                time,
                time,
                322,
                channelId
        );

        StreamResponseDto actual = underTest.toDto(stream);
        assertEquals(expected, actual);
    }
}