package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.StreamerResponseDto;
import com.github.he305.twitchproducer.common.entities.Streamer;
import org.springframework.stereotype.Component;

@Component
public class StreamerResponseMapper {
    public StreamerResponseDto toDto(Streamer streamer) {
        return new StreamerResponseDto(
                streamer.getId(),
                streamer.getNickname(),
                streamer.getPlatform(),
                streamer.getPerson().getFullName()
        );
    }
}
