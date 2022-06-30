package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.mapper.ChannelResponseMapper;
import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.application.repositories.ChannelRepository;
import com.github.he305.twitchproducer.common.dto.ChannelAddDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.entities.Channel;
import com.github.he305.twitchproducer.common.mapper.ChannelAddMapper;
import com.github.he305.twitchproducer.common.service.ChannelService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
        Optional<Channel> channel = getChannelByNameInner(nickname);
        if (channel.isEmpty())
            return Optional.empty();

        return Optional.of(channelResponseMapper.toDto(channel.get()));
    }

    private Optional<Channel> getChannelByNameInner(@NonNull String nickname) {
        return channelRepository.findByNickname(nickname).stream().findFirst();
    }

    @Override
    public ChannelResponseDto addChannel(@NotNull ChannelAddDto channelAddDto) {
        if (channelAddDto.getNickname().isEmpty() || getChannelByNameInner(channelAddDto.getNickname()).isPresent())
            return new ChannelResponseDto();

        Optional<Person> person = personRepository.findById(channelAddDto.getPersonId());
        if (person.isEmpty())
            return new ChannelResponseDto();

        Channel channel = channelAddMapper.getChannel(channelAddDto);
        channel.setPerson(person.get());
        Channel saved = channelRepository.save(channel);
        return channelResponseMapper.toDto(saved);
    }
}
