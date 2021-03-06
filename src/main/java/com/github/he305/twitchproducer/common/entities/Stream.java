package com.github.he305.twitchproducer.common.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "stream")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Stream extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "started_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime startedAt;

    @Column(name = "ended_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime endedAt;

    @Column(name = "max_viewers")
    private Integer maxViewers;

    @OneToMany(targetEntity = StreamData.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "stream_id")
    private List<StreamData> streamData;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "channel_id", referencedColumnName = "id", nullable = false)
    private Channel channel;

    public void addStreamData(StreamData data) {
        streamData.add(data);
    }
}
