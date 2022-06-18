package com.github.he305.twitchproducer.common.service;

import com.github.he305.twitchproducer.common.dto.PersonDto;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<PersonDto> getAll();

    Optional<PersonDto> getPersonByLastName(String lastName);

    PersonDto addPerson(PersonDto personDto) throws EntityExistsException;

}
