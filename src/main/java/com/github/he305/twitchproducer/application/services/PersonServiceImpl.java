package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.dto.PersonResponseDto;
import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.mapper.PersonAddMapper;
import com.github.he305.twitchproducer.common.mapper.PersonResponseMapper;
import com.github.he305.twitchproducer.common.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PersonResponseMapper responseMapper;
    private final PersonAddMapper addMapper;

    @Override
    public List<PersonResponseDto> getAll() {
        List<Person> persons = personRepository.findAll();
        return persons.stream().map(responseMapper::getPersonDto).collect(Collectors.toList());
    }

    @Override
    public Optional<PersonResponseDto> getPersonById(Long personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(responseMapper.getPersonDto(person.get()));
    }

    @Override
    public PersonResponseDto addPerson(PersonAddDto dto) {
        Person person = addMapper.getPerson(dto);
        if (personRepository.findByLastName(person.getLastName()).isPresent()) {
            throw new EntityExistsException();
        }
        personRepository.save(person);
        return responseMapper.getPersonDto(person);
    }

    @Override
    public void deletePerson(Long personId) throws EntityNotFoundException {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isEmpty()) {
            throw new EntityNotFoundException();
        }
        personRepository.deleteById(personId);
    }

    @Override
    public PersonResponseDto updatePerson(Long personId, PersonAddDto dto) throws EntityNotFoundException {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isEmpty())
            throw new EntityNotFoundException();

        Person personToUpdate = person.get();
        personToUpdate.setFirstName(dto.getFirstName());
        personToUpdate.setLastName(dto.getLastName());
        return responseMapper.getPersonDto(personRepository.save(personToUpdate));
    }
}
