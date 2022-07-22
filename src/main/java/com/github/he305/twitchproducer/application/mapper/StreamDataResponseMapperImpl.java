package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.mapper.StreamDataResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class StreamDataResponseMapperImpl implements StreamDataResponseMapper {

    @Override
    public StreamDataResponseDto toDto(StreamData stream) {
        return new StreamDataResponseDto(
                stream.getId(),
                stream.getGameName(),
                stream.getTitle(),
                stream.getViewerCount(),
                stream.getTimeAt(),
                stream.getStream().getId()
        );
    }
}
