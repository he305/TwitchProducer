package com.github.he305.twitchproducer.common.service;

import com.github.he305.twitchproducer.application.dto.StreamEndRequest;
import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface StreamService {
    StreamResponseDto addStreamData(@NonNull Long channelId, StreamDataAddDto dto);

    StreamResponseDto endStream(@NonNull Long channelId, StreamEndRequest req);

    List<StreamResponseDto> getAllStreams();

    Optional<StreamResponseDto> getStreamById(@NonNull Long streamId);

    List<StreamResponseDto> getCurrentStreams();
}
