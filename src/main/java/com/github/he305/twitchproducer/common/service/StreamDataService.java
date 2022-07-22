package com.github.he305.twitchproducer.common.service;

import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;

import java.util.List;

public interface StreamDataService {
    List<StreamDataResponseDto> getStreamDataForStreamId(Long id);
}
