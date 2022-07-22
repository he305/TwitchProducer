package com.github.he305.twitchproducer.application.service;

import com.github.he305.twitchproducer.common.dao.StreamDataDao;
import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.mapper.StreamDataResponseMapper;
import com.github.he305.twitchproducer.common.service.StreamDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StreamDataServiceImpl implements StreamDataService {
    private final StreamDataDao streamDataDao;
    private final StreamDataResponseMapper responseMapper;

    @Override
    public List<StreamDataResponseDto> getStreamDataForStreamId(Long id) {
        return streamDataDao.getStreamDataForStream(id).stream()
                .map(responseMapper::toDto)
                .collect(Collectors.toList());
    }
}
