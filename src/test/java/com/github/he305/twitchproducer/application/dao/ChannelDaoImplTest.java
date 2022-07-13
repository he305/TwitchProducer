package com.github.he305.twitchproducer.application.dao;

import com.github.he305.twitchproducer.application.repositories.ChannelRepository;
import com.github.he305.twitchproducer.common.entities.Channel;
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
class ChannelDaoImplTest {

    @Mock
    private ChannelRepository channelRepository;

    private ChannelDaoImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new ChannelDaoImpl(channelRepository);
    }

    @Test
    void getChannelByName_nullName() {
        String data = null;

        assertThrows(NullPointerException.class, () -> underTest.getChannelByName(data));
    }

    @Test
    void getChannelByName_notFound() {
        Mockito.when(channelRepository.findByNickname(Mockito.any())).thenReturn(List.of());
        Optional<Channel> expected = Optional.empty();
        Optional<Channel> actual = underTest.getChannelByName("test");
        assertEquals(expected, actual);
    }

    @Test
    void getChannelByName_validName() {
        Channel expected = new Channel();

        Mockito.when(channelRepository.findByNickname(Mockito.any())).thenReturn(List.of(expected));

        Optional<Channel> actual = underTest.getChannelByName("");

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getAll() {
        Channel channel = new Channel();
        List<Channel> expected = List.of(channel);
        Mockito.when(channelRepository.findAll()).thenReturn(List.of(channel));

        List<Channel> actual = underTest.getAll();
        assertEquals(expected.size(), actual.size());
        assertEquals(actual, expected);
    }

    @Test
    void get_existing() {
        Long id = 0L;
        Channel expected = new Channel();
        Mockito.when(channelRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expected));
        Optional<Channel> actual = underTest.get(id);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void get_nonExistent() {
        Long id = 0L;
        Mockito.when(channelRepository.findById(id)).thenReturn(Optional.empty());
        Optional<Channel> actual = underTest.get(id);
        assertTrue(actual.isEmpty());
    }

    @Test
    void save_shouldThrow() {
        Mockito.when(channelRepository.save(Mockito.any())).thenThrow(new DataAccessResourceFailureException("sad"));
        Channel data = new Channel();
        assertThrows(EntitySaveFailedException.class, () -> underTest.save(data));
    }

    @Test
    void save_success() {
        Channel expected = new Channel();
        Mockito.when(channelRepository.save(Mockito.any())).thenReturn(expected);
        Channel data = new Channel();
        assertDoesNotThrow(() -> underTest.save(data));
    }

    @Test
    void delete_notFound() {
        Channel data = new Channel();
        doThrow(new DataAccessResourceFailureException("sad")).when(channelRepository).delete(Mockito.any());
        assertThrows(DataAccessException.class, () ->
                underTest.delete(data));
    }

    @Test
    void delete_success() {
        Channel data = new Channel();
        doNothing().when(channelRepository).delete(Mockito.any());
        assertDoesNotThrow(() -> underTest.delete(data));
    }
}