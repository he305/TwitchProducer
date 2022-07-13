package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.StreamEndRequest;
import com.github.he305.twitchproducer.application.dto.StreamListDto;
import com.github.he305.twitchproducer.common.dao.StreamDao;
import com.github.he305.twitchproducer.common.dto.StreamAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(ApiVersionPathConstants.V1)
@RequiredArgsConstructor
public class StreamController {
    private final StreamDao streamDao;

    @GetMapping("/stream")
    public StreamListDto getAllStreams() {
        return new StreamListDto(streamDao.getAllStreams());
    }

    @GetMapping("/stream/current")
    public StreamListDto getCurrentStreams() {
        return new StreamListDto(streamDao.getCurrentStreams());
    }

    @GetMapping("/stream/{streamId}")
    public ResponseEntity<StreamResponseDto> getStreamById(@PathVariable Long streamId) {
        Optional<StreamResponseDto> response = streamDao.getStreamById(streamId);
        if (response.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(response.get(), HttpStatus.OK);
    }

    @PostMapping("/channel/{channelId}/stream")
    public ResponseEntity<StreamResponseDto> addStream(@PathVariable Long channelId, @RequestBody StreamAddDto dto) {
        try {
            StreamResponseDto response = streamDao.addStream(channelId, dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/stream/{streamId}/end")
    public ResponseEntity<StreamResponseDto> endStream(@PathVariable Long streamId, @RequestBody StreamEndRequest req) {
        try {
            StreamResponseDto response = streamDao.endStream(streamId, req.getTime());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
