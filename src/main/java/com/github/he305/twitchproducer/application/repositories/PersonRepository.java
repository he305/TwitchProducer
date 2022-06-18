package com.github.he305.twitchproducer.application.repositories;

import com.github.he305.twitchproducer.common.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByLastName(String lastName);
}
