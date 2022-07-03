package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.repositories.StreamDataRepository;
import com.github.he305.twitchproducer.application.repositories.StreamRepository;
import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.dto.StreamDataResponseDto;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.entities.StreamData;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.exception.StreamHasEndedException;
import com.github.he305.twitchproducer.common.mapper.StreamDataAddMapper;
import com.github.he305.twitchproducer.common.mapper.StreamDataResponseMapper;
import com.github.he305.twitchproducer.common.service.StreamDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StreamDataServiceImpl implements StreamDataService {
    private final StreamDataRepository streamDataRepository;
    private final StreamRepository streamRepository;
    private final StreamDataAddMapper addMapper;
    private final StreamDataResponseMapper responseMapper;

    @Override
    public StreamDataResponseDto addStreamData(Long streamId, StreamDataAddDto dto) {
        Optional<Stream> stream = streamRepository.findById(streamId);
        if (stream.isEmpty())
            throw new EntityNotFoundException();

        Stream targetStream = stream.get();
        if (targetStream.getEndedAt() != null)
            throw new StreamHasEndedException();

        StreamData streamData = addMapper.toStreamData(dto);
        streamData.setStream(targetStream);
        StreamData saved = streamDataRepository.save(streamData);
        return responseMapper.toDto(saved);
    }

    @Override
    public List<StreamDataResponseDto> getStreamDataForStreamId(Long id) {
        List<StreamData> streamDataList = streamDataRepository
                .findAll()
                .stream()
                .filter(s -> s.getStream().getId().equals(id))
                .collect(Collectors.toList());
        return streamDataList.stream().map(responseMapper::toDto).collect(Collectors.toList());
    }
}
