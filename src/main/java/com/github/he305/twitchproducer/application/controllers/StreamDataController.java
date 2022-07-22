package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiVersionPathConstants.V1)
@AllArgsConstructor
public class StreamDataController {

//    private final StreamDataDao streamDataDao;
//
//    @GetMapping("/stream/{streamId}/streamData")
//    public StreamDataList getAllStreamsForStreamId(@PathVariable Long streamId) {
//        return new StreamDataList(streamDataDao.getStreamDataForStreamId(streamId));
//    }
//
//    @PostMapping("/stream/{streamId}/streamData")
//    public ResponseEntity<StreamDataResponseDto> addStreamData(@PathVariable Long streamId, @RequestBody StreamDataAddDto dto) {
//        try {
//            StreamDataResponseDto response = streamDataDao.addStreamData(streamId, dto);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (EntityNotFoundException | StreamHasEndedException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
}
