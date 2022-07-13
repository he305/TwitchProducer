package com.github.he305.twitchproducer.common.dao;

import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;

import java.util.List;

public interface StreamDataDao {
    StreamDataResponseDto addStreamData(Long streamId, StreamDataAddDto streamData);

    List<StreamDataResponseDto> getStreamDataForStreamId(Long id);
}
