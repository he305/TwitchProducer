package com.github.he305.twitchproducer.common.dao;

import com.github.he305.twitchproducer.common.entities.Person;
import com.github.he305.twitchproducer.common.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface PersonDao {
    List<Person> getAll();

    Optional<Person> getPersonById(Long personId);

    void deletePerson(Person person) throws EntityNotFoundException;

    Person savePerson(Person person);

    Optional<Person> findByLastName(String lastName);
}
