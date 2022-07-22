package com.github.he305.twitchproducer.application.dao;

import com.github.he305.twitchproducer.application.repositories.StreamDataRepository;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.exception.EntitySaveFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class StreamDataDaoImplTest {
    @Mock
    private StreamDataRepository repository;
    private StreamDataDaoImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamDataDaoImpl(repository);
    }

    @Test
    void getAll() {
        StreamData data = new StreamData();
        List<StreamData> expected = List.of(data);
        Mockito.when(repository.findAll()).thenReturn(List.of(data));

        List<StreamData> actual = underTest.getAll();
        assertEquals(expected.size(), actual.size());
        assertEquals(actual, expected);
    }

    @Test
    void get_existing() {
        Long id = 0L;
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(new StreamData()));
        Optional<StreamData> actual = underTest.get(id);
        assertTrue(actual.isPresent());
    }

    @Test
    void get_nonExistent() {
        Long id = 0L;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Optional<StreamData> actual = underTest.get(id);
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
        StreamData data = new StreamData();
        assertThrows(EntitySaveFailedException.class, () -> underTest.save(data));
    }

    @Test
    void save_success() {
        StreamData expected = new StreamData();
        Mockito.when(repository.save(Mockito.any())).thenReturn(expected);
        StreamData data = new StreamData();
        assertDoesNotThrow(() -> underTest.save(data));
    }

    @Test
    void save_null() {
        StreamData data = null;
        assertThrows(NullPointerException.class, () ->
                underTest.save(data));
    }

    @Test
    void delete_notFound() {
        StreamData data = new StreamData();
        doThrow(new DataAccessResourceFailureException("sad")).when(repository).delete(Mockito.any());
        assertThrows(DataAccessException.class, () ->
                underTest.delete(data));
    }

    @Test
    void delete_success() {
        doNothing().when(repository).delete(Mockito.any());
        assertDoesNotThrow(() -> underTest.delete(new StreamData()));
    }

    @Test
    void delete_null() {
        StreamData data = null;
        assertThrows(NullPointerException.class, () ->
                underTest.delete(data));
    }

    @Test
    void getStreamDataForStream_someData() {
        Long streamId = 0L;
        Stream stream = new Stream();
        stream.setId(streamId);

        StreamData expectedData = new StreamData();
        expectedData.setStream(stream);

        Stream fakeStream = new Stream();
        fakeStream.setId(9999L);
        StreamData fakeData = new StreamData();
        fakeData.setStream(fakeStream);

        List<StreamData> expected = List.of(expectedData);
        List<StreamData> dataInDb = List.of(expectedData, fakeData);

        Mockito.when(repository.findAll()).thenReturn(dataInDb);

        List<StreamData> actual = underTest.getStreamDataForStream(streamId);
        assertEquals(1, actual.size());
        assertEquals(expectedData, actual.get(0));
    }

    @Test
    void getStreamDataForStream_noDataForStreamId() {
        Long streamId = 0L;
        Stream stream = new Stream();
        stream.setId(streamId);

        StreamData expectedData = new StreamData();
        expectedData.setStream(stream);

        List<StreamData> dataInDb = List.of(expectedData);

        Mockito.when(repository.findAll()).thenReturn(dataInDb);

        List<StreamData> actual = underTest.getStreamDataForStream(streamId + 1);
        assertTrue(actual.isEmpty());
    }
}