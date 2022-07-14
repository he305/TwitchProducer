package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.StreamEndRequest;
import com.github.he305.twitchproducer.application.dto.StreamListDto;
import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.service.StreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(ApiVersionPathConstants.V1)
@RequiredArgsConstructor
public class StreamController {
    private final StreamService streamService;

    @GetMapping("/stream")
    public StreamListDto getAllStreams() {
        return new StreamListDto(streamService.getAllStreams());
    }

    @GetMapping("/stream/current")
    public StreamListDto getCurrentStreams() {
        return new StreamListDto(streamService.getCurrentStreams());
    }

    @GetMapping("/stream/{streamId}")
    public ResponseEntity<StreamResponseDto> getStreamById(@PathVariable Long streamId) {
        Optional<StreamResponseDto> response = streamService.getStreamById(streamId);
        if (response.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response.get(), HttpStatus.OK);
    }

    @PostMapping("/channel/{channelId}/streamData")
    public ResponseEntity<StreamResponseDto> addStreamData(@PathVariable Long channelId, @RequestBody StreamDataAddDto dto) {
        try {
            StreamResponseDto response = streamService.addStreamData(channelId, dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/channel/{channelId}/end")
    public ResponseEntity<StreamResponseDto> endStream(@PathVariable Long channelId, @RequestBody StreamEndRequest req) {
        try {
            StreamResponseDto response = streamService.endStream(channelId, req);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
