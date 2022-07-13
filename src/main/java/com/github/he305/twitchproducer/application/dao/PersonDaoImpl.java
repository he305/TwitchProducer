package com.github.he305.twitchproducer.application.dao;

import com.github.he305.twitchproducer.application.repositories.PersonRepository;
import com.github.he305.twitchproducer.common.dao.PersonDao;
import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;
import com.github.he305.twitchproducer.common.exception.EntitySaveFailedException;
import lombok.NonNull;
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
    public Optional<Person> get(@NonNull Long personId) {
        return personRepository.findById(personId);
    }

    @Override
    public void delete(@NonNull Person person) throws EntityNotFoundException {
        personRepository.delete(person);
    }

    @Override
    public Person save(@NonNull Person person) {
        try {
            return personRepository.save(person);
        } catch (RuntimeException e) {
            throw new EntitySaveFailedException(e.getMessage());
        }
    }

    @Override
    public Optional<Person> getByLastName(String lastName) {
        return personRepository.findByLastName(lastName);
    }
}
