package com.github.he305.twitchproducer.application.service;

import com.github.he305.twitchproducer.application.dto.StreamEndRequest;
import com.github.he305.twitchproducer.common.dao.ChannelDao;
import com.github.he305.twitchproducer.common.dao.StreamDao;
import com.github.he305.twitchproducer.common.dao.StreamDataDao;
import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Stream;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.mapper.StreamResponseMapper;
import com.github.he305.twitchproducer.common.service.StreamService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StreamServiceImpl implements StreamService {
    private final StreamDao streamDao;
    private final StreamDataDao streamDataDao;
    private final ChannelDao channelDao;

    private final StreamResponseMapper responseMapper;

    @Override
    public StreamResponseDto addStreamData(Long channelId, StreamDataAddDto dto) {
        Optional<Channel> channel = channelDao.get(channelId);
        if (channel.isEmpty())
            throw new EntityNotFoundException();
        Channel savedChannel = channelDao.updateIsLive(channel.get(), true);

        Optional<Stream> currentStream = streamDao.getCurrentStreamForChannel(savedChannel);

        if (currentStream.isEmpty()) {
            Stream newStream = createNewStream(dto, savedChannel);
            streamDataDao.addStreamData(newStream.getId(), dto);
            return getStreamResponseDtoOrThrow(newStream.getId());
        }

        Stream stream = currentStream.get();
        streamDataDao.addStreamData(stream.getId(), dto);
        updateViewerCount(stream, dto);
        return getStreamResponseDtoOrThrow(stream.getId());
    }

    private StreamResponseDto getStreamResponseDtoOrThrow(Long streamId) {
        Optional<Stream> returnStream = streamDao.get(streamId);
        if (returnStream.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return responseMapper.toDto(returnStream.get());
    }

    private void updateViewerCount(Stream stream, StreamDataAddDto dto) {
        if (stream.getMaxViewers() >= dto.getViewerCount())
            return;

        stream.setMaxViewers(dto.getViewerCount());
        streamDao.save(stream);
    }

    private Stream createNewStream(StreamDataAddDto dto, Channel savedChannel) {
        Stream stream = new Stream(null, dto.getTime(), null, dto.getViewerCount(), new ArrayList<>(), savedChannel);
        return streamDao.save(stream);
    }

    @Override
    public StreamResponseDto endStream(@NonNull Long channelId, @NonNull StreamEndRequest req) {
        Optional<Channel> channel = channelDao.get(channelId);
        if (channel.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Channel updatedChannel = channelDao.updateIsLive(channel.get(), false);

        Optional<Stream> currentStreamOptional = streamDao.getCurrentStreamForChannel(updatedChannel);
        if (currentStreamOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Stream currentStream = currentStreamOptional.get();
        currentStream.setEndedAt(req.getTime());
        Stream savedStream = streamDao.save(currentStream);
        return responseMapper.toDto(savedStream);
    }

    @Override
    public List<StreamResponseDto> getAllStreams() {
        return streamDao.getAll().stream().map(responseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<StreamResponseDto> getStreamById(@NonNull Long streamId) {
        Optional<Stream> stream = streamDao.get(streamId);
        if (stream.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(responseMapper.toDto(stream.get()));
    }

    @Override
    public List<StreamResponseDto> getCurrentStreams() {
        return streamDao.getCurrentStreams().stream().map(responseMapper::toDto).collect(Collectors.toList());
    }
}
