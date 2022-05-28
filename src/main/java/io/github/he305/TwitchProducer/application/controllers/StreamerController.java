package io.github.he305.TwitchProducer.application.controllers;

import io.github.he305.TwitchProducer.application.dto.StreamerBodyDto;
import io.github.he305.TwitchProducer.common.entities.Streamer;
import io.github.he305.TwitchProducer.common.interfaces.StreamerService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/streamer")
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
    public Streamer addStreamer(@RequestBody StreamerBodyDto streamerBodyDto) {
        Streamer streamer = convertFromDto(streamerBodyDto);
        return streamerService.addStreamer(streamer);
    }

    private Streamer convertFromDto(StreamerBodyDto streamerBodyDto) {
        Streamer streamer = modelMapper.map(streamerBodyDto, Streamer.class);
        streamer.setId(null);
        return streamer;
    }
}
