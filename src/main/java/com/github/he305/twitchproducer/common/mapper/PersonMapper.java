package com.github.he305.twitchproducer.common.mapper;

import com.github.he305.twitchproducer.common.dto.PersonDto;
import com.github.he305.twitchproducer.common.entities.Person;

public interface PersonMapper {
    Person getPerson(PersonDto personDto);

    PersonDto getPersonDto(Person person);
}
