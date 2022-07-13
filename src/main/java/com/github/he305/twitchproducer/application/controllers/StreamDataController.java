package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.StreamDataList;
import com.github.he305.twitchproducer.common.dao.StreamDataDao;
import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.exception.StreamHasEndedException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiVersionPathConstants.V1)
@AllArgsConstructor
public class StreamDataController {

    private final StreamDataDao streamDataDao;

    @GetMapping("/stream/{streamId}/streamData")
    public StreamDataList getAllStreamsForStreamId(@PathVariable Long streamId) {
        return new StreamDataList(streamDataDao.getStreamDataForStreamId(streamId));
    }

    @PostMapping("/stream/{streamId}/streamData")
    public ResponseEntity<StreamDataResponseDto> addStreamData(@PathVariable Long streamId, @RequestBody StreamDataAddDto dto) {
        try {
            StreamDataResponseDto response = streamDataDao.addStreamData(streamId, dto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException | StreamHasEndedException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
