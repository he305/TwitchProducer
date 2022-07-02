package com.github.he305.twitchproducer.common.mapper;

import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.entities.StreamData;

public interface StreamDataResponseMapper {
    StreamDataResponseDto toDto(StreamData stream);
}
