package com.github.he305.twitchproducer.common.interfaces;

import com.github.he305.twitchproducer.common.entities.StreamData;

public interface StreamDataService {
    StreamData getStream(String nickname);
}
