package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.mapper.ChannelResponseMapper;
import com.github.he305.twitchproducer.application.repositories.ChannelRepository;
import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.mapper.ChannelAddMapper;
import com.github.he305.twitchproducer.common.service.ChannelService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ChannelServiceImpl implements ChannelService {
    @Autowired
    private final ChannelRepository channelRepository;

    @Autowired
    private final PersonRepository personRepository;

    @Autowired
    private final ChannelAddMapper channelAddMapper;

    @Autowired
    private final ChannelResponseMapper channelResponseMapper;

    @Override
    public List<ChannelResponseDto> getAllChannels() {
        List<Channel> channelList = channelRepository.findAll();
        return channelList.stream().map(channelResponseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<ChannelResponseDto> getChannelByName(@NonNull String nickname) {
        Optional<Channel> channel = channelRepository.findByNickname(nickname).stream().findFirst();
        if (channel.isEmpty())
            return Optional.empty();

        return Optional.of(channelResponseMapper.toDto(channel.get()));
    }

    @Override
    public Optional<ChannelResponseDto> getChannelById(@NonNull Long id) {
        Optional<Channel> channel = channelRepository.findById(id);
        if (channel.isEmpty())
            return Optional.empty();
        return Optional.ofNullable(channelResponseMapper.toDto(channel.get()));
    }

    @Override
    public Optional<ChannelResponseDto> getPersonChannelByName(@NonNull Long personId, @NonNull String channelName) {
        Optional<Channel> channel = channelRepository
                .findByNickname(channelName)
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

        Optional<Person> person = personRepository.findById(personId);
        if (person.isEmpty())
            throw new EntityNotFoundException();

        if (getPersonChannelByName(personId, channelAddDto.getNickname()).isPresent())
            throw new EntityExistsException();

        Channel channel = channelAddMapper.getChannel(channelAddDto);
        channel.setPerson(person.get());
        Channel saved = channelRepository.save(channel);
        return channelResponseMapper.toDto(saved);
    }

    @Override
    public void deleteChannel(Long channelId) throws EntityNotFoundException {
        Optional<Channel> channel = channelRepository.findById(channelId);
        if (channel.isEmpty())
            throw new EntityNotFoundException();

        channelRepository.deleteById(channelId);
    }

    @Override
    public ChannelResponseDto updateChannel(Long channelId, ChannelAddDto dto) throws EntityNotFoundException {
        Optional<Channel> existingChannel = channelRepository.findById(channelId);
        if (existingChannel.isEmpty())
            throw new EntityNotFoundException();

        Channel channelToUpdate = existingChannel.get();
        channelToUpdate.setNickname(dto.getNickname());
        channelToUpdate.setPlatform(dto.getPlatform());
        Channel saved = channelRepository.save(channelToUpdate);
        return channelResponseMapper.toDto(saved);
    }
}
