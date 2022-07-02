package com.github.he305.twitchproducer.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StreamAddDto {
    private LocalDateTime startedAt;
}
