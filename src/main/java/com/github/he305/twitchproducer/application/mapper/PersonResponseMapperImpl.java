package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.mapper.PersonResponseMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonResponseMapperImpl implements PersonResponseMapper {

    private final ModelMapper mapper;

    @Override
    public PersonResponseDto getPersonDto(Person person) {
        return mapper.map(person, PersonResponseDto.class);
    }
}
