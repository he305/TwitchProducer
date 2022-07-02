package com.github.he305.twitchproducer.application.repositories;

import com.github.he305.twitchproducer.common.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByLastName(String lastName);
}
