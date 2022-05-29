package io.github.he305.TwitchProducer.application.controllers;

import io.github.he305.TwitchProducer.application.dto.StreamerBodyDto;
import io.github.he305.TwitchProducer.common.entities.Streamer;
import io.github.he305.TwitchProducer.common.interfaces.StreamerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StreamerControllerTest {

    @Mock
    private StreamerService streamerService;
    @Mock
    private ModelMapper modelMapper;
    private StreamerController underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamerController(streamerService, modelMapper);
    }

    @Test
    void getAll() {
        List<Streamer> expected = Collections.emptyList();
        Mockito.when(streamerService.getAllStreamers()).thenReturn(expected);

        List<Streamer> actual = underTest.getAll();
        assertEquals(expected, actual);
    }

    @Test
    void getByName_someData() {
        String data = "1";
        Streamer expected = new Streamer(1l, "1");
        Mockito.when(streamerService.getStreamerByName(data)).thenReturn(Optional.of(expected));
        Streamer actual = underTest.getByName(data);
        assertEquals(expected, actual);
    }

    @Test
    void getByName_noData() {
        String data = "1";
        Streamer expected = new Streamer();
        Mockito.when(streamerService.getStreamerByName(data)).thenReturn(Optional.empty());

        Streamer actual = underTest.getByName(data);
        assertEquals(expected, actual);
    }

    @Test
    void addStreamer_validInput() {
        String nickname = "test";
        StreamerBodyDto streamerBodyDto = new StreamerBodyDto(nickname);
        Streamer expected = new Streamer(0l, nickname);
        Mockito.when(modelMapper.map(streamerBodyDto, Streamer.class)).thenReturn(expected);
        Mockito.when(streamerService.addStreamer(expected)).thenReturn(expected);

        ResponseEntity<Streamer> actual = underTest.addStreamer(streamerBodyDto);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void addStreamer_serviceError() {
        String nickname = "test";
        StreamerBodyDto streamerBodyDto = new StreamerBodyDto(nickname);
        Streamer fromDto = new Streamer(0l, nickname);
        ResponseEntity<Streamer> expected = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        Mockito.when(modelMapper.map(streamerBodyDto, Streamer.class)).thenReturn(fromDto);
        Mockito.when(streamerService.addStreamer(fromDto)).thenReturn(new Streamer());

        ResponseEntity<Streamer> actual = underTest.addStreamer(streamerBodyDto);
        assertEquals(expected, actual);
    }
}