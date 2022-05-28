package io.github.he305.TwitchProducer.common.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stream {
    private Boolean isLive;
    private String gameName;
    private String title;
    private Integer viewerCount;
    private LocalDateTime startedAt;
}
