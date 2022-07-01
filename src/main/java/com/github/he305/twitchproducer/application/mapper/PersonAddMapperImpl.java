package com.github.he305.twitchproducer.application.mapper;

import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.mapper.PersonAddMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class PersonAddMapperImpl implements PersonAddMapper {
    @Override
    public Person getPerson(PersonAddDto dto) {
        return new Person(
                null,
                dto.getFirstName(),
                dto.getLastName(),
                new HashSet<>()
        );
    }
}
