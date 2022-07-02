package com.github.he305.twitchproducer.common.service;

import com.github.he305.twitchproducer.common.dto.StreamAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;

import java.util.List;
import java.util.Optional;

public interface StreamService {
    List<StreamResponseDto> getAllStreams();

    List<StreamResponseDto> getCurrentStreams();

    StreamResponseDto addStream(StreamAddDto dto);

    Optional<StreamResponseDto> getStreamById(Long id);
}
