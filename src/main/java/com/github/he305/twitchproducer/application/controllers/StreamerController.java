package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.dto.StreamerBodyDto;
import com.github.he305.twitchproducer.common.entities.Streamer;
import com.github.he305.twitchproducer.common.interfaces.StreamerService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/streamer")
@AllArgsConstructor
public class StreamerController {
    @Autowired
    private final StreamerService streamerService;
    @Autowired
    private final ModelMapper modelMapper;

    @GetMapping("/all")
    public List<Streamer> getAll() {
        return streamerService.getAllStreamers();
    }

    @GetMapping("/{nickname}")
    public Streamer getByName(@PathVariable String nickname) {
        return streamerService.getStreamerByName(nickname).orElse(new Streamer());
    }

    @PostMapping("/add")
    public ResponseEntity<Streamer> addStreamer(@RequestBody StreamerBodyDto streamerBodyDto) {
        Streamer streamer = convertFromDto(streamerBodyDto);
        Streamer response = streamerService.addStreamer(streamer);
        if (response.getId() != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    private Streamer convertFromDto(StreamerBodyDto streamerBodyDto) {
        return modelMapper.map(streamerBodyDto, Streamer.class);
    }
}