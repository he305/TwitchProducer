package com.github.he305.twitchproducer.application.service;

import com.github.he305.twitchproducer.common.dao.StreamDataDao;
import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.mapper.StreamDataResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class StreamDataServiceImplTest {

    @Mock
    private StreamDataDao streamDataDao;
    @Mock
    private StreamDataResponseMapper responseMapper;

    private StreamDataServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamDataServiceImpl(streamDataDao, responseMapper);
    }

    @Test
    void getStreamDataForStreamId_noData() {
        Mockito.when(streamDataDao.getStreamDataForStream(Mockito.anyLong())).thenReturn(Collections.emptyList());

        List<StreamDataResponseDto> actual = underTest.getStreamDataForStreamId(0L);
        assertTrue(actual.isEmpty());
    }

    @Test
    void getStreamDataForStreamId_someData() {
        StreamData data = new StreamData();
        StreamDataResponseDto expected = new StreamDataResponseDto();

        Mockito.when(streamDataDao.getStreamDataForStream(Mockito.anyLong())).thenReturn(List.of(data));
        Mockito.when(responseMapper.toDto(data)).thenReturn(expected);

        List<StreamDataResponseDto> actual = underTest.getStreamDataForStreamId(0L);
        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }
}