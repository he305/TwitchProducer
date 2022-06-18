package com.github.he305.twitchproducer.application.controllers;

import com.github.he305.twitchproducer.application.constants.ApiVersionPathConstants;
import com.github.he305.twitchproducer.application.dto.PersonDtoListDto;
import com.github.he305.twitchproducer.common.dto.PersonDto;
import com.github.he305.twitchproducer.common.exception.EntityExistsException;
import com.github.he305.twitchproducer.common.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiVersionPathConstants.V1 + "person")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public PersonDtoListDto getAllPersons() {
        List<PersonDto> personList = personService.getAll();
        return new PersonDtoListDto(personList);
    }

    @GetMapping("/{lastName}")
    public ResponseEntity<PersonDto> getPersonByLastName(@PathVariable String lastName) {
        Optional<PersonDto> personDto = personService.getPersonByLastName(lastName);
        return personDto
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping
    public ResponseEntity<PersonDto> addPerson(@RequestBody PersonDto personDto) {
        try {
            PersonDto saved = personService.addPerson(personDto);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (EntityExistsException exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
