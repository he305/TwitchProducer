package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.mapper.StreamDataAddMapper;
import org.springframework.stereotype.Component;

@Component
public class StreamDataAddMapperImpl implements StreamDataAddMapper {
    @Override
    public StreamData toStreamData(StreamDataAddDto dto) {
        return new StreamData(
                null,
                dto.getGameName(),
                dto.getTitle(),
                dto.getViewerCount(),
                null
        );
    }
}
