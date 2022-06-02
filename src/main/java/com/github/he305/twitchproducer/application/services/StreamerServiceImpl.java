package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.repositories.StreamerRepository;
import com.github.he305.twitchproducer.common.entities.Streamer;
import com.github.he305.twitchproducer.common.interfaces.StreamerService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StreamerServiceImpl implements StreamerService {
    @Autowired
    private final StreamerRepository streamerRepository;

    @Override
    public List<Streamer> getAllStreamers() {
        return streamerRepository.findAll();
    }

    @Override
    public Optional<Streamer> getStreamerByName(@NonNull String nickname) {
        List<Streamer> streamers = streamerRepository.findByNickname(nickname);
        return streamers.stream().findFirst();
    }

    @Override
    public Streamer addStreamer(@NotNull Streamer streamer) {
        if (streamer.getNickname().isEmpty() || getStreamerByName(streamer.getNickname()).isPresent())
            return new Streamer();
        return streamerRepository.save(streamer);
    }
}
