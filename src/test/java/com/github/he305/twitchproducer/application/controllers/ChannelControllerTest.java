package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.dto.ChannelListDto;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.entities.Platform;
import com.github.he305.twitchproducer.common.service.ChannelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ChannelControllerTest {

    @Mock
    private ChannelService channelService;
    private ChannelController underTest;

    @BeforeEach
    void setUp() {
        underTest = new ChannelController(channelService);
    }

    @Test
    void getAll() {
        List<ChannelResponseDto> expected = Collections.emptyList();
        Mockito.when(channelService.getAllChannels()).thenReturn(expected);

        ChannelListDto actual = underTest.getAll();
        assertEquals(expected, actual.getChannels());
    }

    @Test
    void getByName_someData() {
        String data = "1";
        ChannelResponseDto expected = new ChannelResponseDto(1L, "1", Platform.TWITCH, null);
        Mockito.when(channelService.getChannelByName(data)).thenReturn(Optional.of(expected));
        ChannelResponseDto actual = underTest.getByName(data);
        assertEquals(expected, actual);
    }

    @Test
    void getByName_noData() {
        String data = "1";
        ChannelResponseDto expected = new ChannelResponseDto();
        Mockito.when(channelService.getChannelByName(data)).thenReturn(Optional.empty());

        ChannelResponseDto actual = underTest.getByName(data);
        assertEquals(expected, actual);
    }

    @Test
    void getPersonChannelByName_found() {
        String nickname = "";
        ChannelResponseDto expected = new ChannelResponseDto(0L, "", Platform.TWITCH, "");
        Mockito.when(channelService.getPersonChannelByName(0L, nickname)).thenReturn(Optional.of(expected));
        ResponseEntity<ChannelResponseDto> actual = underTest.getPersonChannelByName(0L, nickname);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void getPersonChannelByName_notFound() {
        String nickname = "";
        Optional<ChannelResponseDto> expected = Optional.empty();
        Mockito.when(channelService.getPersonChannelByName(0L, nickname)).thenReturn(expected);
        ResponseEntity<ChannelResponseDto> actual = underTest.getPersonChannelByName(0L, nickname);
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void addChannel_validInput() {
        String nickname = "test";
        ChannelAddDto channelAddDto = new ChannelAddDto(nickname, Platform.TWITCH);
        ChannelResponseDto expected = new ChannelResponseDto(0L, nickname, Platform.TWITCH, "");
        Mockito.when(channelService.addChannel(0L, channelAddDto)).thenReturn(expected);

        ResponseEntity<ChannelResponseDto> actual = underTest.addChannel(0L, channelAddDto);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void addChannel_serviceError() {
        String nickname = "test";
        ChannelAddDto data = new ChannelAddDto(nickname, Platform.TWITCH);
        ResponseEntity<ChannelResponseDto> expected = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        Mockito.when(channelService.addChannel(0L, data)).thenThrow(IllegalArgumentException.class);

        ResponseEntity<ChannelResponseDto> actual = underTest.addChannel(0L, data);
        assertEquals(expected, actual);
    }
}