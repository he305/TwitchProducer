package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.StreamerAddDto;
import com.github.he305.twitchproducer.common.entities.Streamer;
import com.github.he305.twitchproducer.common.mapper.StreamerAddMapper;
import org.springframework.stereotype.Component;

@Component
public class StreamerAddMapperImpl implements StreamerAddMapper {
    @Override
    public Streamer getStreamer(StreamerAddDto dto) {
        return new Streamer(
                null,
                dto.getNickname(),
                dto.getPlatform(),
                null
        );
    }
}
