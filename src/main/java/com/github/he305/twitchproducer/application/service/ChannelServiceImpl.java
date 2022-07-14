package com.github.he305.twitchproducer.application.service;

import com.github.he305.twitchproducer.application.mapper.ChannelResponseMapper;
import com.github.he305.twitchproducer.common.dao.ChannelDao;
import com.github.he305.twitchproducer.common.dao.PersonDao;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.exception.EntityAlreadyExistsException;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.mapper.ChannelAddMapper;
import com.github.he305.twitchproducer.common.service.ChannelService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    @Autowired
    private final ChannelDao channelDao;

    @Autowired
    private final PersonDao personDao;

    @Autowired
    private final ChannelAddMapper channelAddMapper;

    @Autowired
    private final ChannelResponseMapper channelResponseMapper;

    @Override
    public List<ChannelResponseDto> getAllChannels() {
        List<Channel> channelList = channelDao.getAll();
        return channelList.stream().map(channelResponseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<ChannelResponseDto> getChannelByName(@NonNull String nickname) {
        Optional<Channel> channel = channelDao.getChannelByName(nickname).stream().findFirst();
        if (channel.isEmpty())
            return Optional.empty();

        return Optional.of(channelResponseMapper.toDto(channel.get()));
    }

    @Override
    public List<ChannelResponseDto> getLiveChannels() {
        return channelDao.getLiveChannels().stream().map(channelResponseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<ChannelResponseDto> getChannelById(@NonNull Long id) {
        Optional<Channel> channel = channelDao.get(id);
        if (channel.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(channelResponseMapper.toDto(channel.get()));
    }

    @Override
    public Optional<ChannelResponseDto> getPersonChannelByName(@NonNull Long personId, @NonNull String channelName) {
        Optional<Channel> channel = channelDao
                .getChannelByName(channelName)
                .stream()
                .filter(s -> s.getPerson().getId().equals(personId))
                .findFirst();
        if (channel.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(channelResponseMapper.toDto(channel.get()));
    }

    @Override
    public ChannelResponseDto addChannel(@NonNull Long personId, @NonNull ChannelAddDto channelAddDto) {
        if (channelAddDto.getNickname().isEmpty())
            throw new IllegalArgumentException();

        Optional<Person> person = personDao.get(personId);
        if (person.isEmpty())
            throw new EntityNotFoundException();

        if (getPersonChannelByName(personId, channelAddDto.getNickname()).isPresent())
            throw new EntityAlreadyExistsException();

        Channel channel = channelAddMapper.getChannel(channelAddDto);
        channel.setPerson(person.get());
        Channel saved = channelDao.save(channel);
        return channelResponseMapper.toDto(saved);
    }

    @Override
    public void deleteChannel(Long channelId) throws EntityNotFoundException {
        Optional<Channel> channel = channelDao.get(channelId);
        if (channel.isEmpty())
            throw new EntityNotFoundException();

        channelDao.delete(channel.get());
    }

    @Override
    public ChannelResponseDto updateChannel(Long channelId, ChannelAddDto dto) throws EntityNotFoundException {
        Optional<Channel> existingChannel = channelDao.get(channelId);
        if (existingChannel.isEmpty())
            throw new EntityNotFoundException();

        Channel channelToUpdate = existingChannel.get();
        channelToUpdate.setNickname(dto.getNickname());
        channelToUpdate.setPlatform(dto.getPlatform());
        channelToUpdate.setIsLive(false);
        Channel saved = channelDao.save(channelToUpdate);
        return channelResponseMapper.toDto(saved);
    }
}
