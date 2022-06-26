package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.StreamerListDto;
import com.github.he305.twitchproducer.common.dto.StreamerAddDto;
import com.github.he305.twitchproducer.common.dto.StreamerResponseDto;
import com.github.he305.twitchproducer.common.service.StreamerService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiVersionPathConstants.V1 + "streamer")
@AllArgsConstructor
public class StreamerController {
    @Autowired
    private final StreamerService streamerService;

    @GetMapping
    public StreamerListDto getAll() {
        return new StreamerListDto(streamerService.getAllStreamers());
    }

    @GetMapping("/{nickname}")
    public StreamerResponseDto getByName(@PathVariable String nickname) {
        return streamerService.getStreamerByName(nickname).orElse(new StreamerResponseDto());
    }

    @PostMapping
    public ResponseEntity<StreamerResponseDto> addStreamer(@RequestBody StreamerAddDto streamer) {
        StreamerResponseDto response = streamerService.addStreamer(streamer);
        if (response.getId() != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
