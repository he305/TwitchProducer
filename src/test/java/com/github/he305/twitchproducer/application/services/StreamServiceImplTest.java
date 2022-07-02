package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.repositories.StreamRepository;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.mapper.StreamResponseMapper;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StreamServiceImplTest {

    @Mock
    private StreamRepository repository;
    @Mock
    private StreamResponseMapper responseMapper;
    private StreamServiceImpl underTest;
    @BeforeEach
    void setUp() {
        underTest = new StreamServiceImpl(repository, responseMapper);
    }

    List<Stream> getData() {
        LocalDateTime time = LocalDateTime.now();
        return List.of(
                new Stream(
                        0L,
                        time,
                        time,
                        1,
                        Collections.emptyList()
                ),
                new Stream(
                        1L,
                        time,
                        null,
                        2,
                        Collections.emptyList()
                ),
                new Stream(
                        2L,
                        time,
                        time,
                        3,
                        Collections.emptyList()
                )
        );
    }

    @Test
    void getAllStreams_someData() {
        List<Stream> expectedData = getData();
        Mockito.when(repository.findAll()).thenReturn(expectedData);
        Mockito.when(responseMapper.toDto(Mockito.any())).thenReturn(new StreamResponseDto());
        List<StreamResponseDto> actual = underTest.getAllStreams();
        assertEquals(expectedData.size(), actual.size());
    }

    @Test
    void getCurrentStreams_oneEntry() {
        List<Stream> expectedData = getData();
        Mockito.when(repository.findAll()).thenReturn(expectedData);
        Mockito.when(responseMapper.toDto(Mockito.any())).thenReturn(new StreamResponseDto());
        List<StreamResponseDto> actual = underTest.getCurrentStreams();
        assertEquals(1, actual.size());
    }

    @Test
    void getStreamById_notFound() {
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.empty());
        Optional<StreamResponseDto> expected = Optional.empty();
        Optional<StreamResponseDto> actual = underTest.getStreamById(0L);
        assertEquals(expected, actual);
    }

    @Test
    void getStreamById_found() {
        StreamResponseDto expected = new StreamResponseDto();
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(new Stream()));
        Mockito.when(responseMapper.toDto(Mockito.any())).thenReturn(expected);
        Optional<StreamResponseDto> actual = underTest.getStreamById(0L);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }
}