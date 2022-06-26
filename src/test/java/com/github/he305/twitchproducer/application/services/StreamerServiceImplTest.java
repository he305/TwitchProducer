package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.mapper.StreamerResponseMapper;
import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.application.repositories.StreamerRepository;
import com.github.he305.twitchproducer.common.dto.StreamerAddDto;
import com.github.he305.twitchproducer.common.dto.StreamerResponseDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.entities.Platform;
import com.github.he305.twitchproducer.common.entities.Streamer;
import com.github.he305.twitchproducer.common.mapper.StreamerAddMapper;
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
    @Mock
    private PersonRepository personRepository;
    @Mock
    private StreamerAddMapper streamerAddMapper;
    @Mock
    private StreamerResponseMapper streamerResponseMapper;
    private StreamerServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamerServiceImpl(streamerRepository, personRepository, streamerAddMapper, streamerResponseMapper);
    }

    @Test
    void getStreamerByName_nullName() {
        String data = null;

        assertThrows(NullPointerException.class, () -> underTest.getStreamerByName(data));
    }

    @Test
    void getStreamerByName_validName() {
        StreamerResponseDto expected = new StreamerResponseDto(1L, "1", Platform.TWITCH, null);

        Mockito.when(streamerRepository.findByNickname(Mockito.any())).thenReturn(List.of(new Streamer()));
        Mockito.when(streamerResponseMapper.toDto(Mockito.any())).thenReturn(expected);

        Optional<StreamerResponseDto> actual = underTest.getStreamerByName("");

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getAllStreamers_emptyList() {
        List<StreamerResponseDto> expected = Collections.emptyList();
        Mockito.when(streamerRepository.findAll()).thenReturn(Collections.emptyList());

        List<StreamerResponseDto> actual = underTest.getAllStreamers();
        assertEquals(expected, actual);
    }

    @Test
    void addStreamer_emptyName() {
        StreamerAddDto data = new StreamerAddDto("", Platform.TWITCH, 0L);
        StreamerResponseDto expected = new StreamerResponseDto();
        StreamerResponseDto actual = underTest.addStreamer(data);
        assertEquals(expected, actual);
    }

    @Test
    void addStreamer_personNotExist() {
        StreamerAddDto data = new StreamerAddDto("test", Platform.TWITCH, 0L);
        Mockito.when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        StreamerResponseDto expected = new StreamerResponseDto();
        StreamerResponseDto actual = underTest.addStreamer(data);
        assertEquals(expected, actual);
    }

    @Test
    void addStreamer_alreadyExistsName() {
        String nickname = "1";
        StreamerAddDto data = new StreamerAddDto(nickname, Platform.TWITCH, 0L);
        StreamerResponseDto expected = new StreamerResponseDto();
        Mockito.when(streamerRepository.findByNickname(nickname)).thenReturn(List.of(new Streamer()));
        StreamerResponseDto actual = underTest.addStreamer(data);
        assertEquals(expected, actual);
    }

    @Test
    void addStreamer_validFull() {
        StreamerAddDto data = new StreamerAddDto("1", Platform.TWITCH, 0L);
        Person expectedPerson = new Person();
        StreamerResponseDto expected = new StreamerResponseDto(0L, "1", Platform.TWITCH, "");
        Mockito.when(streamerRepository.save(Mockito.any())).thenReturn(new Streamer());
        Mockito.when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedPerson));
        Mockito.when(streamerAddMapper.getStreamer(data)).thenReturn(new Streamer());
        Mockito.when(streamerResponseMapper.toDto(Mockito.any())).thenReturn(expected);
        StreamerResponseDto actual = underTest.addStreamer(data);
        assertEquals(expected, actual);
    }
}