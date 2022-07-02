package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.StreamListDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.service.StreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/streams/current")
    public StreamListDto getCurrentStreams() {
        return new StreamListDto(streamService.getCurrentStreams());
    }

    @GetMapping("/streams/{streamId}")
    public ResponseEntity<StreamResponseDto> getStreamById(@PathVariable Long streamId) {
        Optional<StreamResponseDto> response = streamService.getStreamById(streamId);
        if (response.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response.get(), HttpStatus.OK);
    }
}
