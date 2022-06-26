package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.dto.StreamerListDto;
import com.github.he305.twitchproducer.common.dto.StreamerAddDto;
import com.github.he305.twitchproducer.common.dto.StreamerResponseDto;
import com.github.he305.twitchproducer.common.entities.Platform;
import com.github.he305.twitchproducer.common.service.StreamerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
    private StreamerController underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamerController(streamerService);
    }

    @Test
    void getAll() {
        List<StreamerResponseDto> expected = Collections.emptyList();
        Mockito.when(streamerService.getAllStreamers()).thenReturn(expected);

        StreamerListDto actual = underTest.getAll();
        assertEquals(expected, actual.getStreamers());
    }

    @Test
    void getByName_someData() {
        String data = "1";
        StreamerResponseDto expected = new StreamerResponseDto(1L, "1", Platform.TWITCH, null);
        Mockito.when(streamerService.getStreamerByName(data)).thenReturn(Optional.of(expected));
        StreamerResponseDto actual = underTest.getByName(data);
        assertEquals(expected, actual);
    }

    @Test
    void getByName_noData() {
        String data = "1";
        StreamerResponseDto expected = new StreamerResponseDto();
        Mockito.when(streamerService.getStreamerByName(data)).thenReturn(Optional.empty());

        StreamerResponseDto actual = underTest.getByName(data);
        assertEquals(expected, actual);
    }

    @Test
    void addStreamer_validInput() {
        String nickname = "test";
        StreamerAddDto streamerAddDto = new StreamerAddDto(nickname, Platform.TWITCH, 0L);
        StreamerResponseDto expected = new StreamerResponseDto(0L, nickname, Platform.TWITCH, "");
        Mockito.when(streamerService.addStreamer(streamerAddDto)).thenReturn(expected);

        ResponseEntity<StreamerResponseDto> actual = underTest.addStreamer(streamerAddDto);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void addStreamer_serviceError() {
        String nickname = "test";
        StreamerAddDto data = new StreamerAddDto(nickname, Platform.TWITCH, 0L);
        ResponseEntity<StreamerResponseDto> expected = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        Mockito.when(streamerService.addStreamer(data)).thenReturn(new StreamerResponseDto());

        ResponseEntity<StreamerResponseDto> actual = underTest.addStreamer(data);
        assertEquals(expected, actual);
    }
}