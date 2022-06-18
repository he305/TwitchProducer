package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.repositories.StreamerRepository;
import com.github.he305.twitchproducer.common.entities.Platform;
import com.github.he305.twitchproducer.common.entities.Streamer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StreamerServiceImplTest {
    @Mock
    private StreamerRepository streamerRepository;
    private StreamerServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamerServiceImpl(streamerRepository);
    }

    @Test
    void getStreamerByName_nullName() {
        String data = null;

        assertThrows(NullPointerException.class, () -> underTest.getStreamerByName(data));
    }

    @Test
    void getStreamerByName_validName() {
        String data = "";
        Streamer expected = new Streamer(1L, "1", Platform.TWITCH);

        Mockito.when(streamerRepository.findByNickname(data)).thenReturn(List.of(expected));

        Optional<Streamer> actual = underTest.getStreamerByName(data);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getAllStreamers_emptyList() {
        List<Streamer> expected = Collections.emptyList();
        Mockito.when(streamerRepository.findAll()).thenReturn(Collections.emptyList());

        List<Streamer> actual = underTest.getAllStreamers();
        assertEquals(expected, actual);
    }

    @Test
    void addStreamer_emptyName() {
        Streamer data = new Streamer(0L, "", Platform.TWITCH);
        Streamer expected = new Streamer();
        Streamer actual = underTest.addStreamer(data);
        assertEquals(expected, actual);
    }

    @Test
    void addStreamer_alreadyExists() {
        String nickname = "1";
        Streamer data = new Streamer(0L, nickname, Platform.TWITCH);
        Streamer expected = new Streamer();
        Mockito.when(streamerRepository.findByNickname(nickname)).thenReturn(List.of(data));
        Streamer actual = underTest.addStreamer(data);
        assertEquals(expected, actual);
    }

    @Test
    void addStreamer_validFull() {
        Streamer data = new Streamer(1L, "1", Platform.TWITCH);
        Mockito.when(streamerRepository.save(data)).thenReturn(data);

        Streamer expected = underTest.addStreamer(data);
        assertEquals(expected, data);
    }
}