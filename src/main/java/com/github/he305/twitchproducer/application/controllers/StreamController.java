package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.interfaces.StreamService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stream")
@AllArgsConstructor
public class StreamController {

    @Autowired
    private final StreamService streamService;

    @GetMapping("/{name}")
    public Stream getStream(@PathVariable String name) {
        return streamService.getStream(name);
    }
}
