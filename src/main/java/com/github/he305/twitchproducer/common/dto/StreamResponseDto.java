package com.github.he305.twitchproducer.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StreamResponseDto {
    private Long id;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Integer maxViewers;
    private Long channelId;
    private List<StreamDataResponseDto> streamData;
}
