package io.github.he305.TwitchProducer.common.interfaces;

import io.github.he305.TwitchProducer.common.entities.Streamer;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface StreamerService {
    List<Streamer> getAllStreamers();
    Optional<Streamer> getStreamerByName(@NonNull String nickname);
    Streamer addStreamer(@NonNull Streamer streamer);
}
