package com.github.he305.twitchproducer.common.mapper;

import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.entities.Stream;

public interface StreamResponseMapper {
    StreamResponseDto toDto(Stream stream);
}
