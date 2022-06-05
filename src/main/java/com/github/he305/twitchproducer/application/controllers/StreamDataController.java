package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.interfaces.StreamDataService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stream")
@AllArgsConstructor
public class StreamDataController {

    @Autowired
    private final StreamDataService streamDataService;

    @GetMapping("/{name}")
    public StreamData getStream(@PathVariable String name) {
        return streamDataService.getStream(name);
    }
}
