package com.github.he305.twitchproducer.application.dao;

import com.github.he305.twitchproducer.application.repositories.StreamRepository;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.exception.EntitySaveFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class StreamDaoImplTest {

    @Mock
    private StreamRepository repository;
    private StreamDaoImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamDaoImpl(repository);
    }

    @Test
    void getAll() {
        Stream data = new Stream();
        List<Stream> expected = List.of(data);
        Mockito.when(repository.findAll()).thenReturn(List.of(data));

        List<Stream> actual = underTest.getAll();
        assertEquals(expected.size(), actual.size());
        assertEquals(actual, expected);
    }

    @Test
    void get_existing() {
        Long id = 0L;
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Stream()));
        Optional<Stream> actual = underTest.get(id);
        assertTrue(actual.isPresent());
    }

    @Test
    void get_nonExistent() {
        Long id = 0L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Optional<Stream> actual = underTest.get(id);
        assertTrue(actual.isEmpty());
    }

    @Test
    void get_null() {
        Long id = null;
        assertThrows(NullPointerException.class, () ->
                underTest.get(id));
    }

    @Test
    void save_shouldThrow() {
        Mockito.when(repository.save(Mockito.any())).thenThrow(new DataAccessResourceFailureException("sad"));
        Stream data = new Stream();
        assertThrows(EntitySaveFailedException.class, () -> underTest.save(data));
    }

    @Test
    void save_success() {
        Stream expected = new Stream();
        Mockito.when(repository.save(Mockito.any())).thenReturn(expected);
        Stream data = new Stream();
        assertDoesNotThrow(() -> underTest.save(data));
    }

    @Test
    void save_null() {
        Stream data = null;
        assertThrows(NullPointerException.class, () ->
                underTest.save(data));
    }

    @Test
    void delete_notFound() {
        Stream data = new Stream();
        doThrow(new DataAccessResourceFailureException("sad")).when(repository).delete(Mockito.any());
        assertThrows(DataAccessException.class, () ->
                underTest.delete(data));
    }

    @Test
    void delete_success() {
        doNothing().when(repository).delete(Mockito.any());
        assertDoesNotThrow(() -> underTest.delete(new Stream()));
    }

    @Test
    void delete_null() {
        Stream data = null;
        assertThrows(NullPointerException.class, () ->
                underTest.delete(data));
    }

    @Test
    void getCurrentStreams_someData() {
        Stream expected = new Stream();
        Stream placeholderStream = new Stream(null, null, LocalDateTime.now(), 0, null, null);
        List<Stream> existedData = List.of(
                placeholderStream,
                placeholderStream,
                expected
        );

        Mockito.when(repository.findAll()).thenReturn(existedData);
        List<Stream> actual = underTest.getCurrentStreams();
        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }

    @Test
    void getCurrentStreamForChannel_validData() {
        Long channelId = 0L;
        Channel channel = new Channel();
        channel.setId(channelId);

        Stream expected = new Stream();
        expected.setChannel(channel);

        Channel placeholderChannel = new Channel();
        channel.setId(99999L);
        Stream placeholderStream = new Stream();
        placeholderStream.setChannel(placeholderChannel);

        List<Stream> existedData = List.of(
                placeholderStream,
                placeholderStream,
                expected
        );

        Mockito.when(repository.findAll()).thenReturn(existedData);
        Optional<Stream> actual = underTest.getCurrentStreamForChannel(channel);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getCurrentStreamForChannel_noData() {
        Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());
        Optional<Stream> actual = underTest.getCurrentStreamForChannel(new Channel());
        assertTrue(actual.isEmpty());
    }
}