package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.dto.PersonDtoListDto;
import com.github.he305.twitchproducer.application.dto.PersonResponseDto;
import com.github.he305.twitchproducer.common.dto.PersonAddDto;
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
        List<PersonResponseDto> personResponseDtoList = List.of(
                new PersonResponseDto(0L, "", "", null),
                new PersonResponseDto(1L, "", "", null),
                new PersonResponseDto(2L, "", "", null)
        );

        Mockito.when(personService.getAll()).thenReturn(personResponseDtoList);
        PersonDtoListDto actual = underTest.getAllPersons();
        assertEquals(personResponseDtoList.size(), actual.getPersons().size());
    }

    @Test
    void getPersonByLastName_existingEntry() {
        PersonResponseDto data = new PersonResponseDto(0L, "test1", "test2", null);
        Mockito.when(personService.getPersonByLastName(Mockito.any())).thenReturn(Optional.of(data));
        ResponseEntity<PersonResponseDto> actual = underTest.getPersonByLastName("");
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(data, actual.getBody());
    }

    @Test
    void getPersonByLastName_notExistingEntry() {
        PersonResponseDto data = new PersonResponseDto(0L, "test1", "test2", null);
        Mockito.when(personService.getPersonByLastName(Mockito.any())).thenReturn(Optional.empty());
        ResponseEntity<PersonResponseDto> actual = underTest.getPersonByLastName("");
        assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    void addPerson_success() {
        PersonAddDto data = new PersonAddDto("test1", "test2");
        PersonResponseDto expected = new PersonResponseDto(0L, "test1", "test2", null);
        Mockito.when(personService.addPerson(Mockito.any())).thenReturn(expected);
        ResponseEntity<PersonResponseDto> actual = underTest.addPerson(data);
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
    }

    @Test
    void addPerson_alreadyExist() {
        PersonAddDto data = new PersonAddDto("test1", "test2");
        Mockito.when(personService.addPerson(Mockito.any())).thenThrow(EntityExistsException.class);
        ResponseEntity<PersonResponseDto> actual = underTest.addPerson(data);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
    }
}