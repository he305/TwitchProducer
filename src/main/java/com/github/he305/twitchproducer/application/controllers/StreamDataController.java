package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.StreamDataList;
import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.service.StreamDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiVersionPathConstants.V1)
@AllArgsConstructor
public class StreamDataController {

    private final StreamDataService streamDataService;

    @GetMapping("/stream/{streamId}/streamData")
    public StreamDataList getAllStreamsForStreamId(@PathVariable Long streamId) {
        return new StreamDataList(streamDataService.getStreamDataForStreamId(streamId));
    }

    @PostMapping("/stream/{streamId}/streamData")
    public ResponseEntity<StreamDataResponseDto> addStreamData(@PathVariable Long streamId, @RequestBody StreamDataAddDto dto) {
        try {
            StreamDataResponseDto response = streamDataService.addStreamData(streamId, dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
