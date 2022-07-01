package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.application.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.dto.ChannelResponseDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.mapper.PersonResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PersonResponseMapperImpl implements PersonResponseMapper {

    private final ChannelResponseMapper channelResponseMapper;

    @Override
    public PersonResponseDto getPersonDto(Person person) {
        List<ChannelResponseDto> channels = person
                .getChannels()
                .stream()
                .map(channelResponseMapper::toDto)
                .collect(Collectors.toList());

        return new PersonResponseDto(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                channels
        );
    }
}
