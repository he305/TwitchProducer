package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.repositories.StreamDataRepository;
import com.github.he305.twitchproducer.application.repositories.StreamRepository;
import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.mapper.StreamDataAddMapper;
import com.github.he305.twitchproducer.common.mapper.StreamDataResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class StreamDataServiceImplTest {

    @Mock
    private StreamDataRepository streamDataRepository;
    @Mock
    private StreamRepository streamRepository;
    @Mock
    private StreamDataAddMapper addMapper;
    @Mock
    private StreamDataResponseMapper responseMapper;

    private StreamDataServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamDataServiceImpl(streamDataRepository, streamRepository, addMapper, responseMapper);
    }

    @Test
    void addStreamData_streamNotFound() {
//        Optional<Stream> stream = streamRepository.findById(streamId);
//        if (stream.isEmpty())
//            throw new EntityNotFoundException();
//
//        StreamData streamData = addMapper.toStreamData(dto);
//        streamData.setStream(stream.get());
//        StreamData saved = streamDataRepository.save(streamData);
//        return responseMapper.toDto(saved);

        Mockito.when(streamRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        StreamDataAddDto data = new StreamDataAddDto();
        assertThrows(EntityNotFoundException.class, () ->
                underTest.addStreamData(0L, data));
    }

    @Test
    void addStreamData_valid() {
        Stream stream = new Stream();
        Mockito.when(streamRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(stream));
        LocalDateTime time = LocalDateTime.now();
        StreamDataAddDto data = new StreamDataAddDto(
                "test",
                "test",
                0,
                time
        );
        StreamData streamData = new StreamData(
                null,
                "test",
                "test",
                0,
                stream,
                time
        );
        StreamData saved = new StreamData(
                0L,
                "test",
                "test",
                0,
                stream,
                time
        );
        StreamDataResponseDto responseDto = new StreamDataResponseDto(
                0L,
                "test",
                "test",
                0,
                time
        );
        Mockito.when(addMapper.toStreamData(data)).thenReturn(streamData);
        Mockito.when(streamDataRepository.save(streamData)).thenReturn(saved);
        Mockito.when(responseMapper.toDto(saved)).thenReturn(responseDto);

        StreamDataResponseDto actual = underTest.addStreamData(0L, data);
        assertEquals(responseDto, actual);
    }

    @Test
    void getStreamDataForStreamId() {
        LocalDateTime time = LocalDateTime.now();
        Stream targetStream = new Stream(
                0L,
                time,
                time,
                0,
                null,
                null
        );
        Stream wrongStream = new Stream(
                1L,
                time,
                time,
                0,
                null,
                null
        );

        List<StreamData> streams = List.of(
                new StreamData(
                        0L,
                        "test",
                        "test",
                        0,
                        targetStream,
                        time
                ),
                new StreamData(
                        1L,
                        "test",
                        "test",
                        0,
                        wrongStream,
                        time
                ),
                new StreamData(
                        2L,
                        "test",
                        "test",
                        0,
                        wrongStream,
                        time
                )
        );

        StreamDataResponseDto expected = new StreamDataResponseDto(
                0L,
                "test",
                "test",
                0,
                time
        );
        Mockito.when(streamDataRepository.findAll()).thenReturn(streams);
        Mockito.when(responseMapper.toDto(streams.get(0))).thenReturn(expected);

        List<StreamDataResponseDto> actual = underTest.getStreamDataForStreamId(0L);
        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }
}