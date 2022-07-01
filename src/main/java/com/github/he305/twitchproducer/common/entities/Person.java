package com.github.he305.twitchproducer.common.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "person")
@EqualsAndHashCode
public class Person extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @OneToMany(targetEntity = Channel.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private List<Channel> channels;

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }
}
