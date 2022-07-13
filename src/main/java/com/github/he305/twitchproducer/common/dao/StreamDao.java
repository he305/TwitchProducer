package com.github.he305.twitchproducer.common.dao;

import com.github.he305.twitchproducer.common.dto.StreamAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StreamDao {
    List<StreamResponseDto> getAllStreams();

    List<StreamResponseDto> getCurrentStreams();

    StreamResponseDto addStream(Long channelId, StreamAddDto dto);

    Optional<StreamResponseDto> getStreamById(Long id);

    StreamResponseDto endStream(Long streamId, LocalDateTime endTime);
}
