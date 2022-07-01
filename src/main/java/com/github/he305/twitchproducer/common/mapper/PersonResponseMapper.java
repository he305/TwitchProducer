package com.github.he305.twitchproducer.common.mapper;

import com.github.he305.twitchproducer.application.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.entities.Person;

public interface PersonResponseMapper {

    PersonResponseDto getPersonDto(Person person);
}
