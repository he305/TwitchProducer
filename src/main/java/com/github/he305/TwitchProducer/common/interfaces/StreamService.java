package com.github.he305.TwitchProducer.common.interfaces;

import com.github.he305.TwitchProducer.common.entities.Stream;

public interface StreamService {
    Stream getStream(String nickname);
}
