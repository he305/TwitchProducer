package io.github.he305.TwitchProducer.common.interfaces;

import io.github.he305.TwitchProducer.common.entities.Stream;

public interface StreamService {
    Stream getStream(String nickname);
}
