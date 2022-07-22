package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.StreamDataList;
import com.github.he305.twitchproducer.common.service.StreamDataService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiVersionPathConstants.V1)
@AllArgsConstructor
public class StreamDataController {

    private final StreamDataService streamDataService;

    @GetMapping("/stream/{streamId}/streamData")
    public StreamDataList getAllStreamsForStreamId(@PathVariable Long streamId) {
        return new StreamDataList(streamDataService.getStreamDataForStreamId(streamId));
    }

}
