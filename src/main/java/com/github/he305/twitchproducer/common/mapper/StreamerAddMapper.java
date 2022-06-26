package com.github.he305.twitchproducer.common.mapper;

import com.github.he305.twitchproducer.common.dto.StreamerAddDto;
import com.github.he305.twitchproducer.common.entities.Streamer;

public interface StreamerAddMapper {
    Streamer getStreamer(StreamerAddDto dto);
}
