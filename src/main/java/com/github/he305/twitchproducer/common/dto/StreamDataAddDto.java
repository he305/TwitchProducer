package com.github.he305.twitchproducer.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamDataAddDto {
    private String gameName;
    private String title;
    private Integer viewerCount;
    private LocalDateTime time;
}
