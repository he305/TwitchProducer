package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.ChannelListDto;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.exception.EntityAlreadyExistsException;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.service.ChannelService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(ApiVersionPathConstants.V1)
@AllArgsConstructor
public class ChannelController {
    @Autowired
    private final ChannelService channelService;

    @GetMapping("/channel")
    public ChannelListDto getAll() {
        return new ChannelListDto(channelService.getAllChannels());
    }

    @GetMapping("/channel/name/{nickname}")
    public ResponseEntity<ChannelResponseDto> getByName(@PathVariable String nickname) {
        Optional<ChannelResponseDto> channel = channelService.getChannelByName(nickname);
        if (channel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(channel.get(), HttpStatus.OK);
    }

    @GetMapping("/channel/live")
    public ChannelListDto getLiveChannels() {
        return new ChannelListDto(channelService.getLiveChannels());
    }

    @DeleteMapping("/channel/{channelId}")
    public ResponseEntity<String> deleteChannel(@PathVariable Long channelId) {
        try {
            channelService.deleteChannel(channelId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/channel/{channelId}")
    public ResponseEntity<ChannelResponseDto> updateChannel(@PathVariable Long channelId, @RequestBody ChannelAddDto dto) {
        try {
            ChannelResponseDto res = channelService.updateChannel(channelId, dto);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/channel/id/{channelId}")
    public ResponseEntity<ChannelResponseDto> getById(@PathVariable Long channelId) {
        Optional<ChannelResponseDto> channel = channelService.getChannelById(channelId);
        if (channel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(channel.get(), HttpStatus.OK);
    }

    @GetMapping("/person/{personId}/channel/{channelName}")
    public ResponseEntity<ChannelResponseDto> getPersonChannelByName(@PathVariable Long personId, @PathVariable String channelName) {
        Optional<ChannelResponseDto> channel = channelService.getPersonChannelByName(personId, channelName);
        if (channel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(channel.get(), HttpStatus.OK);
    }

    @PostMapping("/person/{personId}/channel")
    public ResponseEntity<ChannelResponseDto> addChannel(@PathVariable Long personId, @RequestBody ChannelAddDto channel) {
        try {
            ChannelResponseDto response = channelService.addChannel(personId, channel);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException | EntityAlreadyExistsException | EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
