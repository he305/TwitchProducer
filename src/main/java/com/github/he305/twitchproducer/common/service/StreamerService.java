package com.github.he305.twitchproducer.common.service;

import com.github.he305.twitchproducer.common.dto.StreamerAddDto;
import com.github.he305.twitchproducer.common.dto.StreamerResponseDto;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface StreamerService {
    List<StreamerResponseDto> getAllStreamers();

    Optional<StreamerResponseDto> getStreamerByName(@NonNull String nickname);

    StreamerResponseDto addStreamer(@NonNull StreamerAddDto streamer);
}
