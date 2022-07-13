package com.github.he305.twitchproducer.common.service;

import com.github.he305.twitchproducer.application.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<PersonResponseDto> getAll();

    Optional<PersonResponseDto> getPersonById(Long personId);

    PersonResponseDto addPerson(PersonAddDto personResponseDto) throws EntityExistsException;

    void deletePerson(Long personId) throws EntityNotFoundException;

    PersonResponseDto updatePerson(Long personId, PersonAddDto personResponseDto) throws EntityNotFoundException;
}
