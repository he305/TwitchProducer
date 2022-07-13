package com.github.he305.twitchproducer.common.service;

import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;

public interface StreamService {
    StreamResponseDto addStreamData(Long channelId, StreamDataAddDto dto);
}
