package com.github.he305.twitchproducer.application.services;

import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.common.dto.PersonDto;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;
import com.github.he305.twitchproducer.common.mapper.PersonMapper;
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
    private final PersonMapper mapper;

    @Override
    public List<PersonDto> getAll() {
        List<Person> persons = personRepository.findAll();
        return persons.stream().map(mapper::getPersonDto).collect(Collectors.toList());
    }

    @Override
    public Optional<PersonDto> getPersonByLastName(String lastName) {
        Optional<Person> person = personRepository.findByLastName(lastName);
        if (person.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(mapper.getPersonDto(person.get()));
    }

    @Override
    public PersonDto addPerson(PersonDto personDto) {
        Person person = mapper.getPerson(personDto);
        if (personRepository.findByLastName(person.getLastName()).isPresent()) {
            throw new EntityExistsException();
        }
        personRepository.save(person);
        return personDto;
    }
}
