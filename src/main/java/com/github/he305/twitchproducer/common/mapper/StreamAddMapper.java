package com.github.he305.twitchproducer.common.mapper;

import com.github.he305.twitchproducer.common.dto.StreamAddDto;
import com.github.he305.twitchproducer.common.entities.Stream;

public interface StreamAddMapper {
    Stream toStream(StreamAddDto dto);
}
