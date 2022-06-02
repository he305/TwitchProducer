package com.github.he305.twitchproducer.common.interfaces;

import com.github.he305.twitchproducer.common.entities.Stream;

public interface StreamService {
    Stream getStream(String nickname);
}
