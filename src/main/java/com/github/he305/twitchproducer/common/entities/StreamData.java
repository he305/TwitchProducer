package com.github.he305.twitchproducer.common.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stream_data")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class StreamData extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_name", nullable = false, length = 100)
    private String gameName;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "viewer_count", nullable = false)
    private Integer viewerCount;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "stream_id", referencedColumnName = "id", nullable = false)
    private Stream stream;

    @Column(name = "timeAt", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime timeAt;
}
