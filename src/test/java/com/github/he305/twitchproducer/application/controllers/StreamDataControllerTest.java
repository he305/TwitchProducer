package com.github.he305.twitchproducer.application.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StreamDataControllerTest {

//    @Mock
//    private StreamDataDao streamDataDao;
//
//    private StreamDataController underTest;
//
//    @BeforeEach
//    void setUp() {
//        underTest = new StreamDataController(streamDataDao);
//    }
//
//    @Test
//    void getAllStreamsForStreamId() {
//        List<StreamDataResponseDto> data = List.of(
//                new StreamDataResponseDto(),
//                new StreamDataResponseDto(),
//                new StreamDataResponseDto()
//        );
//        Mockito.when(streamDataDao.getStreamDataForStreamId(Mockito.anyLong())).thenReturn(data);
//        StreamDataList actual = underTest.getAllStreamsForStreamId(0L);
//        assertEquals(data.size(), actual.getStreamData().size());
//    }
//
//    @Test
//    void addStreamData_notFoundChannel() {
//        StreamDataAddDto dto = new StreamDataAddDto();
//        Mockito.when(streamDataDao.addStreamData(0L, dto)).thenThrow(EntityNotFoundException.class);
//        ResponseEntity<StreamDataResponseDto> actual = underTest.addStreamData(0L, dto);
//        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
//    }
//
//    @Test
//    void addStreamData_streamHasEnded() {
//        StreamDataAddDto dto = new StreamDataAddDto();
//        Mockito.when(streamDataDao.addStreamData(0L, dto)).thenThrow(StreamHasEndedException.class);
//        ResponseEntity<StreamDataResponseDto> actual = underTest.addStreamData(0L, dto);
//        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
//    }
//
//    @Test
//    void addStreamData_valid() {
//        StreamDataAddDto dto = new StreamDataAddDto();
//        StreamDataResponseDto expected = new StreamDataResponseDto();
//        Mockito.when(streamDataDao.addStreamData(0L, dto)).thenReturn(expected);
//        ResponseEntity<StreamDataResponseDto> actual = underTest.addStreamData(0L, dto);
//        assertEquals(HttpStatus.OK, actual.getStatusCode());
//        assertEquals(expected, actual.getBody());
//    }
}