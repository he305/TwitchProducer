package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.ChannelListDto;
import com.github.he305.twitchproducer.common.dao.ChannelDao;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
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
    private final ChannelDao channelDao;

    @GetMapping("/channel")
    public ChannelListDto getAll() {
        return new ChannelListDto(channelDao.getAllChannels());
    }

    @GetMapping("/channel/name/{nickname}")
    public ResponseEntity<ChannelResponseDto> getByName(@PathVariable String nickname) {
        Optional<ChannelResponseDto> channel = channelDao.getChannelByName(nickname);
        if (channel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(channel.get(), HttpStatus.OK);
    }

    @DeleteMapping("/channel/{channelId}")
    public ResponseEntity<String> deleteChannel(@PathVariable Long channelId) {
        try {
            channelDao.deleteChannel(channelId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/channel/{channelId}")
    public ResponseEntity<ChannelResponseDto> updateChannel(@PathVariable Long channelId, @RequestBody ChannelAddDto dto) {
        try {
            ChannelResponseDto res = channelDao.updateChannel(channelId, dto);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/channel/id/{channelId}")
    public ResponseEntity<ChannelResponseDto> getById(@PathVariable Long channelId) {
        Optional<ChannelResponseDto> channel = channelDao.getChannelById(channelId);
        if (channel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(channel.get(), HttpStatus.OK);
    }

    @GetMapping("/person/{personId}/channel/{channelName}")
    public ResponseEntity<ChannelResponseDto> getPersonChannelByName(@PathVariable Long personId, @PathVariable String channelName) {
        Optional<ChannelResponseDto> channel = channelDao.getPersonChannelByName(personId, channelName);
        if (channel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(channel.get(), HttpStatus.OK);
    }

    @PostMapping("/person/{personId}/channel")
    public ResponseEntity<ChannelResponseDto> addChannel(@PathVariable Long personId, @RequestBody ChannelAddDto channel) {
        try {
            ChannelResponseDto response = channelDao.addChannel(personId, channel);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException | EntityExistsException | EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
