package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.dto.StreamDataList;
import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.service.StreamDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

}