package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.mapper.ChannelResponseMapper;
import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.application.repositories.ChannelRepository;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.entities.Platform;
import com.github.he305.twitchproducer.common.entities.Channel;
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
    private ChannelRepository channelRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private ChannelAddMapper channelAddMapper;
    @Mock
    private ChannelResponseMapper channelResponseMapper;
    private ChannelServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new ChannelServiceImpl(channelRepository, personRepository, channelAddMapper, channelResponseMapper);
    }

    @Test
    void getChannelByName_nullName() {
        String data = null;

        assertThrows(NullPointerException.class, () -> underTest.getChannelByName(data));
    }

    @Test
    void getChannelByName_validName() {
        ChannelResponseDto expected = new ChannelResponseDto(1L, "1", Platform.TWITCH, null);

        Mockito.when(channelRepository.findByNickname(Mockito.any())).thenReturn(List.of(new Channel()));
        Mockito.when(channelResponseMapper.toDto(Mockito.any())).thenReturn(expected);

        Optional<ChannelResponseDto> actual = underTest.getChannelByName("");

        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void getAllChannels_emptyList() {
        List<ChannelResponseDto> expected = Collections.emptyList();
        Mockito.when(channelRepository.findAll()).thenReturn(Collections.emptyList());

        List<ChannelResponseDto> actual = underTest.getAllChannels();
        assertEquals(expected, actual);
    }

    @Test
    void addChannel_emptyName() {
        ChannelAddDto data = new ChannelAddDto("", Platform.TWITCH, 0L);
        ChannelResponseDto expected = new ChannelResponseDto();
        ChannelResponseDto actual = underTest.addChannel(data);
        assertEquals(expected, actual);
    }

    @Test
    void addChannel_personNotExist() {
        ChannelAddDto data = new ChannelAddDto("test", Platform.TWITCH, 0L);
        Mockito.when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        ChannelResponseDto expected = new ChannelResponseDto();
        ChannelResponseDto actual = underTest.addChannel(data);
        assertEquals(expected, actual);
    }

    @Test
    void addChannel_alreadyExistsName() {
        String nickname = "1";
        ChannelAddDto data = new ChannelAddDto(nickname, Platform.TWITCH, 0L);
        ChannelResponseDto expected = new ChannelResponseDto();
        Mockito.when(channelRepository.findByNickname(nickname)).thenReturn(List.of(new Channel()));
        ChannelResponseDto actual = underTest.addChannel(data);
        assertEquals(expected, actual);
    }

    @Test
    void addChannel_validFull() {
        ChannelAddDto data = new ChannelAddDto("1", Platform.TWITCH, 0L);
        Person expectedPerson = new Person();
        ChannelResponseDto expected = new ChannelResponseDto(0L, "1", Platform.TWITCH, "");
        Mockito.when(channelRepository.save(Mockito.any())).thenReturn(new Channel());
        Mockito.when(personRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(expectedPerson));
        Mockito.when(channelAddMapper.getChannel(data)).thenReturn(new Channel());
        Mockito.when(channelResponseMapper.toDto(Mockito.any())).thenReturn(expected);
        ChannelResponseDto actual = underTest.addChannel(data);
        assertEquals(expected, actual);
    }
}