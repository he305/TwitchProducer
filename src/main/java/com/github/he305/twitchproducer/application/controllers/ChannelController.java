package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.ChannelListDto;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;
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

    @GetMapping("/channel/{nickname}")
    public ChannelResponseDto getByName(@PathVariable String nickname) {
        return channelService.getChannelByName(nickname).orElse(new ChannelResponseDto());
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
        } catch (IllegalArgumentException | EntityExistsException | EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
