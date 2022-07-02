package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.dto.StreamListDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.service.StreamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StreamControllerTest {

    @Mock
    private StreamService streamService;

    private StreamController underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamController(streamService);
    }

    @Test
    void getAllStreams() {
        List<StreamResponseDto> listData = List.of(
                new StreamResponseDto(),
                new StreamResponseDto(),
                new StreamResponseDto()
        );
        StreamListDto expected = new StreamListDto(listData);
        Mockito.when(streamService.getAllStreams()).thenReturn(listData);

        StreamListDto actual = underTest.getAllStreams();
        assertEquals(expected, actual);
    }

    @Test
    void getCurrentStreams() {
        List<StreamResponseDto> listData = List.of(
                new StreamResponseDto(),
                new StreamResponseDto(),
                new StreamResponseDto()
        );
        StreamListDto expected = new StreamListDto(listData);
        Mockito.when(streamService.getCurrentStreams()).thenReturn(listData);

        StreamListDto actual = underTest.getCurrentStreams();
        assertEquals(expected, actual);
    }

    @Test
    void getStreamById_notFound() {
        Mockito.when(streamService.getStreamById(Mockito.anyLong())).thenReturn(Optional.empty());
        ResponseEntity<StreamResponseDto> actual = underTest.getStreamById(0L);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void getStreamById_found() {
        StreamResponseDto expected = new StreamResponseDto();
        Mockito.when(streamService.getStreamById(Mockito.anyLong())).thenReturn(Optional.of(expected));
        ResponseEntity<StreamResponseDto> actual = underTest.getStreamById(0L);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }
}