package com.github.he305.twitchproducer.application.dto;

import com.github.he305.twitchproducer.common.dto.PersonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDtoListDto {
    private List<PersonDto> persons;
}
