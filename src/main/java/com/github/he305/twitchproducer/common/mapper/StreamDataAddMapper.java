package com.github.he305.twitchproducer.common.mapper;

import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.entities.StreamData;

public interface StreamDataAddMapper {
    StreamData toStreamData(StreamDataAddDto dto);
}
