package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.ChannelListDto;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.service.ChannelService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/channel")
    public ResponseEntity<ChannelResponseDto> addChannel(@RequestBody ChannelAddDto channel) {
        ChannelResponseDto response = channelService.addChannel(channel);
        if (response.getId() != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
