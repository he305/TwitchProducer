package com.github.he305.twitchproducer.common.entities;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "channel")
@Getter
@Setter
@EqualsAndHashCode
public class Channel extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", length = 8, nullable = false)
    private Platform platform;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    private Person person;
}
