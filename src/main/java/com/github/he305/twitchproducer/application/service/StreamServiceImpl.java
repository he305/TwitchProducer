package com.github.he305.twitchproducer.application.service;

import com.github.he305.twitchproducer.common.dao.ChannelDao;
import com.github.he305.twitchproducer.common.dao.StreamDao;
import com.github.he305.twitchproducer.common.dao.StreamDataDao;
import com.github.he305.twitchproducer.common.dto.StreamAddDto;
import com.github.he305.twitchproducer.common.dto.StreamDataAddDto;
import com.github.he305.twitchproducer.common.dto.StreamResponseDto;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.service.StreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StreamServiceImpl implements StreamService {
    private final StreamDao streamDao;
    private final StreamDataDao streamDataDao;
    private final ChannelDao channelDao;

    @Override
    public StreamResponseDto addStreamData(Long channelId, StreamDataAddDto dto) {
        Optional<Channel> channel = channelDao.get(channelId);
        if (channel.isEmpty())
            throw new EntityNotFoundException();
        List<StreamResponseDto> currentStreams = streamDao.getCurrentStreams();
        Optional<StreamResponseDto> currentStream = currentStreams.stream().filter(s -> s.getChannelId().equals(channelId)).findFirst();
        if (currentStream.isEmpty()) {
            StreamResponseDto newStream = streamDao.addStream(channelId, new StreamAddDto(LocalDateTime.now()));
            streamDataDao.addStreamData(newStream.getId(), dto);
            return newStream;
        }

        streamDataDao.addStreamData(currentStream.get().getId(), dto);
        return currentStream.get();
    }
}
