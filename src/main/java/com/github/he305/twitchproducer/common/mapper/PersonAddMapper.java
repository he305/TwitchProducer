package com.github.he305.twitchproducer.common.mapper;

import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.entities.Person;

public interface PersonAddMapper {
    Person getPerson(PersonAddDto dto);
}
