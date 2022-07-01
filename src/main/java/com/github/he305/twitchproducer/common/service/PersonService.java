package com.github.he305.twitchproducer.common.service;

import com.github.he305.twitchproducer.application.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<PersonResponseDto> getAll();

    Optional<PersonResponseDto> getPersonByLastName(String lastName);

    PersonResponseDto addPerson(PersonAddDto personResponseDto) throws EntityExistsException;

}
