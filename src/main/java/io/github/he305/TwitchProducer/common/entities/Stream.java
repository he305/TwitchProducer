package io.github.he305.TwitchProducer.common.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

    public static Stream emptyStream() {
        return Stream.builder()
                .isLive(false)
                .gameName("")
                .title("")
                .viewerCount(0)
                .startedAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC))
                .build();
    }
}
