package com.github.he305.twitchproducer.application.dao;

import com.github.he305.twitchproducer.application.repositories.ChannelRepository;
import com.github.he305.twitchproducer.application.repositories.StreamRepository;
import com.github.he305.twitchproducer.common.dao.StreamDao;
import com.github.he305.twitchproducer.common.dto.StreamAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.mapper.StreamAddMapper;
import com.github.he305.twitchproducer.common.mapper.StreamResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StreamDaoImpl implements StreamDao {
    private final StreamRepository streamRepository;
    private final ChannelRepository channelRepository;
    private final StreamResponseMapper responseMapper;
    private final StreamAddMapper addMapper;

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
    public StreamResponseDto addStream(Long channelId, StreamAddDto dto) {
        Optional<Channel> channel = channelRepository.findById(channelId);
        if (channel.isEmpty())
            throw new EntityNotFoundException();

        Stream stream = addMapper.toStream(dto);
        stream.setChannel(channel.get());
        Stream saved = streamRepository.save(stream);
        return responseMapper.toDto(saved);
    }

    @Override
    public Optional<StreamResponseDto> getStreamById(Long id) {
        Optional<Stream> stream = streamRepository.findById(id);
        if (stream.isEmpty())
            return Optional.empty();
        return Optional.of(responseMapper.toDto(stream.get()));
    }

    @Override
    public StreamResponseDto endStream(Long streamId, LocalDateTime endTime) {
        Optional<Stream> stream = streamRepository.findById(streamId);
        if (stream.isEmpty())
            throw new EntityNotFoundException();

        Stream endedStream = stream.get();
        endedStream.setEndedAt(endTime);
        Stream saved = streamRepository.save(endedStream);
        return responseMapper.toDto(saved);
    }
}
