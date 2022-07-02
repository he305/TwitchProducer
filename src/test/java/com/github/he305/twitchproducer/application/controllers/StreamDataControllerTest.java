package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.dto.StreamDataList;
import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.service.StreamDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class StreamDataControllerTest {

    @Mock
    private StreamDataService streamDataService;

    private StreamDataController underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamDataController(streamDataService);
    }

    @Test
    void getAllStreamsForStreamId() {
        List<StreamDataResponseDto> data = List.of(
                new StreamDataResponseDto(),
                new StreamDataResponseDto(),
                new StreamDataResponseDto()
        );
        Mockito.when(streamDataService.getStreamDataForStreamId(Mockito.anyLong())).thenReturn(data);
        StreamDataList actual = underTest.getAllStreamsForStreamId(0L);
        assertEquals(data.size(), actual.getStreamData().size());
    }

    @Test
    void addStreamData_notFoundChannel() {
        StreamDataAddDto dto = new StreamDataAddDto();
        Mockito.when(streamDataService.addStreamData(0L, dto)).thenThrow(EntityNotFoundException.class);
        ResponseEntity<StreamDataResponseDto> actual = underTest.addStreamData(0L, dto);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void addStreamData_valid() {
        StreamDataAddDto dto = new StreamDataAddDto();
        StreamDataResponseDto expected = new StreamDataResponseDto();
        Mockito.when(streamDataService.addStreamData(0L, dto)).thenReturn(expected);
        ResponseEntity<StreamDataResponseDto> actual = underTest.addStreamData(0L, dto);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }
}