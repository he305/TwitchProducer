package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.repositories.ChannelRepository;
import com.github.he305.twitchproducer.application.repositories.StreamRepository;
import com.github.he305.twitchproducer.common.dto.StreamAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.mapper.StreamAddMapper;
import com.github.he305.twitchproducer.common.mapper.StreamResponseMapper;
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
    private ChannelRepository channelRepository;
    @Mock
    private StreamResponseMapper responseMapper;
    @Mock
    private StreamAddMapper addMapper;
    private StreamServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamServiceImpl(repository, channelRepository, responseMapper, addMapper);
    }

    List<Stream> getData() {
        LocalDateTime time = LocalDateTime.now();
        return List.of(
                new Stream(
                        0L,
                        time,
                        time,
                        1,
                        Collections.emptyList(),
                        null
                ),
                new Stream(
                        1L,
                        time,
                        null,
                        2,
                        Collections.emptyList(),
                        null
                ),
                new Stream(
                        2L,
                        time,
                        time,
                        3,
                        Collections.emptyList(),
                        null
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

    @Test
    void addStream_NotFound() {
        Mockito.when(channelRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        StreamAddDto dto = new StreamAddDto();
        assertThrows(EntityNotFoundException.class, () ->
                underTest.addStream(0L, dto));
    }

    @Test
    void addStream_valid() {
        Mockito.when(channelRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Channel()));
        StreamAddDto dto = new StreamAddDto();
        Mockito.when(addMapper.toStream(dto)).thenReturn(new Stream());
        Mockito.when(responseMapper.toDto(Mockito.any())).thenReturn(new StreamResponseDto());
        assertDoesNotThrow(() ->
                underTest.addStream(0L, dto));
    }

    @Test
    void endStream_notFound() {
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        LocalDateTime now = LocalDateTime.now();
        assertThrows(EntityNotFoundException.class, () ->
                underTest.endStream(0L, now));
    }

    @Test
    void endStream_valid() {
        Stream stream = new Stream();
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(stream));
        LocalDateTime time = LocalDateTime.now();
        Mockito.when(repository.save(Mockito.any())).thenReturn(new Stream());
        Mockito.when(responseMapper.toDto(Mockito.any())).thenReturn(new StreamResponseDto());
        assertDoesNotThrow(() ->
                underTest.endStream(0L, time));
    }
}