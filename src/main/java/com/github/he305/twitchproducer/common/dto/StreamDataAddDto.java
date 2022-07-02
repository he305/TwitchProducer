package com.github.he305.twitchproducer.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StreamDataAddDto {
    private String gameName;
    private String title;
    private Integer viewerCount;
    private LocalDateTime time;
}
