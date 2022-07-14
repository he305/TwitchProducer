package com.github.he305.twitchproducer.application.service;

import com.github.he305.twitchproducer.application.mapper.ChannelResponseMapper;
import com.github.he305.twitchproducer.common.dao.ChannelDao;
import com.github.he305.twitchproducer.common.dao.PersonDao;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.entities.Platform;
import com.github.he305.twitchproducer.common.exception.EntityAlreadyExistsException;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.mapper.ChannelAddMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChannelServiceImplTest {
    @Mock
    private ChannelDao channelDao;
    @Mock
    private PersonDao personDao;
    @Mock
    private ChannelAddMapper channelAddMapper;
    @Mock
    private ChannelResponseMapper channelResponseMapper;
    private ChannelServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new ChannelServiceImpl(channelDao, personDao, channelAddMapper, channelResponseMapper);
    }

    @Test
    void getChannelByName_nullName() {
        String data = null;

        assertThrows(NullPointerException.class, () -> underTest.getChannelByName(data));
    }

    @Test
    void getChannelByName_notFound() {
        Mockito.when(channelDao.getChannelByName(Mockito.any())).thenReturn(List.of());
        Optional<ChannelResponseDto> expected = Optional.empty();
        Optional<ChannelResponseDto> actual = underTest.getChannelByName("test");
        assertEquals(expected, actual);
    }

    @Test
    void getChannelByName_validName() {
        ChannelResponseDto expected = new ChannelResponseDto(1L, "1", Platform.TWITCH, null);

        Mockito.when(channelDao.getChannelByName(Mockito.any())).thenReturn(List.of(new Channel()));
        Mockito.when(channelResponseMapper.toDto(Mockito.any())).thenReturn(expected);

        Optional<ChannelResponseDto> actual = underTest.getChannelByName("");

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getChannelById_notFound() {
        Mockito.when(channelDao.get(Mockito.any())).thenReturn(Optional.empty());
        Optional<ChannelResponseDto> expected = Optional.empty();
        Optional<ChannelResponseDto> actual = underTest.getChannelById(0L);
        assertEquals(expected, actual);
    }

    @Test
    void getChannelById_validName() {
        ChannelResponseDto expected = new ChannelResponseDto(1L, "1", Platform.TWITCH, null);

        Mockito.when(channelDao.get(Mockito.any())).thenReturn(Optional.of(new Channel()));
        Mockito.when(channelResponseMapper.toDto(Mockito.any())).thenReturn(expected);

        Optional<ChannelResponseDto> actual = underTest.getChannelById(0L);

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getChannelById_nullId() {
        assertThrows(NullPointerException.class, () ->
                underTest.getChannelById(null));
    }

    @Test
    void getAllChannels_emptyList() {
        List<ChannelResponseDto> expected = Collections.emptyList();
        Mockito.when(channelDao.getAll()).thenReturn(Collections.emptyList());

        List<ChannelResponseDto> actual = underTest.getAllChannels();
        assertEquals(expected, actual);
    }

    @Test
    void getPersonChannelByName_nullId() {
        assertThrows(NullPointerException.class, () ->
                underTest.getPersonChannelByName(null, "123"));
    }

    @Test
    void getPersonChannelByName_nullNickname() {
        assertThrows(NullPointerException.class, () ->
                underTest.getPersonChannelByName(0L, null));
    }

    @Test
    void getPersonChannelByName_notFoundNickname() {
        Mockito.when(channelDao.getChannelByName(Mockito.any())).thenReturn(List.of());
        Optional<ChannelResponseDto> expected = Optional.empty();
        Optional<ChannelResponseDto> actual = underTest.getPersonChannelByName(0L, "smth");
        assertEquals(expected, actual);
    }

    @Test
    void getPersonChannelByName_noPerson() {
        Long notExistingPersonId = 2L;
        String existingNickname = "test";
        List<Channel> existingChannels = List.of(
                new Channel(0L, existingNickname, Platform.TWITCH, false, new Person(0L, "test", "test", Collections.emptyList()), Collections.emptyList()),
                new Channel(1L, existingNickname, Platform.TWITCH, false, new Person(1L, "test", "test", Collections.emptyList()), Collections.emptyList())
        );
        Mockito.when(channelDao.getChannelByName(Mockito.any())).thenReturn(existingChannels);
        Optional<ChannelResponseDto> expected = Optional.empty();
        Optional<ChannelResponseDto> actual = underTest.getPersonChannelByName(notExistingPersonId, existingNickname);
        assertEquals(expected, actual);
    }

    @Test
    void getPersonChannelByName_valid() {
        Long existingPersonId = 1L;
        String existingNickname = "test";
        List<Channel> existingChannels = List.of(
                new Channel(0L, existingNickname, Platform.TWITCH, false, new Person(1L, "test", "test", Collections.emptyList()), Collections.emptyList()),
                new Channel(1L, existingNickname, Platform.TWITCH, false, new Person(2L, "test1", "test2", Collections.emptyList()), Collections.emptyList())
        );
        Mockito.when(channelDao.getChannelByName(Mockito.any())).thenReturn(existingChannels);
        ChannelResponseDto expected = new ChannelResponseDto(0L, existingNickname, Platform.TWITCH, "");
        Mockito.when(channelResponseMapper.toDto(Mockito.any())).thenReturn(expected);
        Optional<ChannelResponseDto> actual = underTest.getPersonChannelByName(existingPersonId, existingNickname);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void addChannel_nullId() {
        ChannelAddDto data = new ChannelAddDto();
        assertThrows(NullPointerException.class, () ->
                underTest.addChannel(null, data));
    }

    @Test
    void addChannel_nullChannelDto() {
        assertThrows(NullPointerException.class, () ->
                underTest.addChannel(0L, null));
    }

    @Test
    void addChannel_noPersonFound() {
        Mockito.when(personDao.get(Mockito.anyLong())).thenReturn(Optional.empty());
        ChannelAddDto data = new ChannelAddDto("1", Platform.TWITCH);
        assertThrows(EntityNotFoundException.class, () ->
                underTest.addChannel(0L, data));
    }

    @Test
    void addChannel_alreadyExistsName() {
        Person existingPerson = new Person(0L, "", "", Collections.emptyList());
        Mockito.when(personDao.get(Mockito.anyLong())).thenReturn(Optional.of(existingPerson));
        Channel existingChannel = new Channel(0L, "", Platform.TWITCH, false, existingPerson, Collections.emptyList());
        Mockito.when(channelDao.getChannelByName(Mockito.any())).thenReturn(List.of(existingChannel));
        Mockito.when(channelResponseMapper.toDto(Mockito.any())).thenReturn(new ChannelResponseDto());

        Long dataId = existingPerson.getId();
        ChannelAddDto data = new ChannelAddDto("test", Platform.TWITCH);
        assertThrows(EntityAlreadyExistsException.class, () ->
                underTest.addChannel(dataId, data));
    }

    @Test
    void addChannel_emptyName() {
        ChannelAddDto data = new ChannelAddDto("", Platform.TWITCH);
        assertThrows(IllegalArgumentException.class, () ->
                underTest.addChannel(0L, data));
    }

    @Test
    void addChannel_validFull() {
        ChannelAddDto data = new ChannelAddDto("1", Platform.TWITCH);

        Person expectedPerson = new Person(0L, "", "", Collections.emptyList());

        ChannelResponseDto expected = new ChannelResponseDto(0L, "1", Platform.TWITCH, "");
        Mockito.when(personDao.get(Mockito.anyLong())).thenReturn(Optional.of(expectedPerson));
        Mockito.when(channelDao.getChannelByName(Mockito.any())).thenReturn(List.of());

        Mockito.when(channelAddMapper.getChannel(data)).thenReturn(new Channel());
        Mockito.when(channelDao.save(Mockito.any())).thenReturn(new Channel());
        Mockito.when(channelResponseMapper.toDto(Mockito.any())).thenReturn(expected);
        ChannelResponseDto actual = underTest.addChannel(0L, data);
        assertEquals(expected, actual);
    }

    @Test
    void deleteChannel_notFound() {
        Mockito.when(channelDao.get(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                underTest.deleteChannel(0L));
    }

    @Test
    void deletePerson_success() {
        Mockito.when(channelDao.get(Mockito.anyLong())).thenReturn(Optional.of(new Channel()));
        assertDoesNotThrow(() -> underTest.deleteChannel(0L));
    }

    @Test
    void updateChannel_notFound() {
        ChannelAddDto dto = new ChannelAddDto();
        Mockito.when(channelDao.get(Mockito.anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                underTest.updateChannel(0L, dto));
    }

    @Test
    void updateChannel_success() {
        ChannelAddDto dto = new ChannelAddDto(
                "new",
                Platform.GOODGAME
        );
        Channel existing = new Channel(
                0L,
                "old",
                Platform.TWITCH,
                false,
                null,
                null
        );
        Channel changed = new Channel(
                0L,
                "new",
                Platform.GOODGAME,
                false,
                null,
                null
        );
        ChannelResponseDto expected = new ChannelResponseDto(
                0L,
                "new",
                Platform.GOODGAME,
                ""
        );

        Mockito.when(channelDao.get(0L)).thenReturn(Optional.of(existing));
        Mockito.when(channelDao.save(changed)).thenReturn(changed);
        Mockito.when(channelResponseMapper.toDto(changed)).thenReturn(expected);
        ChannelResponseDto actual = underTest.updateChannel(0L, dto);
        assertEquals(actual, expected);
    }
}