package com.github.he305.twitchproducer.common.entities;

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
public class StreamData {

    private Boolean isLive;
    private String gameName;
    private String title;
    private Integer viewerCount;
    private LocalDateTime startedAt;

    public static StreamData emptyStream() {
        return StreamData.builder()
                .isLive(false)
                .gameName("")
                .title("")
                .viewerCount(0)
                .startedAt(LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC))
                .build();
    }
}
