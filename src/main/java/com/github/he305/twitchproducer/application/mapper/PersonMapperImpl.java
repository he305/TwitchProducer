package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.PersonDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.mapper.PersonMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonMapperImpl implements PersonMapper {

    private final ModelMapper mapper;

    @Override
    public Person getPerson(PersonDto personDto) {
        return mapper.map(personDto, Person.class);
    }

    @Override
    public PersonDto getPersonDto(Person person) {
        return mapper.map(person, PersonDto.class);
    }
}
