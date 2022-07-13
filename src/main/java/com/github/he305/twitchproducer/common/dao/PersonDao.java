package com.github.he305.twitchproducer.common.dao;

import com.github.he305.twitchproducer.common.entities.Person;

import java.util.Optional;

public interface PersonDao extends Dao<Person, Long> {
    Optional<Person> getByLastName(String lastName);
}
