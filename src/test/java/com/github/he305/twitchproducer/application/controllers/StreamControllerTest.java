package com.github.he305.twitchproducer.application.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StreamControllerTest {
//
//    @Mock
//    private StreamDao streamDao;
//
//    private StreamController underTest;
//
//    @BeforeEach
//    void setUp() {
//        underTest = new StreamController(streamDao);
//    }
//
//    @Test
//    void getAllStreams() {
//        List<StreamResponseDto> listData = List.of(
//                new StreamResponseDto(),
//                new StreamResponseDto(),
//                new StreamResponseDto()
//        );
//        StreamListDto expected = new StreamListDto(listData);
//        Mockito.when(streamDao.getAllStreams()).thenReturn(listData);
//
//        StreamListDto actual = underTest.getAllStreams();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void getCurrentStreams() {
//        List<StreamResponseDto> listData = List.of(
//                new StreamResponseDto(),
//                new StreamResponseDto(),
//                new StreamResponseDto()
//        );
//        StreamListDto expected = new StreamListDto(listData);
//        Mockito.when(streamDao.getCurrentStreams()).thenReturn(listData);
//
//        StreamListDto actual = underTest.getCurrentStreams();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void getStreamById_notFound() {
//        Mockito.when(streamDao.getStreamById(Mockito.anyLong())).thenReturn(Optional.empty());
//        ResponseEntity<StreamResponseDto> actual = underTest.getStreamById(0L);
//        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
//    }
//
//    @Test
//    void getStreamById_found() {
//        StreamResponseDto expected = new StreamResponseDto();
//        Mockito.when(streamDao.getStreamById(Mockito.anyLong())).thenReturn(Optional.of(expected));
//        ResponseEntity<StreamResponseDto> actual = underTest.getStreamById(0L);
//        assertEquals(HttpStatus.OK, actual.getStatusCode());
//        assertEquals(expected, actual.getBody());
//    }
//
//    @Test
//    void addStream_channelNotFound() {
//        StreamAddDto dto = new StreamAddDto();
//        Mockito.when(streamDao.addStream(0L, dto)).thenThrow(EntityNotFoundException.class);
//        ResponseEntity<StreamResponseDto> actual = underTest.addStream(0L, dto);
//        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
//    }
//
//    @Test
//    void addStream_valid() {
//        StreamAddDto dto = new StreamAddDto();
//        StreamResponseDto expected = new StreamResponseDto();
//        Mockito.when(streamDao.addStream(0L, dto)).thenReturn(expected);
//        ResponseEntity<StreamResponseDto> actual = underTest.addStream(0L, dto);
//        assertEquals(HttpStatus.OK, actual.getStatusCode());
//        assertEquals(expected, actual.getBody());
//    }
//
//    @Test
//    void endStream_notFound() {
//        LocalDateTime time = LocalDateTime.now();
//        StreamEndRequest req = new StreamEndRequest(time);
//        Mockito.when(streamDao.endStream(0L, time)).thenThrow(EntityNotFoundException.class);
//        ResponseEntity<StreamResponseDto> actual = underTest.endStream(0L, req);
//        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
//    }
//
//    @Test
//    void endStream_valid() {
//        LocalDateTime time = LocalDateTime.now();
//        StreamEndRequest req = new StreamEndRequest(time);
//        StreamResponseDto expected = new StreamResponseDto();
//        Mockito.when(streamDao.endStream(0L, time)).thenReturn(expected);
//        ResponseEntity<StreamResponseDto> actual = underTest.endStream(0L, req);
//        assertEquals(HttpStatus.OK, actual.getStatusCode());
//        assertEquals(expected, actual.getBody());
//    }
}