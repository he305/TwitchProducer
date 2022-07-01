package com.github.he305.twitchproducer.common.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PersonResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
}
