package com.github.he305.twitchproducer.application.dao;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StreamDataDaoImplTest {

//    @Mock
//    private StreamDataRepository streamDataRepository;
//    @Mock
//    private StreamRepository streamRepository;
//    @Mock
//    private StreamDataAddMapper addMapper;
//    @Mock
//    private StreamDataResponseMapper responseMapper;
//
//    private StreamDataDaoImpl underTest;
//
//    @BeforeEach
//    void setUp() {
//        underTest = new StreamDataDaoImpl(streamDataRepository, streamRepository, addMapper, responseMapper);
//    }
//
//    @Test
//    void addStreamData_streamNotFound() {
//        Mockito.when(streamRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
//        StreamDataAddDto data = new StreamDataAddDto();
//        assertThrows(EntityNotFoundException.class, () ->
//                underTest.addStreamData(0L, data));
//    }
//
//    @Test
//    void addStreamData_streamHasEnded() {
//        Stream stream = new Stream(
//                0L,
//                LocalDateTime.now(),
//                LocalDateTime.now(),
//                0,
//                null,
//                null
//        );
//
//        Mockito.when(streamRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(stream));
//        StreamDataAddDto data = new StreamDataAddDto();
//        assertThrows(StreamHasEndedException.class, () ->
//                underTest.addStreamData(0L, data));
//    }
//
//    @Test
//    void addStreamData_valid() {
//        Stream stream = new Stream();
//        Long streamId = 90L;
//        stream.setId(streamId);
//
//        Mockito.when(streamRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(stream));
//        LocalDateTime time = LocalDateTime.now();
//        StreamDataAddDto data = new StreamDataAddDto(
//                "test",
//                "test",
//                0,
//                time
//        );
//        StreamData streamData = new StreamData(
//                null,
//                "test",
//                "test",
//                0,
//                stream,
//                time
//        );
//        StreamData saved = new StreamData(
//                0L,
//                "test",
//                "test",
//                0,
//                stream,
//                time
//        );
//        StreamDataResponseDto responseDto = new StreamDataResponseDto(
//                0L,
//                "test",
//                "test",
//                0,
//                time,
//                streamId
//        );
//        Mockito.when(addMapper.toStreamData(data)).thenReturn(streamData);
//        Mockito.when(streamDataRepository.save(streamData)).thenReturn(saved);
//        Mockito.when(responseMapper.toDto(saved)).thenReturn(responseDto);
//
//        StreamDataResponseDto actual = underTest.addStreamData(0L, data);
//        assertEquals(responseDto, actual);
//    }
//
//    @Test
//    void getStreamDataForStreamId() {
//        LocalDateTime time = LocalDateTime.now();
//        Stream targetStream = new Stream(
//                0L,
//                time,
//                time,
//                0,
//                null,
//                null
//        );
//        Stream wrongStream = new Stream(
//                1L,
//                time,
//                time,
//                0,
//                null,
//                null
//        );
//
//        List<StreamData> streams = List.of(
//                new StreamData(
//                        0L,
//                        "test",
//                        "test",
//                        0,
//                        targetStream,
//                        time
//                ),
//                new StreamData(
//                        1L,
//                        "test",
//                        "test",
//                        0,
//                        wrongStream,
//                        time
//                ),
//                new StreamData(
//                        2L,
//                        "test",
//                        "test",
//                        0,
//                        wrongStream,
//                        time
//                )
//        );
//
//        StreamDataResponseDto expected = new StreamDataResponseDto(
//                0L,
//                "test",
//                "test",
//                0,
//                time,
//                0L
//        );
//        Mockito.when(streamDataRepository.findAll()).thenReturn(streams);
//        Mockito.when(responseMapper.toDto(streams.get(0))).thenReturn(expected);
//
//        List<StreamDataResponseDto> actual = underTest.getStreamDataForStreamId(0L);
//        assertEquals(1, actual.size());
//        assertEquals(expected, actual.get(0));
//    }
}