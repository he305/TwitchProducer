package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.StreamAddDto;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.mapper.StreamAddMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class StreamAddMapperImpl implements StreamAddMapper {
    @Override
    public Stream toStream(StreamAddDto dto) {
        return new Stream(
                null,
                dto.getStartedAt(),
                null,
                0,
                Collections.emptyList(),
                null
        );
    }
}
