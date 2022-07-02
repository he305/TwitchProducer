package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.repositories.StreamRepository;
import com.github.he305.twitchproducer.common.dto.StreamAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.mapper.StreamResponseMapper;
import com.github.he305.twitchproducer.common.service.StreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StreamServiceImpl implements StreamService {
    private final StreamRepository streamRepository;
    private final StreamResponseMapper responseMapper;

    @Override
    public List<StreamResponseDto> getAllStreams() {
        List<Stream> streams = streamRepository.findAll();
        return streams.stream().map(responseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<StreamResponseDto> getCurrentStreams() {
        List<Stream> streams = streamRepository.findAll();

        return streams.stream()
                .filter(s -> s.getEndedAt() == null)
                .map(responseMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    public StreamResponseDto addStream(StreamAddDto dto) {
        return new StreamResponseDto();
    }

    @Override
    public Optional<StreamResponseDto> getStreamById(Long id) {
        Optional<Stream> stream = streamRepository.findById(id);
        if (stream.isEmpty())
            return Optional.empty();
        return Optional.of(responseMapper.toDto(stream.get()));
    }
}
