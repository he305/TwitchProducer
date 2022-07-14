package com.github.he305.twitchproducer.application.service;

import com.github.he305.twitchproducer.application.dto.StreamEndRequest;
import com.github.he305.twitchproducer.common.dao.ChannelDao;
import com.github.he305.twitchproducer.common.dao.StreamDao;
import com.github.he305.twitchproducer.common.dao.StreamDataDao;
import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.entities.Platform;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.mapper.StreamResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StreamServiceImplTest {

    @Mock
    private StreamDao streamDao;
    @Mock
    private StreamDataDao streamDataDao;
    @Mock
    private ChannelDao channelDao;
    @Mock
    private StreamResponseMapper responseMapper;

    private StreamServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new StreamServiceImpl(streamDao, streamDataDao, channelDao, responseMapper);
    }

    @Test
    void addStreamData_noChannelFound() {
        Long channelId = 0L;
        StreamDataAddDto dto = new StreamDataAddDto();
        Mockito.when(channelDao.get(channelId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                underTest.addStreamData(0L, dto));
    }

    @Test
    void addStreamData_createNewStream() {
        LocalDateTime time = LocalDateTime.now();
        StreamDataAddDto dto = new StreamDataAddDto("", "", 0, time);
        Channel existedChannel = new Channel(0L, "test", Platform.TWITCH, false, new Person(), List.of());
        Channel savedChannel = new Channel(0L, "test", Platform.TWITCH, true, new Person(), new ArrayList<>());
        Stream newStream = new Stream(1L, time, null, 0, new ArrayList<>(), savedChannel);
        StreamResponseDto expected = new StreamResponseDto();

        Mockito.when(channelDao.get(Mockito.anyLong())).thenReturn(Optional.of(existedChannel));
        Mockito.when(channelDao.updateIsLive(existedChannel, true)).thenReturn(savedChannel);
        Mockito.when(streamDao.getCurrentStreamForChannel(savedChannel)).thenReturn(Optional.empty());
        Mockito.when(streamDao.save(Mockito.any())).thenReturn(newStream);
        Mockito.when(streamDao.get(newStream.getId())).thenReturn(Optional.of(newStream));
        Mockito.when(responseMapper.toDto(newStream)).thenReturn(expected);

        StreamResponseDto actual = underTest.addStreamData(0L, dto);
        assertEquals(expected, actual);
    }

    @Test
    void addStreamData_existedStream_withoutUpdate() {
        LocalDateTime time = LocalDateTime.now();
        StreamDataAddDto dto = new StreamDataAddDto("", "", 0, time);
        Channel existedChannel = new Channel(0L, "test", Platform.TWITCH, false, new Person(), List.of());
        Channel savedChannel = new Channel(0L, "test", Platform.TWITCH, true, new Person(), new ArrayList<>());
        Stream existedStream = new Stream(1L, time, null, 0, new ArrayList<>(), savedChannel);
        StreamResponseDto expected = new StreamResponseDto();

        Mockito.when(channelDao.get(Mockito.anyLong())).thenReturn(Optional.of(existedChannel));
        Mockito.when(channelDao.updateIsLive(existedChannel, true)).thenReturn(savedChannel);
        Mockito.when(streamDao.getCurrentStreamForChannel(savedChannel)).thenReturn(Optional.of(existedStream));
        Mockito.when(streamDao.get(existedStream.getId())).thenReturn(Optional.of(existedStream));
        Mockito.when(responseMapper.toDto(existedStream)).thenReturn(expected);

        StreamResponseDto actual = underTest.addStreamData(0L, dto);
        assertEquals(expected, actual);
    }

    @Test
    void addStreamData_existedStream_withUpdate() {
        LocalDateTime time = LocalDateTime.now();
        StreamDataAddDto dto = new StreamDataAddDto("", "", 1, time);
        Channel existedChannel = new Channel(0L, "test", Platform.TWITCH, false, new Person(), List.of());
        Channel savedChannel = new Channel(0L, "test", Platform.TWITCH, true, new Person(), new ArrayList<>());
        Stream existedStream = new Stream(1L, time, null, 0, null, savedChannel);
        Stream updatedStream = new Stream(1L, time, null, 1, null, savedChannel);
        StreamResponseDto expected = new StreamResponseDto();

        Mockito.when(channelDao.get(Mockito.anyLong())).thenReturn(Optional.of(existedChannel));
        Mockito.when(channelDao.updateIsLive(existedChannel, true)).thenReturn(savedChannel);
        Mockito.when(streamDao.getCurrentStreamForChannel(savedChannel)).thenReturn(Optional.of(existedStream));
        Mockito.when(streamDao.get(existedStream.getId())).thenReturn(Optional.of(existedStream));
        Mockito.when(streamDao.save(existedStream)).thenReturn(updatedStream);
        Mockito.when(responseMapper.toDto(updatedStream)).thenReturn(expected);

        StreamResponseDto actual = underTest.addStreamData(0L, dto);
        assertEquals(expected, actual);
    }

    @Test
    void endStream_channelNotFound() {
        StreamEndRequest req = new StreamEndRequest();
        Mockito.when(channelDao.get(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                underTest.endStream(0L, req));
    }

    @Test
    void endStream_nullChannelId() {
        Long id = null;
        StreamEndRequest req = new StreamEndRequest();
        assertThrows(NullPointerException.class, () ->
                underTest.endStream(id, req));
    }

    @Test
    void endStream_nullStreamEndRequest() {
        Long id = 0L;
        StreamEndRequest req = null;
        assertThrows(NullPointerException.class, () ->
                underTest.endStream(id, req));
    }

    @Test
    void endStream_noCurrentStream() {
        Channel channel = new Channel();
        StreamEndRequest req = new StreamEndRequest();
        Channel updatedChannel = new Channel();

        Mockito.when(channelDao.get(Mockito.anyLong())).thenReturn(Optional.of(channel));
        Mockito.when(channelDao.updateIsLive(channel, false)).thenReturn(updatedChannel);
        Mockito.when(streamDao.getCurrentStreamForChannel(updatedChannel)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                underTest.endStream(0L, req));
    }

    @Test
    void endStream_valid() {
        StreamEndRequest req = new StreamEndRequest(LocalDateTime.now());
        Channel updatedChannel = new Channel();
        LocalDateTime startTime = LocalDateTime.now();
        Stream currentStream = new Stream(0L, startTime, null, 0, null, updatedChannel);
        Stream updatedStream = new Stream(0L, startTime, req.getTime(), 0, null, updatedChannel);
        StreamResponseDto expected = new StreamResponseDto(0L, startTime, req.getTime(), 0, 0L, null);

        Mockito.when(channelDao.get(Mockito.anyLong())).thenReturn(Optional.of(new Channel()));
        Mockito.when(channelDao.updateIsLive(updatedChannel, false)).thenReturn(updatedChannel);
        Mockito.when(streamDao.getCurrentStreamForChannel(updatedChannel)).thenReturn(Optional.of(currentStream));
        Mockito.when(streamDao.save(updatedStream)).thenReturn(updatedStream);
        Mockito.when(responseMapper.toDto(updatedStream)).thenReturn(expected);

        StreamResponseDto actual = underTest.endStream(0L, req);
        assertEquals(expected, actual);
    }

    @Test
    void getAllStreams() {
        StreamResponseDto placeholder = new StreamResponseDto();
        List<StreamResponseDto> expected = List.of(
                placeholder,
                placeholder,
                placeholder
        );
        int expectedSize = expected.size();

        Stream expectedStream = new Stream();
        List<Stream> expectedStreams = List.of(
                expectedStream,
                expectedStream,
                expectedStream
        );

        Mockito.when(streamDao.getAll()).thenReturn(expectedStreams);
        Mockito.when(responseMapper.toDto(expectedStream)).thenReturn(placeholder);

        List<StreamResponseDto> actual = underTest.getAllStreams();
        assertEquals(expectedSize, actual.size());
        actual.forEach(c -> assertEquals(placeholder, c));
    }

    @Test
    void getStreamById_notFound() {
        Mockito.when(streamDao.get(Mockito.anyLong())).thenReturn(Optional.empty());
        Optional<StreamResponseDto> actual = underTest.getStreamById(0L);
        assertTrue(actual.isEmpty());
    }

    @Test
    void getStreamById_found() {
        Stream expectedStream = new Stream();
        Mockito.when(streamDao.get(Mockito.anyLong())).thenReturn(Optional.of(expectedStream));
        StreamResponseDto expected = new StreamResponseDto();
        Mockito.when(responseMapper.toDto(expectedStream)).thenReturn(expected);

        Optional<StreamResponseDto> actual = underTest.getStreamById(0L);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getStreamById_null() {
        assertThrows(NullPointerException.class, () ->
                underTest.getStreamById(null));
    }

    @Test
    void getCurrentStreams() {
        StreamResponseDto placeholder = new StreamResponseDto();
        List<StreamResponseDto> expected = List.of(
                placeholder,
                placeholder,
                placeholder
        );
        int expectedSize = expected.size();

        Stream expectedStream = new Stream();
        List<Stream> expectedStreams = List.of(
                expectedStream,
                expectedStream,
                expectedStream
        );

        Mockito.when(streamDao.getCurrentStreams()).thenReturn(expectedStreams);
        Mockito.when(responseMapper.toDto(expectedStream)).thenReturn(placeholder);

        List<StreamResponseDto> actual = underTest.getCurrentStreams();
        assertEquals(expectedSize, actual.size());
        actual.forEach(c -> assertEquals(placeholder, c));
    }
}