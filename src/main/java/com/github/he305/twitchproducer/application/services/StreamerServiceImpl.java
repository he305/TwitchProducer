package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.mapper.StreamerResponseMapper;
import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.application.repositories.StreamerRepository;
import com.github.he305.twitchproducer.common.dto.StreamerAddDto;
import com.github.he305.twitchproducer.common.dto.StreamerResponseDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.entities.Streamer;
import com.github.he305.twitchproducer.common.mapper.StreamerAddMapper;
import com.github.he305.twitchproducer.common.service.StreamerService;
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
public class StreamerServiceImpl implements StreamerService {
    @Autowired
    private final StreamerRepository streamerRepository;

    @Autowired
    private final PersonRepository personRepository;

    @Autowired
    private final StreamerAddMapper streamerAddMapper;

    @Autowired
    private final StreamerResponseMapper streamerResponseMapper;

    @Override
    public List<StreamerResponseDto> getAllStreamers() {
        List<Streamer> streamerList = streamerRepository.findAll();
        return streamerList.stream().map(streamerResponseMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<StreamerResponseDto> getStreamerByName(@NonNull String nickname) {
        Optional<Streamer> streamer = getStreamerByNameInner(nickname);
        if (streamer.isEmpty())
            return Optional.empty();

        return Optional.of(streamerResponseMapper.toDto(streamer.get()));
    }

    private Optional<Streamer> getStreamerByNameInner(@NonNull String nickname) {
        return streamerRepository.findByNickname(nickname).stream().findFirst();
    }

    @Override
    public StreamerResponseDto addStreamer(@NotNull StreamerAddDto streamerAddDto) {
        if (streamerAddDto.getNickname().isEmpty() || getStreamerByNameInner(streamerAddDto.getNickname()).isPresent())
            return new StreamerResponseDto();

        Optional<Person> person = personRepository.findById(streamerAddDto.getPersonId());
        if (person.isEmpty())
            return new StreamerResponseDto();

        Streamer streamer = streamerAddMapper.getStreamer(streamerAddDto);
        streamer.setPerson(person.get());
        Streamer saved = streamerRepository.save(streamer);
        return streamerResponseMapper.toDto(saved);
    }
}
