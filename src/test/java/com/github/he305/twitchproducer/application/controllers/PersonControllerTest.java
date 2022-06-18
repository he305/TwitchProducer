package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.dto.PersonDtoListDto;
import com.github.he305.twitchproducer.common.dto.PersonDto;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;
import com.github.he305.twitchproducer.common.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock
    private PersonService personService;

    private PersonController underTest;

    @BeforeEach
    public void setUp() {
        underTest = new PersonController(personService);
    }

    @Test
    void getAllPersons() {
        List<PersonDto> personDtoList = List.of(
                new PersonDto("", ""),
                new PersonDto("", ""),
                new PersonDto("", "")
        );

        Mockito.when(personService.getAll()).thenReturn(personDtoList);
        PersonDtoListDto actual = underTest.getAllPersons();
        assertEquals(personDtoList.size(), actual.getPersons().size());
    }

    @Test
    void getPersonByLastName_existingEntry() {
        PersonDto data = new PersonDto("test1", "test2");
        Mockito.when(personService.getPersonByLastName(Mockito.any())).thenReturn(Optional.of(data));
        ResponseEntity<PersonDto> actual = underTest.getPersonByLastName("");
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(data, actual.getBody());
    }

    @Test
    void getPersonByLastName_notExistingEntry() {
        PersonDto data = new PersonDto("test1", "test2");
        Mockito.when(personService.getPersonByLastName(Mockito.any())).thenReturn(Optional.empty());
        ResponseEntity<PersonDto> actual = underTest.getPersonByLastName("");
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void addPerson_success() {
        PersonDto data = new PersonDto("test1", "test2");
        Mockito.when(personService.addPerson(Mockito.any())).thenReturn(data);
        ResponseEntity<PersonDto> actual = underTest.addPerson(data);
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(data, actual.getBody());
    }

    @Test
    void addPerson_alreadyExist() {
        PersonDto data = new PersonDto("test1", "test2");
        Mockito.when(personService.addPerson(Mockito.any())).thenThrow(EntityExistsException.class);
        ResponseEntity<PersonDto> actual = underTest.addPerson(data);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }
}