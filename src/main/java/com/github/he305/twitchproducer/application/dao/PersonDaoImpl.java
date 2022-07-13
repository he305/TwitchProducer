package com.github.he305.twitchproducer.application.dao;

import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.common.dao.PersonDao;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonDaoImpl implements PersonDao {

    private final PersonRepository personRepository;

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> getPersonById(Long personId) {
        return personRepository.findById(personId);
    }

    @Override
    public void deletePerson(Person person) throws EntityNotFoundException {
        personRepository.delete(person);
    }

    @Override
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Optional<Person> findByLastName(String lastName) {
        return personRepository.findByLastName(lastName);
    }
}
