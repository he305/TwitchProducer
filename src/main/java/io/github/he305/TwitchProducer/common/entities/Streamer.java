package io.github.he305.TwitchProducer.common.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Streamer {
    private String nickname;
    private Integer id;
}
